package com.example.dailychatbot.service;

import com.example.dailychatbot.dto.request.ParsedTransaction;
import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.entity.Expense;
import com.example.dailychatbot.mapper.ExpenseMapper;
import com.example.dailychatbot.repository.ExpenseRepository;
import com.example.dailychatbot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final RestTemplate restTemplate;

    public ExpenseResponse saveFromTelegram(TelegramMessage telegramMessage) {
        Expense expense = expenseMapper.toExpense(telegramMessage);
        if (expense == null) {
            throw new IllegalArgumentException("Expense entity must not be null");
        }
        return expenseMapper.toExpenseResponse(expenseRepository.save(expense));
    }


    public List<ExpenseResponse> getAllExpenses() {
        return expenseMapper.toExpenseResponseList(expenseRepository.findAll());
    }

    public ExpenseResponse getExpenseById(String id) {
        return expenseMapper.toExpenseResponse(expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu với id: " + id)));
    }

    @Value("${jwt.botToken}")
    private String botToken;

    public void sendReplyToUser(Long chatId, String text) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("✅ Đã gửi phản hồi tới Telegram: {}", response.getBody());
        } catch (Exception e) {
            log.error("❌ Gửi phản hồi tới Telegram thất bại: {}", e.getMessage(), e);
        }
    }

    public ParsedTransaction parseTransactionMessage(String messageText) {
        String originalText = messageText.trim();
        boolean isIncome = originalText.startsWith("+");
        boolean isExpense = originalText.startsWith("-");
        if (!isIncome && !isExpense) {
            isExpense = true;
        }
        // Nếu không có dấu thì mặc định là chi tiêu
        String cleanedText = originalText.replaceFirst("^[+-]", "").trim();
        String[] parts = cleanedText.split("\\s+", 2);

        String rawAmount = parts[0].replaceAll("[^\\d]", "");
        int amount = 0;
        if (!rawAmount.isEmpty()) {
            amount = Integer.parseInt(rawAmount);
        } else {
            throw new IllegalArgumentException("Không thể xác định số tiền từ chuỗi: " + cleanedText);
        }
        String description = parts.length > 1 ? parts[1] : "";

        String formattedAmount = String.format("%,dđ", amount * 1000);
        String formattedText = description.isEmpty()
                ? formattedAmount
                : String.format("%s - %s", formattedAmount, description);

        ParsedTransaction result = new ParsedTransaction();
        result.setIncome(isIncome);
        result.setExpense(isExpense);
        result.setAmount(formattedAmount);
        result.setDescription(description);
        result.setFormattedText(formattedText);
        return result;
    }

    // Gửi thống kê theo tháng
    public void sendStatistical(Long chatId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        long startEpoch = start.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        long endEpoch = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();

        List<Expense> allExpenses = expenseRepository.findByFrom_IdAndDateBetween(chatId, startEpoch, endEpoch);
        List<Expense> userExpenses = allExpenses.stream()
                .filter(e -> e.getFrom() != null)
                .toList();

        int income = 0;
        int expense = 0;

        StringBuilder detail = new StringBuilder();

        for (Expense e : userExpenses) {
            String text = e.getText().toLowerCase().trim();
            int amount = extractAmount(text) * 1000;
            String description = text.replaceAll("^(\\+|\\-|thu|chi|income|expense)", "").trim();

            if (text.startsWith("+") || text.startsWith("thu") || text.startsWith("income")) {
                income += amount;
                detail.append(String.format("- %s: 🟢 +%,dđ - %s\n", formatDate(e.getDate()), amount, description));
            } else {
                expense += amount;
                detail.append(String.format("- %s: 🔴 -%,dđ - %s\n", formatDate(e.getDate()), amount, description));
            }
        }
        String message = String.format("""
                        📊 Thống kê tháng %02d/%d
                        
                        🟢 Thu: %,dđ
                        🔴 Chi: %,dđ
                        🔁 Giao dịch: %d lần
                        
                        Chi tiết:
                        %s
                        """,
                currentMonth.getMonthValue(), currentMonth.getYear(),
                income, expense, userExpenses.size(),
                detail.toString()
        );

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("✅ Đã gửi thống kê tới Telegram: {}", response.getBody());
        } catch (Exception e) {
            log.error("❌ Gửi thống kê tới Telegram thất bại: {}", e.getMessage(), e);
        }
    }

    // Hàm hiển thị thông tin chào mừng người dùng
    public String getWelcomeMessage(long chatId) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        String welcomeText = BotMessages.WELCOME_MESSAGE;
        requestBody.put("text", welcomeText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }

    private int extractAmount(String text) {
        String rawAmount = text.replaceAll("[^\\d]", "");
        return rawAmount.isEmpty() ? 0 : Integer.parseInt(rawAmount);
    }

    public String formatDate(long epochSecond) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm"); // hoặc "yyyy-MM-dd HH:mm"
        return dateTime.format(formatter);
    }

}

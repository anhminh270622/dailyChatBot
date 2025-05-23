package com.example.dailychatbot.utils;

public class BotMessages {
    public static final String TRANSACTION_GUIDE = """
            ❌ Không thể xác định loại giao dịch bạn vừa nhập.
            
            📌 Hướng dẫn nhập giao dịch:
            
            ➕ Thu nhập  
            Ví dụ: `+ 500k lương`
            
            ➖ Chi tiêu  
            Ví dụ: `- 100k ăn sáng`
            
            💡 Dấu `+` là thu, dấu `-` là chi.
            
            📊 Xem thống kê tháng: /thongke
            """;


    public static final String WELCOME_MESSAGE = """
            👋 Xin chào! Chào mừng bạn đến với ✨ Daily Chatbot ✨
            
            💡 Bạn có thể nhập giao dịch theo các cách sau:
            
            ➕ Thu nhập  
            Ví dụ: `+ 5 triệu lương tháng 5`
            
            ➖ Chi tiêu  
            Ví dụ: `- 100k ăn sáng`
            
            📊 Xem báo cáo chi tiêu tháng hiện tại:  
            Gõ lệnh: `/thongke`
            
            📌 Tip: Bạn có thể bấm vào các nút bên dưới để thao tác nhanh hơn!
            
            💪 Chúc bạn một ngày tài chính thật hiệu quả!
            """;

}

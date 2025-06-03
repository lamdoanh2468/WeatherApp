package com.example.weatherapp.utils;

public class ClothingSuggestion {
    private static final double HOT_TEMPERATURE = 30.0; // Nhiệt độ nóng (Celsius)
    private static final double WARM_TEMPERATURE = 25.0; // Nhiệt độ ấm
    private static final double COOL_TEMPERATURE = 20.0; // Nhiệt độ mát
    private static final double COLD_TEMPERATURE = 15.0; // Nhiệt độ lạnh
    
    private static final double HIGH_HUMIDITY = 70.0; // Độ ẩm cao
    private static final double HIGH_WIND_SPEED = 20.0; // Tốc độ gió cao (km/h)

    public static String getClothingSuggestion(double temperature, String weatherCondition, double humidity, double windSpeed) {
        StringBuilder suggestion = new StringBuilder();
        
        // Đề xuất dựa trên nhiệt độ
        if (temperature >= HOT_TEMPERATURE) {
            suggestion.append("Nhiệt độ cao \uD83D\uDD25, bạn nên mặc:\n");
            suggestion.append("- Áo phông cotton mỏng\n");
            suggestion.append("- Quần short hoặc váy nhẹ\n");
            suggestion.append("- Giày sandal hoặc giày thể thao thoáng khí\n");
            suggestion.append("- Đội mũ và đeo kính râm để bảo vệ khỏi ánh nắng\n");
        } else if (temperature >= WARM_TEMPERATURE) {
            suggestion.append("Thời tiết ấm ☀\uFE0F, bạn nên mặc:\n");
            suggestion.append("- Áo phông hoặc áo sơ mi cotton\n");
            suggestion.append("- Quần jean mỏng hoặc quần kaki\n");
            suggestion.append("- Giày thể thao hoặc giày mở\n");
        } else if (temperature >= COOL_TEMPERATURE) {
            suggestion.append("Thời tiết mát \uD83C\uDF43, bạn nên mặc:\n");
            suggestion.append("- Áo sơ mi dài tay\n");
            suggestion.append("- Quần jean hoặc quần dài\n");
            suggestion.append("- Áo khoác nhẹ hoặc áo len mỏng\n");
            suggestion.append("- Giày kín hoặc giày thể thao\n");
        } else if (temperature >= COLD_TEMPERATURE) {
            suggestion.append("Thời tiết lạnh ❄\uFE0F, bạn nên mặc:\n");
            suggestion.append("- Áo len dày\n");
            suggestion.append("- Quần dày\n");
            suggestion.append("- Áo khoác ấm\n");
            suggestion.append("- Giày kín và tất ấm\n");
        } else {
            suggestion.append("Thời tiết rất lạnh \uD83E\uDD76, bạn nên mặc:\n");
            suggestion.append("- Nhiều lớp áo\n");
            suggestion.append("- Áo khoác dày\n");
            suggestion.append("- Quần dày\n");
            suggestion.append("- Găng tay và khăn quàng cổ\n");
            suggestion.append("- Giày ấm và tất dày\n");
        }

        // Đề xuất dựa trên điều kiện thời tiết
        if (weatherCondition != null) {
            switch (weatherCondition.toLowerCase()) {
                case "thunderstorm":
                    suggestion.append("\nCó giông bão \uD83C\uDF29\uFE0F, bạn nên:\n");
                    suggestion.append("- Mặc áo mưa hoặc mang ô\n");
                    suggestion.append("- Mang giày không thấm nước\n");
                    suggestion.append("- Tránh ra ngoài nếu có thể\n");
                    break;
                case "drizzle":
                    suggestion.append("\nCó mưa phùn \uD83C\uDF26\uFE0F, bạn nên:\n");
                    suggestion.append("- Mang ô nhỏ\n");
                    suggestion.append("- Mang giày không thấm nước\n");
                    break;
                case "rain":
                    suggestion.append("\nTrời mưa \uD83C\uDF27\uFE0F, bạn nên:\n");
                    suggestion.append("- Mặc áo mưa hoặc mang ô\n");
                    suggestion.append("- Mang giày không thấm nước\n");
                    suggestion.append("- Mang thêm áo khoác chống thấm\n");
                    break;
                case "snow":
                    suggestion.append("\nCó tuyết \uD83C\uDF28\uFE0F, bạn nên:\n");
                    suggestion.append("- Mặc nhiều lớp áo ấm\n");
                    suggestion.append("- Mang giày chống trượt\n");
                    suggestion.append("- Đội mũ và đeo găng tay\n");
                    break;
                case "atmosphere":
                    suggestion.append("\nThời tiết có sương mù/khói \uD83C\uDF01 , bạn nên:\n");
                    suggestion.append("- Mặc áo khoác nhẹ\n");
                    suggestion.append("- Mang khẩu trang\n");
                    break;
                case "clear":
                    suggestion.append("\nTrời quang ⛅, bạn nên:\n");
                    suggestion.append("- Mang kính râm\n");
                    suggestion.append("- Thoa kem chống nắng\n");
                    break;
                case "clouds":
                    suggestion.append("\nTrời nhiều mây ☁\uFE0F, bạn nên:\n");
                    suggestion.append("- Mang áo khoác nhẹ\n");
                    suggestion.append("- Mang ô phòng mưa\n");
                    break;
                case "extreme":
                    suggestion.append("\nThời tiết cực đoan ⛈\uFE0F, bạn nên:\n");
                    suggestion.append("- Ở trong nhà nếu có thể\n");
                    suggestion.append("- Mặc quần áo bảo hộ\n");
                    suggestion.append("- Mang đồ bảo vệ đầu\n");
                    break;
            }
        }

        // Đề xuất da trên độ ẩm
        if (humidity >= HIGH_HUMIDITY) {
            suggestion.append("\nĐộ ẩm cao, bạn nên:\n");
            suggestion.append("- Mặc quần áo thoáng khí\n");
            suggestion.append("- Mang thêm áo để thay\n");
            suggestion.append("- Mang khăn tay\n");
        }

        // Đề xuất dựa trên tốc độ gió
        if (windSpeed >= HIGH_WIND_SPEED) {
            suggestion.append("\nGió mạnh, bạn nên:\n");
            suggestion.append("- Mặc áo khoác chắn gió\n");
            suggestion.append("- Đội mũ có dây buộc\n");
            suggestion.append("- Mang khăn quàng cổ\n");
        }

        return suggestion.toString();
    }
} 
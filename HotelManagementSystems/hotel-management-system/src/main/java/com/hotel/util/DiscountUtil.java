package com.hotel.util;

public class DiscountUtil {

    public static double getDiscountPercent(String discountType, int plannedDays) {
        return switch (discountType) {
            case "Festival Discount" -> 10.0;
            case "Loyalty Discount" -> 12.0;
            case "Student Discount" -> 8.0;
            case "Corporate Discount" -> 15.0;
            case "Long Stay Discount" -> plannedDays >= 5 ? 18.0 : 0.0;
            default -> 0.0;
        };
    }

    public static double calculateDiscountAmount(double amount, String discountType, int plannedDays) {
        double percent = getDiscountPercent(discountType, plannedDays);
        return amount * percent / 100.0;
    }
}
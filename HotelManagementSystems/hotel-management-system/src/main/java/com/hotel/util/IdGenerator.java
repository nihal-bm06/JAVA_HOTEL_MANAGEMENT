package com.hotel.util;

import java.util.UUID;

public class IdGenerator {
    public static String generateCustomerId() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateBookingId() {
        return "BOOK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
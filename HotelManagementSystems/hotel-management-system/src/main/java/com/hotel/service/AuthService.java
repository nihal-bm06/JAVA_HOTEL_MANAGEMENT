package com.hotel.service;

public class AuthService {
    public static boolean login(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }
}
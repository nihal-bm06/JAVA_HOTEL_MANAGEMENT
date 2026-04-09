package com.hotel.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogService {
    private static final String LOG_FILE = "data/logs.txt";

    public static void log(String action, String details) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write(LocalDateTime.now() + " | " + action + " | " + details);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
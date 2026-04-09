package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.model.Feedback;
import com.hotel.model.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final String ROOM_FILE = "data/rooms.txt";
    private static final String BOOKING_FILE = "data/bookings.txt";
    private static final String FEEDBACK_FILE = "data/feedback.txt";

    public static List<Room> loadRooms() {
    List<Room> rooms = new ArrayList<>();
    File file = new File(ROOM_FILE);
    if (!file.exists()) return rooms;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",", 6);

            rooms.add(new Room(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    Double.parseDouble(parts[2]),
                    parts[3],
                    parts[4].replace(";", ","),
                    parts[5].replace(";", ",")
            ));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return rooms;
}

    public static void saveRooms(List<Room> rooms) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ROOM_FILE))) {
            for (Room room : rooms) {
                bw.write(room.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKING_FILE);
        if (!file.exists()) return bookings;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", 22);

                bookings.add(new Booking(
                        p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]), p[5],
                        p[6], p[7],
                        p[8], p[9],
                        p[10], p[11],
                        Integer.parseInt(p[12]), Integer.parseInt(p[13]),
                        Double.parseDouble(p[14]), Double.parseDouble(p[15]),
                        Double.parseDouble(p[16]), Double.parseDouble(p[17]),
                        Double.parseDouble(p[18]),
                        p[19], p[20], p[21]
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static void saveBookings(List<Booking> bookings) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
            for (Booking booking : bookings) {
                bw.write(booking.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Feedback> loadFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        File file = new File(FEEDBACK_FILE);
        if (!file.exists()) return feedbackList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", 3);
                feedbackList.add(new Feedback(
                        p[0].replace(";", ","),
                        Double.parseDouble(p[1]),
                        p[2].replace(";", ",")
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public static void saveFeedback(List<Feedback> feedbackList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEEDBACK_FILE))) {
            for (Feedback feedback : feedbackList) {
                bw.write(feedback.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
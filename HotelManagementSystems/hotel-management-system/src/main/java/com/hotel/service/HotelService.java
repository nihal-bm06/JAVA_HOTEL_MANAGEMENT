package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.model.Feedback;
import com.hotel.model.Room;
import com.hotel.util.BillGenerator;
import com.hotel.util.DiscountUtil;
import com.hotel.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class HotelService {
    private List<Room> rooms;
    private List<Booking> bookings;
    private List<Feedback> feedbackList;

    public HotelService() {
        reloadData();
    }

    public void reloadData() {
        rooms = FileService.loadRooms();
        bookings = FileService.loadBookings();
        feedbackList = FileService.loadFeedback();
    }

    public List<Room> getRooms() { return rooms; }
    public List<Booking> getBookings() { return bookings; }
    public List<Feedback> getFeedbackList() { return feedbackList; }

    public List<Booking> getActiveBookings() {
        List<Booking> active = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getStatus().equalsIgnoreCase("Active")) active.add(b);
        }
        return active;
    }

    public Booking findActiveBookingById(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equalsIgnoreCase(bookingId) && b.getStatus().equalsIgnoreCase("Active")) {
                return b;
            }
        }
        return null;
    }

    public synchronized void addRoom(Room room) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == room.getRoomNumber()) {
                throw new IllegalArgumentException("Room number already exists.");
            }
        }
        rooms.add(room);
        FileService.saveRooms(rooms);
        LogService.log("ADD_ROOM", "Room " + room.getRoomNumber() + " added.");
    }

    public synchronized void updateRoom(int roomNumber, String type, double price, String amenities, String notes) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                room.setRoomType(type);
                room.setPricePerDay(price);
                room.setAmenities(amenities);
                room.setNotes(notes);
            }
        }
        FileService.saveRooms(rooms);
        LogService.log("EDIT_ROOM", "Room " + roomNumber + " updated.");
    }

    public synchronized void deleteRoom(int roomNumber) {
        rooms.removeIf(r -> r.getRoomNumber() == roomNumber && r.isAvailable());
        FileService.saveRooms(rooms);
        LogService.log("DELETE_ROOM", "Room " + roomNumber + " deleted.");
    }

    public List<Room> getAvailableRooms() {
        List<Room> list = new ArrayList<>();
        for (Room room : rooms) if (room.isAvailable()) list.add(room);
        return list;
    }

    public List<Room> searchRooms(String keyword) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (String.valueOf(room.getRoomNumber()).contains(keyword) ||
                    room.getRoomType().toLowerCase().contains(keyword.toLowerCase()) ||
                    room.getAmenities().toLowerCase().contains(keyword.toLowerCase()) ||
                    room.getStatus().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(room);
            }
        }
        return result;
    }

    public List<Booking> searchBookingHistory(String keyword) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getBookingId().toLowerCase().contains(keyword.toLowerCase()) ||
                    b.getCustomerName().toLowerCase().contains(keyword.toLowerCase()) ||
                    String.valueOf(b.getRoomNumber()).contains(keyword)) {
                result.add(b);
            }
        }
        return result;
    }

    public synchronized Booking bookRoom(String customerName, String contactNumber, int roomNumber,
                                         LocalDate checkIn, LocalDate plannedCheckOut,
                                         String discountType, String paymentMethod) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber && room.isAvailable()) {
                int plannedDays = (int) ChronoUnit.DAYS.between(checkIn, plannedCheckOut);
                if (plannedDays <= 0) return null;

                room.setStatus("Occupied");

                double originalBill = room.getPricePerDay() * plannedDays;
                double discountAmount = DiscountUtil.calculateDiscountAmount(originalBill, discountType, plannedDays);
                double finalBill = originalBill - discountAmount;

                Booking booking = new Booking(
                        IdGenerator.generateBookingId(),
                        IdGenerator.generateCustomerId(),
                        customerName,
                        contactNumber,
                        roomNumber,
                        room.getRoomType(),
                        checkIn.toString(),
                        "14:00",
                        plannedCheckOut.toString(),
                        "11:00",
                        "-",
                        "-",
                        plannedDays,
                        plannedDays,
                        originalBill,
                        discountAmount,
                        0.0,
                        finalBill,
                        0.0,
                        discountType,
                        paymentMethod,
                        "Active"
                );

                bookings.add(booking);
                FileService.saveRooms(rooms);
                FileService.saveBookings(bookings);
                LogService.log("BOOK_ROOM", "Room " + roomNumber + " booked by " + customerName);
                return booking;
            }
        }
        return null;
    }

    public synchronized Booking checkoutByBookingId(String bookingId, LocalDate actualDate) {
        Booking booking = findActiveBookingById(bookingId);
        if (booking == null) return null;

        int roomNumber = booking.getRoomNumber();

        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                LocalDate checkIn = LocalDate.parse(booking.getCheckInDate());
                LocalDate plannedOut = LocalDate.parse(booking.getPlannedCheckOutDate());
                LocalTime actualTime = LocalTime.now().withSecond(0).withNano(0);

                if (actualDate == null || actualDate.isBefore(checkIn)) {
                    return null;
                }

                int stayedDays = (int) ChronoUnit.DAYS.between(checkIn, actualDate);
                if (stayedDays < 1) stayedDays = 1;

                double perDay = booking.getOriginalBill() / booking.getPlannedDays();
                double recalculatedOriginal = perDay * stayedDays;
                double discountAmount = DiscountUtil.calculateDiscountAmount(recalculatedOriginal, booking.getDiscountType(), stayedDays);
                double lateFee = calculateLateCheckoutFee(actualDate, plannedOut, actualTime, perDay);

                double newFinal = recalculatedOriginal - discountAmount + lateFee;
                double refund = booking.getFinalBill() - newFinal;
                if (refund < 0) refund = 0;

                booking.setActualCheckOutDate(actualDate.toString());
                booking.setActualCheckOutTime(actualTime.toString());
                booking.setChargedDays(stayedDays);
                booking.setLateCheckoutFee(lateFee);
                booking.setFinalBill(newFinal);
                booking.setRefundAmount(refund);
                booking.setStatus("Checked Out");

                room.setStatus("Cleaning");

                BillGenerator.generateBillFile(booking);
                FileService.saveRooms(rooms);
                FileService.saveBookings(bookings);
                LogService.log("CHECKOUT", "Booking " + bookingId + " checked out.");

                return booking;
            }
        }

        return null;
    }

    private double calculateLateCheckoutFee(LocalDate actualDate, LocalDate plannedOutDate, LocalTime actualTime, double perDay) {
        LocalTime standardCheckout = LocalTime.of(11, 0);

        if (actualDate.isBefore(plannedOutDate)) return 0.0;

        if (actualDate.isEqual(plannedOutDate)) {
            if (actualTime.isBefore(standardCheckout.plusMinutes(30))) return 0.0;
            else if (actualTime.isBefore(standardCheckout.plusHours(1))) return perDay * 0.10;
            else if (actualTime.isBefore(LocalTime.of(16, 0))) return perDay * 0.25;
            else return perDay;
        }

        long extraDays = ChronoUnit.DAYS.between(plannedOutDate, actualDate);
        if (extraDays < 1) extraDays = 1;
        return perDay * extraDays;
    }

    // ---------------- HOUSEKEEPING + THREAD SYNCHRONIZATION ----------------

    public synchronized String markRoomClean(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {

                if (!room.getStatus().equalsIgnoreCase("Cleaning")) {
                    return "Room is not currently in Cleaning status.";
                }

                // Intentional delay to make race condition visible for demo
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Re-check AFTER delay while still inside synchronized block
                if (!room.getStatus().equalsIgnoreCase("Cleaning")) {
                    return "Room was already cleaned by another staff member.";
                }

                room.setStatus("Available");
                FileService.saveRooms(rooms);
                LogService.log("HOUSEKEEPING", "Room " + roomNumber + " marked clean and available.");
                return "Room marked as clean successfully.";
            }
        }
        return "Room not found.";
    }

    public String simulateHousekeepingRace(int roomNumber) {
        StringBuilder resultLog = new StringBuilder();

        Runnable task = () -> {
            String result = markRoomClean(roomNumber);
            synchronized (resultLog) {
                resultLog.append(Thread.currentThread().getName())
                        .append(" -> ")
                        .append(result)
                        .append("\n");
            }
        };

        Thread t1 = new Thread(task, "Housekeeping-Staff-1");
        Thread t2 = new Thread(task, "Housekeeping-Staff-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return resultLog.toString();
    }

    public synchronized void addFeedback(String customerName, double rating, String comments) {
        feedbackList.add(new Feedback(customerName, rating, comments));
        FileService.saveFeedback(feedbackList);
        LogService.log("FEEDBACK", "Feedback submitted by " + customerName);
    }

    public int getTotalRooms() { return rooms.size(); }
    public int getAvailableRoomCount() { return (int) rooms.stream().filter(Room::isAvailable).count(); }
    public int getOccupiedRoomCount() { return (int) rooms.stream().filter(r -> r.getStatus().equalsIgnoreCase("Occupied")).count(); }
    public double getTotalRevenue() { return bookings.stream().mapToDouble(Booking::getFinalBill).sum(); }
    public double getOccupancyRate() { return rooms.isEmpty() ? 0 : ((double)getOccupiedRoomCount()/rooms.size())*100.0; }
    public long getCheckedOutCount() { return bookings.stream().filter(b -> b.getStatus().equalsIgnoreCase("Checked Out")).count(); }
    public long getActiveBookingCount() { return bookings.stream().filter(b -> b.getStatus().equalsIgnoreCase("Active")).count(); }

    public double getAverageRating() {
        if (feedbackList.isEmpty()) return 0.0;
        return feedbackList.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
    }

    public long getLowRatingCount() {
        return feedbackList.stream().filter(f -> f.getRating() < 3).count();
    }

    public long getMidRatingCount() {
        return feedbackList.stream().filter(f -> f.getRating() >= 3 && f.getRating() < 4.5).count();
    }

    public long getHighRatingCount() {
        return feedbackList.stream().filter(f -> f.getRating() >= 4.5).count();
    }

    public int getSingleRoomCount() {
        return (int) rooms.stream().filter(r -> r.getRoomType().equalsIgnoreCase("Single")).count();
    }

    public int getDoubleRoomCount() {
        return (int) rooms.stream().filter(r -> r.getRoomType().equalsIgnoreCase("Double")).count();
    }

    public int getDeluxeRoomCount() {
        return (int) rooms.stream().filter(r -> r.getRoomType().equalsIgnoreCase("Deluxe")).count();
    }

    public int getSuiteRoomCount() {
        return (int) rooms.stream().filter(r -> r.getRoomType().equalsIgnoreCase("Suite")).count();
    }

    public double getRevenueForRoomType(String roomType) {
        return bookings.stream()
                .filter(b -> b.getRoomType().equalsIgnoreCase(roomType))
                .mapToDouble(Booking::getFinalBill)
                .sum();
    }
}
package com.hotel.model;

public class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerDay;
    private String status; // Available, Occupied, Cleaning, Maintenance
    private String amenities;
    private String notes;

    public Room(int roomNumber, String roomType, double pricePerDay, boolean available, String amenities, String notes) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.status = available ? "Available" : "Occupied";
        this.amenities = amenities;
        this.notes = notes;
    }

    public Room(int roomNumber, String roomType, double pricePerDay, String status, String amenities, String notes) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.status = status;
        this.amenities = amenities;
        this.notes = notes;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public String getStatus() {
        return status;
    }

    public String getAmenities() {
        return amenities;
    }

    public String getNotes() {
        return notes;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAvailable() {
        return status.equalsIgnoreCase("Available");
    }

    public String getAvailabilityStatus() {
        return status;
    }

    public String toFileString() {
        return roomNumber + "," + roomType + "," + pricePerDay + "," +
                status + "," +
                amenities.replace(",", ";") + "," +
                notes.replace(",", ";");
    }
}
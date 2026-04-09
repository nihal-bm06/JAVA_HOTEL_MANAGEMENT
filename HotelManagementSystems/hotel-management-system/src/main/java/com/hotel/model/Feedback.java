package com.hotel.model;

public class Feedback {
    private String customerName;
    private double rating;
    private String comments;

    public Feedback(String customerName, double rating, String comments) {
        this.customerName = customerName;
        this.rating = rating;
        this.comments = comments;
    }

    public String getCustomerName() { return customerName; }
    public double getRating() { return rating; }
    public String getComments() { return comments; }

    public String toFileString() {
        return customerName.replace(",", ";") + "," + rating + "," + comments.replace(",", ";");
    }
}
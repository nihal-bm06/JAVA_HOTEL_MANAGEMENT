package com.hotel.model;

public class Booking {
    private String bookingId;
    private String customerId;
    private String customerName;
    private String contactNumber;
    private int roomNumber;
    private String roomType;

    private String checkInDate;
    private String checkInTime;

    private String plannedCheckOutDate;
    private String plannedCheckOutTime;

    private String actualCheckOutDate;
    private String actualCheckOutTime;

    private int plannedDays;
    private int chargedDays;

    private double originalBill;
    private double discountAmount;
    private double lateCheckoutFee;
    private double finalBill;
    private double refundAmount;

    private String discountType;
    private String paymentMethod;
    private String status;

    public Booking(String bookingId, String customerId, String customerName, String contactNumber,
                   int roomNumber, String roomType,
                   String checkInDate, String checkInTime,
                   String plannedCheckOutDate, String plannedCheckOutTime,
                   String actualCheckOutDate, String actualCheckOutTime,
                   int plannedDays, int chargedDays,
                   double originalBill, double discountAmount, double lateCheckoutFee,
                   double finalBill, double refundAmount,
                   String discountType, String paymentMethod, String status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.plannedCheckOutDate = plannedCheckOutDate;
        this.plannedCheckOutTime = plannedCheckOutTime;
        this.actualCheckOutDate = actualCheckOutDate;
        this.actualCheckOutTime = actualCheckOutTime;
        this.plannedDays = plannedDays;
        this.chargedDays = chargedDays;
        this.originalBill = originalBill;
        this.discountAmount = discountAmount;
        this.lateCheckoutFee = lateCheckoutFee;
        this.finalBill = finalBill;
        this.refundAmount = refundAmount;
        this.discountType = discountType;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getContactNumber() { return contactNumber; }
    public int getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }

    public String getCheckInDate() { return checkInDate; }
    public String getCheckInTime() { return checkInTime; }
    public String getPlannedCheckOutDate() { return plannedCheckOutDate; }
    public String getPlannedCheckOutTime() { return plannedCheckOutTime; }
    public String getActualCheckOutDate() { return actualCheckOutDate; }
    public String getActualCheckOutTime() { return actualCheckOutTime; }

    public int getPlannedDays() { return plannedDays; }
    public int getChargedDays() { return chargedDays; }

    public double getOriginalBill() { return originalBill; }
    public double getDiscountAmount() { return discountAmount; }
    public double getLateCheckoutFee() { return lateCheckoutFee; }
    public double getFinalBill() { return finalBill; }
    public double getRefundAmount() { return refundAmount; }

    public String getDiscountType() { return discountType; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }

    public void setActualCheckOutDate(String actualCheckOutDate) { this.actualCheckOutDate = actualCheckOutDate; }
    public void setActualCheckOutTime(String actualCheckOutTime) { this.actualCheckOutTime = actualCheckOutTime; }
    public void setChargedDays(int chargedDays) { this.chargedDays = chargedDays; }
    public void setLateCheckoutFee(double lateCheckoutFee) { this.lateCheckoutFee = lateCheckoutFee; }
    public void setFinalBill(double finalBill) { this.finalBill = finalBill; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return bookingId + "," + customerId + "," + customerName + "," + contactNumber + "," +
                roomNumber + "," + roomType + "," +
                checkInDate + "," + checkInTime + "," +
                plannedCheckOutDate + "," + plannedCheckOutTime + "," +
                actualCheckOutDate + "," + actualCheckOutTime + "," +
                plannedDays + "," + chargedDays + "," +
                originalBill + "," + discountAmount + "," + lateCheckoutFee + "," +
                finalBill + "," + refundAmount + "," +
                discountType + "," + paymentMethod + "," + status;
    }
}
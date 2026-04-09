package com.hotel.model;

public class BillPreview {
    private String bookingId;
    private String customerName;
    private double originalBill;
    private double discountAmount;
    private double finalBill;
    private double refundAmount;
    private String discountType;
    private String paymentMethod;
    private String status;

    public BillPreview(String bookingId, String customerName, double originalBill, double discountAmount,
                       double finalBill, double refundAmount, String discountType,
                       String paymentMethod, String status) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.originalBill = originalBill;
        this.discountAmount = discountAmount;
        this.finalBill = finalBill;
        this.refundAmount = refundAmount;
        this.discountType = discountType;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public double getOriginalBill() { return originalBill; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalBill() { return finalBill; }
    public double getRefundAmount() { return refundAmount; }
    public String getDiscountType() { return discountType; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
}
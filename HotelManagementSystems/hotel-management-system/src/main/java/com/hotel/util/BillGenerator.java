package com.hotel.util;

import com.hotel.model.Booking;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillGenerator {

    public static void generateBillFile(Booking booking) {
        generateTextBill(booking);
        generatePdfBill(booking);
    }

    private static String billContent(Booking booking) {
        String generatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"));

        return """
                ====================================================
                           GRAND STAY HOTEL & SUITES
                          Premium Hospitality Invoice
                ====================================================
                Bill Generated At : %s

                Booking ID        : %s
                Customer ID       : %s
                Customer Name     : %s
                Contact Number    : %s

                Room Number       : %d
                Room Type         : %s

                Check-In Date     : %s
                Check-In Time     : %s
                Planned Check-Out : %s
                Planned Out Time  : %s
                Actual Check-Out  : %s
                Actual Out Time   : %s

                Planned Nights    : %d
                Charged Nights    : %d

                ----------------------------------------------------
                Original Room Bill : ₹ %.2f
                Discount Type      : %s
                Discount Amount    : ₹ %.2f
                Late Checkout Fee  : ₹ %.2f
                Refund             : ₹ %.2f
                ----------------------------------------------------
                NET PAYABLE        : ₹ %.2f
                Payment Method     : %s
                Booking Status     : %s
                ====================================================
                Thank you for choosing Grand Stay Hotel & Suites!
                We look forward to welcoming you again.
                ====================================================
                """.formatted(
                generatedAt,
                booking.getBookingId(),
                booking.getCustomerId(),
                booking.getCustomerName(),
                booking.getContactNumber(),
                booking.getRoomNumber(),
                booking.getRoomType(),
                booking.getCheckInDate(),
                booking.getCheckInTime(),
                booking.getPlannedCheckOutDate(),
                booking.getPlannedCheckOutTime(),
                booking.getActualCheckOutDate(),
                booking.getActualCheckOutTime(),
                booking.getPlannedDays(),
                booking.getChargedDays(),
                booking.getOriginalBill(),
                booking.getDiscountType(),
                booking.getDiscountAmount(),
                booking.getLateCheckoutFee(),
                booking.getRefundAmount(),
                booking.getFinalBill(),
                booking.getPaymentMethod(),
                booking.getStatus()
        );
    }

    private static void generateTextBill(Booking booking) {
        try {
            File dir = new File("bills");
            if (!dir.exists()) dir.mkdirs();

            String fileName = "bills/" + booking.getBookingId() + ".txt";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                bw.write(billContent(booking));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePdfBill(Booking booking) {
        try {
            File dir = new File("bills");
            if (!dir.exists()) dir.mkdirs();

            String pdfName = "bills/" + booking.getBookingId() + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfName));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph title = new Paragraph("GRAND STAY HOTEL & SUITES - INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(billContent(booking), bodyFont));
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
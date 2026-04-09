package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.service.HotelService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DashboardController {

    private final HotelService hotelService = new HotelService();

    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label occupiedRoomsLabel;
    @FXML private Label revenueLabel;
    @FXML private Label occupancyRateLabel;
    @FXML private Label activeBookingsLabel;
    @FXML private Label checkedOutLabel;
    @FXML private Label avgRatingLabel;

    @FXML private TableView<Room> roomTable;
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableView<Booking> historyTable;

    @FXML private TextField roomNumberField;
    @FXML private ComboBox<String> roomTypeBox;
    @FXML private TextField priceField;
    @FXML private TextField amenitiesField;
    @FXML private TextField notesField;
    @FXML private TextField searchRoomField;

    @FXML private TextField customerNameField;
    @FXML private TextField contactField;
    @FXML private TextField bookingRoomNumberField;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private ComboBox<String> discountBox;
    @FXML private ComboBox<String> paymentBox;

    @FXML private TextField checkoutBookingIdField;
    @FXML private TextField checkoutCustomerField;
    @FXML private TextField checkoutRoomField;
    @FXML private DatePicker actualCheckoutDatePicker;

    @FXML private TextArea billPreviewArea;
    @FXML private TextField historySearchField;

    @FXML private TextField feedbackCustomerField;
    @FXML private TextField ratingField;
    @FXML private TextArea feedbackCommentsArea;

    @FXML private PieChart occupancyPieChart;
    @FXML private PieChart bookingStatusPieChart;
    @FXML private BarChart<String, Number> roomTypeBarChart;
    @FXML private BarChart<String, Number> revenueByTypeBarChart;
    @FXML private BarChart<String, Number> ratingBarChart;

    @FXML private TextField housekeepingRoomField;

    @FXML
    public void initialize() {
        roomTypeBox.getItems().addAll("Single", "Double", "Deluxe", "Suite");
        discountBox.getItems().addAll("No Discount", "Festival Discount", "Loyalty Discount",
                "Student Discount", "Corporate Discount", "Long Stay Discount");
        paymentBox.getItems().addAll("Cash", "UPI", "Card");

        setupRoomTable();
        setupBookingTable();
        setupHistoryTable();

        roomTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                roomNumberField.setText(String.valueOf(selected.getRoomNumber()));
                roomTypeBox.setValue(selected.getRoomType());
                priceField.setText(String.valueOf(selected.getPricePerDay()));
                amenitiesField.setText(selected.getAmenities());
                notesField.setText(selected.getNotes());
            }
        });

        bookingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                checkoutBookingIdField.setText(selected.getBookingId());
                checkoutCustomerField.setText(selected.getCustomerName());
                checkoutRoomField.setText(String.valueOf(selected.getRoomNumber()));
                showBillPreview(selected);
            }
        });

        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) showBillPreview(selected);
        });

        refreshAll();
    }

    private void setupRoomTable() {
        roomTable.getColumns().clear();

        TableColumn<Room, Integer> roomNoCol = new TableColumn<>("Room No");
        roomNoCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRoomNumber()).asObject());

        TableColumn<Room, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomType()));

        TableColumn<Room, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPricePerDay()).asObject());

        TableColumn<Room, String> amenityCol = new TableColumn<>("Amenities");
        amenityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAmenities()));

        TableColumn<Room, String> noteCol = new TableColumn<>("Notes");
        noteCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNotes()));

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAvailabilityStatus()));

        roomTable.getColumns().addAll(roomNoCol, typeCol, priceCol, amenityCol, noteCol, statusCol);
        roomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupBookingTable() {
        bookingTable.getColumns().clear();

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingId()));

        TableColumn<Booking, String> nameCol = new TableColumn<>("Customer");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));

        TableColumn<Booking, Integer> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRoomNumber()).asObject());

        TableColumn<Booking, String> inDateCol = new TableColumn<>("Check-In");
        inDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckInDate()));

        TableColumn<Booking, String> outPlanCol = new TableColumn<>("Planned Checkout");
        outPlanCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlannedCheckOutDate()));

        TableColumn<Booking, Double> billCol = new TableColumn<>("Expected Bill");
        billCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getFinalBill()).asObject());

        bookingTable.getColumns().addAll(idCol, nameCol, roomCol, inDateCol, outPlanCol, billCol);
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupHistoryTable() {
        historyTable.getColumns().clear();

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingId()));

        TableColumn<Booking, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));

        TableColumn<Booking, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactNumber()));

        TableColumn<Booking, Integer> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRoomNumber()).asObject());

        TableColumn<Booking, String> checkInCol = new TableColumn<>("Check-In");
        checkInCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckInDate()));

        TableColumn<Booking, String> plannedOutCol = new TableColumn<>("Planned Checkout");
        plannedOutCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlannedCheckOutDate()));

        TableColumn<Booking, String> actualOutCol = new TableColumn<>("Actual Checkout");
        actualOutCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActualCheckOutDate()));

        TableColumn<Booking, String> actualOutTimeCol = new TableColumn<>("Checkout Time");
        actualOutTimeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActualCheckOutTime()));

        TableColumn<Booking, Double> lateFeeCol = new TableColumn<>("Late Fee");
        lateFeeCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getLateCheckoutFee()).asObject());

        TableColumn<Booking, Double> refundCol = new TableColumn<>("Refund");
        refundCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getRefundAmount()).asObject());

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        historyTable.getColumns().addAll(idCol, customerCol, contactCol, roomCol, checkInCol,
                plannedOutCol, actualOutCol, actualOutTimeCol, lateFeeCol, refundCol, statusCol);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleAddRoom() {
        try {
            hotelService.addRoom(new Room(
                    Integer.parseInt(roomNumberField.getText()),
                    roomTypeBox.getValue(),
                    Double.parseDouble(priceField.getText()),
                    true,
                    amenitiesField.getText().trim(),
                    notesField.getText().trim()
            ));
            clearRoomFields();
            refreshAll();
            showAlert("Success", "Room added successfully.");
        } catch (Exception e) {
            showAlert("Error", "Invalid room details.");
        }
    }

    @FXML
    public void handleEditRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Select a room first.");
            return;
        }

        try {
            hotelService.updateRoom(
                    selected.getRoomNumber(),
                    roomTypeBox.getValue(),
                    Double.parseDouble(priceField.getText()),
                    amenitiesField.getText().trim(),
                    notesField.getText().trim()
            );
            refreshAll();
            showAlert("Success", "Room updated successfully.");
        } catch (Exception e) {
            showAlert("Error", "Could not update room.");
        }
    }

    @FXML
    public void handleDeleteRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            hotelService.deleteRoom(selected.getRoomNumber());
            refreshAll();
            showAlert("Success", "Room deleted successfully.");
        } else {
            showAlert("No Selection", "Select a room first.");
        }
    }

    @FXML
    public void handleSearchRoom() {
        roomTable.setItems(FXCollections.observableArrayList(
                hotelService.searchRooms(searchRoomField.getText().trim())
        ));
    }

    @FXML
    public void handleViewAllRooms() {
        refreshAll();
    }

    @FXML
    public void handleAvailableRooms() {
        roomTable.setItems(FXCollections.observableArrayList(hotelService.getAvailableRooms()));
    }

   @FXML
public void handleBookRoom() {
    try {
        String customerName = customerNameField.getText().trim();
        String contact = contactField.getText().trim();

        if (!isValidCustomerName(customerName)) {
            showAlert("Invalid Name", "Customer name should contain only letters and spaces.");
            return;
        }

        if (!isValidContactNumber(contact)) {
            showAlert("Invalid Contact Number", "Contact number must be exactly 10 digits.");
            return;
        }

        Booking booking = hotelService.bookRoom(
                customerName,
                contact,
                Integer.parseInt(bookingRoomNumberField.getText().trim()),
                checkInPicker.getValue(),
                checkOutPicker.getValue(),
                discountBox.getValue() == null ? "No Discount" : discountBox.getValue(),
                paymentBox.getValue() == null ? "Cash" : paymentBox.getValue()
        );

        if (booking != null) {
            clearBookingFields();
            billPreviewArea.clear();
            refreshAll();
            showAlert("Booking Success", "Room booked successfully.\nBooking ID: " + booking.getBookingId());
        } else {
            showAlert("Booking Failed", "Invalid booking or room unavailable.");
        }
    } catch (Exception e) {
        showAlert("Error", "Invalid booking details.");
    }
}
    @FXML
    public void handleCheckoutByBookingId() {
        try {
            String bookingId = checkoutBookingIdField.getText().trim();
            Booking checkedOut = hotelService.checkoutByBookingId(bookingId, actualCheckoutDatePicker.getValue());

            if (checkedOut != null) {
                refreshAll();
                showBillPreview(checkedOut);
                clearCheckoutFields();
                showAlert("Checkout Complete", "Checkout completed successfully for Booking ID: " + bookingId);
            } else {
                showAlert("Checkout Failed", "Invalid Booking ID or invalid checkout date.");
            }
        } catch (Exception e) {
            showAlert("Error", "Checkout failed.");
        }
    }

    @FXML
    public void handleSearchHistory() {
        historyTable.setItems(FXCollections.observableArrayList(
                hotelService.searchBookingHistory(historySearchField.getText().trim())
        ));
    }

    @FXML
    public void handleAddFeedback() {
        try {
            double rating = Double.parseDouble(ratingField.getText().trim());

            if (rating < 0 || rating > 5) {
                showAlert("Invalid Rating", "Rating must be between 0 and 5.");
                return;
            }

            hotelService.addFeedback(
                    feedbackCustomerField.getText().trim(),
                    rating,
                    feedbackCommentsArea.getText().trim()
            );

            feedbackCustomerField.clear();
            ratingField.clear();
            feedbackCommentsArea.clear();
            refreshAll();
            showAlert("Success", "Feedback submitted successfully.");
        } catch (Exception e) {
            showAlert("Error", "Enter a valid decimal rating between 0 and 5.");
        }
    }

    @FXML
    public void handleRefreshAll() {
        refreshAll();
        clearCheckoutFields();
        clearRoomFields();
        clearBookingFields();
        historySearchField.clear();
        billPreviewArea.clear();
        roomTable.getSelectionModel().clearSelection();
        bookingTable.getSelectionModel().clearSelection();
        historyTable.getSelectionModel().clearSelection();
    }

    @FXML
public void handleMarkClean() {
    try {
        int roomNo = Integer.parseInt(housekeepingRoomField.getText().trim());
        String result = hotelService.markRoomClean(roomNo);
        refreshAll();
        showAlert("Housekeeping", result);
    } catch (Exception e) {
        showAlert("Error", "Enter a valid room number.");
    }
}

@FXML
public void handleSimulateCleaningRace() {
    try {
        int roomNo = Integer.parseInt(housekeepingRoomField.getText().trim());

        String simulationResult = hotelService.simulateHousekeepingRace(roomNo);

        refreshAll();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Synchronization Demo Result");
        alert.setHeaderText("Housekeeping Thread Synchronization");
        alert.setContentText(simulationResult);
        alert.showAndWait();

    } catch (Exception e) {
        showAlert("Error", "Enter a valid room number.");
    }
}

@FXML
public void handleLogout() {
    try {
        Stage stage = (Stage) totalRoomsLabel.getScene().getWindow();
        Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/login.fxml")));
        loginScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(loginScene);
        stage.setTitle("Hotel Management Login");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.centerOnScreen();
    } catch (Exception e) {
        showAlert("Error", "Failed to logout.");
    }
}

private boolean isValidCustomerName(String name) {
    return name != null && name.matches("[A-Za-z ]+");
}

private boolean isValidContactNumber(String contact) {
    return contact != null && contact.matches("\\d{10}");
}

    private void refreshAll() {
        hotelService.reloadData();

        roomTable.setItems(FXCollections.observableArrayList(hotelService.getRooms()));
        bookingTable.setItems(FXCollections.observableArrayList(hotelService.getActiveBookings()));
        historyTable.setItems(FXCollections.observableArrayList(hotelService.getBookings()));

        totalRoomsLabel.setText(String.valueOf(hotelService.getTotalRooms()));
        availableRoomsLabel.setText(String.valueOf(hotelService.getAvailableRoomCount()));
        occupiedRoomsLabel.setText(String.valueOf(hotelService.getOccupiedRoomCount()));
        revenueLabel.setText(String.format("%.2f", hotelService.getTotalRevenue()));
        occupancyRateLabel.setText(String.format("%.2f %%", hotelService.getOccupancyRate()));
        activeBookingsLabel.setText(String.valueOf(hotelService.getActiveBookingCount()));
        checkedOutLabel.setText(String.valueOf(hotelService.getCheckedOutCount()));
        avgRatingLabel.setText(String.format("%.2f / 5", hotelService.getAverageRating()));

        loadCharts();
    }

    private void loadCharts() {
        if (occupancyPieChart != null) {
            occupancyPieChart.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Available", hotelService.getAvailableRoomCount()),
                    new PieChart.Data("Occupied", hotelService.getOccupiedRoomCount())
            ));
        }

        if (bookingStatusPieChart != null) {
            bookingStatusPieChart.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Active", hotelService.getActiveBookingCount()),
                    new PieChart.Data("Checked Out", hotelService.getCheckedOutCount())
            ));
        }

        if (roomTypeBarChart != null) {
            roomTypeBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Room Types");
            series.getData().add(new XYChart.Data<>("Single", hotelService.getSingleRoomCount()));
            series.getData().add(new XYChart.Data<>("Double", hotelService.getDoubleRoomCount()));
            series.getData().add(new XYChart.Data<>("Deluxe", hotelService.getDeluxeRoomCount()));
            series.getData().add(new XYChart.Data<>("Suite", hotelService.getSuiteRoomCount()));
            roomTypeBarChart.getData().add(series);
        }

        if (revenueByTypeBarChart != null) {
            revenueByTypeBarChart.getData().clear();
            XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
            revenueSeries.setName("Revenue by Room Type");
            revenueSeries.getData().add(new XYChart.Data<>("Single", hotelService.getRevenueForRoomType("Single")));
            revenueSeries.getData().add(new XYChart.Data<>("Double", hotelService.getRevenueForRoomType("Double")));
            revenueSeries.getData().add(new XYChart.Data<>("Deluxe", hotelService.getRevenueForRoomType("Deluxe")));
            revenueSeries.getData().add(new XYChart.Data<>("Suite", hotelService.getRevenueForRoomType("Suite")));
            revenueByTypeBarChart.getData().add(revenueSeries);
        }

        if (ratingBarChart != null) {
            ratingBarChart.getData().clear();
            XYChart.Series<String, Number> ratingSeries = new XYChart.Series<>();
            ratingSeries.setName("Customer Ratings");
            ratingSeries.getData().add(new XYChart.Data<>("Low", hotelService.getLowRatingCount()));
            ratingSeries.getData().add(new XYChart.Data<>("Mid", hotelService.getMidRatingCount()));
            ratingSeries.getData().add(new XYChart.Data<>("High", hotelService.getHighRatingCount()));
            ratingBarChart.getData().add(ratingSeries);
        }
    }

    private void showBillPreview(Booking booking) {
        if (booking == null) {
            billPreviewArea.clear();
            return;
        }

        billPreviewArea.setText("""
                ====================================================
                           GRAND STAY HOTEL & SUITES
                                BILL PREVIEW
                ====================================================

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
                """.formatted(
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
        ));
    }

    private void clearRoomFields() {
        roomNumberField.clear();
        roomTypeBox.setValue(null);
        priceField.clear();
        amenitiesField.clear();
        notesField.clear();
    }

    private void clearBookingFields() {
        customerNameField.clear();
        contactField.clear();
        bookingRoomNumberField.clear();
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);
        discountBox.setValue(null);
        paymentBox.setValue(null);
    }

    private void clearCheckoutFields() {
        checkoutBookingIdField.clear();
        checkoutCustomerField.clear();
        checkoutRoomField.clear();
        actualCheckoutDatePicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
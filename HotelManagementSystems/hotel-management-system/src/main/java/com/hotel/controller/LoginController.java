package com.hotel.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.equals("admin") && password.equals("admin123")) {
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene dashboardScene = new Scene(FXMLLoader.load(getClass().getResource("/dashboard.fxml")));
                dashboardScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

                stage.setScene(dashboardScene);
                stage.setTitle("Hotel Management Dashboard");
                stage.setMaximized(true);
            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Failed to load dashboard.");
            }
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }
}
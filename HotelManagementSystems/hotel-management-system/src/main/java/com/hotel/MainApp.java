package com.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/login.fxml")));
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("Hotel Management System");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
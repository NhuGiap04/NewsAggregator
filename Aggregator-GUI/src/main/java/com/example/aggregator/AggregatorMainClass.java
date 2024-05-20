package com.example.aggregator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class AggregatorMainClass extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, 1280, 780);

        stage.setTitle("Blockchain News Aggregator 1.0.0");
        //stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            int[] ports = {9696, 9999};
            for (int port : ports) {
                try {
                    URI uri = new URI("http://localhost:" + port + "/shutdown");
                    HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                    http.setRequestMethod("GET");
                    http.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public static void main(String[] args) {
        launch();
    }
}
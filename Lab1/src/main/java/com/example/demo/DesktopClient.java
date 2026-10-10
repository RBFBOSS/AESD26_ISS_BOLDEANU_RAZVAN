package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DesktopClient {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/demo_war_exploded/desktop");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded"
        );

        String parameters = "value=1";

        try (OutputStream output = connection.getOutputStream()) {
            output.write(parameters.getBytes(StandardCharsets.UTF_8));
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(), StandardCharsets.UTF_8))) {

            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("Server response: " + response);
            }
        }

        connection.disconnect();
    }

}
package com.example.demoproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Converter {

    public static List<Article> convertFromFile(String jsonFilePath) throws IOException {
        // Create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Read JSON file and convert to Article object
        return mapper.readValue(new File(jsonFilePath), new TypeReference<List<Article>>(){});
    }

    public static List<Article> convertFromRequest(String apiUrl) throws IOException {
        // Create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Create a URI object from the apiUrl
        URI uri = null;
        try {
            uri = new URI(apiUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Open a connection to the URI and cast to HttpURLConnection
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        // Set the request method
        http.setRequestMethod("GET");

        // Get the input stream of the http connection
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(http.getInputStream()));

        // Read the response line by line
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = inputReader.readLine()) != null) {
            content.append(inputLine);
        }

        // Close connections
        inputReader.close();
        http.disconnect();

        // Convert the response to a list of Article objects
        return mapper.readValue(content.toString(), new TypeReference<List<Article>>() {
        });
    }
}
package com.example.aggregator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Scene1Controller extends ArticleDisplay implements Initializable {

    @FXML
    private Button userGuide;

    List<Article> recentArticles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentArticles = new ArrayList<>(recentArticles());
        displayResult(recentArticles);
    }
    private List<Article> recentArticles() {
        return Converter.convertFromFile("src/main/resources/com/example/aggregator/recent.json");
    }


}
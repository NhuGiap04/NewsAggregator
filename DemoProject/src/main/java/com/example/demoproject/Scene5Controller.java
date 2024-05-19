package com.example.demoproject;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Scene5Controller extends ArticleDisplay implements Initializable {

    private static List<Article> history = new ArrayList<Article>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayResult(history);
    }

    public static void addToHistory(Article article) {
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).equals(article)) {
                history.remove(i);
                break;
            }
        }
        history.add(article);
    }
}

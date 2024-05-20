package com.example.aggregator;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Scene4Controller extends ArticleDisplay implements Initializable {
    private static List<Article> bookmarkedArticles = new ArrayList<Article>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayResult(bookmarkedArticles);
    }


    public static void addBookmark (Article article) {
        if (bookmarkedArticles.contains(article)) {return;}
        bookmarkedArticles.add(article);
    }

    public static void removeBookmark (Article article) {
        for (int i = 0; i < bookmarkedArticles.size(); i++) {
            if (bookmarkedArticles.get(i).equals(article)) {
                bookmarkedArticles.remove(i);
                return;
            }
        }
    }

}

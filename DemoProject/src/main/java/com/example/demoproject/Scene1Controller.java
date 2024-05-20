package com.example.demoproject;

import com.example.demoproject.article.Article;
import com.example.demoproject.article.ArticleCardController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    private List<Article> recentArticles(){
        List<Article> list = new ArrayList<>();
        Article article = new Article();
        list.add(article);
        list.add(article);
        list.add(article);
        return list;
    }


}
package com.example.demoproject;

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

public class Scene1Controller extends Scenes implements Initializable {

    @FXML
    private Button userGuide;
    @FXML
    VBox articlesLayout;
    List<Article> recentArticles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentArticles = new ArrayList<>(recentArticles());
        try {
            for (Article recentArticle : recentArticles) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("articleCard.fxml"));
                HBox box = fxmlLoader.load();
                ArticleCardController articleCardController = fxmlLoader.getController();
                articleCardController.setData(recentArticle);
                articlesLayout.getChildren().add(box);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
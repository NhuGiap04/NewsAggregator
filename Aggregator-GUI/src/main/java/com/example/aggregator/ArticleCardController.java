package com.example.aggregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ArticleCardController implements Initializable {
    private static final int NUMBER_OF_THUMBNAILS = 22;

    private Article article;

    @FXML
    private ImageView articleImage;

    @FXML
    private Label articleDate;

    @FXML
    private Label articleContent;

    @FXML
    private Label articleSubtitle;

    @FXML
    private Label articleTitle;

    @FXML
    private HBox box;

    @FXML
    private Button openInBrowserButton;

    @FXML
    private Button readButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void readArticle (ActionEvent event) throws IOException {
        Scene5Controller.addToHistory(article);

        FXMLLoader fxmlLoader = new FXMLLoader(AggregatorMainClass.class.getResource("main3.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Scene3Controller scene3controller = fxmlLoader.getController();
        scene3controller.setPreScene(readButton.getScene());
        scene3controller.setArticle(article);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openInBrowser (ActionEvent event) throws IOException {
        try {
            Desktop.getDesktop().browse(new URI(article.getLink()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Image generateImage(){
        Random rand = new Random();
        int i = rand.nextInt(NUMBER_OF_THUMBNAILS) + 1;
        return new Image("com/example/aggregator/images/articlethumbnails/" +i+".png");
    }

    public void setData(Article article) {
        this.article = article;

        //Image image = new Image(getClass().getResourceAsStream(article.getImageSrc()));
        //Image image = new Image("com/example/aggregator/images/article01.png");
        //articleImage.setImage(image);
        articleImage.setImage(generateImage());

        Rectangle clip = new Rectangle();
        clip.setWidth(140.0f);
        clip.setHeight(105.0f);
        clip.setArcHeight(20);
        clip.setArcWidth(20);
        articleImage.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = articleImage.snapshot(parameters, null);

        articleImage.setClip(null);
        articleImage.setImage(image);

        articleTitle.setText(article.getTitle());
        articleSubtitle.setText(article.getLink());
        articleDate.setText(article.getDate());
        articleContent.setText(article.getContent());
    }
}

package com.example.aggregator;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Scenes {

    // Functionalities related to switching scenes

    private Scene preScene;
    public void setPreScene(Scene preScene) {
        this.preScene = preScene;
    }

    public void newScene(ActionEvent event, String address) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AggregatorMainClass.class.getResource(address));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Scenes sceneController = fxmlLoader.getController();
        sceneController.setPreScene(((Node)event.getSource()).getScene());

        stage.setScene(scene);
        stage.show();
    }
    public void onClickBack(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(preScene);
        stage.show();
    }
    public void toHomePage(ActionEvent event) throws IOException {
        newScene(event, "main.fxml");
    }
    public void toBookmarkPage(ActionEvent event) throws IOException {
        settingsMenu.setVisible(false);
        coverPane.setVisible(false);
        newScene(event, "main4.fxml");
    }
    public void toHistoryPage(ActionEvent event) throws IOException {
        settingsMenu.setVisible(false);
        coverPane.setVisible(false);
        newScene(event, "main5.fxml");
    }
    public void openManual(ActionEvent event) throws URISyntaxException {
        URI userManualLink = new URI("https://husteduvn-my.sharepoint.com/:f:/g/personal/duc_nm225437_sis_hust_edu_vn/EnppM9_fW_NBmloH1ERA3vwBGH75U3x4Py09yPza09APvg?e=7zWjYs");
        try {
            Desktop.getDesktop().browse(userManualLink);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Functionalities related to the setting button
    @FXML
    private Button settings;
    @FXML
    private AnchorPane coverPane;
    @FXML
    private AnchorPane settingsMenu;
    @FXML
    private Button openBookmark;
    @FXML
    private Button openHistory;
    @FXML
    private Button openManual;
    @FXML
    private Button closeSettings;
    public void openSettings (ActionEvent event) {
        settingsMenu.setVisible(true);
        coverPane.setVisible(true);
    }
    public void closeSettings (ActionEvent event) {
        settingsMenu.setVisible(false);
        coverPane.setVisible(false);
    }


    // Functionalities related to using the search bar

    @FXML
    private TextField searchBar;

    public void search(ActionEvent event) throws IOException {
        String query = searchBar.getText();
        if (!query.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main2.fxml"));
            Parent root = loader.load();

            Scene2Controller scene2Controller = loader.getController();
            scene2Controller.search(query);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}

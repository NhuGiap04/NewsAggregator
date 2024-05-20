package com.example.aggregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Scene2Controller extends ArticleDisplay implements Initializable {

    @FXML
    private Button byDate;

    @FXML
    private Button bySource;

    @FXML
    private Button postOrder;

    @FXML
    private Button resetFilters;

    @FXML
    private Button search;

    @FXML
    private Button toHomePage;

    @FXML
    private AnchorPane filterWrapPane;

    @FXML
    private AnchorPane byDateWrapPane;

    @FXML
    private AnchorPane bySourceWrapPane;

    @FXML
    private DatePicker beginDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private CheckBox source1;
    @FXML
    private CheckBox source2;
    @FXML
    private CheckBox source3;
    @FXML
    private CheckBox source4;

    public String query;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FilterEngine.searchByQuery(query);

        displayResult(FilterEngine.searchResults);
        filterWrapPane.setVisible(false);
        byDateWrapPane.setVisible(false);
        bySourceWrapPane.setVisible(false);
    }

    public void search(String query) throws IOException {
        this.query = query;
        resetResult();
        displayResult(FilterEngine.searchByQuery(query));
    }

    public void resetResult() {
        cardLayout.getChildren().clear();
    }

    public void orderSelect(ActionEvent event) throws IOException {
        if (FilterEngine.mostRecentSelected) {
            postOrder.setText("Most Relevant");
            FilterEngine.mostRecentSelected = false;
            resetResult();
            displayResult(FilterEngine.applyFilter());
        } else {
            postOrder.setText("Most Recent");
            FilterEngine.mostRecentSelected = true;
            resetResult();
            displayResult(FilterEngine.applyFilter());
        }
    }

    public void byDateSelect(ActionEvent event) throws IOException {
        filterWrapPane.setVisible(true);
        byDateWrapPane.setVisible(true);
        byDate.getStyleClass().removeAll("filter-button");
        byDate.getStyleClass().add("filter-button-selected");
    }

    public void byDateConfirm(ActionEvent event) throws IOException {
        FilterEngine.beginDate = beginDatePicker.getValue();
        FilterEngine.endDate = endDatePicker.getValue();
        if (FilterEngine.beginDate == null && FilterEngine.endDate == null) {
            byDate.getStyleClass().removeAll("filter-button-selected");
            byDate.getStyleClass().add("filter-button");
        }
        filterWrapPane.setVisible(false);
        byDateWrapPane.setVisible(false);
        resetResult();
        displayResult(FilterEngine.applyFilter());
    }

    public void bySourceSelect(ActionEvent event) throws IOException {
        filterWrapPane.setVisible(true);
        bySourceWrapPane.setVisible(true);
        bySource.getStyleClass().removeAll("filter-button");
        bySource.getStyleClass().add("filter-button-selected");
    }

    public void bySourceConfirm(ActionEvent event) throws IOException {
        FilterEngine.sources = new ArrayList<>();
        List<CheckBox> listOfSources = Arrays.asList(source1,source2,source3,source4);
        for (CheckBox checkBox : listOfSources) {
            if (checkBox.isSelected()) {
                FilterEngine.sources.add(checkBox.getText());}
        }
        filterWrapPane.setVisible(false);
        bySourceWrapPane.setVisible(false);
        resetResult();
        displayResult(FilterEngine.applyFilter());
    }

    public void resetFilter(ActionEvent event) throws IOException {
        FilterEngine.mostRecentSelected = false;
        postOrder.setText("Most Relevant");

        byDate.getStyleClass().removeAll("filter-button-selected");
        byDate.getStyleClass().add("filter-button");
        FilterEngine.beginDate = null;
        FilterEngine.endDate = null;

        bySource.getStyleClass().removeAll("filter-button-selected");
        bySource.getStyleClass().add("filter-button");
        FilterEngine.sources = null;

        resetResult();
        displayResult(FilterEngine.searchResults);
    }
}
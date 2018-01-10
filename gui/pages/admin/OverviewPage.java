package com.TobyMellor.TrainRouteFinder.gui.pages.admin;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.BasicRouteTableRow;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.JourneyTableRow;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.StationTableRow;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.TableManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Allows admin users to see all of the three main data points in the system
 *     - <code>Station</code>s
 *     - <code>BasicRoute</code>s
 *     - <code>Journey</code>s
 *
 * The page contains 3 tables showing these three main data points and provides
 * a simple explanation beneath each table.
 *
 * Each table allows the user to delete the selected item. An item can be selected by clicking on a row on the table.
 * However, in this version <code>BasicRoute</code>s and <code>DestinationStation</code>s cannot be deleted as this
 * was not specified in the requirements document.
 *
 * An additional feature is 'Force reload all from file' which removes all changes from the current session
 * and overwrites them with whatever is written in the XML files
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/28 00:42:00 $
 */

public class OverviewPage extends Layout {
    TableView<JourneyTableRow> journeyTable;
    TableView<StationTableRow> stationTable;
    TableView<BasicRouteTableRow> basicRouteTable;

    Button deleteSelectedIntermediateStationButton;
    Button deleteSelectedJourneyButton;
    Button deleteSelectedBasicRouteButton;

    public OverviewPage(Stage primaryStage) {
        setPrimaryStage(primaryStage);

        Button backButton = ElementManager.createButton("<- Back to the Admin Menu", 0);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new AdminMenuPage(getPrimaryStage());
            }
        });

        Button forceReloadButton = ElementManager.createButton("Force reload all from file", 0, 0, null, Pos.TOP_RIGHT, "button-green");
        forceReloadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                reloadXML(); // replace session data with the data in the XML files
                new OverviewPage(getPrimaryStage()); // just restart the page to save calling multiple functions
            }
        });

        Text stationTableTitle = ElementManager.createText("All Stations", 40, "h3");

        stationTable = TableManager.setupStationTable();
        StackPane.setAlignment(stationTable, Pos.TOP_LEFT);
        StackPane.setMargin(stationTable, new Insets(70, 0, 0, 0));
        stationTable.setMaxHeight(150);
        stationTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StationTableRow>() {
            public void changed(ObservableValue<? extends StationTableRow> observable, StationTableRow oldValue, StationTableRow newValue) {
                deleteSelectedIntermediateStationButton.setDisable(false);
            }
        });

        TableManager.populateStationTable(stationTable, App.getStationManager().getDestinationStations(), App.getStationManager().getIntermediateStations());

        deleteSelectedIntermediateStationButton = ElementManager.createButton("Delete Selected Station", 225, 0, null, Pos.TOP_RIGHT, "button-red");
        deleteSelectedIntermediateStationButton.setDisable(true);
        deleteSelectedIntermediateStationButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                deleteIntermediateStation();
            }
        });
        
        Text stationTableExplanation = ElementManager.createText("A 'Destination Station' is where a customer will get on and off, this type can never be deleted.\nAn 'Intermediate Station' is where the train calls at on a Journey between Destination Stations.", 225, "h6");
        Text journeyTableTitle = ElementManager.createText("All Journeys", 250, "h3");

        journeyTable = TableManager.setupJourneyTable("No journeys have been created!");
        StackPane.setAlignment(journeyTable, Pos.TOP_LEFT);
        StackPane.setMargin(journeyTable, new Insets(280, 0, 0, 0));
        journeyTable.setMaxHeight(150);
        journeyTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<JourneyTableRow>() {
            public void changed(ObservableValue<? extends JourneyTableRow> observable, JourneyTableRow oldValue, JourneyTableRow newValue) {
                deleteSelectedJourneyButton.setDisable(false);
            }
        });

        TableManager.populateJourneyTable(journeyTable, App.getJourneyManager().getJourneys(), false);

        deleteSelectedJourneyButton = ElementManager.createButton("Delete Selected Journey", 435, 0, null, Pos.TOP_RIGHT, "button-red");
        deleteSelectedJourneyButton.setDisable(true);
        deleteSelectedJourneyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                deleteJourney();
            }
        });

        Text journeyTableExplanation = ElementManager.createText("A Journey is a Basic Route with a time of departure and intermediate stations.\nThis is what the customer is searching for.", 435, "h6");
        Text basicRouteTableTitle = ElementManager.createText("All Basic Routes", 455, "h3");

        basicRouteTable = TableManager.setupBasicRouteTable();
        StackPane.setAlignment(basicRouteTable, Pos.TOP_LEFT);
        StackPane.setMargin(basicRouteTable, new Insets(490, 0, 0, 0));
        basicRouteTable.setMaxHeight(150);
        basicRouteTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BasicRouteTableRow>() {
            public void changed(ObservableValue<? extends BasicRouteTableRow> observable, BasicRouteTableRow oldValue, BasicRouteTableRow newValue) {
                deleteSelectedBasicRouteButton.setDisable(false);
            }
        });

        TableManager.populateBasicRouteTable(basicRouteTable, App.getBasicRouteManager().getBasicRoutes());

        deleteSelectedBasicRouteButton = ElementManager.createButton("Delete Selected Basic Route", 645, 0, null, Pos.TOP_RIGHT, "button-red");
        deleteSelectedBasicRouteButton.setDisable(true);
        deleteSelectedBasicRouteButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                deleteBasicRoute();
            }
        });

        Text basicRouteTableExplanation = ElementManager.createText("When a Journey is created, it extends a Basic Route.\nBasic Routes don't hold the time of departure or intermediate stations.\nBasic Routes can never be deleted.", 645, "h6");

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(30, 30, 30, 30));

        stackPane.getChildren().addAll(
                backButton,
                forceReloadButton,
                stationTableTitle,
                stationTable,
                deleteSelectedIntermediateStationButton,
                stationTableExplanation,
                journeyTableTitle,
                journeyTable,
                deleteSelectedJourneyButton,
                journeyTableExplanation,
                basicRouteTableTitle,
                basicRouteTable,
                deleteSelectedBasicRouteButton,
                basicRouteTableExplanation
        );

        renderPage(stackPane, "AdminPanel", "Admin > Overview");
    }

    /**
     * Removes the selected <code>Journey</code> from the table and the current session
     */
    private void deleteJourney() {
        App.getJourneyManager().deleteJourney(journeyTable.getSelectionModel().getSelectedItem().getId());
        journeyTable.getItems().remove(journeyTable.getSelectionModel().getSelectedItem());
        journeyTable.getSelectionModel().clearSelection();

        deleteSelectedJourneyButton.setDisable(true);
    }

    /**
     * Removes the selected <code>IntermediateStation</code> from the table and the current session
     * If the user tries to delete a row with the 'type' = 'Intermediate', a warning alert will be displayed
     */
    private void deleteIntermediateStation() {
        StationTableRow stationTableRow = stationTable.getSelectionModel().getSelectedItem();

        if (stationTableRow.getType() == "Intermediate") {
            App.getStationManager().deleteIntermediateStation(stationTable.getSelectionModel().getSelectedItem().getId());
            stationTable.getItems().remove(stationTableRow);
        } else {
            ElementManager.showAlert(Alert.AlertType.WARNING, "That's not available in this version!", "That's not available in this version!", "You can't delete a Destination Station in this version, only Intermediate Stations!");
        }

        stationTable.getSelectionModel().clearSelection();
        deleteSelectedIntermediateStationButton.setDisable(true);
    }

    /**
     * A warning alert will be displayed, telling the user this is not available in this version
     */
    private void deleteBasicRoute() {
        ElementManager.showAlert(Alert.AlertType.WARNING, "That's not available in this version!", "That's not available in this version!", "You can't delete a Basic Route in this version!");

        basicRouteTable.getSelectionModel().clearSelection();
        deleteSelectedBasicRouteButton.setDisable(true);
    }
}
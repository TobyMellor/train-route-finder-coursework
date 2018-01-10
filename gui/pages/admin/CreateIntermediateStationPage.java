package com.TobyMellor.TrainRouteFinder.gui.pages.admin;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.StationTableRow;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.TableManager;
import com.TobyMellor.TrainRouteFinder.stations.DestinationStation;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Allows admin users to create an <code>IntermediateStation</code> to be used on any future <code>Journey</code>
 *
 * A text field is shown where the user can enter the <code>IntermediateStation</code> name and a
 * create button which will validate the input and append it onto a table
 *
 * The table is at the bottom and shows all IntermediateStations in the system.
 * The user can delete <code>IntermediateStation</code>s from here too.
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/28 00:50:00 $
 */

public class CreateIntermediateStationPage extends Layout {
    TableView<StationTableRow> table;
    TextField stationName;
    Button deleteSelectedStationButton;

    public CreateIntermediateStationPage(Stage primaryStage) {
        setPrimaryStage(primaryStage);

        Button backButton = ElementManager.createButton("<- Back to the Admin Menu", 0);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new AdminMenuPage(getPrimaryStage());
            }
        });

        Text title = ElementManager.createText("Create an Intermediate Station!", 40, "h3");
        Text subtitle = ElementManager.createText("Fill in the details below", 70, "h6");

        TextFlow stationNameLabel = ElementManager.createTextLabel("Enter a station name", 100, "h6");

        stationName = new TextField();
        stationName.setMaxWidth(250);
        stationName.setPromptText("Enter a station name...");
        StackPane.setMargin(stationName, new Insets(115, 0, 0, 0));
        StackPane.setAlignment(stationName, Pos.TOP_LEFT);

        Button createStationButton = ElementManager.createButton("Create Intermediate Station", 155, "button-green");
        createStationButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                createIntermediateStation();
            }
        });

        Text tableTitle = ElementManager.createText("All Intermediate Stations", 420, "h3");

        table = TableManager.setupStationTable();

        StackPane.setAlignment(table, Pos.TOP_LEFT);
        StackPane.setMargin(table, new Insets(455, 0, 40, 0));
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StationTableRow>() {
            public void changed(ObservableValue<? extends StationTableRow> observable, StationTableRow oldValue, StationTableRow newValue) {
                deleteSelectedStationButton.setDisable(false);
            }
        });

        loadIntermediateStations();

        deleteSelectedStationButton = ElementManager.createButton("Delete Selected Station", 0, 0, null, Pos.BOTTOM_RIGHT, "button-red");
        deleteSelectedStationButton.setDisable(true);
        deleteSelectedStationButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                deleteIntermediateStation();
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(30, 30, 30, 30));

        stackPane.getChildren().addAll(
                backButton,
                title,
                subtitle,
                stationNameLabel,
                stationName,
                createStationButton,
                tableTitle,
                table,
                deleteSelectedStationButton
        );

        renderPage(stackPane, "AdminPanel", "Admin > Create Intermediate Station");
    }

    /**
     * Loads all of the <code>IntermediateStation</code>s in the system and puts them in the table
     */
    private void loadIntermediateStations() {
        TableManager.populateStationTable(table, new ArrayList<DestinationStation>(), App.getStationManager().getIntermediateStations());
    }

    /**
     * Deletes the <code>IntermediateStation</code> from the system and the table based on the selected item in the table
     */
    private void deleteIntermediateStation() {
        App.getStationManager().deleteIntermediateStation(table.getSelectionModel().getSelectedItem().getId());
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
        table.getSelectionModel().clearSelection();

        deleteSelectedStationButton.setDisable(true);
    }

    /**
     * Creates an <code>IntermediateStation</code> with the name from the <code>stationName</code> <code>TextField</code>
     *
     * @throws ValidationException if com.TobyMellor.TrainRouteFinder.validation.validators.IntermediateStationValidator fails
     * @see com.TobyMellor.TrainRouteFinder.validation.validators.IntermediateStationValidator
     */
    private void createIntermediateStation() {
        try {
            App.getStationManager().createIntermediateStation(stationName.getText());

            stationName.clear();
            loadIntermediateStations();
        } catch (ValidationException e) {
            ElementManager.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getMessage(), e.getValidationMessages());
        }
    }
}

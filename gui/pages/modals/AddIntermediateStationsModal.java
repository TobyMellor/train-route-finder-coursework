package com.TobyMellor.TrainRouteFinder.gui.pages.modals;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.GUI;
import com.TobyMellor.TrainRouteFinder.gui.KeyValuePair;
import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;
import com.TobyMellor.TrainRouteFinder.gui.pages.admin.CreateIntermediateStationPage;
import com.TobyMellor.TrainRouteFinder.gui.pages.admin.CreateJourneyPage;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.IntermediateStationTableRow;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.TableManager;
import com.TobyMellor.TrainRouteFinder.stations.IntermediateStation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A Modal that allows users to select <code>IntermediateStation</code>s from a <code>ChoiceBox</code> and
 * add them to a table at the bottom
 *
 * The table at the bottom lists all of the <code>IntermediateStation</code>s on route, along with the order they appear on route
 *
 * The <code>IntermediateStation</code>s can be sorted by the order they appear on route or by alphabetical order
 *
 * If a user wishes to create more <code>IntermediateStation</code>s that are not available from the <code>ChoiceBox</code>
 * they can click on a button to redirect them to <code>CreateIntermediateStationPage</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/28 00:05:00 $
 */

public class AddIntermediateStationsModal extends Layout {
    Button addIntermediateStations;
    Button deleteSelectedStationButton;
    Button sortIntermediateStationsButton;

    Boolean sortAlphabetically = false;

    ChoiceBox<KeyValuePair> intermediatePicker;

    TableView<IntermediateStationTableRow> intermediateStationTable;

    List<String> intermediateStationIds = new ArrayList<String>();

    public StackPane AddIntermediateStationsModal(final Stage primaryStage, final Stage modalStage, final CreateJourneyPage createJourneyPage, final List<String> intermediateStationIds) {
        this.intermediateStationIds = intermediateStationIds;

        Text title = ElementManager.createText("Add stops to the Journey!", 0, "h3");
        Text subtitle = ElementManager.createText("Fill in the details below", 30, "h6");

        TextFlow intermediateLabel = ElementManager.createTextLabel("Choose an Intermediate Station", 60, "h6");

        intermediatePicker = new ChoiceBox<KeyValuePair>();
        StackPane.setMargin(intermediatePicker, new Insets(75, 0, 0, 0));
        StackPane.setAlignment(intermediatePicker, Pos.TOP_LEFT);
        intermediatePicker.setPrefWidth(200);
        intermediatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                KeyValuePair selectedItem = intermediatePicker.getSelectionModel().getSelectedItem();

                if (selectedItem != null && selectedItem.getKey() == "new-intermediate-station") { // 'Other' selected, then redirect to CreateIntermediateStationPage
                    GUI.close(modalStage);
                    new CreateIntermediateStationPage(primaryStage);
                }

                addIntermediateStations.setDisable(false);
            }
        });

        addIntermediateStations = ElementManager.createButton("Add", 75, 205, null, Pos.TOP_LEFT, "button-green", "button-small");
        addIntermediateStations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addIntermediateStation();
            }
        });

        Text createMoreLabel = ElementManager.createText("Create more Intermediate Stations to use", 115, "h5");

        Button createMoreIntermediateStations = ElementManager.createButton("Create Stations (new window)", 140);
        createMoreIntermediateStations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modalStage.close();
                new CreateIntermediateStationPage(primaryStage);
            }
        });

        sortIntermediateStationsButton = ElementManager.createButton("Sort by Station Name", 200, 0, null, Pos.TOP_RIGHT, "button-small");
        sortIntermediateStationsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sortIntermediateStations();
            }
        });

        Text intermediateStationTableTitle = ElementManager.createText("All stops on Route", 200, "h3");

        deleteSelectedStationButton = ElementManager.createButton("Remove Intermediate Station", 0, 0, null, Pos.BOTTOM_RIGHT, "button-red");
        deleteSelectedStationButton.setDisable(true);
        deleteSelectedStationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeSelectedStation();
            }
        });

        intermediateStationTable = TableManager.setupIntermediateStationTable();
        loadIntermediateStations();

        StackPane.setAlignment(intermediateStationTable, Pos.TOP_LEFT);
        StackPane.setMargin(intermediateStationTable, new Insets(235, 0, 40, 0));
        intermediateStationTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IntermediateStationTableRow>() {
            public void changed(ObservableValue<? extends IntermediateStationTableRow> observable, IntermediateStationTableRow oldValue, IntermediateStationTableRow newValue) {
                deleteSelectedStationButton.setDisable(false);
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(500, 500);
        stackPane.setPadding(new Insets(30, 30, 30, 30));

        stackPane.getChildren().addAll(
                title,
                subtitle,
                intermediateLabel,
                intermediatePicker,
                addIntermediateStations,
                createMoreLabel,
                createMoreIntermediateStations,
                sortIntermediateStationsButton,
                intermediateStationTableTitle,
                intermediateStationTable,
                deleteSelectedStationButton
        );

        // when a user presses 'Close Modal' button in footer
        modalStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                createJourneyPage.setIntermediateStationIdsFromModal(intermediateStationIds);
            }
        });

        // when a user presses 'x'
        modalStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                createJourneyPage.setIntermediateStationIdsFromModal(intermediateStationIds);
            }
        });

        return stackPane;
    }

    /**
     * Clears the table and the <code>ChoiceBox</code>, loops through all of the <code>IntermediateStation</code>s
     * and if the <code>IntermediateStation</code> is not in the table then it is added to the <code>ChoiceBox</code>,
     * otherwise it gets added to the table
     */
    private void loadIntermediateStations() {
        intermediateStationTable.getItems().clear();
        intermediatePicker.getItems().clear();

        List<IntermediateStation> intermediateStations = new ArrayList<IntermediateStation>();

        for (IntermediateStation intermediateStation : App.getStationManager().getIntermediateStations()) {
            if (!intermediateStationIds.contains(intermediateStation.getId())) {
                intermediatePicker.getItems().add(new KeyValuePair(intermediateStation.getId(), intermediateStation.getName()));
            } else {
                intermediateStations.add(intermediateStation);
            }
        }

        intermediatePicker.getItems().add(new KeyValuePair("new-intermediate-station", "Other")); // if this element is clicked it redirects a user to create a new IntermediateStation

        TableManager.populateIntermediateStationTable(intermediateStationTable, intermediateStationIds);

        addIntermediateStations.setDisable(true);
        deleteSelectedStationButton.setDisable(true);
    }

    /**
     * Adds the selected <code>IntermediateStation</code> to the table and reloads the table/<code>ChoiceBox</code>
     */
    private void addIntermediateStation() {
        String intermediateStationId = intermediatePicker.getSelectionModel().getSelectedItem().getKey();

        intermediateStationIds.add(intermediateStationId);

        loadIntermediateStations();
    }

    /**
     * Removes the selected <code>IntermediateStation</code> from table and reloads the table/<code>ChoiceBox</code>
     */
    private void removeSelectedStation() {
        String intermediateStationId = intermediateStationTable.getSelectionModel().getSelectedItem().getId();

        intermediateStationIds.remove(intermediateStationId);

        loadIntermediateStations();
    }

    /**
     * Toggles the way the <code>IntermediateStation</code>s table is currently sorted,
     * to either sort by Order or by Station Name alphabetically
     */
    private void sortIntermediateStations() {
        intermediateStationTable.getSortOrder().clear();

        sortAlphabetically = !sortAlphabetically; // flip the value of sortAlphabetically

        if (sortAlphabetically) {
            sortIntermediateStationsButton.setText("Sort by Order");

            intermediateStationTable.getSortOrder().add(intermediateStationTable.getColumns().get(1));
        } else {
            sortIntermediateStationsButton.setText("Sort by Station Name");

            intermediateStationTable.getSortOrder().add(intermediateStationTable.getColumns().get(0));
        }
    }
}

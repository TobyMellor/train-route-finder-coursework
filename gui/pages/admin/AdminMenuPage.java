package com.TobyMellor.TrainRouteFinder.gui.pages.admin;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.GUI;
import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;
import com.TobyMellor.TrainRouteFinder.gui.pages.MenuPage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Admin Page
 *
 * Sets up a stage containing
 *      - centered welcome titles
 *      - a button taking the user to 'Create/Delete a Journey (CreateJourneyPage)'
 *      - a button taking the user to 'Show All Data (OverviewPage)'
 *      - a button taking the user to 'Add Intermediate Stops (CreateIntermediateStops)'
 *      - a button that removes all current session data and replaces it with
 *        data found in the XML files
 *      - a button that saves all the current session data and replaces the data found
 *        in the XML files
 *      - end button
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 00:53:00 $
 */

public class AdminMenuPage extends Layout {
    public AdminMenuPage(Stage primaryStage) {
        setPrimaryStage(primaryStage);

        Button backButton = ElementManager.createButton("<- Back to the Main Menu", 0);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new MenuPage(getPrimaryStage());
            }
        });

        Text title = ElementManager.createText("Admin Panel", 70, 0, Pos.TOP_CENTER, "h2");
        Text subtitle = ElementManager.createText("Built by Toby Mellor (B619693)", 105, 0, Pos.TOP_CENTER, "h3");

        Button createJourneyButton = ElementManager.createButton("Create/Delete a Journey", 170, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        createJourneyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new CreateJourneyPage(getPrimaryStage());
            }
        });

        Button showAllDataButton = ElementManager.createButton("Show All Data", 220, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        showAllDataButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new OverviewPage(getPrimaryStage());
            }
        });

        Button addIntermediateStationsButton = ElementManager.createButton("Add Intermediate Stops", 270, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        addIntermediateStationsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new CreateIntermediateStationPage(getPrimaryStage());
            }
        });

        Button forceReloadButton = ElementManager.createButton("Force reload all from file", 320, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        forceReloadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                reloadXML();
            }
        });

        Button saveButton = ElementManager.createButton("Save", 370, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                save();
            }
        });

        Button endButton = ElementManager.createButton("End", 500, 0, 300, Pos.TOP_CENTER, "button-red", "button-large");
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUI.close(getPrimaryStage());
            }
        });
        StackPane.setAlignment(endButton, Pos.TOP_CENTER);

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(30, 30, 30, 30));

        stackPane.getChildren().addAll(
                backButton,
                title,
                subtitle,
                createJourneyButton,
                showAllDataButton,
                addIntermediateStationsButton,
                forceReloadButton,
                saveButton,
                endButton
        );

        renderPage(stackPane, "AdminPanel", "Admin Menu Page");
    }

    /**
     * Saves all the current session data and replaces the data found
     * in the XML files
     *
     * Shows an alert to the user to say this has been completed
     */
    private void save() {
        App.saveXML();

        ElementManager.showAlert(Alert.AlertType.INFORMATION, "Successfully saved!", "Successfully saved!", "Your Journeys, Routes and Stations have been successfully saved!");
    }
}

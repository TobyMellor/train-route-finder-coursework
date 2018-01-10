package com.TobyMellor.TrainRouteFinder.gui.pages;

import com.TobyMellor.TrainRouteFinder.gui.GUI;
import com.TobyMellor.TrainRouteFinder.gui.pages.admin.AdminMenuPage;
import com.TobyMellor.TrainRouteFinder.gui.pages.journeys.FindJourneyPage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The landing page
 *
 * Sets up a stage containing
 *      - centered welcome titles
 *      - buttons to navigate to different menus
 *      - end button
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 22:39:00 $
 */

public class MenuPage extends Layout {
    public MenuPage(Stage primaryStage) {
        setPrimaryStage(primaryStage);

        Text title = ElementManager.createText("Welcome to the Train Finder App!", 0, 0, Pos.TOP_CENTER, "h2");
        Text subtitle = ElementManager.createText("Built by Toby Mellor (B619693)", 35, 0, Pos.TOP_CENTER, "h3");

        Button findJourneysButton = ElementManager.createButton("Find Journeys", 100, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        findJourneysButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FindJourneyPage(getPrimaryStage());
            }
        });

        Button adminPanelButton = ElementManager.createButton("Admin Panel", 150, 0, 300, Pos.TOP_CENTER, "button-green", "button-large");
        adminPanelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new AdminMenuPage(getPrimaryStage());
            }
        });

        Button endButton = ElementManager.createButton("End", 240, 0, 300, Pos.TOP_CENTER, "button-red", "button-large");
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GUI.close(getPrimaryStage());
            }
        });
        StackPane.setAlignment(endButton, Pos.TOP_CENTER);

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(100, 30, 30, 30));

        stackPane.getChildren().addAll(
                title,
                subtitle,
                findJourneysButton,
                adminPanelButton,
                endButton
        );

        renderPage(stackPane, "MainMenuPage", "Main Menu");
    }
}

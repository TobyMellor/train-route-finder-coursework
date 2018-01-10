package com.TobyMellor.TrainRouteFinder.gui.pages;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.GUI;
import com.TobyMellor.TrainRouteFinder.gui.pages.admin.AdminMenuPage;
import com.TobyMellor.TrainRouteFinder.gui.pages.journeys.FindJourneyPage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The layout, extended by most pages
 *
 * This class is used to easily get elements that are common in many pages.
 * For example, the navbar at the top of each page.
 *
 * This class is also called by all pages when their setup has completed and they wish to render their stage
 *
 * Layout also includes a few useful utilities used by some pages
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 22:43:00 $
 */

abstract public class Layout {
    Stage primaryStage;

    /**
     * Creates the header navbar and sets one of the buttons to 'Active', depending on the page the user
     * is currently viewing
     *
     * The headerbar buttons simply redirect the user to another page or end the program
     *
     * @param pageName the category name of the page the user is currently looking at (MainMenuPage, FindJourneyPage or AdminMenuPage)
     *
     * @return HBox
     */
    public HBox getHeaderBar(String pageName) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(8, 30, 8, 30));
        hbox.setSpacing(8);
        hbox.getStyleClass().add("navbar");

        Button homeButton = ElementManager.createButton("Home", 0, "navbar-element");
        homeButton.setPrefSize(110, 20);
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new MenuPage(getPrimaryStage());
            }
        });

        Button findJourneysButton = ElementManager.createButton("Find Journeys", 0, "navbar-element");
        findJourneysButton.setPrefSize(110, 20);
        findJourneysButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new FindJourneyPage(getPrimaryStage());
            }
        });

        Button adminPanelButton = ElementManager.createButton("Admin Panel", 0, "navbar-element");
        adminPanelButton.setPrefSize(110, 20);
        adminPanelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new AdminMenuPage(getPrimaryStage());
            }
        });

        if (pageName.equals("MainMenuPage")) { // grey-out the currentPage's button to show the user is there
            homeButton.getStyleClass().add("active");
        } else if (pageName.equals("FindJourneyPage")) {
            findJourneysButton.getStyleClass().add("active");
        } else {
            adminPanelButton.getStyleClass().add("active");
        }

        Pane spacer = new Pane();
        HBox.setHgrow(
                spacer,
                Priority.SOMETIMES
        ); // shift the 'end' button to the right

        Button endButton = ElementManager.createButton("End", 0, "navbar-element");
        endButton.setPrefSize(110, 20);
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                GUI.close(getPrimaryStage());
            }
        });

        hbox.getChildren().addAll(homeButton, findJourneysButton, adminPanelButton, spacer, endButton);

        return hbox;
    }

    /**
     * Creates a footer bar similar to the header navbar but only contains one button which closes the current stage
     * This is only used for Modals at the moment
     *
     * @param stage the current stage to close when the user clicks close
     *
     * @return HBox
     */
    public HBox getModalFooterBar(final Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(8));
        hbox.setSpacing(8);
        hbox.getStyleClass().add("navbar");

        Pane spacer = new Pane();
        HBox.setHgrow(
                spacer,
                Priority.SOMETIMES
        ); // even though there are no other buttons, the modal close button still needs to be on the right

        Button closeModalButton = ElementManager.createButton("Close Modal", 0, "navbar-element");
        closeModalButton.setPrefSize(110, 20);
        closeModalButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        hbox.getChildren().addAll(spacer, closeModalButton);

        return hbox;
    }

    /**
     * Renders the <code>StackPane</code>, provided by each page class
     *
     * Sets the <code>title</code>, adds the navbar, renders the .css style, sets the window dimensions and shows the window
     *
     * @param stackPane the <code>StackPane</code> containing page specific elements
     * @param pageName  the category name of the page the user is currently looking at (MainMenuPage, FindJourneyPage or AdminMenuPage)
     * @param title     the description of the page to be prepended to ' | Train Finder App by Toby Mellor (B619693)'
     */
    public void renderPage(final StackPane stackPane, final String pageName, final String title) {
        primaryStage.setTitle(title + " | Train Finder App by Toby Mellor (B619693)");

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(getHeaderBar(pageName));
        borderPane.setCenter(stackPane);

        Scene scene = new Scene(borderPane);

        try {
            scene.getStylesheets().add((new File("resources/css/style.css")).toURI().toURL().toExternalForm()); // the global stylesheet
        } catch (MalformedURLException e) {
            System.out.print(e.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.setWidth(700);
        primaryStage.setHeight(800);

        primaryStage.show();
    }

    public void setPrimaryStage(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    /**
     * Similar to <code>render()</code>
     *
     * Sets the <code>title</code>, adds the footer, renders the .css style, sets the window dimensions and shows the window
     *
     * @param stage     the <code>Stage</code> containing page specific elements
     * @param stackPane the <code>StackPane</code> to center on the users screen
     * @param event     the button click event called this method
     * @param title     the description of the page to be shown at the top of the window
     */
    protected void showModal(final Stage stage, final StackPane stackPane, final ActionEvent event, final String title) {
        BorderPane borderPane = new BorderPane();

        borderPane.setBottom(getModalFooterBar(stage));
        borderPane.setCenter(stackPane);

        Scene scene = new Scene(borderPane);

        try {
            scene.getStylesheets().add((new File("resources/css/style.css")).toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            System.out.print(e.getMessage());
        }

        stage.setScene(scene);

        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());

        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Adds the correct suffix on the end of the corresponding <code>day</code>
     *
     * e.g. 1  to 1st
     *      21 to 21st
     *      3  to 3rd
     *      11 to 11th
     *
     * @param day the day to append the correct suffix to
     *
     * @return String
     */
    public static String getDaySuffix(final int day) {
        if (day >= 11 && day <= 13) { // every day count ending in 1, 2 or 3 has a custom suffix besides 11th, 12th, 13th. e.g. (1st, 11th 21st, 31st)
            return "th";
        }

        switch (day % 10) { // we only need the last digit to determine the correct suffix
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * Formats the correct date from all of the <code>ChoiceBox's</code> that are found in <code>CreateJourneyPage</code> and <code>FindJourneyPage</code>
     *
     * @param hourDatePicker   the <code>ChoiceBox</code> containing the hours, in the format <code>HH</code>
     * @param minuteDatePicker the <code>ChoiceBox</code> containing the minutes, in the format <code>mm</code>
     * @param dateDatePicker   the <code>ChoiceBox</code> containing the current month and day of month, in the format <code>d MMM</code>
     * @param yearDatePicker   the <code>ChoiceBox</code> containing the years, in the format <code>yyyy</code>
     *
     * @return Date
     *
     * @see com.TobyMellor.TrainRouteFinder.gui.pages.admin.CreateJourneyPage
     * @see FindJourneyPage
     */
    protected Date getDateFromChoiceBoxes(final ChoiceBox<String> hourDatePicker, final ChoiceBox<String> minuteDatePicker, final ChoiceBox<String> dateDatePicker, final ChoiceBox<Integer> yearDatePicker) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:d MMM:yyyy");

            Date date = simpleDateFormat.parse(hourDatePicker.getSelectionModel().getSelectedItem() + ":"
                    + minuteDatePicker.getSelectionModel().getSelectedItem() + ":"
                    + dateDatePicker.getSelectionModel().getSelectedItem() + ":"
                    + yearDatePicker.getSelectionModel().getSelectedItem());

            return date;
        } catch (ParseException e) {
            System.out.print(e.getMessage());
        }

        return null;
    }

    /**
     * Loads the XML files again and replaces the managers
     */
    protected void reloadXML() {
        try {
            App.loadXML();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            ElementManager.showAlert(Alert.AlertType.INFORMATION, "Successfully reloaded data!", "Successfully reloaded data!", "Successfully reloaded all Destination Stations, Intermediate Stations, Journeys and Basic Routes!");
        }
    }
}

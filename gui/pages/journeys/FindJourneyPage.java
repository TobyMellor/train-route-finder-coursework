package com.TobyMellor.TrainRouteFinder.gui.pages.journeys;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.MenuPage;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.JourneyTableRow;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.TableManager;
import com.TobyMellor.TrainRouteFinder.journeys.Journey;
import com.TobyMellor.TrainRouteFinder.journeys.JourneyManager;
import com.TobyMellor.TrainRouteFinder.routes.BasicRouteManager;
import com.TobyMellor.TrainRouteFinder.stations.DestinationStation;
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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Allows customers to find a <code>Journey</code>, based on the <code>DestinationStation</code> they wish to
 * depart from and to. The customer can specify whether they want to search for Journeys only after a given time too.
 *
 * Results appear at the bottom in a table, based on what the user has entered. The <code>IntermediateStation</code>s in each row of the table
 * can be sorted by order or alphabetically. The rows themselves are sorted based on the departure date.
 *
 * Additional features:
 *     - The user can specify their time search type to 'Leaving after [time]', 'Arriving by [time]' or 'All routes, don't search'
 *     - There is an option to show all journeys
 *     - The time picker is automatically set to the next 15 minute interval e.g. 00:22 to 00:30, 00:46 to 01:00
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/28 00:22:00 $
 */

public class FindJourneyPage extends Layout {
    ChoiceBox<String> departingStationPicker;
    ChoiceBox<String> destinationStationPicker;

    ChoiceBox<String> leavingAtArrivingByPicker;

    Text hourDatePickerLabel;
    Text minuteDatePickerLabel;
    Text dateDatePickerLabel;
    Text yearDatePickerLabel;
    ChoiceBox<String> hourDatePicker;
    ChoiceBox<String> minuteDatePicker;
    ChoiceBox<String> dateDatePicker;
    ChoiceBox<Integer> yearDatePicker;

    TableView<JourneyTableRow> journeysTable;
    Text journeysTableTitle;

    Button findJourneysButton;
    Button sortIntermediateStationsButton;

    Boolean intermediateStationsAlphabeticalOrder = false;

    List<Journey> loadedJourneys = new ArrayList<Journey>();

    public FindJourneyPage(Stage primaryStage) {
        setPrimaryStage(primaryStage);

        Button backButton = ElementManager.createButton("<- Back to the Main Menu", 0);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new MenuPage(getPrimaryStage());
            }
        });

        Text mainTitle = ElementManager.createText("Find your Journey!", 50, "h3");
        Text subtitle = ElementManager.createText("Fill in the details below", 80, "h6");

        TextFlow departingStationLabel = ElementManager.createTextLabel("Choose the station to depart from", 105, "h6");

        departingStationPicker = new ChoiceBox<String>();
        StackPane.setMargin(departingStationPicker, new Insets(120, 0, 0, 0));
        StackPane.setAlignment(departingStationPicker, Pos.TOP_LEFT);
        departingStationPicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadDestinationStations(newValue);
            }
        });
        departingStationPicker.setPrefWidth(281);

        for (DestinationStation destinationStation : App.getStationManager().getDestinationStations()) {
            departingStationPicker.getItems().add(destinationStation.getName());
        }

        TextFlow destinationStationLabel = ElementManager.createTextLabel("Choose the station to arrive at", 160, "h6");

        destinationStationPicker = new ChoiceBox<String>();
        StackPane.setMargin(destinationStationPicker, new Insets(175, 0, 0, 0));
        StackPane.setAlignment(destinationStationPicker, Pos.TOP_LEFT);
        destinationStationPicker.setDisable(true);
        destinationStationPicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                findJourneysButton.setDisable(false);
            }
        });

        Text datePickerTitle = ElementManager.createText("Search Conditions", 220, "h5");

        TextFlow leavingAtArrivingByPickerLabel = ElementManager.createTextLabel("Choose the search type", 245, "h6");

        leavingAtArrivingByPicker = new ChoiceBox<String>();
        StackPane.setMargin(leavingAtArrivingByPicker, new Insets(260, 0, 0, 0));
        StackPane.setAlignment(leavingAtArrivingByPicker, Pos.TOP_LEFT);
        leavingAtArrivingByPicker.getItems().addAll("Leaving after", "Arriving by", "All routes, don't search");
        leavingAtArrivingByPicker.getSelectionModel().selectFirst();
        leavingAtArrivingByPicker.setPrefWidth(281);
        leavingAtArrivingByPicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                toggleDatePicker(!newValue.contains("search")); // if 'Leaving After' or 'Arriving By' selected, pass true
            }
        });

        Calendar roundedCalendar = Calendar.getInstance();

        int unroundedMinutes = roundedCalendar.get(Calendar.MINUTE);
        int modMinutes = unroundedMinutes % 15;
        roundedCalendar.add(Calendar.MINUTE, 15 - modMinutes); // round calendar to the next 15 minutes to prevent setting date in past

        hourDatePickerLabel = ElementManager.createText("Hour:", 300, 0, Pos.TOP_LEFT, "");

        hourDatePicker = new ChoiceBox<String>();
        StackPane.setMargin(hourDatePicker, new Insets(316, 0, 0, 0));
        StackPane.setAlignment(hourDatePicker, Pos.TOP_LEFT);

        for (int i = 0; i < 24; i++) {
            hourDatePicker.getItems().add(String.format("%02d", i)); // %02d forces 00, 01, 02 etc. instead of 0, 1, 2

            if (roundedCalendar.get(Calendar.HOUR_OF_DAY) == i) {
                hourDatePicker.getSelectionModel().selectLast();
            }
        }

        minuteDatePickerLabel = ElementManager.createText("Min:", 300, 60, Pos.TOP_LEFT, "");

        minuteDatePicker = new ChoiceBox<String>();
        StackPane.setMargin(minuteDatePicker, new Insets( 316, 0, 0, 60));
        StackPane.setAlignment(minuteDatePicker, Pos.TOP_LEFT);

        for (int i = 0; i < 60; i++) {
            minuteDatePicker.getItems().add(String.format("%02d", i));

            if (roundedCalendar.get(Calendar.MINUTE) == i) {
                minuteDatePicker.getSelectionModel().selectLast();
            }
        }

        dateDatePickerLabel = ElementManager.createText("Day:", 300, 120, Pos.TOP_LEFT, "");

        dateDatePicker = new ChoiceBox<String>();
        StackPane.setMargin(dateDatePicker, new Insets(316, 0, 0, 120));
        StackPane.setAlignment(dateDatePicker, Pos.TOP_LEFT);

        Calendar start = roundedCalendar;
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 1);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM");

            dateDatePicker.getItems().add(sdf.format(date));
        }

        dateDatePicker.getSelectionModel().selectFirst();

        yearDatePickerLabel = ElementManager.createText("Year:", 300, 209, Pos.TOP_LEFT, "");

        yearDatePicker = new ChoiceBox<Integer>();
        StackPane.setMargin(yearDatePicker, new Insets(316, 0, 0, 209));
        StackPane.setAlignment(yearDatePicker, Pos.TOP_LEFT);
        yearDatePicker.getItems().addAll(2017, 2018, 2019); // only implement 3 years to save on iteration and so we don't have to take leap years into account :3
        yearDatePicker.getSelectionModel().selectFirst();

        findJourneysButton = ElementManager.createButton("Search for journeys", 355, "button-green");
        findJourneysButton.setDisable(true);
        findJourneysButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                findJourneyResults();
            }
        });

        Text miscellaneousTitle = ElementManager.createText("Additional Options", 0, 0, Pos.TOP_RIGHT, "h3");

        Button allJourneysButton = ElementManager.createButton("Show all journeys", 40, 0, null, Pos.TOP_RIGHT);
        allJourneysButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                findAllJourneysResults();
            }
        });

        sortIntermediateStationsButton = ElementManager.createButton("Intermediate Stations by Station Name", 400, 0, null, Pos.TOP_RIGHT, "button-small");
        sortIntermediateStationsButton.setVisible(false);
        sortIntermediateStationsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                intermediateStationsAlphabeticalOrder = !intermediateStationsAlphabeticalOrder;

                if (intermediateStationsAlphabeticalOrder) {
                    sortIntermediateStationsButton.setText("Sort Intermediate Stations by Order");
                } else {
                    sortIntermediateStationsButton.setText("Sort Intermediate Stations by Station Name");
                }

                loadJourneyResults(loadedJourneys);
            }
        });

        journeysTableTitle = ElementManager.createText("Results" ,400, "h3");
        journeysTableTitle.setVisible(false);

        journeysTable = TableManager.setupJourneyTable("No Journeys have matched your search!");

        journeysTable.setVisible(false);
        StackPane.setAlignment(journeysTable, Pos.TOP_LEFT);
        StackPane.setMargin(journeysTable, new Insets(430, 0, 0, 0));

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(30, 30, 30, 30));

        stackPane.getChildren().addAll(
                backButton,
                mainTitle,
                subtitle,
                departingStationLabel,
                departingStationPicker,
                destinationStationLabel,
                destinationStationPicker,
                leavingAtArrivingByPickerLabel,
                leavingAtArrivingByPicker,
                datePickerTitle,
                minuteDatePickerLabel,
                minuteDatePicker,
                hourDatePickerLabel,
                hourDatePicker,
                dateDatePickerLabel,
                dateDatePicker,
                yearDatePickerLabel,
                yearDatePicker,
                findJourneysButton,
                miscellaneousTitle,
                allJourneysButton,
                sortIntermediateStationsButton,
                journeysTableTitle,
                journeysTable
        );

        renderPage(stackPane, "FindJourneyPage", "Find Journey Page");
    }

    /**
     * Gets all <code>Journey</code>s based on the <code>basicRouteId</code> from the 'depart from' and 'arrive at' <code>ChoiceBox</code>'s
     * If 'Leaving after' or 'Arriving by' options are selected, an additional filter is made for Journeys based on the date <code>ChoiceBox</code>
     *
     * The <code>Journey</code>s are then loaded into the table
     */
    private void findJourneyResults() {
        BasicRouteManager basicRouteManager = App.getBasicRouteManager();
        JourneyManager journeyManager = App.getJourneyManager();

        List<Journey> journeys = new ArrayList<Journey>();
        String basicRouteId = basicRouteManager.getBasicRoute(departingStationPicker.getSelectionModel().getSelectedItem(), destinationStationPicker.getSelectionModel().getSelectedItem()).getId();

        if (leavingAtArrivingByPicker.getSelectionModel().getSelectedItem().contains("search")) {
            journeys = journeyManager.getJourneys(basicRouteId);
        } else {
            journeys = journeyManager.getJourneys(basicRouteId, getDateFromChoiceBoxes(hourDatePicker, minuteDatePicker, dateDatePicker, yearDatePicker).getTime(), leavingAtArrivingByPicker.getSelectionModel().getSelectedItem().contains("after"));
        }

        loadJourneyResults(journeys);
    }

    /**
     * Gets all <code>Journey</code>s
     *
     * The <code>Journey</code>s are then loaded into the table
     */
    private void findAllJourneysResults() {
        JourneyManager journeyManager = App.getJourneyManager();

        loadJourneyResults(journeyManager.getJourneys());
    }

    /**
     * Takes a list of journeys and displays them on the table
     * Sets the <code>sortIntermediateStationsButton</code>, <code>journeysTableTitle</code> and <code>journeysTable</code> to visible
     *
     * @param journeys a list of <code>Journey</code> objects to display on the table
     */
    private void loadJourneyResults(List<Journey> journeys) {
        TableManager.populateJourneyTable(journeysTable, journeys, intermediateStationsAlphabeticalOrder);
        journeysTable.getSortOrder().add(journeysTable.getColumns().get(8)); // sort based on departure timestamp on the hidden 8th column

        this.loadedJourneys = journeys;

        sortIntermediateStationsButton.setVisible(true);
        journeysTableTitle.setVisible(true);
        journeysTable.setVisible(true);
    }

    /**
     * Clears the 'arrive at' <code>ChoiceBox</code> since we want stations not picked in the 'depart from' <code>ChoiceBox</code>
     * Loops through all <code>DestinationStation</code>s and loads non-used stations to the <code>ChoiceBox</code>
     *
     * Sets the width of the picker to the same as all other elements, allows the user to select the <code>ChoiceBox</code> and
     * allow the user to search
     *
     * @param selectedDepartureStation the <code>DestinationStation</code> name of the 'depart from' <code>ChoiceBox</code>
     */
    private void loadDestinationStations(String selectedDepartureStation) {
        destinationStationPicker.getItems().clear();

        for (DestinationStation destinationStation : App.getStationManager().getDestinationStations()) {
            if (!destinationStation.getName().equals(selectedDepartureStation)) {
                destinationStationPicker.getItems().add(destinationStation.getName());
            }
        }

        destinationStationPicker.setDisable(false);
        destinationStationPicker.setPrefWidth(281);
        findJourneysButton.setDisable(true);
    }

    /**
     * Toggles the <code>Visible</code> status of the date <code>ChoiceBox</code>'s
     * Used when a user clicks on a different value in the 'Search options' <code>ChoiceBox</code>
     *
     * @param shouldHide whether the <code>ChoiceBox</code>'s should be hidden or not
     */
    private void toggleDatePicker(Boolean shouldHide) {
        hourDatePickerLabel.setVisible(shouldHide);
        hourDatePicker.setVisible(shouldHide);
        minuteDatePickerLabel.setVisible(shouldHide);
        minuteDatePicker.setVisible(shouldHide);
        dateDatePickerLabel.setVisible(shouldHide);
        dateDatePicker.setVisible(shouldHide);
        yearDatePickerLabel.setVisible(shouldHide);
        yearDatePicker.setVisible(shouldHide);
    }
}
package com.TobyMellor.TrainRouteFinder.gui.pages.admin;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.modals.AddIntermediateStationsModal;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.JourneyTableRow;
import com.TobyMellor.TrainRouteFinder.gui.KeyValuePair;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;
import com.TobyMellor.TrainRouteFinder.gui.pages.tables.TableManager;
import com.TobyMellor.TrainRouteFinder.journeys.Journey;
import com.TobyMellor.TrainRouteFinder.journeys.JourneyManager;
import com.TobyMellor.TrainRouteFinder.routes.BasicRoute;
import com.TobyMellor.TrainRouteFinder.routes.BasicRouteManager;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Allows admin users to create a <code>Journey</code> for a given <code>BasicRoute</code>
 *
 * A <code>BasicRoute</code> is selected, and then the admin user can add/remove <code>IntermediateStations</code>
 * that the train will call at along the route
 * In addition, a departure date can be selected and for convenience the next 15 minute interval is selected.
 *
 * You cannot create routes set in the past.
 *
 * While the user is building the <code>Journey</code>, <code>Journey</code> details are shown in the top right.
 *
 * At the bottom of the page, there is a table listing all <code>Journey</code>s in the system. This is sorted
 * based on the order they were created. The <code>IntermediateStation</code>s along the route can be sorted by Order
 * they appear on the route or in alphabetical order.
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/28 00:42:00 $
 */

public class CreateJourneyPage extends Layout {
    ChoiceBox<KeyValuePair> routePicker;

    ChoiceBox<Integer> yearDatePicker;
    ChoiceBox<String> dateDatePicker;

    ChoiceBox<String> hourDatePicker;
    ChoiceBox<String> minuteDatePicker;

    Text routeDateDetails;
    Text routeStationDetails;
    Text routeDetails;
    Text routeIntermediateStationDetails;

    Button addRemoveIntermediateStationButton;
    Button createJourneyButton;
    Button deleteSelectedJourneyButton;
    Button sortIntermediateStationsButton;

    TableView<JourneyTableRow> table;

    List<String> intermediateStationIds = new ArrayList<String>();

    Boolean intermediateStationsAlphabeticalOrder = false;

    public CreateJourneyPage(Stage primaryStage) {
        setPrimaryStage(primaryStage);

        Button backButton = ElementManager.createButton("<- Back to the Admin Menu", 0);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new AdminMenuPage(getPrimaryStage());
            }
        });

        Text routeDetailsTitle = ElementManager.createText("Your Journey details:", 0, 0, Pos.TOP_RIGHT, "h5");
        routeDateDetails = ElementManager.createText("Date: Date not yet selected!", 25, 0, Pos.TOP_RIGHT, "h6");
        routeStationDetails = ElementManager.createText("Route: Route not yet selected!", 40, 0, Pos.TOP_RIGHT, "h6");
        routeIntermediateStationDetails = ElementManager.createText("Calling at: No intermediate stations selected yet!", 55, 0, Pos.TOP_RIGHT, "h6");
        routeDetails = ElementManager.createText("Details: Route not yet selected!", 70, 0, Pos.TOP_RIGHT, "h6");

        Text mainTitle = ElementManager.createText("Create a Journey!", 50, "h3");
        Text subtitle = ElementManager.createText("Fill in the details below", 80, "h6");

        TextFlow routeLabel = ElementManager.createTextLabel("Choose your Route", 105, "h6");

        routePicker = new ChoiceBox<KeyValuePair>();
        StackPane.setMargin(routePicker, new Insets(120, 0, 0, 0));
        StackPane.setAlignment(routePicker, Pos.TOP_LEFT);

        for (BasicRoute basicRoute : App.getBasicRouteManager().getBasicRoutes()) {
            routePicker.getItems().add(new KeyValuePair(basicRoute.getId(), App.getStationManager().getDestinationStation(basicRoute.getDepartingStationId()).getName() + " to " + App.getStationManager().getDestinationStation(basicRoute.getDestinationStationId()).getName()));
        }

        Text intermediateLabel = ElementManager.createText("Modify Intermediate Stations", 155, "h6");

        addRemoveIntermediateStationButton = ElementManager.createButton("Add/remove Intermediate Stations", 170, "button-green");
        addRemoveIntermediateStationButton.setOnAction(new EventHandler<ActionEvent>() {
            private CreateJourneyPage createJourneyPage; // the current page

            private EventHandler init(CreateJourneyPage createJourneyPage) {
                this.createJourneyPage = createJourneyPage;

                return this;
            }

            public void handle(ActionEvent event) {
                Stage stage = new Stage();

                showModal(stage, new AddIntermediateStationsModal().AddIntermediateStationsModal(getPrimaryStage(), stage, createJourneyPage, intermediateStationIds), event, "Add Intermediate Stations");

                //addIntermediateStation();
            }
        }.init(this)); // passes in the current stage to the anonymous function so we can use it in the modal

        Text datePickerTitle = ElementManager.createText("Choose the departure time", 215, "h5");

        Calendar roundedCalendar = Calendar.getInstance();

        int unroundedMinutes = roundedCalendar.get(Calendar.MINUTE);
        int modMinutes = unroundedMinutes % 15;
        roundedCalendar.add(Calendar.MINUTE, 15 - modMinutes); // round calendar to the next 15 minutes to prevent setting date in past

        TextFlow hourDatePickerLabel = ElementManager.createTextLabel("Hour:", 239, "");

        hourDatePicker = new ChoiceBox<String>();
        StackPane.setMargin(hourDatePicker, new Insets(255, 0, 0, 0));
        StackPane.setAlignment(hourDatePicker, Pos.TOP_LEFT);

        for (int i = 0; i < 24; i++) {
            hourDatePicker.getItems().add(String.format("%02d", i)); // force format 00, 01, 02 instead of 0, 1, 2

            if (roundedCalendar.get(Calendar.HOUR_OF_DAY) == i) {
                hourDatePicker.getSelectionModel().selectLast();
            }
        }

        TextFlow minuteDatePickerLabel = ElementManager.createTextLabel("Min:", 239, 60, "");

        minuteDatePicker = new ChoiceBox<String>();
        StackPane.setMargin(minuteDatePicker, new Insets(255, 0, 0, 60));
        StackPane.setAlignment(minuteDatePicker, Pos.TOP_LEFT);

        for (int i = 0; i < 60; i++) {
            minuteDatePicker.getItems().add(String.format("%02d", i));

            if (roundedCalendar.get(Calendar.MINUTE) == i) {
                minuteDatePicker.getSelectionModel().selectLast();
            }
        }

        TextFlow dateDatePickerLabel = ElementManager.createTextLabel("Day:", 239, 120, "");

        dateDatePicker = new ChoiceBox<String>();
        StackPane.setMargin(dateDatePicker, new Insets(255, 0, 0, 120));
        StackPane.setAlignment(dateDatePicker, Pos.TOP_LEFT);

        Calendar start = roundedCalendar;
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 1);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM");

            dateDatePicker.getItems().add(sdf.format(date));
        }

        dateDatePicker.getSelectionModel().selectFirst();

        TextFlow yearDatePickerLabel = ElementManager.createTextLabel("Year:", 239, 209, "");

        yearDatePicker = new ChoiceBox<Integer>();
        StackPane.setMargin(yearDatePicker, new Insets(255, 0, 0, 209));
        StackPane.setAlignment(yearDatePicker, Pos.TOP_LEFT);
        yearDatePicker.getItems().addAll(2017, 2018, 2019);
        yearDatePicker.getSelectionModel().selectFirst();

        createJourneyButton = ElementManager.createButton("Create Journey!", 290, "button-green");
        createJourneyButton.setDisable(true);
        createJourneyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                createJourney();
            }
        });

        routePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValuePair>() {
            public void changed(ObservableValue<? extends KeyValuePair> observable, KeyValuePair oldValue, KeyValuePair newValue) {
                loadJourneyDetails();
            }
        });
        hourDatePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadJourneyDetails();
            }
        });
        minuteDatePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadJourneyDetails();
            }
        });
        dateDatePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadJourneyDetails();
            }
        });
        yearDatePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                loadJourneyDetails();
            }
        });

        loadJourneyDetails();

        sortIntermediateStationsButton = ElementManager.createButton("Sort Intermediate Stations by Order", 415, 0, null, Pos.TOP_RIGHT, "button-small");
        sortIntermediateStationsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                intermediateStationsAlphabeticalOrder = !intermediateStationsAlphabeticalOrder;

                if (intermediateStationsAlphabeticalOrder) {
                    sortIntermediateStationsButton.setText("Sort Intermediate Stations by Order");
                } else {
                    sortIntermediateStationsButton.setText("Sort Intermediate Stations by Station Name");
                }

                loadJourneys();
                loadIntermediateStations();
            }
        });

        Text tableTitle = ElementManager.createText("All Journeys:", 415, "h3");

        table = TableManager.setupJourneyTable("No Journeys have been created!");

        StackPane.setAlignment(table, Pos.TOP_LEFT);
        StackPane.setMargin(table, new Insets(450, 0, 40, 0));
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<JourneyTableRow>() {
            public void changed(ObservableValue<? extends JourneyTableRow> observable, JourneyTableRow oldValue, JourneyTableRow newValue) {
                deleteSelectedJourneyButton.setDisable(false);
            }
        });

        deleteSelectedJourneyButton = ElementManager.createButton("Delete Selected Journey", 0, 0, null, Pos.BOTTOM_RIGHT, "button-red");
        deleteSelectedJourneyButton.setDisable(true);
        deleteSelectedJourneyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                deleteJourney();
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(30, 30, 30, 30));

        stackPane.getChildren().addAll(
                backButton,
                mainTitle,
                subtitle,
                routeLabel,
                routePicker,
                intermediateLabel,
                addRemoveIntermediateStationButton,
                datePickerTitle,
                hourDatePickerLabel,
                hourDatePicker,
                minuteDatePickerLabel,
                minuteDatePicker,
                dateDatePickerLabel,
                dateDatePicker,
                yearDatePickerLabel,
                yearDatePicker,
                routeDetailsTitle,
                routeDateDetails,
                routeStationDetails,
                routeIntermediateStationDetails,
                routeDetails,
                createJourneyButton,
                sortIntermediateStationsButton,
                tableTitle,
                table,
                deleteSelectedJourneyButton
        );

        loadJourneys();

        renderPage(stackPane, "AdminPanel", "Admin > Create Journey Page");
    }

    /**
     * Gets all of the Journeys in the system, puts them in reverse order to get the most
     * recently created <code>Journey</code> first, then populates the table with those Journeys
     */
    private void loadJourneys() {
        JourneyManager journeyManager = App.getJourneyManager();

        List<Journey> journeys = journeyManager.getJourneys();
        ArrayList<Journey> reversedJourneys = new ArrayList<Journey>(journeys); // otherwise it will flip JourneyManager's journeys permanently
        Collections.reverse(reversedJourneys); // reverse to get the most recently created Journey first

        routePicker.getSelectionModel().clearSelection();
        createJourneyButton.setDisable(true);

        TableManager.populateJourneyTable(table, journeys, intermediateStationsAlphabeticalOrder);
    }

    /**
     * Creates a <code>Journey</code> based on the data from all of the <code>ChoiceBox</code>'s
     *
     * @throws ValidationException when com.TobyMellor.TrainRouteFinder.validation.validators.JourneyValidator fails
     *
     * @see com.TobyMellor.TrainRouteFinder.validation.validators.JourneyValidator
     */
    private void createJourney() {
        BasicRouteManager basicRouteManager = App.getBasicRouteManager();
        JourneyManager journeyManager = App.getJourneyManager();

        BasicRoute basicRoute = basicRouteManager.getBasicRoute(routePicker.getSelectionModel().getSelectedItem().getKey());

        try {
            journeyManager.createJourney(basicRoute.getId(), intermediateStationIds, getDateFromChoiceBoxes(hourDatePicker, minuteDatePicker, dateDatePicker, yearDatePicker).getTime());

            loadJourneys();
        } catch (ValidationException e) {
            ElementManager.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getMessage(), e.getValidationMessages());
        }
    }

    /**
     * Loads the current <code>Journey</code> details on-the-fly based on the input from all of
     * the <code>ChoiceBox</code>'s
     *
     * The <code>createJourneyButton</code> will be disabled if the either date or stations are not set
     */
    private void loadJourneyDetails() {
        Boolean isDateSet = false;
        Boolean isStationsSet = false;

        if (!hourDatePicker.getSelectionModel().isEmpty()
                && !minuteDatePicker.getSelectionModel().isEmpty()
                && !dateDatePicker.getSelectionModel().isEmpty()
                && !yearDatePicker.getSelectionModel().isEmpty()) {
            Date date = getDateFromChoiceBoxes(hourDatePicker, minuteDatePicker, dateDatePicker, yearDatePicker);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            SimpleDateFormat newSimpleDateFormat = new SimpleDateFormat("EEEE 'the' d'" + getDaySuffix(calendar.get(Calendar.DAY_OF_MONTH)) + " of 'MMMM' at 'HH:mm', 'YYYY");
            routeDateDetails.setText("Date: " + newSimpleDateFormat.format(date));

            isDateSet = true;
        }

        if (!routePicker.getSelectionModel().isEmpty()) {
            routeStationDetails.setText("Stations: " + routePicker.getSelectionModel().getSelectedItem().toString());

            BasicRoute basicRoute = App.getBasicRouteManager().getBasicRoute(routePicker.getSelectionModel().getSelectedItem().getKey());

            routeDetails.setText("Details: Single: £" + basicRoute.getSinglePrice().setScale(2, BigDecimal.ROUND_HALF_UP) + ", Return: £" + basicRoute.getReturnPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + ", Duration: " + basicRoute.getDuration() + " (minutes)");

            isStationsSet = true;
        }

        if (isDateSet && isStationsSet) {
            createJourneyButton.setDisable(false);
        }
    }

    /**
     * Loads the formatted <code>IntermediateStation</code>s under 'Your Journey Details:'
     */
    private void loadIntermediateStations() {
        String intermediateStationList = TableManager.buildIntermediateStationList(intermediateStationIds, "No intermediate stations selected yet!", intermediateStationsAlphabeticalOrder);

        routeIntermediateStationDetails.setText("Calling at: " + intermediateStationList);
    }

    /**
     * Deletes the selected <code>Journey</code> from the table and from the current session
     */
    private void deleteJourney() {
        App.getJourneyManager().deleteJourney(table.getSelectionModel().getSelectedItem().getId());
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
        table.getSelectionModel().clearSelection();

        deleteSelectedJourneyButton.setDisable(true);
    }

    /**
     * Sets the <code>intermediateStationIds</code> and reloads the <code>IntermediateStation</code>s
     *
     * Used from the Modal when the modal is closed and needs to deposit data to this page
     *
     * @param intermediateStationIds an array of IntermediateStation IDs to store into intermediateStationIds
     */
    public void setIntermediateStationIdsFromModal(List<String> intermediateStationIds) {
        this.intermediateStationIds = intermediateStationIds;

        loadIntermediateStations();
    }
}
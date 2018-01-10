package com.TobyMellor.TrainRouteFinder.gui.pages.tables;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.journeys.Journey;
import com.TobyMellor.TrainRouteFinder.routes.BasicRoute;
import com.TobyMellor.TrainRouteFinder.routes.BasicRouteManager;
import com.TobyMellor.TrainRouteFinder.stations.DestinationStation;
import com.TobyMellor.TrainRouteFinder.stations.IntermediateStation;
import com.TobyMellor.TrainRouteFinder.stations.StationManager;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Shorthand function for setting up common tables and populating their fields based on input
 *
 * This class also includes helper functions to tables, such as <code>intermediateIds</code> formatting
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:14:00 $
 */

public class TableManager {
    /**
     * Creates a table with the fields
     *     - Departure Time (<code>departureDate</code>)
     *     - Arrives (<code>arrivalDate</code>)
     *     - Departing (<code>departingStationName</code>)
     *     - Destination (<code>destinationStationName</code>)
     *     - Calling at (<code>intermediateStationNames</code>)
     *     - Duration (<code>duration</code>)
     *     - Single (<code>singlePrice</code>)
     *     - Return (<code>returnPrice</code>)
     *     - Timestamp (<code>timestamp</code>) (hidden)
     *
     * The timestamp field is hidden as it contains no use formatted information to the user. It is only in place
     * to enable easy <code>Departure Time</code> sorting
     *
     * @param label text to be displayed when no elements are in the table
     *
     * @return TableView<JourneyTableRow>
     */
    public static TableView<JourneyTableRow> setupJourneyTable(String label) {
        TableColumn<JourneyTableRow, String> departureTimeColumn = new TableColumn<JourneyTableRow, String>("Departure Time");
        departureTimeColumn.setMinWidth(175);
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("departureDate"));

        TableColumn<JourneyTableRow, String> arrivalTimeColumn = new TableColumn<JourneyTableRow, String>("Arrives");
        arrivalTimeColumn.setMinWidth(75);
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("arrivalDate"));

        TableColumn<JourneyTableRow, String> departingStationNameColumn = new TableColumn<JourneyTableRow, String>("Departing");
        departingStationNameColumn.setMinWidth(100);
        departingStationNameColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("departingStationName"));

        TableColumn<JourneyTableRow, String> destinationStationNameColumn = new TableColumn<JourneyTableRow, String>("Destination");
        destinationStationNameColumn.setMinWidth(100);
        destinationStationNameColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("destinationStationName"));

        TableColumn<JourneyTableRow, String> intermediateStationNamesColumn = new TableColumn<JourneyTableRow, String>("Calling at");
        intermediateStationNamesColumn.setMinWidth(150);
        intermediateStationNamesColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("intermediateStationNames"));

        TableColumn<JourneyTableRow, String> durationColumn = new TableColumn<JourneyTableRow, String>("Duration");
        durationColumn.setMinWidth(100);
        durationColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("duration"));

        TableColumn<JourneyTableRow, String> singlePriceColumn = new TableColumn<JourneyTableRow, String>("Single");
        singlePriceColumn.setMinWidth(120);
        singlePriceColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("singlePrice"));

        TableColumn<JourneyTableRow, String> returnPriceColumn = new TableColumn<JourneyTableRow, String>("Return");
        returnPriceColumn.setMinWidth(120);
        returnPriceColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("returnPrice"));

        TableColumn<JourneyTableRow, Long> timestampColumn = new TableColumn<JourneyTableRow, Long>("Timestamp");
        timestampColumn.setMinWidth(50);
        timestampColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, Long>("timestamp"));
        timestampColumn.setVisible(false); // hidden

        TableView table = new TableView<JourneyTableRow>();
        table.getColumns().addAll(departureTimeColumn, arrivalTimeColumn, departingStationNameColumn, destinationStationNameColumn, intermediateStationNamesColumn, durationColumn, singlePriceColumn, returnPriceColumn, timestampColumn);
        table.setPlaceholder(new Label(label));

        return table;
    }

    /**
     * Populates TableView<JourneyTableRow> with <code>Journey</code> data
     *
     * Clears the current table contents, loops through all passed journeys, creates a new <code>JourneyTableRow</code>
     * and appends it onto the table
     *
     * @param table                                 the table to populate
     * @param journeys                              the list of journeys to append to the table
     * @param intermediateStationsAlphabeticalOrder whether or not the <code>intermediateStationNames</code> should be sorted alphabetically (true)
     *                                              or in the order they appear in the <code>Journey</code>
     */
    public static void populateJourneyTable(TableView table, List<Journey> journeys, Boolean intermediateStationsAlphabeticalOrder) {
        StationManager stationManager = App.getStationManager();
        BasicRouteManager basicRouteManager = App.getBasicRouteManager();

        table.getItems().clear();

        for (Journey journey : journeys) {
            BasicRoute basicRoute = basicRouteManager.getBasicRoute(journey.getBasicRouteId());

            table.getItems().add(new JourneyTableRow(
                    journey.getId(),
                    stationManager.getDestinationStation(basicRoute.getDepartingStationId()).getName(),
                    stationManager.getDestinationStation(basicRoute.getDestinationStationId()).getName(),
                    buildIntermediateStationList(journey.getIntermediateStationIds(), "N/A - Direct Route!", intermediateStationsAlphabeticalOrder),
                    basicRoute.getDuration(),
                    formatCurrency(basicRoute.getSinglePrice(), journey.getTimestamp()),
                    formatCurrency(basicRoute.getReturnPrice(), journey.getTimestamp()),
                    journey.getTimestamp()
            ));
        }
    }

    /**
     * Creates a table with the fields
     *     - Order (order)
     *     - ID (id)
     *     - Station Name (stationName)
     *     - Station Type (stationType)
     *
     * @return TableView<StationTableRow>
     */
    public static TableView<StationTableRow> setupStationTable() {
        TableColumn<StationTableRow, Integer> orderColumn = new TableColumn<StationTableRow, Integer>("Order");
        orderColumn.setMinWidth(100);
        orderColumn.setCellValueFactory(new PropertyValueFactory<StationTableRow, Integer>("order"));

        TableColumn<StationTableRow, Integer> idColumn = new TableColumn<StationTableRow, Integer>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<StationTableRow, Integer>("id"));

        TableColumn<StationTableRow, String> nameColumn = new TableColumn<StationTableRow, String>("Station Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<StationTableRow, String>("stationName"));

        TableColumn<JourneyTableRow, String> typeColumn = new TableColumn<JourneyTableRow, String>("Station Type");
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("type"));

        TableView table = new TableView<StationTableRow>();
        table.getColumns().addAll(idColumn, nameColumn, typeColumn);
        table.setPlaceholder(new Label("No Stations have been created!"));

        return table;
    }

    /**
     * Populates TableView<StationTable> with <code>DestinationStation</code> and <code>IntermediateStation</code> data
     *
     * Clears the current table contents, loops through all passed <code>DestinationStation</code>s/<code>IntermediateStation</code>s, creates a new <code>StationTableRow</code>
     * and appends it onto the table
     *
     * @param table                the table to populate
     * @param destinationStations  the list of <code>DestinationStation</code>s to append to the table
     * @param intermediateStations the list of <code>IntermediateStation</code>s to append to the table
     */
    public static void populateStationTable(TableView<StationTableRow> table, List<DestinationStation> destinationStations, List<IntermediateStation> intermediateStations) {
        table.getItems().clear();

        for (DestinationStation destinationStation : destinationStations) {
            table.getItems().add(new StationTableRow(destinationStation.getId(), destinationStation.getName(), "Destination"));
        }

        for (IntermediateStation intermediateStation : intermediateStations) {
            table.getItems().add(new StationTableRow(intermediateStation.getId(), intermediateStation.getName(), "Intermediate"));
        }
    }

    /**
     * Creates a table with the fields
     *     - Order (order)
     *     - Station Name (stationName)
     *
     * @return TableView<IntermediateStationTableRow>
     */
    public static TableView<IntermediateStationTableRow> setupIntermediateStationTable() {
        TableColumn<IntermediateStationTableRow, Integer> orderColumn = new TableColumn<IntermediateStationTableRow, Integer>("Order");
        orderColumn.setMinWidth(100);
        orderColumn.setCellValueFactory(new PropertyValueFactory<IntermediateStationTableRow, Integer>("order"));

        TableColumn<IntermediateStationTableRow, String> nameColumn = new TableColumn<IntermediateStationTableRow, String>("Station Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<IntermediateStationTableRow, String>("stationName"));

        TableView table = new TableView<StationTableRow>();
        table.getColumns().addAll(orderColumn, nameColumn);
        table.setPlaceholder(new Label("No Intermediate Stations have been added yet!"));

        return table;
    }

    /**
     * Populates TableView<IntermediateStationTableRow> with <code>IntermediateStation</code> data
     *
     * Clears the current table contents, loops through the <code>intermediateStationIds</code>, creates a new <code>IntermediateStationTableRow</code>
     * and appends it onto the table
     *
     * Order is simply based on the <code>List</code> order, with the first element beginning with 1
     *
     * @param table                  the table to populate
     * @param intermediateStationIds intermediateStationIds to loop through
     */
    public static void populateIntermediateStationTable(TableView<IntermediateStationTableRow> table, List<String> intermediateStationIds) {
        table.getItems().clear();

        Integer order = 1; // order given by intermediateStationIds is correct, so start at 1

        for (String intermediateStationId : intermediateStationIds) {
            table.getItems().add(new IntermediateStationTableRow(intermediateStationId, order, App.getStationManager().getIntermediateStation(intermediateStationId).getName()));

            order++;
        }
    }

    /**
     * Creates a table with the fields
     *     - ID (id)
     *     - Departing (departingStationName)
     *     - Destination (destinationStationName)
     *     - Duration (duration)
     *     - Single (singlePrice)
     *     - Return (returnPrice)
     *
     * @return TableView<BasicRouteTableRow>
     */
    public static TableView<BasicRouteTableRow> setupBasicRouteTable() {
        TableColumn<BasicRouteTableRow, String> idColumn = new TableColumn<BasicRouteTableRow, String>("ID");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<BasicRouteTableRow, String>("id"));

        TableColumn<JourneyTableRow, String> departingStationNameColumn = new TableColumn<JourneyTableRow, String>("Departing");
        departingStationNameColumn.setMinWidth(100);
        departingStationNameColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("departingStationName"));

        TableColumn<JourneyTableRow, String> destinationStationNameColumn = new TableColumn<JourneyTableRow, String>("Destination");
        destinationStationNameColumn.setMinWidth(100);
        destinationStationNameColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("destinationStationName"));

        TableColumn<JourneyTableRow, String> durationColumn = new TableColumn<JourneyTableRow, String>("Duration");
        durationColumn.setMinWidth(100);
        durationColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("duration"));

        TableColumn<JourneyTableRow, String> singlePriceColumn = new TableColumn<JourneyTableRow, String>("Single");
        singlePriceColumn.setMinWidth(120);
        singlePriceColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("singlePrice"));

        TableColumn<JourneyTableRow, String> returnPriceColumn = new TableColumn<JourneyTableRow, String>("Return");
        returnPriceColumn.setMinWidth(120);
        returnPriceColumn.setCellValueFactory(new PropertyValueFactory<JourneyTableRow, String>("returnPrice"));

        TableView table = new TableView<JourneyTableRow>();
        table.getColumns().addAll(idColumn, departingStationNameColumn, destinationStationNameColumn, durationColumn, singlePriceColumn, returnPriceColumn);
        table.setPlaceholder(new Label("No Basic Routes have been created! Add more manually to the XML file!"));

        return table;
    }

    /**
     * Populates TableView<BasicRouteTableRow> with <code>BasicRoute</code> data
     *
     * Clears the current table contents, loops through the <code>BasicRoute</code> data, creates a new <code>BasicRouteTableRow</code>
     * and appends it onto the table
     *
     * Currency is formatted here, discounts for end of month are not applied here though since
     * <code>BasicRoute</code>s don't include timestamps, only <code>Journey</code>s do
     *
     * @param table       the table to populate
     * @param basicRoutes basicRoutes to loop through
     */
    public static void populateBasicRouteTable(TableView<BasicRouteTableRow> table, List<BasicRoute> basicRoutes) {
        table.getItems().clear();

        for (BasicRoute basicRoute : basicRoutes) {
            table.getItems().add(new BasicRouteTableRow(
                    basicRoute.getId(),
                    App.getStationManager().getDestinationStation(basicRoute.getDepartingStationId()).getName(),
                    App.getStationManager().getDestinationStation(basicRoute.getDestinationStationId()).getName(),
                    basicRoute.getDuration(),
                    formatCurrency(basicRoute.getSinglePrice()),
                    formatCurrency(basicRoute.getReturnPrice())
            ));
        }
    }

    /**
     * Takes <code>intermediateStationIds</code>, and separates them into a formatted list.
     *
     * If <code>alphabeticalOrder</code> is true, <code>intermediateStationNames</code> will be separated in an English sentence list format
     * where the list is sorted alphabetically
     *      e.g. [Eggs, Milk, Carrots] to carrots, eggs and milk
     *
     * Otherwise, the order of the <code>intermediateStationNames</code> will be presented in the order provided through <code>intermediateStationIds</code>
     *      e.g. [Eggs, Milk, Carrots] to 1. Eggs 2. Milk 3. Carrots
     *
     * @param intermediateStationIds the ordered list of <code>id</code>s
     * @param noStationsMessage      the message provided when <code>intermediateStationIds</code> is empty
     * @param alphabeticalOrder      whether or not to sort alphabetically or in the provided order
     *
     * @return String
     */
    public static String buildIntermediateStationList(List<String> intermediateStationIds, String noStationsMessage, Boolean alphabeticalOrder) {
        StringBuilder stringBuilder = new StringBuilder();

        if (intermediateStationIds.size() > 0) {
            List<String> intermediateStationNames = new ArrayList<String>();

            for (String intermediateStationId : intermediateStationIds) {
                intermediateStationNames.add(App.getStationManager().getIntermediateStation(intermediateStationId).getName());
            }

            String delimiter = "";

            if (alphabeticalOrder) { // order alphabetically, list as English sentence
                Collections.sort(intermediateStationNames);

                int index = 0;

                for (String intermediateStationName : intermediateStationNames) {
                    stringBuilder.append(delimiter).append(intermediateStationName);

                    if (index == intermediateStationNames.size() - 2) {
                        delimiter = " and ";
                    } else {
                        delimiter = ", ";
                    }

                    index++;
                }
            } else { // order same as route, in format 1. Test > 2. Another etc.
                int order = 1;

                for (String intermediateStationName : intermediateStationNames) {
                    stringBuilder.append(delimiter).append(order + ". ").append(intermediateStationName);

                    delimiter = " > ";

                    order++;
                }
            }
        } else {
            stringBuilder.append(noStationsMessage);
        }

        return stringBuilder.toString();
    }

    /**
     * Formats the <code>BigDecimal</code> forcefully to 2 decimal places and prepends "£"
     *
     * @param currency the amount
     *
     * @return String
     */
    public static String formatCurrency(BigDecimal currency) {
        return "£" + currency.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Formats the <code>BigDecimal</code> forcefully to 2 decimal places and prepends "£"
     *
     * However, if the provided <code>timestamp</code> references the last day of a given month,
     * the function will take 10% off the price and tell the user what's just happened
     *
     * @param currency the amount
     * @param timestamp a timestamp to be checked against
     *
     * @return String
     */
    public static String formatCurrency(BigDecimal currency, long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));

        if (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) { // getActualMaximum returns the highest day for the current month
            currency = currency.multiply(new BigDecimal(0.9)); // * 0.9 gives 10% off

            return "£" + currency.setScale(2, BigDecimal.ROUND_HALF_UP) + " (10% off!)";
        }

        return "£" + currency.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
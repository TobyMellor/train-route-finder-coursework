package com.TobyMellor.TrainRouteFinder;

import com.TobyMellor.TrainRouteFinder.gui.GUI;
import com.TobyMellor.TrainRouteFinder.journeys.Journey;
import com.TobyMellor.TrainRouteFinder.journeys.JourneyManager;
import com.TobyMellor.TrainRouteFinder.routes.BasicRoute;
import com.TobyMellor.TrainRouteFinder.routes.BasicRouteManager;
import com.TobyMellor.TrainRouteFinder.stations.DestinationStation;
import com.TobyMellor.TrainRouteFinder.stations.IntermediateStation;
import com.TobyMellor.TrainRouteFinder.stations.StationManager;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;
import com.TobyMellor.TrainRouteFinder.validation.validators.BasicRouteValidator;
import com.TobyMellor.TrainRouteFinder.validation.validators.DestinationStationValidator;
import com.TobyMellor.TrainRouteFinder.validation.validators.IntermediateStationValidator;
import com.TobyMellor.TrainRouteFinder.validation.validators.JourneyValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main class which will load data from the XML files
 * and instantiate the corresponding managers and classes.
 *
 * The GUI is started from this class.
 *
 * Manager Instances can be obtained statically from this class.
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 19:18:00 $
 */

public class App {
    private static StationManager stationManager = null;
    private static BasicRouteManager basicRouteManager = null;
    private static JourneyManager journeyManager = null;

    public static void main(String[] args) {
        try {
            loadXML();

            final GUI gui = new GUI();
            gui.startGUI(); // starts the GUI and waits
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    /**
     * Populates <code>destinationStations</code>, <code>intermediateStations</code> and <code>basicRoutes</code>
     * with their respective objects.
     *
     * Instantiates the three managers with the populated lists and stores them in public variables in this class.
     *
     * Validates all of the populated lists before adding them to their managers.
     *
     * @throws IOException if file stations.xml, basic_routes.xml or journeys.xml are not found or cannot be opened
     * @throws SAXException if one of the XML files cannot be parsed
     * @throws ParserConfigurationException if the document cannot be built
     */
    public static void loadXML() throws IOException, SAXException, ParserConfigurationException {
        List<DestinationStation> destinationStations = new ArrayList<DestinationStation>();
        List<IntermediateStation> intermediateStations = new ArrayList<IntermediateStation>();
        List<BasicRoute> basicRoutes = new ArrayList<BasicRoute>();
        List<Journey> journeys = new ArrayList<Journey>();

        StationManager sm = new StationManager(destinationStations, intermediateStations);
        BasicRouteManager brm = new BasicRouteManager(basicRoutes);
        JourneyManager jm = new JourneyManager(journeys);

        setStationManager(sm);
        setBasicRouteManager(brm);
        setJourneyManager(jm);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document stationsXML = builder.parse("resources/xml/stations.xml");

        NodeList destinationStationsXML = stationsXML.getElementsByTagName("destinationStation");
        NodeList intermediateStationsXML = stationsXML.getElementsByTagName("intermediateStation");

        for (int i = 0; i < destinationStationsXML.getLength(); i++) {
            Node destinationStationXML = destinationStationsXML.item(i);

            if (destinationStationXML.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) destinationStationXML;

                String id = element.getAttribute("id"); // we're using String UUIDs instead of incrementing numerical IDs
                String stationName = element.getElementsByTagName("name").item(0).getTextContent(); // there's only one element in the parent called "name"

                DestinationStation destinationStation = new DestinationStation(id, stationName);

                try {
                    destinationStation.validate(new DestinationStationValidator());
                    destinationStations.add(destinationStation); // push a new instance of DestinationStation to the list, provided it has been correctly validated
                } catch (ValidationException e) {
                    System.out.print(e.getMessage() + "\n");
                    System.out.print(e.getValidationMessages() + "\n");
                }
            }
        }

        for (int i = 0; i < intermediateStationsXML.getLength(); i++) {
            Node intermediateStationXML = intermediateStationsXML.item(i);

            if (intermediateStationXML.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) intermediateStationXML;

                String id = element.getAttribute("id");
                String stationName = element.getElementsByTagName("name").item(0).getTextContent();

                IntermediateStation intermediateStation = new IntermediateStation(id, stationName);

                try {
                    intermediateStation.validate(new IntermediateStationValidator());
                    intermediateStations.add(intermediateStation);
                } catch (ValidationException e) {
                    System.out.print(e.getMessage() + "\n");
                    System.out.print(e.getValidationMessages() + "\n");
                }
            }
        }

        NodeList basicRoutesXML = builder.parse("resources/xml/basic_routes.xml").getElementsByTagName("basicRoute");

        for (int i = 0; i < basicRoutesXML.getLength(); i++) {
            Node basicRouteXML = basicRoutesXML.item(i);

            if (basicRouteXML.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) basicRouteXML;

                String id = element.getAttribute("id");
                String departingStationId = element.getElementsByTagName("departingStationId").item(0).getTextContent();
                String destinationStationId = element.getElementsByTagName("destinationStationId").item(0).getTextContent();
                BigDecimal singlePrice = new BigDecimal(element.getElementsByTagName("singlePrice").item(0).getTextContent());
                BigDecimal returnPrice = new BigDecimal(element.getElementsByTagName("returnPrice").item(0).getTextContent());
                int duration = Integer.parseInt(element.getElementsByTagName("duration").item(0).getTextContent());

                BasicRoute basicRoute = new BasicRoute(id, departingStationId, destinationStationId, singlePrice, returnPrice, duration);

                try {
                    basicRoute.validate(new BasicRouteValidator());
                    basicRoutes.add(basicRoute);
                } catch (ValidationException e) {
                    System.out.print(e.getMessage() + "\n");
                    System.out.print(e.getValidationMessages() + "\n");
                }
            }
        }

        NodeList journeysXML = builder.parse("resources/xml/journeys.xml").getElementsByTagName("journey");

        for (int i = 0; i < journeysXML.getLength(); i++) {
            Node journeyXML = journeysXML.item(i);

            if (journeyXML.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) journeyXML;

                String id = element.getAttribute("id");
                String basicRouteId = element.getElementsByTagName("basicRouteId").item(0).getTextContent();
                long timestamp = Long.parseLong(element.getElementsByTagName("timestamp").item(0).getTextContent());

                NodeList intermediateStationIdsXML = element.getElementsByTagName("intermediateStationId");
                List<String> intermediateStationIds = new ArrayList<String>();

                for (int j = 0; j < intermediateStationIdsXML.getLength(); j++) {
                    intermediateStationIds.add(intermediateStationIdsXML.item(j).getTextContent());
                }

                Journey journey = new Journey(id, basicRouteId, intermediateStationIds, timestamp);

                try {
                    journey.validate(new JourneyValidator(true));
                    journeys.add(journey);
                } catch (ValidationException e) {
                    System.out.print(e.getMessage() + "\n");
                    System.out.print(e.getValidationMessages() + "\n");
                }
            }
        }
    }

    /**
     * Saves the XML files for each manager.
     *
     * @see Manager
     */
    public static void saveXML() {
        getStationManager().save();
        getBasicRouteManager().save();
        getJourneyManager().save();
    }

    /**
     * Returns the instance of <code>StationManager</code>
     *
     * @return StationManager
     */
    public static StationManager getStationManager() {
        return App.stationManager;
    }

    /**
     * Sets an instance of <code>StationManager</code> to the public <code>stationManager</code> variable in this class
     *
     * @param stationManager the <code>StationManager</code> instance
     */
    public static void setStationManager(final StationManager stationManager) {
        App.stationManager = stationManager;
    }

    /**
     * Returns the instance of <code>BasicRouteManager</code>
     *
     * @return BasicRouteManager
     */
    public static BasicRouteManager getBasicRouteManager() {
        return App.basicRouteManager;
    }

    /**
     * Sets an instance of <code>BasicRouteManager</code> to the public <code>basicRouteManager</code> variable in this class
     *
     * @param basicRouteManager the <code>BasicRouteManager</code> instance
     */
    public static void setBasicRouteManager(final BasicRouteManager basicRouteManager) {
        App.basicRouteManager = basicRouteManager;
    }

    /**
     * Returns the instance of <code>JourneyManager</code>
     *
     * @return JourneyManager
     */
    public static JourneyManager getJourneyManager() {
        return App.journeyManager;
    }

    /**
     * Sets an instance of <code>JourneyManager</code> to the public <code>journeyManager</code> variable in this class
     *
     * @param journeyManager the <code>JourneyManager</code> instance
     */
    public static void setJourneyManager(final JourneyManager journeyManager) {
        App.journeyManager = journeyManager;
    }
}

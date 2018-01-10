package com.TobyMellor.TrainRouteFinder.stations;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.Manager;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;
import com.TobyMellor.TrainRouteFinder.validation.validators.IntermediateStationValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Manager containing Lists containing the <code>destinationStations</code> and <code>intermediateStations</code>
 * Used to perform operations on the whole collection of objects
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:43:00 $
 */

public class StationManager extends Manager {
    private List<DestinationStation> destinationStations;
    private List<IntermediateStation> intermediateStations;

    public StationManager(List<DestinationStation> destinationStations, List<IntermediateStation> intermediateStations) {
        setDestinationStations(destinationStations);
        setIntermediateStations(intermediateStations);
    }

    /**
     * Retrieves the whole collection of loaded <code>DestinationStation</code>s.
     *
     * @return List<DestinationStation>
     */
    public List<DestinationStation> getDestinationStations() {
        return destinationStations;
    }

    /**
     * Sets the whole collection of <code>DestinationStation</code>s.
     *
     * @param destinationStations the collection of <code>DestinationStation</code>s
     *
     * @return List<DestinationStation>
     */
    public void setDestinationStations(final List<DestinationStation> destinationStations) {
        this.destinationStations = destinationStations;
    }

    /**
     * Retrieves the <code>DestinationStation</code> where the <code>id</code> is the <code>destinationStationId</code>.
     * Returns null if a <code>DestinationStation</code> with that <code>id</code> is not found.
     *
     * @param destinationStationId the ID of the DestinationStation to return
     *
     * @return DestinationStation
     */
    public DestinationStation getDestinationStation(final String destinationStationId) {
        final List<DestinationStation> destinationStations = getDestinationStations();

        for (DestinationStation destinationStation : destinationStations) {
            if (destinationStationId.equals(destinationStation.getId())) {
                return destinationStation;
            }
        }

        return null;
    }

    /**
     * Retrieves the whole collection of loaded <code>IntermediateStation</code>s.
     *
     * @return List<IntermediateStation>
     */
    public List<IntermediateStation> getIntermediateStations() {
        return intermediateStations;
    }

    /**
     * Sets the whole collection of <code>IntermediateStation</code>s.
     *
     * @param intermediateStations the collection of <code>IntermediateStation</code>s
     *
     * @return List<IntermediateStation>
     */
    public void setIntermediateStations(final List<IntermediateStation> intermediateStations) {
        this.intermediateStations = intermediateStations;
    }

    /**
     * Retrieves the <code>IntermediateStation</code> where the <code>id</code> is the <code>intermediateStationId</code>.
     * Returns null if a <code>IntermediateStation</code> with that <code>id</code> is not found.
     *
     * @param intermediateStationId the ID of the IntermediateStation to return
     *
     * @return IntermediateStation
     */
    public IntermediateStation getIntermediateStation(final String intermediateStationId) {
        final List<IntermediateStation> intermediateStations = getIntermediateStations();

        for (IntermediateStation intermediateStation : intermediateStations) {
            if (intermediateStationId.equals(intermediateStation.getId())) {
                return intermediateStation;
            }
        }

        return null;
    }

    /**
     * Validates and creates a new <code>IntermediateStation</code> with <code>stationName</code>.
     * Returns the <code>IntermediateStation</code> that has just been created.
     *
     * @param stationName the name of the <code>IntermediateStation</code>
     *
     * @return IntermediateStation
     *
     * @throws ValidationException if the stationName is not valid
     */
    public IntermediateStation createIntermediateStation(final String stationName) throws ValidationException {
        final String intermediateStationId = UUID.randomUUID().toString();
        final IntermediateStation intermediateStation = new IntermediateStation(intermediateStationId, stationName);

        intermediateStation.validate(new IntermediateStationValidator()); // throws ValiationException up the calling chain

        intermediateStations.add(intermediateStation);
        return intermediateStation;
    }

    /**
     * Deletes the <code>IntermediateStation</code> where the <code>id</code> is <code>intermediateStationId</code>
     *
     * @param intermediateStationId id of the <code>IntermediateStation</code>
     */
    public void deleteIntermediateStation(final String intermediateStationId) {
        final List<IntermediateStation> intermediateStations = getIntermediateStations();

        for (IntermediateStation intermediateStation : intermediateStations) {
            if (intermediateStationId.equals(intermediateStation.getId())) {
                intermediateStations.remove(intermediateStation);

                break;
            }
        }

        App.getJourneyManager().removeIntermediateStationIdFromJourneys(intermediateStationId);
    }

    /**
     * Converts the collection of <code>DestinationStation</code>s and <code>IntermediateStation</code>s
     * into XML and writes it to the corresponding XML file
     *
     * Any errors are thrown and caught at the lower level
     */
    public void save() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("stations");
            Element intermediateStationsElement = document.createElement("intermediateStations");
            Element destinationStationsElement = document.createElement("destinationStations");

            for (IntermediateStation intermediateStation : getIntermediateStations()) {
                Element intermediateStationElement = document.createElement("intermediateStation");
                intermediateStationElement.setAttribute("id", intermediateStation.getId());

                intermediateStationElement.appendChild(createXMLElement(document, "name", intermediateStation.getName()));

                intermediateStationsElement.appendChild(intermediateStationElement);
            }

            for (DestinationStation destinationStation : getDestinationStations()) {
                Element destinationStationElement = document.createElement("destinationStation");
                destinationStationElement.setAttribute("id", destinationStation.getId());

                destinationStationElement.appendChild(createXMLElement(document, "name", destinationStation.getName()));

                destinationStationsElement.appendChild(destinationStationElement);
            }

            rootElement.appendChild(intermediateStationsElement);
            rootElement.appendChild(destinationStationsElement);

            document.appendChild(rootElement);

            writeXML(document, new File("resources/xml/stations.xml"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

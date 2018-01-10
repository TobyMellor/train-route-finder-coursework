package com.TobyMellor.TrainRouteFinder.journeys;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.Manager;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;
import com.TobyMellor.TrainRouteFinder.validation.validators.JourneyValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Manager containing Lists containing the <code>journeys</code>
 * Used to perform operations on the whole collection of objects
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:44:00 $
 */

public class JourneyManager extends Manager {
    private List<Journey> journeys;

    public JourneyManager(List<Journey> journeys) {
        setJourneys(journeys);
    }

    /**
     * Retrieves the whole collection of loaded <code>Journey</code>s
     *
     * @return List<Journey>
     */
    public List<Journey> getJourneys() {
        return journeys;
    }

    /**
     * Retrieves the <code>Journey</code>s with the <code>basicRouteId</code>
     *
     * @return List<Journey>
     */
    public List<Journey> getJourneys(final String basicRouteId) {
        final List<Journey> queriedJourneys = new ArrayList<Journey>();

        for (Journey journey : getJourneys()) {
            if (journey.getBasicRouteId().equals(basicRouteId)) {
                queriedJourneys.add(journey);
            }
        }

        return queriedJourneys;
    }

    /**
     * Retrieves the <code>Journey</code>s with the <code>basicRouteId</code>
     * and where either the
     *     - train is departing after <code>timestamp</code> or
     *     - train is arriving before <code>timestamp</code> but not departing in the past
     *
     * @param basicRouteId the ID of the BasicRoute
     * @param timestamp the time at which the train departs
     * @param isLeavingAfter whether or not to search for trains leaving after the provided timestamp
     *
     * @return List<Journey>
     */
    public List<Journey> getJourneys(final String basicRouteId, final long timestamp, final Boolean isLeavingAfter) {
        final List<Journey> queriedJourneys = new ArrayList<Journey>();
        Date dateFromChoiceBoxes = new Date(timestamp);

        for (Journey journey : getJourneys(basicRouteId)) {
            Date departureDate = new Date(journey.getTimestamp());

            if (!isLeavingAfter) {
                Date arrivalDate = new Date(journey.getTimestamp() + (App.getBasicRouteManager().getBasicRoute(journey.getBasicRouteId()).getDuration() * 60000)); // the Journey departure time, plus the duration of the Journey in millis

                if (departureDate.after(new Date()) && !arrivalDate.after(dateFromChoiceBoxes)) { // don't show Journeys that leave in the past but show Journeys that arrive before the given date
                    queriedJourneys.add(journey);
                }
            } else {
                if (departureDate.after(dateFromChoiceBoxes)) {
                    queriedJourneys.add(journey);
                }
            }
        }

        return queriedJourneys;
    }

    /**
     * Sets the whole collection of <code>Journey</code>s
     *
     * @param journeys the collection of <code>Journey</code>s
     *
     * @return List<Journey>
     */
    public void setJourneys(final List<Journey> journeys) {
        this.journeys = journeys;
    }

    /**
     * Retrieves the <code>Journey</code> where the <code>id</code> is <code>journeyId</code>
     *
     * Returns null if the <code>Journey</code> cannot be found
     *
     * @param journeyId the ID of the Journey to return
     *
     * @return Journey
     */
    public Journey getJourney(final String journeyId) {
        for (Journey journey : getJourneys()) {
            if (journey.getId().equals(journeyId)) {
                return journey;
            }
        }

        return null;
    }

    /**
     * Validates and creates a new <code>Journey</code> with the passed information.
     * Returns the <code>Journey</code> that has just been created
     *
     * @param basicRouteId           the <code>id</code> of the linked <code>BasicRoute</code>
     * @param intermediateStationIds a list of <code>IntermediateStation</code> ids
     * @param timestamp              the time at which the <code>Journey</code> departs
     *
     * @return Journey
     *
     * @throws ValidationException if the validator fails
     *
     * @see JourneyValidator
     */
    public Journey createJourney(final String basicRouteId, final List<String> intermediateStationIds, final Long timestamp) throws ValidationException {
        final String journeyId = UUID.randomUUID().toString();
        final Journey journey = new Journey(journeyId, basicRouteId, new ArrayList<String>(intermediateStationIds), timestamp);

        journey.validate(new JourneyValidator(false)); // throws ValidationException up the calling chain

        journeys.add(journey);
        return journey;
    }

    /**
     * Deletes the <code>Journey</code> where the <code>id</code> is <code>journeyId</code>
     *
     * @param journeyId id of the <code>Journey</code>
     */
    public void deleteJourney(final String journeyId) {
        final List<Journey> journeys = getJourneys();

        for (Journey journey : journeys) {
            if (journey.getId().equals(journeyId)) {
                journeys.remove(journey);
                setJourneys(journeys);

                break;
            }
        }
    }

    /**
     * Removes the <code>intermediateStationId</code>s from all <code>Journey</code>s
     * where the <code>intermediateStationId</code> is <code>intermediateStationId</code>
     *
     * Prevents <code>Journey</code>s trying to load information about a station that doesn't exist
     *
     * @param intermediateStationId id of the <code>IntermediateStation</code> to remove from all <code>Journey</code>s
     */
    public void removeIntermediateStationIdFromJourneys(final String intermediateStationId) {
        List<Journey> journeys = getJourneys();

        for (Journey journey : journeys) {
            for (String journeyIntermediateStationId : journey.getIntermediateStationIds()) {
                if (journeyIntermediateStationId.equals(intermediateStationId)) {
                    List<String> intermediateStationIds = journey.getIntermediateStationIds(); // the intermediateStationIds for the current Journey

                    intermediateStationIds.remove(journeyIntermediateStationId); // remove the intermediateStationId from the current Journey's intermediateStationIds

                    break;
                }
            }
        }
    }

    /**
     * Converts the collection of <code>Journey</code>s
     * into XML and writes it to the corresponding XML file
     *
     * Any errors are thrown and caught at the lower level
     */
    public void save() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("journeys");

            for (Journey journey : getJourneys()) {
                Element journeyElement = document.createElement("journey");
                journeyElement.setAttribute("id", journey.getId());

                journeyElement.appendChild(createXMLElement(document, "basicRouteId", journey.getBasicRouteId()));

                Element intermediateStationIdsElement = document.createElement("intermediateStationIds");

                for (String intermediateStationId : journey.getIntermediateStationIds()) {
                    intermediateStationIdsElement.appendChild(createXMLElement(document, "intermediateStationId", intermediateStationId));
                }

                journeyElement.appendChild(intermediateStationIdsElement);
                journeyElement.appendChild(createXMLElement(document, "timestamp", String.valueOf(journey.getTimestamp())));

                rootElement.appendChild(journeyElement);
            }

            document.appendChild(rootElement);

            writeXML(document, new File("resources/xml/journeys.xml"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

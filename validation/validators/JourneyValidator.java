package com.TobyMellor.TrainRouteFinder.validation.validators;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.journeys.Journey;
import com.TobyMellor.TrainRouteFinder.validation.ValidationUtils;
import com.TobyMellor.TrainRouteFinder.validation.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Passed into an objects <code>validate()</code> method and checks validation rules
 * specific to <code>Journeys</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:26:00 $
 */

public class JourneyValidator extends ValidationUtils implements Validator<Journey> {
    private List<String> messages = new ArrayList<String>();
    public Boolean alreadyExists;

    public JourneyValidator(Boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }

    /**
     * When a <code>Journey</code> is validated, this method checks if
     *     - <code>id</code> is unique
     *     - <code>basicRouteId</code> exists
     *     - all of the <code>intermediateStationIds</code> exist
     *     - none of the <code>intermediateStationIds</code> are duplicated
     *     - <code>timestamp</code> is not in the past (if validating a non-existant <code>journey</code>)
     *
     * @param journey the <code>Journey</code> object
     *
     * @return Boolean
     */
    @Override
    public boolean validate(final Journey journey) {
        if (App.getJourneyManager().getJourney(journey.getId()) != null) {
            messages.add("A Journey with that ID already exists!");
        }

        if (App.getBasicRouteManager().getBasicRoute(journey.getBasicRouteId()) == null) {
            messages.add("The Basic Route with that ID doesn't exist!");
        }

        List<String> checkedIntermediateStationIds = new ArrayList<String>();

        for (String intermediateStationId : journey.getIntermediateStationIds()) {
            if (checkedIntermediateStationIds.indexOf(intermediateStationId) > -1) {
                messages.add("There is already an Intermediate Station with the ID '" + intermediateStationId + "' in your list of Intermediate Stations");
            } else if (App.getStationManager().getIntermediateStation(intermediateStationId) == null) {
                messages.add("The Intermediate Station with the ID '" + intermediateStationId + "' does not exist!");
            }

            checkedIntermediateStationIds.add(intermediateStationId);
        }

        if (!alreadyExists && !new Date().before(new Date(journey.getTimestamp()))) {
            messages.add("The Journey Date cannot be set in the past!");
        }

        return messages.size() == 0;
    }

    @Override
    public List<String> getValidationMessages() {
        return this.messages;
    }
}

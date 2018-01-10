package com.TobyMellor.TrainRouteFinder.validation.validators;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.stations.IntermediateStation;
import com.TobyMellor.TrainRouteFinder.validation.ValidationUtils;
import com.TobyMellor.TrainRouteFinder.validation.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Passed into an objects <code>validate()</code> method and checks validation rules
 * specific to <code>IntermediateStations</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:26:00 $
 */

public class IntermediateStationValidator extends ValidationUtils implements Validator<IntermediateStation> {
    private List<String> messages = new ArrayList<String>();

    /**
     * When a <code>IntermediateStation</code> is validated, this method checks if
     *     - <code>id</code> is unique
     *     - <code>stationName</code> is of correct length
     *     - <code>stationName</code> is not called 'Other' (since 'Other' is used in <code>ChoiceBox's</code>)
     *     - <code>stationName</code> contains only alphanumeric characters, spaces and hyphens
     *     - the <code>stationName</code> is unique
     *
     * @param intermediateStation the <code>IntermediateStation</code> object
     *
     * @return Boolean
     */
    @Override
    public boolean validate(final IntermediateStation intermediateStation) {
        if (App.getStationManager().getIntermediateStation(intermediateStation.getId()) != null) {
            messages.add("An Intermediate Station with that ID already exists!");
        }

        if (!ValidationUtils.isBetween(intermediateStation.getName(), 3, 20)) {
            messages.add("The Station Name must be between 3 and 20 characters in length!");
        }

        if (intermediateStation.getName().equalsIgnoreCase("Other")) {
            messages.add("The Station Name cannot be called 'Other'");
        }

        if (!ValidationUtils.isValidName(intermediateStation.getName())) {
            messages.add("The Station Name can only contain alphanumeric characters, spaces and hyphens!");
        }

        for (IntermediateStation im : App.getStationManager().getIntermediateStations()) {
            if (im.getName().equalsIgnoreCase(intermediateStation.getName())) {
                messages.add("The Station Name must be unique! A station called '" + intermediateStation.getName() + "' already exists!");
                break;
            }
        }

        return messages.size() == 0;
    }

    @Override
    public List<String> getValidationMessages() {
        return this.messages;
    }
}

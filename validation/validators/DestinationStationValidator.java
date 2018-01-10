package com.TobyMellor.TrainRouteFinder.validation.validators;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.stations.DestinationStation;
import com.TobyMellor.TrainRouteFinder.stations.IntermediateStation;
import com.TobyMellor.TrainRouteFinder.validation.ValidationUtils;
import com.TobyMellor.TrainRouteFinder.validation.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Passed into an objects <code>validate()</code> method and checks validation rules
 * specific to <code>DestinationStations</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:26:00 $
 */

public class DestinationStationValidator extends ValidationUtils implements Validator<DestinationStation> {
    private List<String> messages = new ArrayList<String>();

    /**
     * When a <code>DestinationStation</code> is validated, this method checks if
     *     - <code>id</code> is unique
     *     - <code>stationName</code> is of correct length
     *     - <code>stationName</code> contains only alphanumeric characters, spaces and hyphens
     *     - the <code>stationName</code> is unique
     *
     * @param destinationStation the <code>DestinationStation</code> object
     *
     * @return Boolean
     */
    @Override
    public boolean validate(final DestinationStation destinationStation) {
        if (App.getStationManager().getDestinationStation(destinationStation.getId()) != null) {
            messages.add("A Destination Station with that ID already exists!");
        }

        if (!ValidationUtils.isBetween(destinationStation.getName(), 3, 20)) {
            messages.add("The Station Name must be between 3 and 20 characters in length!");
        }

        if (!ValidationUtils.isValidName(destinationStation.getName())) {
            messages.add("The Station Name can only contain alpha characters, spaces and hyphens!");
        }

        for (DestinationStation ds : App.getStationManager().getDestinationStations()) {
            if (ds.getName().equalsIgnoreCase(destinationStation.getName())) {
                messages.add("The Station Name must be unique! A station called '" + destinationStation.getName() + "' already exists!");
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

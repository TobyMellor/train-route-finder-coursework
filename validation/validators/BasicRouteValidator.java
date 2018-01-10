package com.TobyMellor.TrainRouteFinder.validation.validators;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.routes.BasicRoute;
import com.TobyMellor.TrainRouteFinder.validation.ValidationUtils;
import com.TobyMellor.TrainRouteFinder.validation.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Passed into an objects <code>validate()</code> method and checks validation rules
 * specific to <code>BasicRoutes</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:26:00 $
 */

public class BasicRouteValidator extends ValidationUtils implements Validator<BasicRoute> {
    private List<String> messages = new ArrayList<String>();

    /**
     * When a <code>BasicRoute</code> is validated, this method checks if the
     *     - <code>id</code> is unique
     *     - <code>departingStationId</code> exists
     *     - <code>destinationStationId</code> exists
     *     - <code>departingStationId</code> and <code>destinationStationId</code> do not match
     *     - <code>singlePrice</code> and <code>returnPrice</code> are non-negative, valid BigIntegers
     *     - <code>duration</code> is a positive integer
     *
     * @param basicRoute the <code>BasicRoute</code> object
     *
     * @return Boolean
     */
    @Override
    public boolean validate(final BasicRoute basicRoute) {
        if (App.getBasicRouteManager().getBasicRoute(basicRoute.getId()) != null) {
            messages.add("A Basic Route with that ID already exists!");
        }

        if (App.getStationManager().getDestinationStation(basicRoute.getDepartingStationId()) == null) {
            messages.add("The Departing Station does not exist!");
        }

        if (App.getStationManager().getDestinationStation(basicRoute.getDestinationStationId()) == null) {
            messages.add("The Destination Station does not exist!");
        }

        if (basicRoute.getDepartingStationId().equals(basicRoute.getDestinationStationId())) {
            messages.add("The Departing Station and Destination cannot match!");
        }

        if (!ValidationUtils.isValidPrice(basicRoute.getSinglePrice())) {
            messages.add("The Single Price must be a valid chargeable amount!");
        }

        if (!ValidationUtils.isValidPrice(basicRoute.getReturnPrice())) {
            messages.add("The Return Price must be a valid chargeable amount!");
        }

        if (basicRoute.getDuration() < 0) {
            messages.add("The Duration cannot be negative!");
        }

        return messages.size() == 0;
    }

    @Override
    public List<String> getValidationMessages() {
        return this.messages;
    }
}

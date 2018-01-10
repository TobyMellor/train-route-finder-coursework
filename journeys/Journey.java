package com.TobyMellor.TrainRouteFinder.journeys;

import com.TobyMellor.TrainRouteFinder.validation.Validatable;
import com.TobyMellor.TrainRouteFinder.validation.Validator;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;

import java.util.List;

/**
 * Stores information about a particular <code>Journey</code> and can be used to
 * validate said information
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 22:31:00 $
 */

public class Journey implements Validatable {
    private Validator<Journey> validator;

    private String id;
    private String basicRouteId;
    private List<String> intermediateStationIds;
    private Long timestamp;

    public Journey(final String id, final String basicRouteId, final List<String> intermediateStationIds, final Long timestamp) {
        setId(id);
        setBasicRouteId(basicRouteId);
        setIntermediateStationIds(intermediateStationIds);
        setTimestamp(timestamp);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getBasicRouteId() {
        return basicRouteId;
    }

    public void setBasicRouteId(final String basicRouteId) {
        this.basicRouteId = basicRouteId;
    }

    public List<String> getIntermediateStationIds() {
        return intermediateStationIds;
    }

    public void setIntermediateStationIds(final List<String> intermediateStationIds) {
        this.intermediateStationIds = intermediateStationIds;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public void validate(final Validator<Journey> v) throws ValidationException {
        validator = v;

        if (!v.validate(this)){
            throw new ValidationException("A ValidationException occurred when validating a Journey!", getValidationMessages());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return this.validator.getValidationMessages();
    }
}

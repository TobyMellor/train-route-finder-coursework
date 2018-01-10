package com.TobyMellor.TrainRouteFinder.routes;

import com.TobyMellor.TrainRouteFinder.validation.Validatable;
import com.TobyMellor.TrainRouteFinder.validation.Validator;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Stores information about a particular <code>BasicRoute</code> and can be used to
 * validate said information
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 22:31:00 $
 */

public class BasicRoute implements Validatable {
    private Validator<BasicRoute> validator;

    private String id;
    private String departingStationId;
    private String destinationStationId;
    private BigDecimal singlePrice;
    private BigDecimal returnPrice;
    private int duration;

    public BasicRoute(final String id, final String departingStationId, final String destinationStationId, final BigDecimal singlePrice, final BigDecimal returnPrice, final int duration) {
        setId(id);
        setDepartingStationId(departingStationId);
        setDestinationStationId(destinationStationId);
        setSinglePrice(singlePrice);
        setReturnPrice(returnPrice);
        setDuration(duration);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDepartingStationId() {
        return departingStationId;
    }

    public void setDepartingStationId(final String departingStationId) {
        this.departingStationId = departingStationId;
    }

    public String getDestinationStationId() {
        return destinationStationId;
    }

    public void setDestinationStationId(final String destinationStationId) {
        this.destinationStationId = destinationStationId;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(final BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(final BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void validate(final Validator<BasicRoute> v) throws ValidationException {
        validator = v;

        if (!v.validate(this)){
            throw new ValidationException("A ValidationException occurred when validating a Route!", getValidationMessages());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return this.validator.getValidationMessages();
    }
}

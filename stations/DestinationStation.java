package com.TobyMellor.TrainRouteFinder.stations;

import com.TobyMellor.TrainRouteFinder.validation.Validatable;
import com.TobyMellor.TrainRouteFinder.validation.Validator;
import com.TobyMellor.TrainRouteFinder.validation.exceptions.ValidationException;

import java.util.List;

/**
 * Extends the <code>Station</code> class and is used to validate specific to <code>DestinationStation</code>s
 * and implement methods specific to <code>DestinationStation</code>s
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:57:00 $
 */

public class DestinationStation extends Station implements Validatable {
    private Validator<DestinationStation> validator;

    public DestinationStation(final String id, final String name) {
        super(id, name);
    }

    public void validate(final Validator<DestinationStation> v) throws ValidationException {
        validator = v;

        if (!v.validate(this)){
            throw new ValidationException("A ValidationException occurred when validating a Destination Station!", getValidationMessages());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return this.validator.getValidationMessages();
    }
}
package com.TobyMellor.TrainRouteFinder.validation;

import java.util.List;

/**
 * Implemented by classes which contains validation methods
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 20:41:00 $
 */

public interface Validatable {
    /**
     * Abstract method that must be implemented by classes which implement <code>Validatable</code>
     * Returns a list of error messages
     *
     * @return List<String>
     */
    List<String> getValidationMessages();
}

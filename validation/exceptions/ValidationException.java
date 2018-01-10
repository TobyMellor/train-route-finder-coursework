package com.TobyMellor.TrainRouteFinder.validation.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * The Exception thrown by objects containing the <code>validate()</code> method
 * Extends Exception, and can be used to get detailed validation messages from each <code>Validator</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:38:00 $
 */

public class ValidationException extends Exception {
    private List<String> validationMessages = new ArrayList<String>();

    public ValidationException(final String generalMessage, final List<String> validationMessages) {
        super(generalMessage);
        this.validationMessages = validationMessages;
    }

    /**
     * Turns List<String> into a single string, formatted nicely.
     *
     * @return String
     */
    public String getValidationMessages() {
        StringBuilder sb = new StringBuilder();

        for (String validationMessage : this.validationMessages) {
            sb.append(" - " + validationMessage + "\n");
        }

        return sb.toString();
    }
}
package com.TobyMellor.TrainRouteFinder.gui;

/**
 * A class solely used for <code>ChoiceBox</code>'s in order to store a records
 * id along with its string value
 *
 * This helps as its better to search for objects using their Ids rather than a string, where the
 * string might not be in the correct format.
 *
 * Example:
 * On the <code>CreateJourneyPage</code>, 'Choose your Route' contains dropdown records in the form:
 *      '<code>departureStationName</code> to <code>destinationStationName</code>'
 * There is no nice way to get the <code>BasicRoute</code> from this record by examining just the string
 * so a hidden key is used.
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 22:36:00 $
 */

public class KeyValuePair {
    private final String key;
    private final String value;

    public KeyValuePair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String toString() { // method name 'toString' is essential as it is called by JavaFX, can't have getValue()
        return value;
    }
}

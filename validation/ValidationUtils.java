package com.TobyMellor.TrainRouteFinder.validation;

import java.math.BigDecimal;

/**
 * A collection of useful utilities that return a Boolean value based on the conditions
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 20:32:00 $
 */

abstract public class ValidationUtils {
    /**
     * Checks if <code>string</code>'s character length is between <code>lowerBound</code> and <code>upperBound</code>
     *
     * @param string the string to be checked
     * @param lowerBound the minimum amount of characters required to return true
     * @param upperBound the maximum amount of characters required to return true
     *
     * @return Boolean
     */
    protected static Boolean isBetween(final String string, final int lowerBound, final int upperBound) {
        return string.length() >= lowerBound && string.length() <= upperBound;
    }

    /**
     * Checks if a <code>stationName</code> contains only spaces, alpha characters and hyphens
     *
     * @param name the name of the <code>Station</code> to be checked
     *
     * @return Boolean
     */
    protected static Boolean isValidName(final String name) {
        return name.matches("^[\\p{L} .'-]+$");
    }

    /**
     * Checks if a given <code>price</code> is non-negative valid BigDecimal
     *
     * @param price the price to be checked
     *
     * @return Boolean
     */
    protected static Boolean isValidPrice(final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) >= 0;
    }
}
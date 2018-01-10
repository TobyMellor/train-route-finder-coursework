package com.TobyMellor.TrainRouteFinder.gui.pages.tables;

import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;
import com.TobyMellor.TrainRouteFinder.gui.pages.Layout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A TableRow element with the fields
 *     - id
 *     - departingStationName
 *     - destinationStationName
 *     - intermediateStationNames
 *     - duration
 *     - singlePrice
 *     - returnPrice
 *     - departureDate
 *     - arrivalDate
 *     - timestamp
 *
 * Field values can be accessed through public getters
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:48:00 $
 */

public class JourneyTableRow extends TableRow {
    final private String departingStationName;
    final private String destinationStationName;
    final private String intermediateStationNames;
    final private String duration;
    final private String singlePrice;
    final private String returnPrice;
    final private String departureDate;
    final private String arrivalDate;
    final private Long timestamp;

    /**
     * Constructor for <code>JourneyTableRow</code>
     *
     * The <code>duration</code> is converted from an <code>int</code> value representing minutes into
     * a formatted string showing minutes and hours
     *
     * The <code>departureDate</code> is derrived from <code>timestamp</code> and is shown in the format "d'" + suffix + " of 'MMMM' at 'HH:mm', 'YYYY"
     * The <code>arrivalDate</code> is derrived from the <code>timestamp</code> plus the duration and is shown in the format "HH:mm"
     *
     * @param id                       <code>Journey</code> id
     * @param departingStationName     <code>Station</code> name of departing station
     * @param destinationStationName   <code>Station</code> name of destination station
     * @param intermediateStationNames a formatted list of <code>IntermediateStation</code> names
     * @param duration                 duration of the journey in minutes
     * @param singlePrice              the formatted price of a single ticket (discount applied)
     * @param returnPrice              the formatted price of a return ticket (discount applied)
     * @param timestamp                the timestamp of the departure
     */
    public JourneyTableRow(String id, String departingStationName, String destinationStationName, String intermediateStationNames, int duration, String singlePrice, String returnPrice, long timestamp) {
        this.id = id;
        this.departingStationName = departingStationName;
        this.destinationStationName = destinationStationName;
        this.intermediateStationNames = intermediateStationNames;

        this.duration = ElementManager.formatDuration(duration);

        this.singlePrice = singlePrice;
        this.returnPrice = returnPrice;

        Calendar departureTime = Calendar.getInstance();
        departureTime.setTime(new Date(timestamp));

        this.departureDate = new SimpleDateFormat("d'" + Layout.getDaySuffix((departureTime.get(Calendar.DAY_OF_MONTH))) + " of 'MMMM' at 'HH:mm', 'YYYY").format(new Date(timestamp));
        this.arrivalDate = new SimpleDateFormat("HH:mm").format(new Date(timestamp + (duration * 60000))); // departure timestamp, plus the duration of the journey in millis

        this.timestamp = timestamp;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartingStationName() {
        return departingStationName;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public String getIntermediateStationNames() {
        return intermediateStationNames;
    }

    public String getDuration() {
        return duration;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public String getReturnPrice() {
        return returnPrice;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}

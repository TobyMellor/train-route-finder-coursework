package com.TobyMellor.TrainRouteFinder.gui.pages.tables;

import com.TobyMellor.TrainRouteFinder.gui.pages.ElementManager;

/**
 * A TableRow element with the fields
 *     - id
 *     - departingStationName
 *     - destinationStationName
 *     - singlePrice
 *     - returnPrice
 *     - duration
 *
 * Field values can be accessed through public getters
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:48:00 $
 */

public class BasicRouteTableRow extends TableRow {
    final private String departingStationName;
    final private String destinationStationName;
    final private String singlePrice;
    final private String returnPrice;
    final private String duration;

    public BasicRouteTableRow(String id, String departingStationName, String destinationStationName, int duration, String singlePrice, String returnPrice) {
        this.id = id;
        this.departingStationName = departingStationName;
        this.destinationStationName = destinationStationName;

        this.duration = ElementManager.formatDuration(duration);

        this.singlePrice = singlePrice;
        this.returnPrice = returnPrice;
    }

    public String getDepartingStationName() {
        return departingStationName;
    }

    public String getDestinationStationName() {
        return destinationStationName;
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
}

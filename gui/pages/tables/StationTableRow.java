package com.TobyMellor.TrainRouteFinder.gui.pages.tables;

/**
 * A TableRow element with the fields
 *     - id
 *     - stationName
 *     - type
 *
 * Field values can be accessed through public getters
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:45:00 $
 */

public class StationTableRow extends TableRow {
    final private String stationName;
    final private String type;

    public StationTableRow(String id, String stationName, String type) {
        this.id = id;
        this.stationName = stationName;
        this.type = type;
    }

    public String getStationName() {
        return stationName;
    }

    public String getType() {
        return type;
    }
}


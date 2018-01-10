package com.TobyMellor.TrainRouteFinder.gui.pages.tables;

/**
 * A TableRow element with the fields
 *     - id
 *     - order
 *     - stationName
 *
 * Field values can be accessed through public getters
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:48:00 $
 */

public class IntermediateStationTableRow extends TableRow {
    final private Integer order;
    final private String stationName;

    public IntermediateStationTableRow(String id, Integer order, String stationName) {
        this.id = id;
        this.order = order;
        this.stationName = stationName;
    }

    public Integer getOrder() {
        return order;
    }

    public String getStationName() {
        return stationName;
    }
}

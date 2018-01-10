package com.TobyMellor.TrainRouteFinder.stations;

/**
 * Abstract class extended by <code>DestinationStation</code> and <code>IntermediateStation</code>
 * All stations have an <code>id</code> and <code>name</code>
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:56:00 $
 */

abstract public class Station {
    private final String id;
    private final String name;

    public Station(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

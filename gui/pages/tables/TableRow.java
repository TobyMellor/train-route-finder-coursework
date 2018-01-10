package com.TobyMellor.TrainRouteFinder.gui.pages.tables;

/**
 * Abstract class extended by all TableRow elements which have <code>id</code> fields
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:12:00 $
 */

abstract public class TableRow {
    protected String id;

    public String getId() {
        return id;
    }
}

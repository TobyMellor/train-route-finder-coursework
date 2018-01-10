package com.TobyMellor.TrainRouteFinder.gui;

import com.TobyMellor.TrainRouteFinder.gui.pages.MenuPage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Simple class used to launch the GUI, set the initial page and close any stages
 * e.g. modals or the actual windows
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 22:39:00 $
 */

public class GUI extends Application {
    public void startGUI() {
        launch();
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        new MenuPage(primaryStage);
    }

    public static void close(final Stage primaryStage) {
        primaryStage.close();
    }
}
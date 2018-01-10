package com.TobyMellor.TrainRouteFinder.gui.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The <code>ElementManager</code>, used statically by most pages
 *
 * A lot of the code in <code>JavaFX</code> needed to create simple elements can be
 * repetitive, without using <code>FXML</code>
 *
 * For most elements, there is a method that creates a element with a label, margin, width, position and class names
 * in the form of varargs
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 23:02:00 $
 */

public class ElementManager {
    public static Button createButton(String label, int marginTop, int marginLeft, Integer preferredWidth, Pos position, String... classNames) {
        Button button = new Button(label);

        if (preferredWidth != null) {
            button.setPrefWidth(preferredWidth);
        }

        StackPane.setAlignment(button, position);
        StackPane.setMargin(button, new Insets(marginTop, 0, 0, marginLeft));
        button.getStyleClass().addAll(classNames);

        return button;
    }

    public static Button createButton(String label, int marginTop, String... classNames) {
        return createButton(label, marginTop, 0, null, Pos.TOP_LEFT, classNames);
    }

    public static Text createText(String content, int marginTop, int marginLeft, Pos position, String className) {
        Text text = new Text(content);

        StackPane.setAlignment(text, position);
        StackPane.setMargin(text, new Insets(marginTop, 0, 0, marginLeft));
        text.getStyleClass().add(className);

        return text;
    }
    public static Text createText(String content, int marginTop, String className) {
        return createText(content, marginTop, 0, Pos.TOP_LEFT, className);
    }

    public static TextFlow createTextLabel(String content, int marginTop, int marginLeft, String className) {
        Text text = new Text(content);
        TextFlow textFlow = new TextFlow(text, getRequiredLabel());

        StackPane.setAlignment(textFlow, Pos.TOP_LEFT);
        StackPane.setMargin(textFlow, new Insets(marginTop, 0, 0, marginLeft));
        textFlow.getStyleClass().add(className);

        return textFlow;
    }

    public static TextFlow createTextLabel(String content, int marginTop, String className) {
        return createTextLabel(content, marginTop, 0, className);
    }

    /**
     * Shows an alert of type with the specified content
     *
     * @param alertType   the type of <code>AlertType</code> e.g. AlertType.error
     * @param title       the title of the alert window
     * @param headerText  the text above the delimiter line above the contentText
     * @param contentText the main detailed text
     */
    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Creates a red text element with the label ' *'
     * Appended to <code>TextFlow</code> elements whose corresponding field is required
     *
     * @return Text
     */
    private static Text getRequiredLabel() {
        Text requiredLabel = new Text(" *");
        requiredLabel.setFill(Color.RED);

        return requiredLabel;
    }

    /**
     * Converts a <code>duration</code> in minutes, into minutes and hours
     * If there is less than 60 minutes, the hours label will not be shown.
     *
     * Formats grammar correctly e.g. 0 minutes, 1 minute, 2 minutes etc.
     *
     * @param duration time in minutes
     *
     * @return String
     */
    public static String formatDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        String minutesString = " minutes";
        String hoursString = " hours, ";

        if (minutes == 1) {
            minutesString = " minute";
        }

        if (hours > 0) {
            if (hours == 1) {
                hoursString = " hour, ";
            }

            return hours + hoursString + minutes + minutesString;
        }

        return minutes + minutesString;
    }
}

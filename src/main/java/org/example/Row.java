package org.example;

import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;


public class Row {
    /**
     * Used as a model class for the display of data in the rows of
     * the tale in inputvalues controller
     */
    private final String filename;
    private LocalDateTime time;
    private final ImageView picture;
    private final CheckBox action;

    /**
     * default constructor
     * @param filename of the photo
     * @param time of photo
     * @param picture file
     * @param action checkbox for selection of rows
     */

    public Row(String filename, LocalDateTime time, ImageView picture, CheckBox action) {
        this.filename = filename;
        this.time = time;
        this.picture = picture;
        this.action = action;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

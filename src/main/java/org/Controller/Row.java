package org.Controller;

import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;


public class Row {
    /**
     * Used as a model class for the display of da
     */
    private String filename;
    private LocalDateTime time;
    private ImageView picture;
    private CheckBox action;

    public Row(String filename, LocalDateTime time, ImageView picture, CheckBox action) {
        this.filename = filename;
        this.time = time;
        this.picture = picture;
        this.action = action;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    public CheckBox getAction() {
        return action;
    }

    public void setAction(CheckBox action) {
        this.action = action;
    }
}

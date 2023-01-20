package org.example;


import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;


public class Row {
    private String filename;
    private LocalDateTime time;
    private String type;
    private ImageView picture;
    private CheckBox action;

    public Row(String filename, LocalDateTime time, String type, ImageView picture, CheckBox action) {
        this.filename = filename;
        this.time = time;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

package org.example;

public class Row {
    private String filename;
    private String time;
    private String type;
    private String picture;
    private String action;

    public Row(String filename, String time, String type, String picture, String action) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

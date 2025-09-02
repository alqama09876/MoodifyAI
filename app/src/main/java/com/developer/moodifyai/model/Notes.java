package com.developer.moodifyai.model;

import java.util.Date;

public class Notes {
    private String uid;
    private String thought;
    private String day;
    private String time;
    private String date;

    public Notes() {
    }

    public Notes(String uid, String thought, String day, String time, String date) {
        this.uid = uid;
        this.thought = thought;
        this.day = day;
        this.time = time;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public String getThought() {
        return thought;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
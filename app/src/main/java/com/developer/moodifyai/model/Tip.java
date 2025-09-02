package com.developer.moodifyai.model;

public class Tip {
    private String text;
    private String description;
    private String example;
    private int imageResId;

    public Tip(String text, int imageResId) {
        this.text = text;
        this.imageResId = imageResId;
    }

    public Tip(String text, int imageResId, String description, String example) {
        this.text = text;
        this.imageResId = imageResId;
        this.description = description;
        this.example = example;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public String getText() {
        return text;
    }

    public int getImageResId() {
        return imageResId;
    }
}
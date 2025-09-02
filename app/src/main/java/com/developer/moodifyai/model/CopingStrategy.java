package com.developer.moodifyai.model;

import java.util.List;

public class CopingStrategy {
    private String title;
    private String description;
    private String subTitle;
    private List<String> subDescription;
    private String image;

    public CopingStrategy() {
    }

    public CopingStrategy(String title, String description, String subTitle, List<String> subDescription, String image) {
        this.title = title;
        this.description = description;
        this.subTitle = subTitle;
        this.subDescription = subDescription;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(List<String> subDescription) {
        this.subDescription = subDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
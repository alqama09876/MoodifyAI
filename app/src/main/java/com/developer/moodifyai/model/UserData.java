package com.developer.moodifyai.model;

import java.util.List;

public class UserData {
    private String userName;
    private String userGender;
    private int userAge;
    private List<String> mainGoal;
    private List<String> mentalHealthCause;
    private String stressFrequency;
    private String healthyEatingHabit;
    private String triedMeditationBefore;
    private String sleepQualityLevel;
    private String happinessLevel;

    public UserData(String userName, String userGender, int userAge, List<String> mainGoal, List<String> mentalHealthCause,
                    String stressFrequency, String healthyEatingHabit, String triedMeditationBefore,
                    String sleepQualityLevel, String happinessLevel) {
        this.userName = userName;
        this.userGender = userGender;
        this.userAge = userAge;
        this.mainGoal = mainGoal;
        this.mentalHealthCause = mentalHealthCause;
        this.stressFrequency = stressFrequency;
        this.healthyEatingHabit = healthyEatingHabit;
        this.triedMeditationBefore = triedMeditationBefore;
        this.sleepQualityLevel = sleepQualityLevel;
        this.happinessLevel = happinessLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public List<String> getMainGoal() {
        return mainGoal;
    }

    public void setMainGoal(List<String> mainGoal) {
        this.mainGoal = mainGoal;
    }

    public List<String> getMentalHealthCause() {
        return mentalHealthCause;
    }

    public void setMentalHealthCause(List<String> mentalHealthCause) {
        this.mentalHealthCause = mentalHealthCause;
    }

    public String getStressFrequency() {
        return stressFrequency;
    }

    public void setStressFrequency(String stressFrequency) {
        this.stressFrequency = stressFrequency;
    }

    public String getHealthyEatingHabit() {
        return healthyEatingHabit;
    }

    public void setHealthyEatingHabit(String healthyEatingHabit) {
        this.healthyEatingHabit = healthyEatingHabit;
    }

    public String getTriedMeditationBefore() {
        return triedMeditationBefore;
    }

    public void setTriedMeditationBefore(String triedMeditationBefore) {
        this.triedMeditationBefore = triedMeditationBefore;
    }

    public String getSleepQualityLevel() {
        return sleepQualityLevel;
    }

    public void setSleepQualityLevel(String sleepQualityLevel) {
        this.sleepQualityLevel = sleepQualityLevel;
    }

    public String getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(String happinessLevel) {
        this.happinessLevel = happinessLevel;
    }
}
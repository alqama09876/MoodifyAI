package com.developer.moodifyai.model;

public class Therapist {
    private String uid;
    private String email;
    private String role;
    private String name;
    private String specialization;
    private String fcmToken;

    public Therapist() {}

    public Therapist(String uid, String email, String role, String name, String specialization) {
        this.uid = uid;
        this.email = email;
        this.role = role;
        this.name = name;
        this.specialization = specialization;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getChatIdWith(String otherUserId) {
        if (uid == null || otherUserId == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }
        return uid.compareTo(otherUserId) < 0 ? uid + "_" + otherUserId : otherUserId + "_" + uid;
    }
}



//package com.developer.moodifyai.model;
//
//public class Therapist {
//    private String uid;
//    private String email;
//    private String role;
//    private String name;
//    private String specialization;
//    private String profileImageUrl;
//    private boolean isAvailable;
//    private String fcmToken;
//
//    // Empty constructor for Firebase
//    public Therapist() {
//    }
//
//    public Therapist(String uid, String email, String role, String name,
//                     String specialization, String profileImageUrl, boolean isAvailable) {
//        this.uid = uid;
//        this.email = email;
//        this.role = role;
//        this.name = name;
//        this.specialization = specialization;
//        this.profileImageUrl = profileImageUrl;
//        this.isAvailable = isAvailable;
//    }
//
//    // Getters and Setters
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSpecialization() {
//        return specialization;
//    }
//
//    public void setSpecialization(String specialization) {
//        this.specialization = specialization;
//    }
//
//    public String getProfileImageUrl() {
//        return profileImageUrl;
//    }
//
//    public void setProfileImageUrl(String profileImageUrl) {
//        this.profileImageUrl = profileImageUrl;
//    }
//
//    public boolean isAvailable() {
//        return isAvailable;
//    }
//
//    public void setAvailable(boolean available) {
//        isAvailable = available;
//    }
//
//    public String getFcmToken() {
//        return fcmToken;
//    }
//
//    public void setFcmToken(String fcmToken) {
//        this.fcmToken = fcmToken;
//    }
//
//    // Helper method for chat ID generation
//    public String getChatIdWith(String otherUserId) {
//        if (uid == null || otherUserId == null) {
//            throw new IllegalArgumentException("User IDs cannot be null");
//        }
//        return uid.compareTo(otherUserId) < 0 ? uid + "_" + otherUserId : otherUserId + "_" + uid;
//    }
//}


//package com.developer.moodifyai.model;
//
//public class Therapist {
//    private String uid;
//    private String email;
//    private String role;
//    private String name;
//    private String specialization;
//
//    public Therapist() {
//    }
//
//    public Therapist(String uid, String email, String role, String name, String specialization) {
//        this.uid = uid;
//        this.email = email;
//        this.role = role;
//        this.name = name;
//        this.specialization = specialization;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSpecialization() {
//        return specialization;
//    }
//
//    public void setSpecialization(String specialization) {
//        this.specialization = specialization;
//    }
//}
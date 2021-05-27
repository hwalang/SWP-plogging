package com.example.myapplication;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserModel {

    public String userName;
    public String profileImageUrl;
    public String uid;
    public String birth;
    public String gender;
    public String address;
    public int steps;
    public Map<String, Object> postValues = new HashMap<>();

    public UserModel(){

    }

    public UserModel(String userName, String profileImageUrl, String uid, String birth, String gender, String address, int steps) {
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.uid = uid;
        this.birth = birth;
        this.gender = gender;
        this.address = address;
        this.steps = steps;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("profileImageUrl", profileImageUrl);
        result.put("uid", uid);
        result.put("birth", birth);
        result.put("gender", gender);
        result.put("address", address);
        result.put("steps", steps);

        return result;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}

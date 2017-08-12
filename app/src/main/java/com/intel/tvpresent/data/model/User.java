package com.intel.tvpresent.data.model;

public class User {
    public String name;
    public int order;
    public String photoUrl;
    public VideoRecord videoRecord;

    public User() {

    }

    public User(String name, int order, String photoUrl, VideoRecord videoRecord) {
        this.name = name;
        this.order = order;
        this.photoUrl = photoUrl;
        this.videoRecord = videoRecord;
    }
}
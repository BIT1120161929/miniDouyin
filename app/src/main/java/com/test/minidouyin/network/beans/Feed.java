package com.test.minidouyin.network.beans;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Feed {

    @SerializedName("student_id")
    private String studentId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("video_url")
    private String videoURL;

    public String getStudentId() {
        return studentId;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}

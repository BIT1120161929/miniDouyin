package com.test.minidouyin.network.beans;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Feed {

    @SerializedName("student_id")
    private String studentId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("image_url")
    private URL imageUrl;
    @SerializedName("video_url")
    private URL videoURL;

    public String getStudentId() {
        return studentId;
    }

    public String getUserName() {
        return userName;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public URL getVideoURL() {
        return videoURL;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoURL(URL videoURL) {
        this.videoURL = videoURL;
    }
}

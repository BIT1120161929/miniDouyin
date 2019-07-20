package com.test.minidouyin.network.beans;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class PostVideoResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("url")
    private URL url;

    public boolean isSuccess() {
        return success;
    }

    public URL getUrl() {
        return url;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
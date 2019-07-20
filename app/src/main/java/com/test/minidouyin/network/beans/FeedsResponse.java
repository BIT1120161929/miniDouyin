package com.test.minidouyin.network.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedsResponse {

    @SerializedName("feeds")
    private List<Feed> feeds;
    @SerializedName("success")
    private boolean success;

    public List<Feed> getFeeds() {
        return feeds;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

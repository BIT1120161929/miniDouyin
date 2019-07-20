package com.test.minidouyin.network;

import android.util.Log;

import com.test.minidouyin.network.beans.Feed;
import com.test.minidouyin.network.beans.FeedsResponse;
import com.test.minidouyin.network.service.VideoListService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkServiceImpl {
    private List<Feed> feedList;

    public List<Feed> getFeedList() {
        return feedList;
    }

    public void fetchFeed(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<FeedsResponse> call = retrofit.create(VideoListService.class).feedRequest();
        call.enqueue(new Callback<FeedsResponse>() {
            @Override
            public void onResponse(Call<FeedsResponse> call, Response<FeedsResponse> response) {
                feedList = response.body().getFeeds();
            }

            @Override
            public void onFailure(Call<FeedsResponse> call, Throwable t) {
            }
        });
    }
}

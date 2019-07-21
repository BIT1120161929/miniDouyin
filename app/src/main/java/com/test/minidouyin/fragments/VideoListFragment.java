package com.test.minidouyin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.minidouyin.MainActivity;
import com.test.minidouyin.R;
import com.test.minidouyin.RecyclerViewAdapter;
import com.test.minidouyin.network.NetworkServiceImpl;
import com.test.minidouyin.network.beans.Feed;
import com.test.minidouyin.network.beans.FeedsResponse;
import com.test.minidouyin.network.service.VideoListService;
import com.test.minidouyin.utils.TransportUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 播放列表
 */
public class VideoListFragment extends Fragment {
    private RecyclerView videoRecyclerView;

    public VideoListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list,container,false);
        videoRecyclerView = view.findViewById(R.id.rv_videolist);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        fetchFeed();

        return view;
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

                List<Feed> feedList = response.body().getFeeds();
                Collections.shuffle(feedList);
                RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(getActivity(),feedList);

                videoRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, String videoUrl) {
                        EventBus.getDefault().post(new TransportUtils(videoUrl));
                    }
                });
            }

            @Override
            public void onFailure(Call<FeedsResponse> call, Throwable t) {}
        });
    }
}

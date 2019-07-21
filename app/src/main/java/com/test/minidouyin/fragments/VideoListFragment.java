package com.test.minidouyin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.minidouyin.R;
import com.test.minidouyin.adapter.RecyclerView4VideoListAdapter;
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
        DividerItemDecoration divider = new DividerItemDecoration(container.getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(container.getContext(),R.drawable.dimension_cool_divider));
        videoRecyclerView.addItemDecoration(divider);
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
                RecyclerView4VideoListAdapter mAdapter = new RecyclerView4VideoListAdapter(getActivity(),feedList);

                videoRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new RecyclerView4VideoListAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, String videoUrl, String name, String id) {
                        EventBus.getDefault().post(new TransportUtils(videoUrl,name,id));
                    }
                });
            }

            @Override
            public void onFailure(Call<FeedsResponse> call, Throwable t) {}
        });
    }
}

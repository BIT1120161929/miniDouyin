package com.test.minidouyin.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.test.minidouyin.R;
import com.test.minidouyin.utils.TransportUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.test.minidouyin.MainActivity.PLAY;

/**
 * 播放界面
 */
public class VideoPlayFragment extends Fragment {

    private VideoView videoView;
    private String videoURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_play,container,false);
        videoView = view.findViewById(R.id.vv_play);

        return view;
    }

    @Subscribe
    public void onEventMainThread(TransportUtils transportUtils){
        if(transportUtils.PALY == PLAY){
            videoURL = transportUtils.videoURL;
            Uri uri = Uri.parse(videoURL);
            videoView.setMediaController(new MediaController(getActivity()));
            videoView.setVideoURI(uri);
            videoView.start();
        }
    }
}

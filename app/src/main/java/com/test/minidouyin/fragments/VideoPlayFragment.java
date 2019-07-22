package com.test.minidouyin.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.test.minidouyin.R;
import com.test.minidouyin.utils.OnDoubleClickListener;
import com.test.minidouyin.utils.TransportUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.test.minidouyin.activity.MainActivity.PLAY;

/**
 * 播放界面
 */
public class VideoPlayFragment extends Fragment {

    private VideoView videoView;
    private String videoURL;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!isVisibleToUser&&videoView!=null){
            videoView.pause();
        }
    }

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
        final LottieAnimationView animationView=view.findViewById(R.id.btn_like);
        final LottieAnimationView animationView1=view.findViewById(R.id.btn_random);
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.playAnimation();
                animationView.loop(false);
            }
        });
        animationView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView1.playAnimation();
                animationView1.loop(false);
            }
        });


        videoView.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                if(videoView.isPlaying())videoView.pause();
                else if(!videoView.isPlaying())videoView.start();
            }
        }));

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        return view;
    }

    @Subscribe
    public void onEventMainThread(TransportUtils transportUtils){
        if(transportUtils.PALY == PLAY){
            videoURL = transportUtils.videoURL;
            Uri uri = Uri.parse(videoURL);
            videoView.setVideoURI(uri);
            videoView.start();
        }
    }
}

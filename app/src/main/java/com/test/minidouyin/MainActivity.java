package com.test.minidouyin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.test.minidouyin.fragments.ShootPostFragment;
import com.test.minidouyin.fragments.VideoListFragment;
import com.test.minidouyin.fragments.VideoPlayFragment;
import com.test.minidouyin.utils.TransportUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pager;

    public static final int PLAY = 1;
    public static final int COME_BACK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.vp_viewpager);
        pager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:return new VideoPlayFragment();
                    case 1:return new VideoListFragment();
                    case 2:return new ShootPostFragment();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });
        pager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * URL传递到Activity
     * @param transportUtils
     */
    @Subscribe
    public void onEventMainThread2Play(TransportUtils transportUtils){
        if(transportUtils.PALY == PLAY){
            pager.setCurrentItem(0);
        }
    }

    @Subscribe
    public void onEventMainThread2Back(Integer type){
        if(type == COME_BACK){
            pager.setCurrentItem(1);
        }
    }
}

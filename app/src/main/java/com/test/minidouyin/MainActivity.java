package com.test.minidouyin;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.test.minidouyin.fragments.ShootVideoFragment;
import com.test.minidouyin.fragments.VideoListFragment;
import com.test.minidouyin.fragments.VideoPlayFragment;
import com.test.minidouyin.network.NetworkServiceImpl;
import com.test.minidouyin.network.beans.Feed;
import com.test.minidouyin.network.beans.FeedsResponse;
import com.test.minidouyin.network.service.VideoListService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = findViewById(R.id.vp_viewpager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:return new VideoPlayFragment();
                    case 1:return new VideoListFragment();
                    case 2:return new ShootVideoFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position){
                switch (position){
                    case 0:return "播放";
                    case 1:return "列表";
                    case 2:return "录制";
                }
                return null;
            }
        });
        pager.setCurrentItem(1);

        TabLayout tabLayout = findViewById(R.id.tl_navigator);
        tabLayout.setupWithViewPager(pager);
    }
}

package com.test.minidouyin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.test.minidouyin.R;

/**
 * 播放界面和用户信息界面的合体界面，用ViewPager2实现的垂直方向滑动切换
 * 第0页为播放页面，第一页为用户信息页面
 */
public class PlayInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_info,null);
        ViewPager2 pager = view.findViewById(R.id.fr_playinfo_vp_infoplay);
        pager.setAdapter(new FragmentStateAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:return new VideoPlayFragment();
                    case 1:return new UserInfoFragment();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        return view;
    }
}

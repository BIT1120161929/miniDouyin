package com.test.minidouyin.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.minidouyin.R;
import com.test.minidouyin.utils.VerticalViewPager;

public class ShootPostFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoot_post,null);
        VerticalViewPager pager = view.findViewById(R.id.vp_postshoot);
        pager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch(i){
                    case 0:return new ShootVideoFragment();
                    case 1:return new PostFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


        return view;
    }
}

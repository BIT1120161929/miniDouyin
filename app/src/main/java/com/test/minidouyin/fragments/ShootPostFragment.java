package com.test.minidouyin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.test.minidouyin.R;
import com.test.minidouyin.utils.VerticalViewPager;

public class ShootPostFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoot_post,null);
        ViewPager2 pager = view.findViewById(R.id.vp_postshoot);
        pager.setAdapter(new FragmentStateAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:return new ShootVideoFragment();
                    case 1:return new PostFragment();
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

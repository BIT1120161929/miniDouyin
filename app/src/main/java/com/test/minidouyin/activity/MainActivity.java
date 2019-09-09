package com.test.minidouyin.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.minidouyin.R;
import com.test.minidouyin.fragments.PlayInfoFragment;
import com.test.minidouyin.fragments.ShootVideoFragment;
import com.test.minidouyin.fragments.VideoListFragment;
import com.test.minidouyin.utils.TransportUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 可以左右滑动的三格ViewPager2，从0号到2号分别为播放和个人信息的组合窗口，播放列表，拍摄和录制窗口
 * 右下方按钮为上传按钮
 */
public class MainActivity extends AppCompatActivity {
    
    private final String TAG = "MainActivity";


    private ViewPager2 pager;
    private FloatingActionButton floatingActionButton;

    public static final Integer REFRESH = 110;

    public static final int REQUEST_PERMISSIONS = 123;
    public String[] permissionArray = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    public static final int PLAY = 1;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        floatingActionButton = findViewById(R.id.ac_main_fb_post);

        //申请权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionArray, REQUEST_PERMISSIONS);
        }

        pager = findViewById(R.id.ac_main_vp_viewpager);
        pager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:return new PlayInfoFragment();
                    case 1:return new VideoListFragment();
                    case 2:return new ShootVideoFragment();
                }
                return null;
            }
            @Override
            public int getItemCount() {
                return 3;
            }
        });
        pager.setCurrentItem(1,false);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * URL传递到Activity
     * @param transportUtils 为传输的工具类
     */
    @Subscribe
    public void onEventMainThread2Play(TransportUtils transportUtils){
        if(transportUtils.PALY == PLAY){
            pager.setCurrentItem(0);
        }
    }
}

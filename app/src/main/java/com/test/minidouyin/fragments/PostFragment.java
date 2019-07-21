package com.test.minidouyin.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.test.minidouyin.R;
import com.test.minidouyin.network.RetrofitManager;
import com.test.minidouyin.network.beans.PostVideoResponse;
import com.test.minidouyin.network.service.VideoListService;
import com.test.minidouyin.utils.OnDoubleClickListener;
import com.test.minidouyin.utils.ResourceUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    private static final int GRANT_PERMISSION = 3;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;

    private VideoView videoView;
    private ImageView imageView;
    private Button btnPost;

    private Uri mSelectedImage;
    private Uri mSelectedVideo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post,container,false);

        videoView = view.findViewById(R.id.vv_video);
        imageView = view.findViewById(R.id.iv_cover);
        btnPost = view.findViewById(R.id.btn_post);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestReadExternalStoragePermission("select an image")){
                    chooseImage();
                    if(mSelectedImage!=null&&mSelectedVideo!=null){
                        btnPost.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestReadExternalStoragePermission("select a video")){
                    chooseVideo();
                    
                    if(mSelectedImage!=null&&mSelectedVideo!=null){
                        btnPost.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedVideo!=null&&mSelectedImage!=null){
                    postVideo();
                }
            }
        });
        return view;
    }



    private boolean requestReadExternalStoragePermission(String explanation) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, GRANT_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {

            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
            }
        }
    }

    private void postVideo() {
        Retrofit retrofit = RetrofitManager.get("http://test.androidcamp.bytedance.com/");

        MultipartBody.Part image = getMultipartFromUri("cover_image",mSelectedImage);
        MultipartBody.Part video = getMultipartFromUri("video",mSelectedVideo);

        Call<PostVideoResponse> call = retrofit.create(VideoListService.class).postVideo("1120161929","QSJ",image,video);

        call.enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                PostVideoResponse body = response.body();
                if(body!=null&&body.isSuccess()){
                    btnPost.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {
            }
        });
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(getContext(), uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }
}

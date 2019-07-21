package com.test.minidouyin.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.test.minidouyin.activity.PostActivity;
import com.test.minidouyin.R;
import com.test.minidouyin.utils.OnDoubleClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.test.minidouyin.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.test.minidouyin.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.test.minidouyin.utils.Utils.getOutputMediaFile;

/**
 * 拍摄
 */
public class ShootVideoFragment extends Fragment implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private Button btnRecord;
    private Button btnFacing;

    private Camera mCamera;

    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean isRecording = false;
    private int rotationDegree = 0;
    private SurfaceHolder mSurfaceHolder;
    private File outputMediaFile;

    private Intent videoIntent;
    private Uri videoUri;
    private Intent imageIntent;
    private Uri ImageUri;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                imageIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                imageIntent.setData(Uri.fromFile(pictureFile));
                getActivity().sendBroadcast(imageIntent);
            } catch (IOException e) {
                Log.d("mPicture", "Error accessing file: " + e.getMessage());
            }

            mCamera.startPreview();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mCamera = getCamera(CAMERA_TYPE);
        mCamera.startPreview();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCamera = getCamera(CAMERA_TYPE);
        mCamera.startPreview();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoot_video, container, false);
        surfaceView = view.findViewById(R.id.sv_img);
        btnRecord = view.findViewById(R.id.btn_record);
        btnFacing = view.findViewById(R.id.btn_facing);

        mCamera = getCamera(CAMERA_TYPE);
        surfaceView = view.findViewById(R.id.sv_img);

        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);

        surfaceView.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                if (isRecording) {
                    releaseMediaRecorder();
                    isRecording = false;
                    btnRecord.setBackgroundColor(Color.GREEN);
                    btnRecord.setText("GOTO_POST");
                } else if("GOTO_POST".equals(btnRecord.getText())){
                    btnRecord.setBackgroundColor(Color.GRAY);
                    btnRecord.setText("RECORD");
                }else{
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        }));

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("GOTO_POST".equals(btnRecord.getText())) {
                    gotoPost(videoIntent);
                    btnRecord.setText("RECORD");
                    btnRecord.setBackgroundColor(Color.GRAY);
                } else if (isRecording) {
                    //todo 停止录制
                    releaseMediaRecorder();
                    isRecording = false;
                    btnRecord.setBackgroundColor(Color.GREEN);
                    btnRecord.setText("GOTO_POST");
                } else {
                    //todo 录制
                    prepareVideoRecorder();
                    isRecording = true;
                    btnRecord.setBackgroundColor(Color.RED);
                }
            }
        });

        btnFacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseCameraAndPreview();
                mCamera = CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK ? getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT) : getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                mSurfaceHolder.addCallback(ShootVideoFragment.this);
                startPreview(mSurfaceHolder);
            }
        });

        return view;
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);

        rotationDegree = getCameraDisplayOrientation(CAMERA_TYPE);

        cam.setDisplayOrientation(rotationDegree);
        if (position == Camera.CameraInfo.CAMERA_FACING_BACK) {
            Camera.Parameters parameters = cam.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            cam.setParameters(parameters);
        }
        return cam;
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void startPreview(SurfaceHolder holder) {
        //todo 开始预览
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = getActivity().getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }

    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        //todo 准备MediaRecorder
        mMediaRecorder = new MediaRecorder();
        //Unlock and set camera t MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        //Set Resources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //Set a CamcorderProfile
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        //set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        //set the preview output
        mMediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);
        //prepare configured MediaRecorder

        outputMediaFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        //todo 释放MediaRecorder
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();

        if (outputMediaFile != null) {
            videoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            videoIntent.setData(Uri.fromFile(outputMediaFile));
            getActivity().sendBroadcast(videoIntent);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = getCamera(CAMERA_TYPE);
        }
        startPreview(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null) {
            mCamera = getCamera(CAMERA_TYPE);
        }
        startPreview(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void gotoPost(Intent intent) {
        Intent carryIntent = new Intent(getActivity(), PostActivity.class);
        carryIntent.setData(intent.getData());
        startActivity(carryIntent);
    }
}

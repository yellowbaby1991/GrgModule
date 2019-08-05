package com.grg.face;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.grg.face.callback.FaceCheckCallback;
import com.grg.face.core.FaceAuth;
import com.grg.face.view.SingleFaceCameraView;


/**
 * 带人脸追踪框的单目SingleFaceCameraView
 */
public class SingleFaceCameraViewActivity extends AppCompatActivity {

    private ImageView mFaceIv, mImageView;

    private SingleFaceCameraView mFaceCheckCameraView;

    private LinearLayout mLinearLayout;

    private FaceCheckCallback mFaceCheckCallback = new FaceCheckCallback() {

        @Override
        public void getFaceLocation(RectF faceRect) {

        }

        @Override
        public void getFace(final Bitmap bitmap, final Bitmap pribitmap) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFaceIv.setImageBitmap(bitmap);
                    mImageView.setImageBitmap(pribitmap);
                }
            });
        }

        @Override
        public void loseFace() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFaceIv.setImageBitmap(null);
                    mImageView.setImageBitmap(null);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleface_cameraview);


        DisplayMetrics dm = getResources().getDisplayMetrics();
        mLinearLayout = findViewById(R.id.ll_container);
        mFaceCheckCameraView = new SingleFaceCameraView(getApplicationContext());
        int heigth = dm.widthPixels;
        int width = heigth * 640 / 480;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, heigth);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mFaceCheckCameraView.setLayoutParams(layoutParams);
        //mFaceCheckCameraView.setCameraRotate(270);
        mFaceCheckCameraView.setGifPath("frame.gif");
        mFaceIv = findViewById(R.id.face_iv);
        mImageView = findViewById(R.id.img_iv);

        mLinearLayout.addView(mFaceCheckCameraView);
        mFaceCheckCameraView.startPreview(0, 640, 480);

        mFaceCheckCameraView.setFaceCheckCallback(mFaceCheckCallback);
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        Process.killProcess(Process.myPid());
        System.exit(0);
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        manager.killBackgroundProcesses(getPackageName());
    }


}

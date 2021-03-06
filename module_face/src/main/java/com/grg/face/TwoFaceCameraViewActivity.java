package com.grg.face;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.grg.face.callback.FaceCheckCallback;
import com.grg.face.view.SingleFaceCameraView;
import com.grg.face.view.TwoFaceCameraView;


/**
 * 带人脸追踪框的单目SingleFaceCameraView
 */
public class TwoFaceCameraViewActivity extends AppCompatActivity {

    private ImageView mFaceIv, mImageView;

    private TwoFaceCameraView mFaceCheckCameraView;

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

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twoface_cameraview);

        mFaceCheckCameraView = findViewById(R.id.camera_view);
        mFaceIv = findViewById(R.id.face_iv);
        mImageView = findViewById(R.id.img_iv);

        mFaceCheckCameraView.startTwoPreview(0, 1);
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

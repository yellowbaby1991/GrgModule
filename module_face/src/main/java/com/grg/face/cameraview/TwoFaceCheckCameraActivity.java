package com.grg.face.cameraview;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.grg.face.R;
import com.grg.face.callback.CompareCallback;
import com.grg.face.core.FaceAuth;
import com.grg.face.view.TwoFaceCheckCameraView;

/**
 * 带人脸追踪框的双目FaceCheckCameraView
 */
public class TwoFaceCheckCameraActivity extends AppCompatActivity {

    private ImageView mFaceIv, mImageView;

    private TwoFaceCheckCameraView mFaceCheckCameraView;

    private CompareCallback mCompareCallback = new CompareCallback() {
        @Override
        public void showFaceFrame(RectF rectF) {

        }

        @Override
        public void showFace(final Bitmap bitmap, final Bitmap pribitmap) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFaceIv.setImageBitmap(bitmap);
                    mImageView.setImageBitmap(pribitmap);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twofacecheck_camera);

        initView();

        String deviceId = FaceAuth.getDeviceId(this);//ffffffff-cdb6-cfea-0033-c5870033c587
        String license = "4D5697C772B9D04A227FA452B4BEE4464E53FDC684951A4261570FD61E03F5BC07CF7D7F17B0B443647E8AD297F25BC53B606E0C3D986A3609218BC6485D659CC72C024AA1FB8C68D59F07D4E2327CBB53D50E1D43EA74160E54AACCDD9025CFC3EB3F58DA4BB44003CAD535BB1A970626D6B95890139600D2D4FE571E1425AD";

        FaceAuth.authByLocal(this, license, new FaceAuth.AuthCallBack() {
            @Override
            public void authSuccess() {
                mFaceCheckCameraView.startPreview(0,1, 640, 480);
                mFaceCheckCameraView.isShowFrame(true);//是否打开默认画框
                mFaceCheckCameraView.setCompareCallback(mCompareCallback);
            }

            @Override
            public void authFail() {

            }
        });
    }


    private void initView() {
        mFaceCheckCameraView = findViewById(R.id.face_check_view);
        mFaceIv = findViewById(R.id.face_iv);
        mImageView = findViewById(R.id.img_iv);
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

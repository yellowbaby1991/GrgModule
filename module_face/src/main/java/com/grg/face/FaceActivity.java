package com.grg.face;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.face.callback.CompareCallback;
import com.grg.face.core.FaceAuth;
import com.grg.face.core.FaceDetecter;
import com.grg.face.view.CameraTextureView;

@Route(path = "/face/activity")
public class FaceActivity extends AppCompatActivity {

    private FaceDetecter mFaceDetecter;

    private ImageView mFaceIv, mImageView;

    private CameraTextureView mCameraTextureView;
    //private CameraViewWithFace mCameraViewWithFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face);

        initView();


        //mCameraViewWithFace = findViewById(R.id.camera_vtv_withface);

        ARouter.getInstance().inject(this);

        String deviceId = FaceAuth.getDeviceId(this);//ffffffff-cdb6-cfea-0033-c5870033c587
        String license = "4D5697C772B9D04A227FA452B4BEE4464E53FDC684951A4261570FD61E03F5BC07CF7D7F17B0B443647E8AD297F25BC53B606E0C3D986A3609218BC6485D659CC72C024AA1FB8C68D59F07D4E2327CBB53D50E1D43EA74160E54AACCDD9025CFC3EB3F58DA4BB44003CAD535BB1A970626D6B95890139600D2D4FE571E1425AD";

        FaceAuth.authByLocal(this, license, new FaceAuth.AuthCallBack() {
            @Override
            public void authSuccess() {
                initFaceDetecter();
                setDecterToCamera();
            }

            @Override
            public void authFail() {

            }
        });


    }

    private void setDecterToCamera() {
        mCameraTextureView.setOnPreviewCallback(new CameraTextureView.OnPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                mFaceDetecter.onPreviewFrame(data, camera);
            }
        });
    }

    private void initView() {
        mCameraTextureView = findViewById(R.id.camera_vtv);
        mCameraTextureView.startPreview(0, 640, 480);
        mFaceIv = findViewById(R.id.face_iv);
        mImageView = findViewById(R.id.img_iv);
    }

    private void initFaceDetecter() {
        mFaceDetecter = new FaceDetecter();
        mFaceDetecter.setIslive(false);
        mFaceDetecter.setCameraRotate(0);
        mFaceDetecter.init(new CompareCallback() {
            @Override
            public void showfaceFrame(RectF rectF) {

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
        }, this);
    }

}

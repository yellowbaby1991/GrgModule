package com.grg.face.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import com.grg.face.R;
import com.grg.face.core.FaceDetecter;
import com.grg.face.core.SingleFaceLivingDetecter;

/**
 * 双目人脸检测画框控件
 */
public class TwoFaceCameraView extends BaseCameraView{

    //相机预览控件
    protected CameraTextureView mCameraVtv1,mCameraVtv2;

    public TwoFaceCameraView(Context context) {
        this(context,null);
    }

    public TwoFaceCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_twoface_cameraview;
    }

    @Override
    public void initView() {
        mCameraVtv1 = findViewById(R.id.camera_vtv1);
        mCameraVtv2 = findViewById(R.id.camera_vtv2);
        mFrameDraw = findViewById(R.id.camera_frame_draw);
    }

    @Override
    protected void openTwoCamera(int cameraID1, int cameraID2, int width, int height) {
        super.openTwoCamera(cameraID1,cameraID2,width,height);
        mCameraVtv1.startPreview(cameraID1, width, height);
        mCameraVtv2.startPreview(cameraID2, width, height);
    }


    @Override
    protected void setDecterToCamera() {
        mCameraVtv1.setOnPreviewCallback(new CameraTextureView.OnPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (camera != null) {
                    mFaceDetecter.onPreviewFrame(data, camera);
                }

            }
        });
        mCameraVtv2.setOnPreviewCallback(new CameraTextureView.OnPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (camera != null) {
                    mFaceDetecter.onPreviewFrameRed(data, camera);
                }

            }
        });
    }

    @Override
    protected FaceDetecter createFaceDetater() {
        FaceDetecter faceDetecter = new FaceDetecter();
        faceDetecter.setTwoCamera(true);
        return faceDetecter;
    }

    @Override
    public void stopCameraView() {
        super.stopCameraView();
        mCameraVtv1.stopCamera();
        mCameraVtv2.stopCamera();
        mFaceDetecter.release();
    }

    @Override
    public void setCameraRotate(int cameraRotate) {
        super.setCameraRotate(cameraRotate);
        mCameraVtv1.setRotation(-cameraRotate);
        mCameraVtv2.setRotation(-cameraRotate);
    }
}

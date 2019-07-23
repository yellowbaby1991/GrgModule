package com.grg.face.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;

import com.grg.face.R;
import com.grg.face.core.FaceDetecter;
import com.grg.face.core.SingleFaceLivingDetecter;


/**
 * 单目人脸检测画框控件
 */
public class SingleFaceCameraView extends BaseCameraView{

    //是否支持单目活体
    private boolean mIsCheckLiving = false;

    //相机预览控件
    protected CameraTextureView mCameraVtv;

    public SingleFaceCameraView(Context context) {
        this(context,null);
    }

    public SingleFaceCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_singeface_cameraview;
    }

    @Override
    public void initView() {
        mCameraVtv = findViewById(R.id.camera_vtv);
        mFrameDraw = findViewById(R.id.camera_frame_draw);
    }

    @Override
    protected void openCamera(int cameraID, int width, int height) {
        setOpenIgore(true);//单目非活体的时候过滤前两帧废数据
        super.openCamera(cameraID,width,height);
        mCameraVtv.startPreview(cameraID, width, height);
    }

    @Override
    protected void setDecterToCamera() {
        mCameraVtv.setOnPreviewCallback(new CameraTextureView.OnPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (camera != null) {
                    mFaceDetecter.onPreviewFrame(data, camera);
                }

            }
        });
    }

    @Override
    protected FaceDetecter createFaceDetater() {
        if (mIsCheckLiving){
            return new SingleFaceLivingDetecter();
        }else {
            return new FaceDetecter();
        }
    }

    public boolean isCheckLiving() {
        return mIsCheckLiving;
    }

    public void setCheckLiving(boolean checkLiving) {
        mIsCheckLiving = checkLiving;
    }

    public CameraTextureView getCameraVtv() {
        return mCameraVtv;
    }

    public void stopCameraView(){
        super.stopCameraView();
        mCameraVtv.stopCamera();
        mFaceDetecter.release();
    }
}

package com.grg.face.view;


import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;

import java.util.List;


/***
 * 相机预览控件
 */
public class CameraTextureView extends TextureView {

    private int mCameraID;

    private boolean mIsRelease;

    private Camera mCamera;

    private int mWidth, mHeight;

    public CameraTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 默认打开分辨率640×480的相机
     * @param cameraID 相机序号
     */
    public void startPreview(int cameraID) {
        mWidth = 640;
        mHeight = 480;
        mCameraID = cameraID;
        init();

    }

    /**
     * 打开指定分辨率的相机
     * @param cameraID 相机序号
     */
    public void startPreview(int cameraID, int width, int height) {
        mCameraID = cameraID;
        mWidth = width;
        mHeight = height;
        init();
    }

    private OnPreviewCallback mOnPreviewCallback;

    public void setOnPreviewCallback(OnPreviewCallback onPreviewCallback) {
        mOnPreviewCallback = onPreviewCallback;
    }

    public interface OnPreviewCallback {
        public void onPreviewFrame(byte[] data, Camera camera);
    }

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mOnPreviewCallback != null && !mIsRelease) {
                mOnPreviewCallback.onPreviewFrame(data, camera);
                data = null;
            }
        }
    };

    public Camera getCamera() {
        return mCamera;
    }

    public int getCameraHeight() {
        return mHeight;
    }

    public int getCameraWidth() {
        return mWidth;
    }

    private void init() {

        mCamera = Camera.open(mCameraID);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mWidth,mHeight);
        mCamera.setParameters(parameters);

        this.setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                try {
                    mCamera.setPreviewTexture(surfaceTexture);
                    mCamera.setPreviewCallback(mPreviewCallback);
                    mCamera.startPreview();
                    mIsRelease = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

            }


            @Override

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                mIsRelease = true;
                if (mCamera != null){
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {


            }

        });

    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


}

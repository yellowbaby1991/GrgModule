package com.grg.face;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class CameraTextureView1 extends TextureView {

    private boolean isRelease;

    public Camera mCamera;

    public CameraTextureView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        EventBus.TAG = "1";
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
            if (mOnPreviewCallback != null && !isRelease) {
                mOnPreviewCallback.onPreviewFrame(data, camera);
                data = null;
            }
        }
    };


    private void init() {

        mCamera = Camera.open(0);
        this.setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                try {
                    mCamera.setPreviewTexture(surfaceTexture);
                    mCamera.setPreviewCallback(mPreviewCallback);
                    mCamera.startPreview();
                    isRelease = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

            }


            @Override

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                isRelease = true;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {


            }

        });

    }

    public void stopCamera(){
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    public void take() {
        if (mCamera != null)
            mCamera.takePicture(null, null, mPictureCallback);
    }


    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override

        public void onPictureTaken(byte[] data, Camera camera) {
            mCamera.stopPreview();
            new FileSaver(data).save();

        }

    };

    private class FileSaver implements Runnable {

        private byte[] buffer;


        public FileSaver(byte[] buffer) {
            this.buffer = buffer;
        }


        public void save() {
            new Thread(this).start();
        }


        @Override

        public void run() {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "zhangphil.png");
                file.createNewFile();
                FileOutputStream os = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
                os.close();

                Log.d("照片已保存", file.getAbsolutePath());

                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }

}

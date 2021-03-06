package com.grg.face.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;

import com.aibee.face.facesdk.AuthorityException;
import com.aibee.face.facesdk.FaceIdentifier;
import com.aibee.face.facesdk.FaceInfo;
import com.aibee.face.facesdk.FaceSDK;
import com.aibee.face.facesdk.FaceTracker;
import com.aibee.face.facesdk.FaceVerifyData;
import com.grg.face.bean.YuvData;

import java.util.concurrent.Semaphore;


/**
 * 人脸算法处理器，内部长期运行算法处理线程
 */
public class FaceDetecter {

    private boolean working = true;

    protected FaceTracker mTracker;

    private int angle = 0;

    private int flip = 0;

    private int format;

    private int preframe = 0; //可见光处理队列开关

    private int preframered = 0; //红外光处理队列开关

    protected Context mContext;

    private RectF rectF = new RectF();

    private boolean isTwoCamera = false; //是否双目，如果双目自带活体检测

    private byte[] yuvimg = null;   //可见光图片

    private byte[] yuvred = null;   //红外光图片

    protected int cameraRotate;    //旋转角度

    private Semaphore mSemaphore = new Semaphore(0);

    private FaceDetecterCallback compareCallback;  //回调

    private int width; //视频分辨率宽

    private int height; //视频分辨率高

    public void setCameraRotate(int cameraRotate) {
        this.cameraRotate = cameraRotate;
    }

    public void init(FaceDetecterCallback compareCallback, Context context) {
        mContext = context;
        this.compareCallback = compareCallback;
        init(isTwoCamera);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().setName("FaceDecter");

                while (working) {
                    try {
                        mSemaphore.acquire();
                        if (mTracker != null) {
                            start(mTracker, yuvimg, yuvred);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    preframe = 0;
                    preframered = 0;
                }
            }
        }).start();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        setFormat(camera);
        onPreviewFrame(data);
    }

    public void onPreviewFrameRed(byte[] data, Camera camera) {
        setFormat(camera);
        if (isTwoCamera) {
            onPreviewFrameRed(data);
        } else {
            onPreviewFrame(data);
        }
    }

    public void init(boolean isTwoCameara) {
        if (mTracker == null) {
            try {
                mTracker = new FaceTracker(mContext.getAssets());
            } catch (AuthorityException e) {
                e.printStackTrace();
            }
            if (mTracker != null) {
                if (isTwoCameara) {
                    mTracker.setIsVerifyLive(1);  // set: Whether to do liveness checking
                } else {
                    mTracker.setIsVerifyLive(0);  // set: Whether to do liveness checking
                }
                mTracker.setIsCheckQuality(isTwoCameara);     // set check the face image quality
                mTracker.setFaceScoreThr(0.2f);      // set: the face confidence threshold.
                mTracker.setMinFaceSize(100);    // set the smallest face size to be detected (in pixels)
                mTracker.setIsCheckQuality(true);     // set check the face image quality
                mTracker.setMaxCollectNum(1);        // set: How many face images to crop for each face in maximum
                mTracker.setDetectIntervalHasface(1000); // set: the minimum interval to detect new faces, when there is at least one face in the scene. (in ms)
                mTracker.setDetectIntervalNoface(500);   // set: the minimum interval to detect faces, when no face is in the scene. (in ms)
                mTracker.setCollectInterval(300); // set: the minimum interval between the cropped faces (in ms)
                mTracker.setEulurAngleThr(15, 15, 15);    // set: the cropped face pose must be in these angles (in degrees)
                mTracker.setIllumThr(50.0f);
                mTracker.setBlurThr(0.5f);
                mTracker.setOccluThr(0.5f);
                mTracker.setCropFaceSize(256);
                mTracker.setCropFaceEnlargeRatio(1.8f);
                mTracker.setIsEnsureOneFace(false);
                mTracker.setLiveThr(0.1f);
                mTracker.setThreadNum(2);      // Set the number of threads to use
                mTracker.setFaceOrientation(cameraRotate);
                //mTracker.enableDebugLog(true);
            }
        }
    }

    private void start(FaceTracker mTracker, byte[] yuvimg, byte[] yuvred) {

        int rows = angle / 90 % 2 != 0 ? width : height;
        int cols = angle / 90 % 2 != 0 ? height : width;

        int[] ARGBdata = null;

        FaceSDK.ErrCode code = null;

        if (!isTwoCamera) {
            if (yuvimg != null) {
                ARGBdata = new int[rows * cols];
                FaceTracker.getARGBFromYUVimg(yuvimg, ARGBdata, width, height, angle, flip);//将摄像机的YUV视频流转为argb
                code = mTracker.trackAndCollect(ARGBdata, rows, cols, FaceSDK.ImgType.ARGB);//对每一帧图片进行人脸检测

            }
        } else {
            if (yuvimg != null && yuvred != null) {
                ARGBdata = new int[rows * cols];
                FaceTracker.getARGBFromYUVimg(yuvimg, ARGBdata, width, height, angle, flip);
                int[] Red_ARGBdata = new int[rows * cols];
                FaceTracker.getARGBFromYUVimg(yuvred, Red_ARGBdata, width, height, angle, flip);
                code = mTracker.trackAndCollect(ARGBdata, rows, cols, FaceSDK.ImgType.ARGB, Red_ARGBdata, FaceSDK.ImgType.ARGB);
            }
        }

        FaceInfo[] trackedfaces = mTracker.getTrackedFaceInfo();
        if (trackedfaces != null && trackedfaces.length > 0) {
            FaceInfo faceInfo = trackedfaces[0];
            rectF = new RectF();
            rectF.left = faceInfo.mCenter_x - faceInfo.mWidth / 2;
            rectF.top = faceInfo.mCenter_y - faceInfo.mWidth / 2;
            rectF.right = faceInfo.mCenter_x + faceInfo.mWidth / 2;
            rectF.bottom = faceInfo.mCenter_y + faceInfo.mWidth / 2;
            int[] la = new int[4];
            la[0] = (int) rectF.left;
            la[1] = (int) rectF.top;
            la[2] = (int) rectF.right;
            la[3] = (int) rectF.bottom;
            compareCallback.getFaceLocation(rectF);

            //Log.e("TAG", "====人脸=====");

            if (isTwoCamera) {
                if (code == FaceSDK.ErrCode.OK) {
                    FaceVerifyData[] faceVerifyData = mTracker.getFaceVerifyData(0);
                    if (faceVerifyData != null && faceVerifyData.length > 0) {
                        YuvData yuvData = new YuvData();
                        yuvData.setFormat(format);
                        yuvData.setCameraRotate(cameraRotate);
                        yuvData.setLocation(la);
                        yuvData.setYuvs(faceVerifyData[0].mRegJpeg);
                        yuvData.setPriview(ARGBdata);
                        int[] pridata = yuvData.getPriview();
                        //mNormalQueue.offer(yuvData);
                        byte[] yuv = yuvData.getYuvs();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.length);
                        Bitmap pribitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                        pribitmap.setPixels(pridata, 0, width, 0, 0, width, height);
                        /*bitmap = mirrorConvert(bitmap, 0);
                        pribitmap = mirrorConvert(pribitmap, 0);*/
                        if (cameraRotate != 0) {
                            bitmap = adjustPhotoRotation(bitmap,-cameraRotate);
                            pribitmap= adjustPhotoRotation(pribitmap,-cameraRotate);
                        }
                        compareCallback.getFace(bitmap, pribitmap);
                        //Log.e("TAG", "====活体==========================================");
                    }
                }
            } else {
                if (code == FaceSDK.ErrCode.OK) {
                    YuvData yuvData = new YuvData();
                    yuvData.setFormat(format);
                    yuvData.setCameraRotate(cameraRotate);
                    yuvData.setLocation(la);
                    yuvData.setPriview(ARGBdata);
                    //mNormalQueue.offer(yuvData);
                    int[] pridata = yuvData.getPriview();
                    if (pridata != null) {
                        int[] rectF = yuvData.getLocation();
                        Bitmap pribitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                        pribitmap.setPixels(pridata, 0, width, 0, 0, width, height);
                        int padding = 30;
                        int toppadding = 40;
                        int left = rectF[0] - padding;
                        int top = rectF[1] - toppadding;
                        if (left < 0) {
                            left = 0;
                        }
                        if (top < 0) {
                            top = 0;
                        }
                        int w = rectF[2] - rectF[0] + padding * 2;
                        if (w < 0) {
                            w = 0;
                        }
                        int h = rectF[3] - rectF[1] + toppadding * 2;
                        if (h < 0) {
                            h = 0;
                        }
                        if (left + w > pribitmap.getWidth()) {
                            w = pribitmap.getWidth() - left;
                        }
                        if (top + h > pribitmap.getHeight()) {
                            h = pribitmap.getHeight() - top;
                        }
                        if (h <= 0 || w <= 0) {
                            compareCallback.loseFace();
                        }
                        try {
                            Bitmap bitmap = Bitmap.createBitmap(pribitmap, left, top, w, h);
                            /*bitmap = mirrorConvert(bitmap, 0);
                            pribitmap = mirrorConvert(pribitmap, 0);*/
                            if (cameraRotate != 0) {
                                bitmap = adjustPhotoRotation(bitmap,-cameraRotate);
                                pribitmap= adjustPhotoRotation(pribitmap,-cameraRotate);
                            }
                            compareCallback.getFace(bitmap, pribitmap);
                        } catch (Exception e) {
                            compareCallback.loseFace();
                            e.printStackTrace();
                        }

                    }
                }
            }
        } else {
            compareCallback.loseFace();
        }

    }

    public void setFormat(Camera camera) {
        if (yuvimg == null) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            width = size.width;
            height = size.height;
            if (format == 0) {
                format = camera.getParameters().getPreviewFormat();
            }
        }
    }

    private void onPreviewFrame(byte[] data) {
        if (mTracker == null) {
            Log.e("TAG", "onPreviewFrame mTracker = null");
            return;
        }
        if (preframe == 1) {
            return;
        }
        if (mSemaphore.hasQueuedThreads()) {
            preframe = 1;
            yuvimg = data.clone();
            if (isTwoCamera) {
                if (preframered == 1) {
                    mSemaphore.release();
                }
            } else {
                mSemaphore.release();
            }
        }
    }

    private void onPreviewFrameRed(byte[] data) {
        if (mTracker == null) {
            return;
        }

        if (preframered == 1) {
            return;
        }
        if (mSemaphore.hasQueuedThreads()) {
            preframered = 1;
            yuvred = data.clone();
            if (preframe == 1) {
                mSemaphore.release();
            }
        }
    }

    public boolean isTwoCamera() {
        return isTwoCamera;
    }

    /**
     * 设置是否开启活体检测，双目摄像头方生效
     *
     * @param twoCamera
     */
    public void setTwoCamera(boolean twoCamera) {
        this.isTwoCamera = twoCamera;
    }


    private Bitmap mirrorConvert(Bitmap srcBitmap, int flag) {
        //flag: 0 左右翻转，1 上下翻转
        Matrix matrix = new Matrix();
        if (flag == 0) //左右翻转
            matrix.setScale(-1, 1);
        if (flag == 1)  //上下翻转
            matrix.setScale(1, -1);
        return Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
    }

    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;

        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    public void release() {
        mSemaphore.release();
        working = false;
    }

    public interface FaceDetecterCallback {

        /**
         * 返回人脸坐标，用于画框
         *
         * @param faceRect
         */
        void getFaceLocation(RectF faceRect);

        /**
         * 返回人脸图和原图
         *
         * @param bitmap    人脸图
         * @param pribitmap 原图
         */
        void getFace(Bitmap bitmap, Bitmap pribitmap);

        /**
         * 丢失人脸，用于清除人脸框
         */
        void loseFace();

    }
}

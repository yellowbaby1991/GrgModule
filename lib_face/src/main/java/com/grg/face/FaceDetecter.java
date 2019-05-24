package com.grg.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;

import com.aibee.face.facesdk.AuthorityException;
import com.aibee.face.facesdk.FaceIdentifier;
import com.aibee.face.facesdk.FaceInfo;
import com.aibee.face.facesdk.FaceSDK;
import com.aibee.face.facesdk.FaceTracker;
import com.aibee.face.facesdk.FaceVerifyData;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class FaceDetecter {

    private Context mContext;

    private static FaceTracker mTracker;

    private static FaceIdentifier faceIdentifier;

    private RectF rectF = new RectF();

    private boolean islive = false; //是否活体

    private byte[] yuvimg = null;   //可见光图片

    private byte[] yuvred = null;   //红外光图片

    private int cameraRotate;    //旋转角度

    private Semaphore mSemaphore = new Semaphore(0);

    private CompareCallback compareCallback;  //回调

    public void setCameraRotate(int cameraRotate) {
        this.cameraRotate = cameraRotate;
    }

    public void init(final Queue mNormalQueue, CompareCallback compareCallback,Context context) {
        mContext = context;
        this.compareCallback = compareCallback;
        init(islive);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("FaceDecter");

                while (true) {
                    try {
                        mSemaphore.acquire();
                        if (mTracker != null) {
                            start(mTracker, yuvimg, yuvred, mNormalQueue);
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


    public void onPreviewFrame(byte[] data,Camera camera) {
        setFormat(camera);
        onPreviewFrame(data);
    }

    public void onPreviewFrameRed(byte[] data,  Camera camera) {
        setFormat(camera);
        if (islive) {
            onPreviewFrameRed(data);
        } else {
            onPreviewFrame(data);
        }
    }


    public void init(boolean islive) {
        if (mTracker == null) {
            try {
                mTracker = new FaceTracker(mContext.getAssets());
            } catch (AuthorityException e) {
                e.printStackTrace();
            }
            if (mTracker != null) {
                mTracker.setIsVerifyLive(islive);  // set: Whether to do liveness checking
                mTracker.setIsCheckQuality(islive);     // set check the face image quality
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
        if (faceIdentifier == null) {
            try {
                faceIdentifier = new FaceIdentifier(mContext.getAssets());
                faceIdentifier.cacheFaceidModel();
                faceIdentifier.setThreadNum(1);
            } catch (AuthorityException e) {
            }
        }
    }


    private void start(FaceTracker mTracker, byte[] yuvimg, byte[] yuvred, Queue mNormalQueue) {

        int rows = angle / 90 % 2 != 0 ? width : height;
        int cols = angle / 90 % 2 != 0 ? height : width;

        int[] ARGBdata = null;

        if (!islive) {
            if (yuvimg != null) {
                ARGBdata = new int[rows * cols];
                FaceTracker.getARGBFromYUVimg(yuvimg, ARGBdata, width, height, angle, flip);
                FaceTracker.ErrCode code = mTracker.trackAndCollect(ARGBdata, rows, cols, FaceTracker.ImgType.ARGB);

            }
        } else {
            if (yuvimg != null && yuvred != null) {
                ARGBdata = new int[rows * cols];
                FaceTracker.getARGBFromYUVimg(yuvimg, ARGBdata, width, height, angle, flip);
                int[] Red_ARGBdata = new int[rows * cols];
                FaceTracker.getARGBFromYUVimg(yuvred, Red_ARGBdata, width, height, angle, flip);
                FaceTracker.ErrCode code = mTracker.trackAndCollect(ARGBdata, rows, cols, FaceTracker.ImgType.ARGB, Red_ARGBdata, FaceTracker.ImgType.ARGB);
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
            compareCallback.showfaceFrame(rectF);

            //Log.e("TAG", "====人脸=====");

            if (islive) {
                FaceVerifyData[] faceVerifyData = mTracker.getFaceVerifyData(0);
                if (faceVerifyData != null && faceVerifyData.length > 0) {
                    YuvData yuvData = new YuvData();
                    yuvData.setFormat(format);
                    yuvData.setCameraRotate(cameraRotate);
                    yuvData.setLocation(la);
                    yuvData.setYuvs(faceVerifyData[0].mRegJpeg);
                    yuvData.setPriview(ARGBdata);
                    mNormalQueue.offer(yuvData);
                    byte[] yuv = yuvData.getYuvs();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.length);
                    compareCallback.showFace(bitmap);
                    Log.e("TAG", "====活体==========================================");
                }
            }else {
                YuvData yuvData = new YuvData();
                yuvData.setFormat(format);
                yuvData.setCameraRotate(cameraRotate);
                yuvData.setLocation(la);
                yuvData.setPriview(ARGBdata);
                mNormalQueue.offer(yuvData);
                int[] pridata = yuvData.getPriview();
                if (pridata != null) {
                    int[] rectF = yuvData.getLocation();
                    Bitmap pribitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_4444);
                    pribitmap.setPixels(pridata, 0, 640, 0, 0, 640, 480);
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
                    Bitmap bitmap = Bitmap.createBitmap(pribitmap, left, top, w, h);
                    compareCallback.showFace(bitmap);
                }
            }

        } else {
            mNormalQueue.clear();
        }

    }

    int format;

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

    private int width;
    private int height;
    private ArrayBlockingQueue<byte[]> arrayBlockingQueue = new ArrayBlockingQueue(1, true);

    public int angle = 0;
    public int flip = 0;
    int preframe = 0;
    int preframered = 0;


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
            if (islive) {
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

    public boolean isIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public float verifyFeature(Object featureA, Object featureB) {
        init(islive);
        if (faceIdentifier == null || featureA == null || featureB == null) {
            return 0;
        }
        return faceIdentifier.calcFeatureSimilarity((float[]) featureA, (float[]) featureB);
    }

    public float[] extractFeatures(Bitmap bitmap) {
        init(islive);
        if (faceIdentifier == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        float[] features = new float[faceIdentifier.featureLength];
        FaceSDK.ErrCode errCode = faceIdentifier.extractFeatures(pixels, h, w, FaceSDK.ImgType.ARGB, features);
        if (errCode == FaceSDK.ErrCode.OK) {
            return features;
        }

        return null;
    }
}

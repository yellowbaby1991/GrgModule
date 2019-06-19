package com.grg.face.callback;

import android.graphics.Bitmap;
import android.graphics.RectF;

public interface FaceCheckCallback {

    /**
     * 返回人脸坐标，用于画框
     * @param faceRect
     */
    void getFaceLocation(int[] faceRect);

    /**
     * 返回人脸图和原图
     * @param bitmap 人脸图
     * @param pribitmap 原图
     */
    void getFace(Bitmap bitmap, Bitmap pribitmap);
}

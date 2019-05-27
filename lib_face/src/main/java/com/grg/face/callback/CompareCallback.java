package com.grg.face.callback;

import android.graphics.Bitmap;
import android.graphics.RectF;

public interface CompareCallback {

    /**
     * 返回人脸坐标，用于画框
     * @param rectF
     */
    void showFaceFrame(RectF rectF);

    /**
     * 返回人脸图和原图
     * @param bitmap 人脸图
     * @param pribitmap 原图
     */
    void showFace(Bitmap bitmap, Bitmap pribitmap); //回调抓取的人脸

}

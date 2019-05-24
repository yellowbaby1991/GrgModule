package com.grg.face;

import android.graphics.Bitmap;
import android.graphics.RectF;

public interface CompareCallback {

    void showfaceFrame(RectF rectF); // 回调人脸框位置

    void showFace(Bitmap bitmap); //回调捕捉人脸位置

}

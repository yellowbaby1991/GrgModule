package com.grg.face.bean;

import android.graphics.Bitmap;

/**
 * 用于存储人脸特征值的对象
 */
public class Face {

    /**
     * 人脸照片
     */
    private Bitmap bitmap;

    /**
     * 特征值数组
     */
    private byte[] featureValue;

    public byte[] getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(byte[] featureValue) {
        this.featureValue = featureValue;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

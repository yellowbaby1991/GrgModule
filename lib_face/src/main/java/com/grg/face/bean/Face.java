package com.grg.face.bean;

import android.graphics.Bitmap;

public class Face {

    private Bitmap bitmap;

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

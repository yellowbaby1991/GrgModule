package com.grg.face.core;

import android.content.Context;
import android.graphics.Bitmap;

import com.aibee.face.facesdk.AuthorityException;
import com.aibee.face.facesdk.FaceIdentifier;
import com.aibee.face.facesdk.FaceSDK;

public class FaceComparer {

    protected static FaceIdentifier faceIdentifier;

    private Context mContext;

    public FaceComparer(Context context) {

        mContext = context;

        if (faceIdentifier == null) {
            try {
                faceIdentifier = new FaceIdentifier(mContext.getAssets());
                faceIdentifier.cacheFaceidModel();
                faceIdentifier.setThreadNum(1);
            } catch (AuthorityException e) {
            }
        }
    }

    /**
     * 对比特征值得到相似度
     *
     * @param featureA 特征值A
     * @param featureB 特征值B
     * @return 相似度
     */
    public float calcFeatureSimilarity(Object featureA, Object featureB) {
        if (faceIdentifier == null || featureA == null || featureB == null) {
            return 0;
        }
        return faceIdentifier.calcFeatureSimilarity((float[]) featureA, (float[]) featureB);
    }

    /**
     * 提取特征值
     *
     * @param bitmap 人脸照片
     * @return 特征值
     */
    public float[] extractFeatures(Bitmap bitmap) {
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

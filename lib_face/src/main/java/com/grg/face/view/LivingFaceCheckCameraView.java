package com.grg.face.view;

import android.content.Context;
import android.util.AttributeSet;

import com.grg.face.core.FaceDetecter;
import com.grg.face.core.SingleFaceLivingDetecter;

/**
 * 带人脸检测画框的单目活体cameraview
 */
public class LivingFaceCheckCameraView extends FaceCheckCameraView {

    public LivingFaceCheckCameraView(Context context) {
        super(context);
    }

    public LivingFaceCheckCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected FaceDetecter createFaceDetater() {
        return new SingleFaceLivingDetecter();
    }
}

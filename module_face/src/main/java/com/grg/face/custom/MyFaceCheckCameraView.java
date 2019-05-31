package com.grg.face.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.grg.face.core.FaceDetecter;
import com.grg.face.view.FaceCheckCameraView;

public class MyFaceCheckCameraView extends FaceCheckCameraView {

    public MyFaceCheckCameraView(Context context) {
        super(context);
    }

    public MyFaceCheckCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected FaceDetecter createFaceDetater() {
        return new MyFaceDetecter();
    }
}

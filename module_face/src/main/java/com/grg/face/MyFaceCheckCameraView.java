package com.grg.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aibee.auth.AibeeAuth;
import com.grg.face.callback.CompareCallback;
import com.grg.face.core.FaceDetecter;
import com.grg.face.view.CameraTextureView;
import com.grg.face.view.FaceCheckCameraView;
import com.grg.face.view.FaceView;

import static com.aibee.auth.AibeeAuth.AuthState.AuthStateSuc;

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

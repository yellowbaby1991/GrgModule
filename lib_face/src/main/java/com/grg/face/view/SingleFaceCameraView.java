package com.grg.face.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aibee.auth.AibeeAuth;
import com.grg.face.R;
import com.grg.face.callback.FaceCheckCallback;
import com.grg.face.core.FaceDetecter;

import static com.aibee.auth.AibeeAuth.AuthState.AuthStateSuc;

public class SingleFaceCameraView  extends RelativeLayout implements CameraView{

    //是否画框
    protected boolean mIsShowFrame = true;

    //人脸检测类
    private FaceDetecter mFaceDetecter;

    //相机预览控件
    private CameraTextureView mCameraVtv;

    //人脸追踪框绘制控件
    private FrameDraw mFrameDraw;

    //是否打开人脸检测
    private boolean mIsOpenCheckFace = true;

    //回调接口
    private FaceCheckCallback mFaceCheckCallback;

    //丢弃前两帧废数据
    private int ingoreNum = 0;

    public SingleFaceCameraView(Context context) {
        this(context,null);
    }

    public SingleFaceCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_singeface_cameraview, this, true);

        initView();

    }

    public void initView() {
        mCameraVtv = findViewById(R.id.camera_vtv);
        mFrameDraw = findViewById(R.id.camera_frame_draw);
    }

    /**
     * 打开分辨率640×480的摄像头
     * @param cameraID 摄像头编号
     */
    public void startPreview(int cameraID) {
        startPreview(cameraID, 640, 480);
    }

    /**
     * 打开指定分辨率的摄像头
     * @param cameraID 摄像头编号
     * @param width 宽
     * @param height 高
     */
    public void startPreview(int cameraID, int width, int height) {
        if (!isAuthSuc()) {
            Toast.makeText(getContext(), "算法授权未成功", Toast.LENGTH_SHORT).show();
            return;
        }
        mCameraVtv.startPreview(cameraID, width, height);
        initFaceDetecter();
        setDecterToCamera();
    }

    private boolean isAuthSuc() {
        AibeeAuth.AuthState state = AibeeAuth.getsInstance().getState();
        if (state == AuthStateSuc) {
            return true;
        } else {
            return false;
        }
    }

    private void initFaceDetecter() {
        mFaceDetecter = createFaceDetater();
        mFaceDetecter.setTwoCamera(false);
        mFaceDetecter.init(new FaceDetecter.FaceDetecterCallback() {
            @Override
            public void getFaceLocation(int[] faceRect) {
                if (ingoreNum < 2){
                    return;
                }
                if (mIsShowFrame && mIsOpenCheckFace) {
                    mFrameDraw.drawBoundingBox(faceRect, 1, Color.WHITE);
                    //mFrameDraw.clearDraw();
                }
                if (mFaceCheckCallback != null && mIsOpenCheckFace) {
                    mFaceCheckCallback.getFaceLocation(faceRect);
                }
            }

            @Override
            public void getFace(Bitmap bitmap, Bitmap pribitmap) {
                if (ingoreNum < 2){
                    ingoreNum++;
                    return;
                }
                if (mFaceCheckCallback != null && mIsOpenCheckFace) {
                    mFaceCheckCallback.getFace(bitmap, pribitmap);
                }
            }
        }, getContext());
    }

    private void setDecterToCamera() {
        mCameraVtv.setOnPreviewCallback(new CameraTextureView.OnPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (camera != null) {
                    mFaceDetecter.onPreviewFrame(data, camera);
                }

            }
        });
    }

    protected FaceDetecter createFaceDetater() {
        return new FaceDetecter();
    }

    public boolean isShowFrame() {
        return mIsShowFrame;
    }

    public void setShowFrame(boolean showFrame) {
        mIsShowFrame = showFrame;
    }

    public FaceCheckCallback getFaceCheckCallback() {
        return mFaceCheckCallback;
    }

    public void setFaceCheckCallback(FaceCheckCallback faceCheckCallback) {
        mFaceCheckCallback = faceCheckCallback;
    }
}

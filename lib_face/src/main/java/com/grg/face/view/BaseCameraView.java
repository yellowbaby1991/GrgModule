package com.grg.face.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aibee.auth.AibeeAuth;
import com.grg.face.callback.FaceCheckCallback;
import com.grg.face.core.FaceDetecter;

import static com.aibee.auth.AibeeAuth.AuthState.AuthStateSuc;

public abstract class BaseCameraView extends RelativeLayout {

    //相机分辨率-宽
    private int mWidth;

    //相机分辨率-高
    private int mHight;

    //人脸追踪框绘制控件
    protected FrameDraw mFrameDraw;

    //是否画框
    protected boolean mIsShowFrame = true;

    //人脸检测类
    protected FaceDetecter mFaceDetecter;

    //是否打开人脸检测
    protected boolean mIsOpenCheckFace = true;

    //回调接口
    protected FaceCheckCallback mFaceCheckCallback;

    //gif路径，如果没有则默认画框
    protected String mGifPath;

    //旋转角度
    protected int cameraRotate;

    //是否对称绘制人脸追踪框
    protected boolean mIsReverseFrame = false;

    public boolean isReverseFrame() {
        return mIsReverseFrame;
    }

    public void setReverseFrame(boolean reverseFrame) {
        mIsReverseFrame = reverseFrame;
    }

    public BaseCameraView(Context context) {
        this(context, null);
    }

    public BaseCameraView(Context context, AttributeSet attrs) {

        super(context, attrs);

        LayoutInflater.from(context).inflate(getLayout(), this, true);

        initView();

    }

    protected abstract int getLayout();

    protected abstract void initView();

    /**
     * 打开分辨率640×480的摄像头
     *
     * @param cameraID 摄像头编号
     */
    public void startPreview(int cameraID) {
        startPreview(cameraID, 640, 480);
    }

    /**
     * 打开指定分辨率的摄像头
     *
     * @param cameraID 摄像头编号
     * @param width    宽
     * @param height   高
     */
    public void startPreview(int cameraID, int width, int height) {
        if (!isAuthSuc()) {
            Toast.makeText(getContext(), "算法授权未成功", Toast.LENGTH_SHORT).show();
            return;
        }
        mWidth = width;
        mHight = height;
        openCamera(cameraID, width, height);
    }

    /**
     * 打开分辨率640×480的摄像头
     *
     * @param cameraID1 可见光摄像头编号
     * @param cameraID2 红外光摄像头编号
     */
    public void startTwoPreview(int cameraID1, int cameraID2) {
        startTwoPreview(cameraID1, cameraID2, 640, 480);
    }

    /**
     * 打开指定分辨率的摄像头
     *
     * @param cameraID1 可见光摄像头编号
     * @param cameraID2 红外光摄像头编号
     * @param width     宽
     * @param height    高
     */
    public void startTwoPreview(int cameraID1, int cameraID2, int width, int height) {
        if (!isAuthSuc()) {
            Toast.makeText(getContext(), "算法授权未成功", Toast.LENGTH_SHORT).show();
            return;
        }
        mWidth = width;
        mHight = height;
        openTwoCamera(cameraID1, cameraID2, width, height);
    }


    protected void openCamera(int cameraID, int width, int height) {
        initFaceDetecter();
        setDecterToCamera();
    }

    protected void openTwoCamera(int cameraID1, int cameraID2, int width, int height) {
        initFaceDetecter();
        setDecterToCamera();
    }

    protected boolean isAuthSuc() {
        AibeeAuth.AuthState state = AibeeAuth.getsInstance().getState();
        if (state == AuthStateSuc) {
            return true;
        } else {
            return false;
        }
    }

    public FaceDetecter getFaceDetecter() {
        return mFaceDetecter;
    }

    protected void initFaceDetecter() {
        mFaceDetecter = createFaceDetater();
        mFaceDetecter.setCameraRotate(cameraRotate);
        mFaceDetecter.init(new FaceDetecter.FaceDetecterCallback() {
            @Override
            public void getFaceLocation(RectF rectF) {
                if (mFrameDraw == null) {
                    return;
                }
                if (mIsShowFrame && mIsOpenCheckFace) {
                    int cameraWidth = mWidth;//相机分辨率宽
                    int width = getWidth();//控件实际物理宽
                    float rate = (float) width / (float) cameraWidth;
                    RectF temp;
                    if (mIsReverseFrame) {
                        temp = new RectF(width - rectF.right * rate, rectF.top * rate, width - rectF.left * rate, rectF.bottom * rate);
                    } else {
                        temp = new RectF(rectF.right * rate, rectF.top * rate, rectF.left * rate, rectF.bottom * rate);
                    }
                    if (TextUtils.isEmpty(mGifPath)) {
                        mFrameDraw.drawBoundingBox(temp, 1, Color.WHITE, -cameraRotate);
                    } else {
                        mFrameDraw.drawGif(temp, mGifPath, -cameraRotate);
                    }

                }
                if (mFaceCheckCallback != null && mIsOpenCheckFace) {
                    mFaceCheckCallback.getFaceLocation(rectF);
                }
            }

            @Override
            public void getFace(Bitmap bitmap, Bitmap pribitmap) {
                if (mFrameDraw == null) {
                    return;
                }
                if (mFaceCheckCallback != null && mIsOpenCheckFace) {
                    mFaceCheckCallback.getFace(bitmap, pribitmap);
                }
            }

            @Override
            public void loseFace() {
                if (mFrameDraw == null) {
                    return;
                }
                if (mFaceCheckCallback != null) {
                    mFaceCheckCallback.loseFace();
                }
                mFrameDraw.clearDraw();
            }
        }, getContext());
    }

    protected abstract void setDecterToCamera();

    protected abstract FaceDetecter createFaceDetater();

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

    public boolean isOpenCheckFace() {
        return mIsOpenCheckFace;
    }

    public void setOpenCheckFace(boolean openCheckFace) {
        mFrameDraw.clearDraw();
        mIsOpenCheckFace = openCheckFace;
    }

    public String getGifPath() {
        return mGifPath;
    }

    public void setGifPath(String gifPath) {
        mGifPath = gifPath;
    }

    public void stopCameraView() {
        mFrameDraw.clearDraw();
    }

    public int getCameraRotate() {
        return cameraRotate;
    }

    public void setCameraRotate(int cameraRotate) {
        this.cameraRotate = cameraRotate;
    }
}

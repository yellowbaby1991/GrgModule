package com.grg.face.view;

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
import com.grg.face.R;
import com.grg.face.callback.CompareCallback;
import com.grg.face.core.FaceDetecter;

import static com.aibee.auth.AibeeAuth.AuthState.AuthStateSuc;


/**
 * 带人脸检测画框的单目cameraview
 */
public class FaceCheckCameraView extends RelativeLayout {

    private boolean mIsShowFrame = true; //是否画框

    private CompareCallback mCompareCallback;

    private final static String TAG = "FaceCheckCameraView";

    private final static int SHOW_FRAME = 1;//画框
    private final static int HIDE_FRAME = 2;//隐藏框

    private static FaceCheckCameraView INSTANCE = null;

    protected FaceView mFvCam; //人脸框

    private FaceDetecter mFaceDetecter;

    private CameraTextureView mCameraVtv;

    private RelativeLayout mCameraRl;

    public FaceCheckCameraView(Context context) {
        this(context, null);
    }

    public FaceCheckCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.camera_texture_withface, this, true);

        INSTANCE = this;

        initView();

    }

    public void isShowFrame(boolean isShowFrame) {
        mIsShowFrame = isShowFrame;
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

    public void stopPreview(){
        mCameraVtv.stopCamera();
    }

    public void setCompareCallback(CompareCallback compareCallback) {
        mCompareCallback = compareCallback;
    }

    private boolean isAuthSuc() {
        AibeeAuth.AuthState state = AibeeAuth.getsInstance().getState();
        if (state == AuthStateSuc) {
            return true;
        } else {
            return false;
        }
    }


    private void initView() {
        mCameraVtv = findViewById(R.id.camera_vtv);
        mCameraRl = findViewById(R.id.camera_rl);
        mFvCam = new FaceView(getContext(), null);
        mCameraRl.addView(mFvCam);
    }

    private void initFaceDetecter() {
        mFaceDetecter = createFaceDetater();
        mFaceDetecter.setTwoCamera(false);
        //mFaceDetecter.setCameraRotate(0);
        mFaceDetecter.init(new CompareCallback() {
            @Override
            public void showFaceFrame(RectF rectF) {
                if (mIsShowFrame) {
                    Log.d(TAG, "检测到人脸");
                    onSendMsg(SHOW_FRAME, rectF);
                }
                if (mCompareCallback != null) {
                    mCompareCallback.showFaceFrame(rectF);
                }
            }

            @Override
            public void showFace(Bitmap bitmap, Bitmap pribitmap) {
                if (mCompareCallback != null) {
                    mCompareCallback.showFace(bitmap, pribitmap);
                }
            }
        }, getContext());
    }

    protected FaceDetecter createFaceDetater() {
        return new FaceDetecter();
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

    public CameraTextureView getCameraVtv() {
        return mCameraVtv;
    }


    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            INSTANCE.onMsgResult(msg);
        }

        ;
    };

    /**
     * Message 消息回调
     *
     * @param msg
     */
    protected void onMsgResult(Message msg) {
        switch (msg.what) {
            case SHOW_FRAME:
                onRemoveMsgs(HIDE_FRAME);
                showFrame((RectF) msg.obj);
                onSendMsgDelayed(HIDE_FRAME, null, 500);
                break;
            case HIDE_FRAME:
                mFvCam.clearFaces();
                break;
        }
    }

    /**
     * 根据坐标画框，可以重写该方法
     *
     * @param rectF
     */
    protected void showFrame(RectF rectF) {
        mFvCam.clearFaces();
        int cameraWidth = mCameraVtv.getCameraWidth();//相机分辨率宽
        int width = getWidth();//控件实际物理宽
        float rate = (float) width / (float) cameraWidth;

        RectF temp = new RectF(width - rectF.right * rate, rectF.top * rate, width - rectF.left * rate, rectF.bottom * rate);
        //RectF temp = new RectF(rectF.right * rate, rectF.top * rate, rectF.left * rate, rectF.bottom * rate);
        mFvCam.setFaces(temp);
    }

    protected void onRemoveMsgs(int what) {
        mHandler.removeMessages(what);
    }

    protected void onSendMsgs(int what) {
        mHandler.sendMessage(mHandler.obtainMessage(what));
    }

    protected void onSendMsg(int what, Object obj) {
        mHandler.sendMessage(mHandler.obtainMessage(what, obj));
    }

    protected void onSendMsgDelayed(int what, Object obj, long delayMillis) {
        mHandler.removeMessages(what, obj);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, obj),
                delayMillis);
    }


}

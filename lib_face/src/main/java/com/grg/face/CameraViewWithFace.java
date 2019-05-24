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

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

public class CameraViewWithFace extends RelativeLayout {

    ShowFaceCallBack mShowFaceCallBack;

    private final static int SHOW_FRAME = 1;

    private final static int HIDE_FRAME = 2;

    private static CameraViewWithFace INSTANCE = null;

    private ArrayBlockingQueue<YuvData> mNormalQueue = new ArrayBlockingQueue(3, true);

    private FaceView mFvCam; // 人脸矩形绘制

    private FaceDetecter mFaceDetecter;

    private CompareCallback mCompareCallback;

    private CameraTextureView mCameraVtv;

    private RelativeLayout mCameraRl;


    private final static String TAG = "CameraTextureWithFace";

    public CameraViewWithFace(Context context) {
        this(context,null);
    }

    public CameraViewWithFace(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.camera_texture_withface, this, true);

        INSTANCE = this;

        initView();
        initAibee();

    }

    private void initView() {
        mCameraVtv = findViewById(R.id.camera_vtv);
        mCameraRl = findViewById(R.id.camera_rl);
    }

    private void initAibee() {
        //initByServer();
        initByLocal();
    }

    private void initByServer() {
        AibeeAuth.setsUseCacheFirst(true);
        AibeeAuth.getsInstance().authByServer(getContext(), "", "", new AibeeAuth.AuthResultCallbck() {
            @Override
            public void onSuccess() {
                String path = getContext().getFilesDir() + File.separator + "aibee-license";
                File file = new File(path);
                if (file.exists()) {
                    String aLicense = FileUtils.readFile(file);
                    final File AIOAibeeL = new File("sdcard/AIOAibeeL.txt");
                    FileUtils.saveFile(AIOAibeeL, aLicense);
                }
                initFace();
                initCamera();
            }

            @Override
            public void onFail(AibeeAuth.AuthResult authResult) {
                if (authResult == AibeeAuth.AuthResult.AuthHttpErrorNetwork) {
                    Toast.makeText(getContext(), "算法授权失败，第一次运行请连接外网", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initByLocal() {
        String license = "";
        final String deviceId = com.aibee.auth.Util.getDeviceID(getContext());
        if ("00000000-16d5-bf6a-0033-c5870033c587".equals(deviceId)) {
            license = "05B91A50A2EF4BF847F2212C876ED08E0DCA9F6E6B6A77D9654995458A5DC5A754BE9E991ABAA5291BDE68850E50398FC4C1E59D846B605CBAC6CA92F1968E8AFE63A00A810D26E3E2EC9E373882FAA09A696EB0F5D896E336A3D548E8AD6E0DCD5987865BC75B21F9772445F55902657D7E4AB9F9BA02D05D48E58D7BB26D25";
        }
        if ("ffffffff-e6ca-bd6e-0033-c5870033c587".equals(deviceId)) {//com.grg.face
            license = "7B212E125DE430843BFAAFD08C39FE0D13BD0A4EA75444E9B7C8D098706243E0C2F6A6A46376EAACA62379F0B6F8D1832048B408B66C98DDCF759ED1D5D8398CBEB2D17159A1A9FD8D3F3939514E4146372B2FBDE3A0487C171A1702450E44CAEF5EBCE09ECB98C123FB186D42C8E067CD7BF963307E191FAF558B043EF6E9E6";
        }
        Log.e("设备编号", deviceId);//得到设备Id后，发给GRG获取license
        AibeeAuth.AuthResult result = AibeeAuth.getsInstance().authByLocal
                (getContext(), "6016310883f488ca", license);
        if (result != AibeeAuth.AuthResult.AuthSuccess) {
            Toast.makeText(getContext(), "算法授权失败", Toast.LENGTH_SHORT).show();
        } else {
            initFace();
            initCamera();
        }
    }

    private void initFace() {
        mFaceDetecter = new FaceDetecter();
        mFaceDetecter.setIslive(false);
        mFaceDetecter.setCameraRotate(0);
        mCompareCallback = new CompareCallback() {
            @Override
            public void showfaceFrame(final RectF rectF) {
                Log.d(TAG,"检测到人脸");
                onSendMsg(SHOW_FRAME, rectF);
            }

            @Override
            public void showFace(final Bitmap bitmap) {
                if (mShowFaceCallBack != null){
                    mShowFaceCallBack.showFace(bitmap);
                }
            }
        };
        mFaceDetecter.init(mNormalQueue, mCompareCallback,getContext());
        mFvCam = new FaceView(getContext(), null);
        mCameraRl.addView(mFvCam);
    }

    private void initCamera() {
        mCameraVtv.setOnPreviewCallback(new CameraTextureView.OnPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (camera != null) {
                    mFaceDetecter.onPreviewFrame(data, camera);
                }

            }
        });
    }

    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            INSTANCE.onMsgResult(msg);
        };
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

    private void showFrame(RectF rectF) {
        mFvCam.clearFaces();
        //RectF temp = new RectF(640 - rectF.right, rectF.top, 640 - rectF.left, rectF.bottom);
        mFvCam.setFaces(rectF);
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

    public interface ShowFaceCallBack{
        public void showFace(final Bitmap bitmap);
    }

    public void setShowFaceCallBack(ShowFaceCallBack showFaceCallBack) {
        mShowFaceCallBack = showFaceCallBack;
    }
}

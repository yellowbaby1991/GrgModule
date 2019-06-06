package com.grg.face.core;

import android.content.Context;

import com.aibee.auth.AibeeAuth;
import com.grg.face.utils.FileUtils;

import java.io.File;

import static com.aibee.auth.AibeeAuth.AuthState.AuthStateSuc;

/**
 * 用于人脸算法授权
 */
public class FaceAuth {

    private static final String TAG = "FaceAuth";

    /**
     * 得到设备编号
     * @param context 上下文环境
     * @return 设备编号
     */
    public static String getDeviceId(Context context){
        return com.aibee.auth.Util.getDeviceID(context);
    }

    /**
     * 是否已经授权成功
     * @return 授权结果
     */
    public static boolean isAuthSuc(){
        AibeeAuth.AuthState state = AibeeAuth.getsInstance().getState();
        if (state == AuthStateSuc) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 进行本地授权
     * @param context 上下文环境
     * @param license 授权license
     * @param apiKey 授权apikey
     * @param authCallBack 授权结果回调接口
     */
    public static void authByLocal(Context context,String license,String apiKey,AuthCallBack authCallBack){
        AibeeAuth.AuthResult result = AibeeAuth.getsInstance().authByLocal
                (context, apiKey, license);
        if (result != AibeeAuth.AuthResult.AuthSuccess) {
            authCallBack.authFail();
        } else {
            authCallBack.authSuccess();
        }
    }

    /**
     * 临时本地授权
     * @param context 上下文
     * @param license 授权license
     * @param authCallBack 授权结果回调接口
     */
    public static void authByLocal(Context context,String license,AuthCallBack authCallBack){
        authByLocal(context,license,"6016310883f488ca",authCallBack);
    }

    /**
     * 在线授权
     * @param context 上下文
     * @param apikey 授权apikey
     * @param secret 授权secret
     * @param authCallBack 授权结果回调接口
     */
    public static void authByServer(final Context context,String apikey, String secret, final AuthCallBack authCallBack){
        AibeeAuth.setsUseCacheFirst(true);
        AibeeAuth.getsInstance().authByServer(context, apikey, secret, new AibeeAuth.AuthResultCallbck() {
            @Override
            public void onSuccess() {
                String path = context.getFilesDir() + File.separator + "aibee-license";
                File file = new File(path);
                if (file.exists()) {
                    String aLicense = FileUtils.readFile(file);
                    final File AIOAibeeL = new File("sdcard/AIOAibeeL.txt");
                    FileUtils.saveFile(AIOAibeeL, aLicense);
                }
                authCallBack.authSuccess();
            }

            @Override
            public void onFail(AibeeAuth.AuthResult authResult) {
                authCallBack.authFail();
            }
        });
    }

    /**
     * 授权回调接口
     */
    public interface AuthCallBack{

        void authSuccess();

        void authFail();
    }

}

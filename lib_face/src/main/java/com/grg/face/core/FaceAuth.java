package com.grg.face.core;

import android.content.Context;

import com.aibee.auth.AibeeAuth;
import com.grg.face.utils.FileUtils;

import java.io.File;

public class FaceAuth {

    private static final String TAG = "FaceAuth";

    public static String getDeviceId(Context context){
        return com.aibee.auth.Util.getDeviceID(context);
    }

    public static void authByLocal(Context context,String license,String apiKey,AuthCallBack authCallBack){
        AibeeAuth.AuthResult result = AibeeAuth.getsInstance().authByLocal
                (context, apiKey, license);
        if (result != AibeeAuth.AuthResult.AuthSuccess) {
            authCallBack.authFail();
        } else {
            authCallBack.authSuccess();
        }
    }

    public static void authByLocal(Context context,String license,AuthCallBack authCallBack){
        authByLocal(context,license,"6016310883f488ca",authCallBack);
    }

    public static void authByServer(final Context context, boolean isLocal, String apikey, String secret, final AuthCallBack authCallBack){
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

    public interface AuthCallBack{

        void authSuccess();

        void authFail();
    }

}

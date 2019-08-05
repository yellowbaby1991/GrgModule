package com.grg.face;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.face.core.FaceAuth;
import com.grg.face.view.CameraTextureView;
import com.lib.common.utils.ToastUtils;

import java.io.DataOutputStream;

@Route(path = "/face/activity")
public class FaceActivity extends AppCompatActivity {

    private CameraTextureView mCameraTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face);

        mCameraTextureView = findViewById(R.id.cv);

        ToastUtils.show(FaceAuth.getDeviceId(this));

        String deviceId = FaceAuth.getDeviceId(this);//00000000-1e3a-f049-0033-c5870033c587
        String license = "";
        if (deviceId.equals("00000000-1e3a-f049-0033-c5870033c587")){
            license = "1B85B44D439FAF13D4C6701C7C1120D865F54EF87942F037D5DE0CCE6475B702D5F6666D3BAFCB3FE3AB8DFDDA004191C3D3C360470E4CA8E36F289D3A448901C2CE984758D2683C75913A1CA0E74F859776DF5C2382D2E0DA4ACBCB5CE65F389D38D493F4F3DC4C33F4B3C68DE99A8BDB173E875E2C3F1963F98DCB6B6BAAC3";
        }
        if (deviceId.equals("00000000-0388-a85f-d49c-30a80033c587")){
            license = "12F1F0475EF4B552C85F9928AB8DC7D44F67EFBCC5625E3E50D7EA84AACCE247C3864AEADE524653E1F24991CE7E674757A6B04629F925B29C1B82D06444AACFC4C478E53AF5297CCCB401B5ABC58ED59B509EBA734E7DCFA6F2BFFBA0BDDC9CCDB00CCA7518234983160BD8BA91157279F884CEC36AA186C61D7A19CF9CEAAC";
        }

        FaceAuth.authByLocal(this, license, new FaceAuth.AuthCallBack() {
            @Override
            public void authSuccess() {
                ToastUtils.show("授权成功");
            }

            @Override
            public void authFail() {
                ToastUtils.show("授权失败");
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                singefacecameraview(null);
            }
        }, 1000);

        ARouter.getInstance().inject(this);

    }


    public void singleLivingCamera(View view) {
       /* Intent intent = new Intent(this, SingleLivingCameraActivity.class);
        startActivity(intent);*/
        finish();
    }

    public void custom(View view) {
       /* Intent intent = new Intent(this, CustomActivity.class);
        startActivity(intent);*/
        finish();
    }

    public void singleCamera(View view) {
        /*Intent intent = new Intent(this, SingleFaceCameraViewActivity.class);
        finish();*/
    }

    public void livingCamera(View view) {
        /*Intent intent = new Intent(this, LivingCameraActivity.class);
        startActivity(intent);*/
        finish();
    }

    public void singefacecameraview(View view) {
        Intent intent = new Intent(this, SingleFaceCameraViewActivity.class);
        startActivity(intent);
        finish();
    }

    public void twofaceCheckCamera(View view) {
        Intent intent = new Intent(this, TwoFaceCameraViewActivity.class);
        startActivity(intent);
        finish();
    }


}

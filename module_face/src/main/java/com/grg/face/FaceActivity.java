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
import com.lib.common.utils.ToastUtils;

import java.io.DataOutputStream;

@Route(path = "/face/activity")
public class FaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face);

        ToastUtils.show(FaceAuth.getDeviceId(this));

        String deviceId = FaceAuth.getDeviceId(this);//ffffffff-cdb6-cfea-0033-c5870033c587
        String license = "4D5697C772B9D04A227FA452B4BEE4464E53FDC684951A4261570FD61E03F5BC07CF7D7F17B0B443647E8AD297F25BC53B606E0C3D986A3609218BC6485D659CC72C024AA1FB8C68D59F07D4E2327CBB53D50E1D43EA74160E54AACCDD9025CFC3EB3F58DA4BB44003CAD535BB1A970626D6B95890139600D2D4FE571E1425AD";
        license = "2B63DE0C21DD9092A99C716B60FA8B2D803242390DEE2A2BCEEB5FA6CD5EF9B5F78DFC3A4A3C8D43D181594261337716C4BD89CAC4BD5D7ABFA732E6A301050AFA64BA7B07B0D25371654AA1D70145EEED2B03E1D9DAA0FB06C9A6DD69E5FB9CB3279D2FDC0BD5EDE037A259A302790A2CA8BF86F519ED40A050A6D578946DCC";

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
        },1000);

        ARouter.getInstance().inject(this);

    }


    public void singleLivingCamera(View view){
       /* Intent intent = new Intent(this, SingleLivingCameraActivity.class);
        startActivity(intent);*/
        finish();
    }

    public void custom(View view){
       /* Intent intent = new Intent(this, CustomActivity.class);
        startActivity(intent);*/
        finish();
    }

    public void singleCamera(View view) {
       /* Intent intent = new Intent(this, SingleCameraActivity.class);
        startActivity(intent);*/
        finish();
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

    public void twofaceCheckCamera(View view){
        Intent intent = new Intent(this, TwoFaceCameraViewActivity.class);
        startActivity(intent);
        finish();
    }



}

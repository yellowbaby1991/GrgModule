package com.grg.face;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.face.cameraview.FaceCheckCameraActivity;
import com.grg.face.cameraview.TwoFaceCheckCameraActivity;
import com.grg.face.core.FaceAuth;
import com.grg.face.custom.CustomActivity;
import com.grg.face.living.LivingCameraActivity;
import com.grg.face.living.SingleLivingCameraActivity;
import com.grg.face.single.SingleCameraActivity;
import com.grg.face.view.FaceCheckCameraView;
import com.lib.common.utils.ToastUtils;

@Route(path = "/face/activity")
public class FaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face);

        ToastUtils.show(FaceAuth.getDeviceId(this));

        ARouter.getInstance().inject(this);

    }

    public void singleLivingCamera(View view){
        Intent intent = new Intent(this, SingleLivingCameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void custom(View view){
        Intent intent = new Intent(this, CustomActivity.class);
        startActivity(intent);
        finish();
    }

    public void singleCamera(View view) {
        Intent intent = new Intent(this, SingleCameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void livingCamera(View view) {
        Intent intent = new Intent(this, LivingCameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void faceCheckCamera(View view) {
        Intent intent = new Intent(this, FaceCheckCameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void twofaceCheckCamera(View view){
        Intent intent = new Intent(this, TwoFaceCheckCameraActivity.class);
        startActivity(intent);
        finish();
    }



}

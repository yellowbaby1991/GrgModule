package com.grg.face;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/face/activity")
public class FaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face);

        ARouter.getInstance().inject(this);


    }

    public void custom(View view){
        ARouter.getInstance().
                build("/face/custom")
                .navigation();
    }

    public void singleCamera(View view) {
        ARouter.getInstance().
                build("/face/single")
                .navigation();
    }

    public void livingCamera(View view) {
        ARouter.getInstance().
                build("/face/living")
                .navigation();
    }

    public void faceCheckCamera(View view) {
        ARouter.getInstance().
                build("/face/facecheck")
                .navigation();
    }



}

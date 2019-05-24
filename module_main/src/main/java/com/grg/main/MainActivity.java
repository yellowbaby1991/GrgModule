package com.grg.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/main/activity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void idcard(View view){

        ARouter.getInstance().
                build("/idcard/activity")
                .withString("name","黄贝")
                .navigation();

    }

    public void face(View view){

        ARouter.getInstance().
                build("/face/activity")
                .navigation();

    }
}

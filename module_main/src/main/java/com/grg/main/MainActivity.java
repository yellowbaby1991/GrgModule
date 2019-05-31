package com.grg.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lib.common.http.HttpHelper;
import com.lib.common.http.interfaces.ICallBack;
import com.lib.common.log.GrgLog;
import com.lib.common.utils.ToastUtils;

@Route(path = "/main/activity")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GrgLog.d(TAG,"MainActivity");

        ARouter.getInstance().inject(this);

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

    public void mvvm(View view){
        ARouter.getInstance().
                build("/mvvm/activity")
                .navigation();
    }
}

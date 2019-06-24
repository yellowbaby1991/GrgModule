package com.grg.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.main.databinding.ActivityHomeBinding;
import com.grg.main.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/home/activity")
public class HomeActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private ActivityHomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mBinding.setViewModel(new HomeViewModel(this));




        ARouter.getInstance().inject(this);


        testGrgOrderView();
    }

    private void testGrgOrderView() {
        List<String> list =new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(i + " 元");
        }

        mBinding.grgOrderView.setItems(list);
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

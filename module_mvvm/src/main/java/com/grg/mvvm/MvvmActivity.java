package com.grg.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.mvvm.databinding.ActivityMvvmBinding;
import com.grg.mvvm.model.User;
import com.grg.mvvm.viewmodel.MainViewModel;

import java.util.Random;

@Route(path = "/mvvm/activity")
public class MvvmActivity extends AppCompatActivity {

    private ActivityMvvmBinding mBinding;
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        mMainViewModel =new MainViewModel(this);
        mBinding.setViewModel(mMainViewModel);
        setSupportActionBar(mBinding.toolbar);


        ARouter.getInstance().inject(this);


    }


}

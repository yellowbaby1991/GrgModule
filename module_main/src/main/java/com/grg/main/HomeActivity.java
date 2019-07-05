package com.grg.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.main.databinding.ActivityHomeBinding;
import com.grg.main.viewmodel.HomeViewModel;
import com.lib.common.utils.ToastUtils;

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

        mBinding.menu.performClick();

        mBinding.menu.animate().alpha(2).x(50).y(10);


    }

    public void idcard(View view) {
        ARouter.getInstance().
                build("/idcard/activity")
                .withString("name", "黄贝")
                .navigation();
    }

    public void face(View view) {
        ARouter.getInstance().
                build("/face/activity")
                .navigation();
    }

    public void mvvm(View view) {
        ARouter.getInstance().
                build("/mvvm/activity")
                .navigation();
    }

    private boolean mIsOpen = false;

    public void onClick(View view) {
        ToastUtils.show("点击按钮");
        if (mIsOpen) {
            closeMenu();
            mIsOpen = false;
        } else {
            openMenu();
            mIsOpen = true;
        }

    }

    private void closeMenu() {
        doAnimateClose(mBinding.item1, 0, 5, 200);
        doAnimateClose(mBinding.item2, 1, 5, 200);
        doAnimateClose(mBinding.item3, 2, 5, 200);
        doAnimateClose(mBinding.item4, 3, 5, 200);
        doAnimateClose(mBinding.item5, 4, 5, 200);

    }

    private void doAnimateClose(Button view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.toRadians(90) / (total - 1) * index;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY,0 ),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 0.1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 0.1f)
        );
        set.setDuration(500).start();
    }

    private void openMenu() {
        doAnimateOpen(mBinding.item1, 0, 5, 200);
        doAnimateOpen(mBinding.item2, 1, 5, 200);
        doAnimateOpen(mBinding.item3, 2, 5, 200);
        doAnimateOpen(mBinding.item4, 3, 5, 200);
        doAnimateOpen(mBinding.item5, 4, 5, 200);
    }

    private void doAnimateOpen(Button view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.toRadians(90) / (total - 1) * index;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1),
                ObjectAnimator.ofFloat(view, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(view, "scaleY", 0, 1)
        );
        set.setDuration(500).start();
    }

}

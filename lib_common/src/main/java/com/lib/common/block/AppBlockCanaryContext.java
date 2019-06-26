package com.lib.common.block;


import android.content.pm.PackageInfo;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.BuildConfig;

public class AppBlockCanaryContext extends BlockCanaryContext {

    //设置卡顿判断的阙值
    public int provideBlockThreshold() {
        return 500;
    }

    //是否需要显示卡顿的信息
    public boolean displayNotification() {
        return true;
    }

    public String providePath() {
        return "/blockcanary/";
    }

}

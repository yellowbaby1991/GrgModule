package com.lib.common.block;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.BuildConfig;

public class AppBlockCanaryContext extends BlockCanaryContext {

    //设置卡顿判断的阙值
    public int getConfigBlockThreshold() {
        return 500;
    }

    //是否需要显示卡顿的信息
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    //设置log保存在sd卡的目录位置
    public String getLogPath() {
        return "/blockcanary/performance";
    }
}

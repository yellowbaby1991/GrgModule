package com.lib.common.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lib.common.base.log.GrgLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/30 0030.
 */

public class BaseApplication extends Application implements Thread.UncaughtExceptionHandler {

    public static String BASE_PATH = android.os.Environment.getExternalStorageDirectory().getPath() + "/";

    private boolean isDebugARouter = true;

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRouter();
        registerGrgLog();
    }


    public static BaseApplication getInstance() {
        return instance;
    }

    private void initRouter() {
        if (isDebugARouter) {
            //一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e == null) {
            return;
        }
        GrgLog.e("Crash", "uncaughtException", e);
    }

    protected String getLogPath(){
        return "grglog";
    }

    private void registerGrgLog() {
        GrgLog.init(BASE_PATH + getLogPath() + "/log/" + new SimpleDateFormat(GrgLog.DATE_FORMAT).format(new Date()));
    }
}

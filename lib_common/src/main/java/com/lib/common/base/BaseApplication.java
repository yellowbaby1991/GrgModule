package com.lib.common.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.moduth.blockcanary.BlockCanary;
import com.lib.common.block.AppBlockCanaryContext;
import com.lib.common.crash.Cockroach;
import com.lib.common.crash.ExceptionHandler;
import com.lib.common.http.HttpHelper;
import com.lib.common.http.processor.VolleyProcessor;
import com.lib.common.log.GrgLog;
import com.squareup.leakcanary.LeakCanary;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BaseApplication,包含以下功能
 * 1）内存泄漏检测
 * 2）崩溃收集但是不闪退
 * 3）集成阿里路由
 */

public class BaseApplication extends Application {

    public static String BASE_PATH = android.os.Environment.getExternalStorageDirectory().getPath() + "/";

    private boolean isDebugARouter = true;

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        LeakCanary.install(this);
        HttpHelper.init(new VolleyProcessor(this));
        installCrashRoach();
        initRouter();
        registerGrgLog();
    }

    //出现异常后不闪退，捕捉异常继续运行APP
    private void installCrashRoach() {
        final Thread.UncaughtExceptionHandler sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler();
        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        Cockroach.install(this, new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                GrgLog.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", throwable);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        toast.setText("Cockroach Worked");
                        toast.show();
                    }
                });
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                throwable.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
                toast.setText("Cockroach Worked");
                toast.show();
            }

            @Override
            protected void onEnterSafeMode() {

            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                Thread thread = Looper.getMainLooper().getThread();
                GrgLog.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }

        });

    }


    public static BaseApplication getInstance() {
        return instance;
    }

    //安装路由
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


    protected String getLogPath(){
        return getPackageName();
    }

    //初始化日志控件
    private void registerGrgLog() {
        GrgLog.init(BASE_PATH + getLogPath() + "/log/" + new SimpleDateFormat(GrgLog.DATE_FORMAT).format(new Date()));
    }
}

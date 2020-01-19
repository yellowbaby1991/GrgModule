package com.lib.common.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.DefaultRetryPolicy;
import com.github.moduth.blockcanary.BlockCanary;
import com.lib.common.block.AppBlockCanaryContext;
import com.lib.common.crash.Cockroach;
import com.lib.common.crash.ExceptionHandler;
import com.lib.common.http.HttpHelper;
import com.lib.common.http.processor.VolleyProcessor;
import com.lib.common.log.GrgLog;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.xuexiang.xui.XUI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xcrash.XCrash;

/**
 * BaseApplication,包含以下功能
 * 1）内存泄漏检测
 * 2）UI堵塞检测
 * 3）崩溃收集但是不闪退
 * 4）集成阿里路由
 */

public class BaseApplication extends Application implements TextToSpeech.OnInitListener{

    public static String BASE_PATH = android.os.Environment.getExternalStorageDirectory().getPath() + "/";

    private boolean isDebugARouter = true;

    private static BaseApplication instance;

    private static TextToSpeech tts;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initSpeekUtils();
        installBlockCanary();
        installLeakCanary();
        initXCrash();
        initHttpHelper();
        initRouter();
        initGrgLog();
        initLogger();
        initXui();

    }

    protected void initLogger() {
/*        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // ( Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(new LogcatLogStrategy())
                .tag("GrgLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();*/
        Logger.addLogAdapter(new AndroidLogAdapter());
        //Logger.addLogAdapter(new DiskLogAdapter());
    }

    private void initSpeekUtils() {
        tts = new TextToSpeech(this, this);
    }

    //用于检测内存异常
    protected void installLeakCanary() {
        LeakCanary.install(this);
    }

    //初始化XUI
    protected void initXui() {
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
    }

    //用于捕获UI阻塞
    protected void installBlockCanary(){
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }


    //初始化网络请求工具
    protected void initHttpHelper(){
        VolleyProcessor volleyProcessor = new VolleyProcessor(this);
        volleyProcessor.setRetryPolicy(new DefaultRetryPolicy());
        HttpHelper.init(volleyProcessor);
    }

    //出现异常后不闪退，捕捉异常继续运行APP
    protected void installCrashRoach() {
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
    protected void initRouter() {
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

    //初始化GRG日志
    protected void initGrgLog() {
        GrgLog.init(BASE_PATH + getLogPath() + "/log/" + new SimpleDateFormat(GrgLog.DATE_FORMAT).format(new Date()));
    }

    //用于捕获崩溃日志，和CrashRoach会冲突
    protected void initXCrash(){
        XCrash.init(this, new XCrash.InitParameters().setLogDir(BASE_PATH + getLogPath() + "/crash/" + new SimpleDateFormat(GrgLog.DATE_FORMAT).format(new Date())));
    }

    @Override
    public void onInit(int status){
        // 判断是否转化成功
        if (status == TextToSpeech.SUCCESS){
            //默认设定语言为中文，原生的android貌似不支持中文。
            int result = tts.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
            }else{
                //不支持中文就将语言设置为英文
                tts.setLanguage(Locale.US);
            }
        }
    }

    public static TextToSpeech getTts() {
        return tts;
    }
}

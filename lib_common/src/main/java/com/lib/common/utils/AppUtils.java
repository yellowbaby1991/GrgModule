package com.lib.common.utils;

import android.content.Context;
import android.content.Intent;

public class AppUtils {

    //重启APP
    public static void reStartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("REBOOT","reboot");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //退出APP
    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}

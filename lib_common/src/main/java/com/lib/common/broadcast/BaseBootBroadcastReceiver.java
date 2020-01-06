package com.lib.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * 
 *本类是开机广播
 * 
 *@author zjxin2 on 2016-10-10
 *@version  
 *
 */
public abstract class BaseBootBroadcastReceiver extends BroadcastReceiver {
	
	private final String TAG = "BootBroadcastReceiver";
	
	public BaseBootBroadcastReceiver() {}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals("android.intent.action.BOOT_COMPLETED")) {
			Log.d(TAG,"重启");
			startCurApp(context,setStartActiviyClass());
		}

	}

	public abstract Class setStartActiviyClass();
	
	private void startCurApp(Context context, Class<?> cls) {
		Intent intentStart = new Intent(context, cls);
		intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentStart);
	}


}

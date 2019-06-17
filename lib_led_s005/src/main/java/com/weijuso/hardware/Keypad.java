package com.weijuso.hardware;


public class Keypad {
	
	static {
	    System.loadLibrary ("led");
	}
	
	public native int ledsetting(int ledname,int onoff);
	public native int readgpio(int buf);
	
	private final int LEDNAME = 8;
	
//	public static final int[] LED_STATES = {0, 1, 2};	// 自动, 打开, 关闭
	
	private static int mLedConfig = 0;
	
	private boolean isOpened;
	
	public boolean isOpened() {
		return isOpened;
	}
	
	/**
	 * 更新补光灯状态
	 * @param ledConfig
	 */
	public void updateLedState(int ledConfig) {
		mLedConfig = ledConfig;
		switch (ledConfig) {
		case 1:
			ledsetting(LEDNAME, 0);
			isOpened = true;
			break;
		default:
			ledsetting(LEDNAME, 1);
			isOpened = false;
			break;
		}
	}
	
	/**
	 * 控制补光灯开关
	 * @param open	是否开启补光灯
	 * @return
	 */
	public int ledOpen(boolean open) {
		switch (mLedConfig) {
		case 0:
			isOpened = open;
			return ledsetting(LEDNAME, open ? 0 : 1);
			
		case 1:
			if(isOpened) {
				return 0;
			} else {
				isOpened = true;
				return ledsetting(LEDNAME, 0);
			}

		default:
			if(isOpened) {
				isOpened = false;
				return ledsetting(LEDNAME, 0);
			} else {
				return 0;
			}
		}
		
		
	}
	
}

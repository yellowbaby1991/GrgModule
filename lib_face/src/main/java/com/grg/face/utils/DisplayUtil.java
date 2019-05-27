package com.grg.face.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera.CameraInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;

/**
 * 
 *屏幕分辨率工具类
 * 
 *@author zjxin2 on 2016-11-4 
 *@version  
 *
 */
public class DisplayUtil {
	public static void prepareMatrix(Matrix matrix, int cameraId, int displayOrientation, int viewWidth,
                                     int viewHeight) {
		// Need mirror for front camera.
		if (cameraId == CameraInfo.CAMERA_FACING_BACK) {
			matrix.setScale(1, 1);
		} else if (cameraId == CameraInfo.CAMERA_FACING_FRONT) {
			matrix.setScale(-1, 1);
		}
		// matrix.setScale(mirror ? -1 : 1, 1);
		// This is the value for android.hardware.Camera.setDisplayOrientation.
		matrix.postRotate(displayOrientation);
		// Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
		// UI coordinates range from (0, 0) to (width, height).
		matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
		matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
	}

	/**
	 * 获取屏幕的分辨率长宽属性
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * 将Bitmap图片字节序列化
	 * 
	 * @param bm
	 * @param quality
	 *            序列化后的图片质量
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

}

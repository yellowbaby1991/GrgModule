package com.grg.face.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * 本类是文件操作类
 * 
 * @author zjxin2 on 2016-05-15
 * @version
 *
 */
public class FileUtils {

	public static final String PARENT_FILE_PATH = Environment.getExternalStorageDirectory().getPath()
			+ "/grgfaceaio";

	public static final String IMG_PATH = PARENT_FILE_PATH
			+ "/img";

	public static final String IMG_CARD = "card.jpg";// 身份证照片路径

	private static final String IMG_CAPTURE = "capture.jpg";// 拍照图像

	private static final String IMG_CAPTURE_FACE = "capture_face.jpg";



	/**
	 * 判断抓取的人脸图片是否存在
	 * 
	 * @return
	 */
	public boolean isCaptureFaceExist() {
		if ((new File(getCaptureFaceImgPath())).exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static String readFile(File file) {
		if (file == null) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			byte[] b = new byte[is.available()];
			is.read(b);
			String data = new String(b);
			if (!TextUtils.isEmpty(data)) {
				return data;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void saveFile(File file, String data) {
		if (file == null) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取拍照图像人脸文件路径
	 * 
	 * @return
	 */
	public String getCaptureFaceImgPath() {
		return IMG_PATH + File.separator + IMG_CAPTURE_FACE;
	}

	/**
	 * 获取拍照图像文件路径
	 * 
	 * @return
	 */
	public String getCaptureImgPath() {
		return IMG_PATH + File.separator + IMG_CAPTURE;
	}

	/**
	 * 获取身份证照片文件路径
	 * 
	 * @return
	 */
	public String getCardImgPath() {
		return IMG_PATH + File.separator + IMG_CARD;
	}

	/**
	 * 获取拍照图像
	 * 
	 * @return
	 */
	public byte[] getCaptureImg() {
		return getImg(IMG_PATH + "/" + IMG_CAPTURE);
	}

	/**
	 * 获取身份证头像图像
	 * 
	 * @return
	 */
	public byte[] getCardImg() {
		return getImg(IMG_PATH + "/" + IMG_CARD);
	}

	/**
	 * 获取图像数据
	 * 
	 * @param filePath
	 * @return
	 */
	public byte[] getImg(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file = null;
			return new byte[1];
		}
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
		byte[] bytes = baos.toByteArray();
		bitmap.recycle();
		bitmap = null;
		try {
			baos.close();
		} catch (IOException e) {
		}
		baos = null;
		file = null;

		return bytes;
	}

	/**
	 * 保存所拍图像
	 * 
	 * @param data
	 * @param rotateDegree
	 *            旋转角度
	 * @return
	 */
	public boolean saveCaptureImg(byte[] data, int rotateDegree) {
		return saveImg(data, IMG_CAPTURE, rotateDegree);
	}

	/**
	 * 保存身份证头像
	 * 
	 * @param data
	 * @return
	 */
	public boolean saveCardImg(byte[] data) {
		return saveImg(data, IMG_CARD, 0);
	}

	/**
	 * 保存图片
	 * 
	 * @param data
	 * @param fileName
	 * @return
	 */
	public boolean saveImg(byte[] data, String fileName, int rotateDegree) {
		if (data == null) {
			return false;
		}
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return false;
		}

		File file = new File(IMG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, fileName);

		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		if (bitmap == null) {
			file = null;
			return false;
		}

		if (rotateDegree != 0) {
			// 旋转 TODO
		}

		boolean result = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
				fos = null;
			}
		}
		file = null;

		return result;
	}


	private boolean saveImgCamera(byte[] data, String fileName,
			int rotateDegree, int width, int height) {
		if (data == null) {
			return false;
		}
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return false;
		}

		File file = new File(IMG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, fileName);
		
		int[] rgb = new int[width * height];
		decodeYUV420SPrgb565(data, width, height, rgb);
		Bitmap bitmapCamera = Bitmap.createBitmap(rgb, width, height,
				Config.ARGB_8888);
		rgb = null;
		if (bitmapCamera == null) {
			return false;
		}

		if (rotateDegree != 0) {
			// 旋转 TODO
		}

		boolean result = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmapCamera.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(bitmapCamera != null) {
				bitmapCamera.recycle();
				bitmapCamera = null;
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {}
				fos = null;
			}
		}
		file = null;

		return result;
	}

	/**
	 * 将相机视频流图像数据转成Bitmap
	 *
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap cameraData2Bitmap(byte[] data, int width, int height) {
		if (data == null) {
			return null;
		}
		int[] rgb = new int[width * height];
		decodeYUV420SPrgb565(data, width, height, rgb);
		Bitmap bitmap = Bitmap.createBitmap(rgb, width, height, Config.ARGB_8888);
		rgb = null;
		return bitmap;
	}

	// public Bitmap

	public Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
		int frameSize = width * height;
		int[] rgba = new int[frameSize * 4];

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				int y = (0xff & ((int) data[i * width + j]));
				int u = (0xff & ((int) data[frameSize + (i >> 1) * width
						+ (j & ~1) + 0]));
				int v = (0xff & ((int) data[frameSize + (i >> 1) * width
						+ (j & ~1) + 1]));
				y = y < 16 ? 16 : y;

				int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
				int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128)
						- 0.391f * (u - 128));
				int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

				r = r < 0 ? 0 : (r > 255 ? 255 : r);
				g = g < 0 ? 0 : (g > 255 ? 255 : g);
				b = b < 0 ? 0 : (b > 255 ? 255 : b);

				rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
			}

		Bitmap bmp = Bitmap
				.createBitmap(width, height, Config.ARGB_8888);
		bmp.setPixels(rgba, 0, width, 0, 0, width, height);
		return bmp;
	}

	public String getImgParentPath() {
		return IMG_PATH;
	}

	public Bitmap getFaceBitmap() {
		return BitmapFactory.decodeFile(getCaptureFaceImgPath());
	}

	/**
	 * 从照片中截取人脸并保存
	 * 
	 * @param rect
	 * @return
	 */
	public boolean catchAndSaveFaceImp(RectF rect) {
		Bitmap bitmapBig = BitmapFactory.decodeFile(getCaptureImgPath());
		if(bitmapBig == null) {
			return false;
		}
		Bitmap bitmap = Bitmap.createBitmap(bitmapBig, Math.round(rect.left),
				Math.round(rect.top), Math.round(rect.right - rect.left),
				Math.round(rect.bottom - rect.top));
		if (bitmap == null) {
			bitmapBig.recycle();
			bitmapBig = null;
			return false;
		}
		File file = new File(IMG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, IMG_CAPTURE_FACE);
		
		boolean result = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			result =  true;
		} catch (FileNotFoundException e) {}
		finally {
			if(bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {}
				fos = null;
			}
		}
		file = null;
		
		return result;
	}

	public boolean saveImgFile(Bitmap bitmap, File file) {
		FileOutputStream fos = null;
		boolean result = false;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {}
				fos = null;
			}
		}

		return result;
	}

	/**
	 * 复制单个文件(可更名复制)
	 * 
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param newPathFile
	 *            拷贝到新绝对路径带文件名(注：目录路径需带文件名)
	 * @return
	 */
	public boolean copySingleFile(String oldPathFile, String newPathFile) {
		boolean result = false;
		try {
			// int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
				@SuppressWarnings("resource")
                FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				inStream = null;
				fs.flush();
				fs.close();
				fs = null;
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 复制整个文件夹的内容(含自身)
	 * 
	 * @param oldPath
	 *            准备拷贝的目录
	 * @param newPath
	 *            指定绝对路径的新目录
	 * @return
	 */
	public static void copyFolderWithSelf(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File dir = new File(oldPath);
			// 目标
			newPath += File.separator + dir.getName();
			File moveDir = new File(newPath);
			if (dir.isDirectory()) {
				if (!moveDir.exists()) {
					moveDir.mkdirs();
				}
			}
			String[] file = dir.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
					output = null;
					input = null;
				}
				if (temp.isDirectory()) { // 如果是子文件夹
					copyFolderWithSelf(oldPath + "/" + file[i], newPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String IMG_CAPTURE_0 = "captured_0.jpg";

	private String[] mNormalFaces = null;

	private int mNormalFaceNumber = 1;

	public static final String IMG_CAPTURE_1 = "captured_1.jpg";

	public static final String IMG_FACE_1 = "face_1.jpg";

	/**
	 * 初始化可见光人脸图像名称列表
	 */
	public void initNormalFaceNameList(int size) {
		mNormalFaceNumber = size;
		// mNormalFaces = new String[size];
		// for(int i = 0; i < size; i++) {
		// mNormalFaces[i] = "face_0_" + i + ".jpg";
		// }
	}

	public String getImgPath(String imgName) {
		return IMG_PATH + File.separator + imgName;
	}

	/**
	 * 判断人脸图片是否存在
	 * 
	 * @param faceImgName
	 * @return
	 */
	public boolean isFaceImgExist(String faceImgName) {
		if ((new File(getImgPath(faceImgName))).exists()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean saveFace1Img(byte[] data, int rotateDegree) {
		return saveImg(data, IMG_FACE_1, rotateDegree);
	}

	/**
	 * 从照片中截取人脸并保存
	 * 
	 * @param rect
	 *            检测到人脸位置
	 * @param orginalImgPath
	 *            原图路径
	 * @param faceImgName
	 *            要保存的人脸图片名称
	 * @return
	 */
	Bitmap mBitmap = null;
	FileOutputStream mFos = null;

	private boolean catchFaceImg(RectF rectf, String orginalImgPath,
                                 String faceImgName) {
		mBitmap = BitmapFactory.decodeFile(orginalImgPath);
		mBitmap = Bitmap.createBitmap(mBitmap, Math.round(rectf.left),
				Math.round(rectf.top), Math.round(rectf.right - rectf.left),
				Math.round(rectf.bottom - rectf.top));
		if (mBitmap == null) {
			return false;
		}
		File file = new File(IMG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, faceImgName);
		mFos = null;
		try {
			mFos = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mFos);

			mBitmap.recycle();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} finally {
			if (mFos != null) {
				try {
					mFos.flush();
					mFos.close();
					mFos = null;
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 根据rectf截取人脸
	 * 
	 * @param bitmap
	 * @param rectf
	 * @return
	 */
	public Bitmap catchBitmapFace(Bitmap bitmap, RectF rectf) {
		return Bitmap.createBitmap(bitmap, Math.round(rectf.left),
				Math.round(rectf.top), Math.round(rectf.right - rectf.left),
				Math.round(rectf.bottom - rectf.top));
	}

	/**
	 * 删除文件（包括文件夹和文件）
	 *
	 * @param file
	 */
	public void deleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFile(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	 * YUV视频流转换argb转成黑白
	 * 
	 * @param yuv
	 * @param width
	 * @param height
	 * @return
	 */
	public static int[] I420toARGB(byte[] yuv, int width, int height) {

		boolean invertHeight = false;
		if (height < 0) {
			height = -height;
			invertHeight = true;
		}

		boolean invertWidth = false;
		if (width < 0) {
			width = -width;
			invertWidth = true;
		}

		int iterations = width * height;
		int[] rgb = new int[iterations];

		for (int i = 0; i < iterations; i++) {
			int nearest = (i / width) / 2 * (width / 2) + (i % width) / 2;

			int y = yuv[i] & 0x000000ff;
			int u = yuv[iterations + nearest] & 0x000000ff;
			int v = yuv[iterations + iterations / 4 + nearest] & 0x000000ff;
			int b = (int) (y + 1.8556 * (u - 128));
			int g = (int) (y - (0.4681 * (v - 128) + 0.1872 * (u - 128)));
			int r = (int) (y + 1.5748 * (v - 128));

			// r = r < 0 ? 0 : (r > 255 ? 255 : r);
			// g = g < 0 ? 0 : (g > 255 ? 255 : g);
			// b = b < 0 ? 0 : (b > 255 ? 255 : b);
			if (b > 255) {
				b = 255;
			} else if (b < 0) {
				b = 0;
			}
			if (g > 255) {
				g = 255;
			} else if (g < 0) {
				g = 0;
			}
			if (r > 255) {
				r = 255;
			} else if (r < 0) {
				r = 0;
			}
			int targetPosition = i;

			if (invertHeight) {
				targetPosition = ((height - 1) - targetPosition / width)
						* width + (targetPosition % width);
			}
			if (invertWidth) {
				targetPosition = (targetPosition / width) * width + (width - 1)
						- (targetPosition % width);
			}

			rgb[targetPosition] = (0xff000000) | (0x00ff0000 & r << 16)
					| (0x0000ff00 & g << 8) | (0x000000ff & b);
		}
		return rgb;

	}

	/**
	 * YUV转成彩色
	 * 
	 * @param rgb
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	public static void decodeYUV420SPrgb565(byte[] yuv420sp, int width,
			int height, int[] rgb) {
		final int frameSize = width * height;
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}
				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);
				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;
				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}

	/**
	 * 获取可见光人脸图像最大数量
	 * 
	 * @return
	 */
	public int getNormalFaceNumber() {
		return mNormalFaceNumber;
	}

	/**
	 * @return the mNormalFaces
	 */
	public String[] getNormalFaces() {
		return mNormalFaces;
	}

}

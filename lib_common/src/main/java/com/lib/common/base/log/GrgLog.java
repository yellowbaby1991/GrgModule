package com.lib.common.base.log;

import android.os.StatFs;
import android.util.Log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 
 *本类是GRG日志组件，需要用到libs里的android-logging-log4j-1.0.3.jar和log4j-1.2.17.jar两个日志组件包
 * 
 *@author zjxin2 on 2016-9-5
 *@version  
 *
 */
public class GrgLog {

	private static final String TAG = "GrgLog";
	private static final int DEBUG = 1;
	private static final int INFO = 2;
	private static final int WARN = 3;
	private static final int ERROR = 4;

	public final static String DATE_FORMAT = "yyyy-MM-dd";

	private static String LOG_PATH = "";
	private static int DAY = 0;

	/**
	 * SD卡保存log的路径
	 */
	// private final static String LOG_PATH_DEFAULT =
	// android.os.Environment.getExternalStorageDirectory()
	// + "/rcc_ui";

	// private final static String LOG_FOLDER = "log/";

	private static ConcurrentLinkedQueue<LogInfo> logQueue = new ConcurrentLinkedQueue<LogInfo>();

	/**
	 * 日志配置
	 *
	 * @param rccPath
	 *            日志文件路径
	 */
	private static void configureLog(String logPath) {
		Log.w(TAG, "logPath = " + logPath);

		if (logPath.charAt(logPath.length() - 1) != '/') {
			logPath += "/";
		}

		LogConfigurator logConfigurator = getLogConfig(logPath);
		try {
			logConfigurator.configure();
		} catch (Exception e) {
			Log.w(TAG, "configureLog : e = ", e);
		}

	}

	/**
	 * 使用线程配置日志
	 *
	 * @author zjxin2
	 */
	private static class ConfigLogThread extends Thread {
		private String mPath = null;

		/**
		 * 日志配置线程
		 *
		 * @param rccPath
		 */
		public ConfigLogThread(String logPath) {
			this.mPath = logPath;
		}

		@Override
		public void run() {
			configureLog(this.mPath);
			super.run();

			new DeleteLogThread(this.mPath.substring(0, mPath.lastIndexOf("/")), 1).start();
		}
	}

	/**
	 * 初始化日志配置。程序启动时，必须调用此方法
	 */
	public static void init(String logPath) {
		LOG_PATH = logPath;
		Calendar c = Calendar.getInstance();
		DAY = c.get(Calendar.DAY_OF_YEAR);
		new ConfigLogThread(logPath).start();
	}

	/**
	 * @param tag
	 * @param msg
	 */
	public static synchronized void d(String tag, String msg) {
		// Logger.getLogger(tag).debug(msg);
		LogInfo logInfo = new LogInfo();
		logInfo.level = GrgLog.DEBUG;
		addLog(logInfo, tag, msg);
	}

	/**
	 * @param tag
	 * @param msg
	 */
	public static synchronized void i(String tag, String msg) {
		// Logger.getLogger(tag).info(msg);
		LogInfo logInfo = new LogInfo();
		logInfo.level = GrgLog.INFO;
		addLog(logInfo, tag, msg);
	}

	/**
	 * @param tag
	 * @param msg
	 */
	public static synchronized void w(String tag, String msg) {
		// Logger.getLogger(tag).warn(msg);
		LogInfo logInfo = new LogInfo();
		logInfo.level = GrgLog.WARN;
		addLog(logInfo, tag, msg);
	}

	public synchronized static void w_array(String tag, byte[] b) {
		String msg = "";
		for (int i = 0; i < b.length; i++) {
			msg += b[i] + " ";
		}
		w(tag, msg);
	}
	
	/**
	 * 将浮点数组转成字符串
	 * @param floats
	 * @return
	 */
	public synchronized static String float2Str(float[] floats) {
		if (floats == null) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			String temp = null;
			for (int i = 0; i < floats.length; i++) {
				temp = Float.toString(floats[i]);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}

				sb.append(temp);
				if (i != (floats.length - 1)) {
					sb.append(" ");
				}
			}

			return sb.toString();
		}
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public synchronized static String bytes2HexStr(byte[] bytes) {
		if (bytes == null) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			String temp = null;
			for (int i = 0; i < bytes.length; i++) {
				temp = Integer.toHexString(bytes[i] & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}

				sb.append(temp);
				if (i != (bytes.length - 1)) {
					sb.append(" ");
				}
			}

			return sb.toString();
		}
	}

	/**
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static synchronized void w(String tag, String msg, Throwable t) {
		// Logger.getLogger(tag).warn(msg, t);
		LogInfo logInfo = new LogInfo();
		logInfo.level = GrgLog.WARN;
		logInfo.throwable = t;
		addLog(logInfo, tag, msg);
	}

	/**
	 * @param tag
	 * @param msg
	 */
	public static synchronized void e(String tag, String msg) {
		// Logger.getLogger(tag).error(msg);
		LogInfo logInfo = new LogInfo();
		logInfo.level = GrgLog.ERROR;
		addLog(logInfo, tag, msg);
	}

	/**
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static synchronized void e(String tag, String msg, Throwable t) {
		// Logger.getLogger(tag).error(msg, t);
		LogInfo logInfo = new LogInfo();
		logInfo.level = GrgLog.ERROR;
		logInfo.throwable = t;
		addLog(logInfo, tag, msg);
	}

	public static synchronized void addLog(LogInfo logInfo, String tag, String msg) {
		logInfo.tag = tag;
		logInfo.msg = msg;
		logQueue.add(logInfo);
		if (!WriteLogThread.isAlive) {
			new WriteLogThread().start();
		}
	}

	/**
	 * 从配置文件中获取日志配置
	 *
	 * @param rccPath
	 * @return
	 */
	private static LogConfigurator getLogConfig(String logPath) {
		// 初始化默认配置，读取配置文件出问题，将使用此默认配置
		LogConfigurator config = new LogConfigurator();
		config.setFileName(logPath + "aio.log");
		config.setRootLevel(Level.DEBUG);
		config.setLevel("RCC.LOG", Level.DEBUG);
		config.setMaxFileSize(10 * 1024 * 1024); // 日志文件的最大限制
		config.setUseFileAppender(true); // 日志是否输出到文件
		config.setUseLogCatAppender(true); // 日志是否输出到LogCat
		config.setImmediateFlush(true);
		config.setFilePattern("%d [%-5p] [%c{2}] %m%n"); // [%-5p] %d{mm:ss,SSS}
															// -- %m%n

		return config;
	}// end

	/**
	 * 写日志线程
	 */
	private static class WriteLogThread extends Thread {
		static boolean isAlive = false;

		@Override
		public void run() {
			isAlive = true;
			LogInfo logInfo = null;
			Calendar c = Calendar.getInstance();
			while (!logQueue.isEmpty()) {
				// Log.e(TAG, "WriteLogThread : DAY = " + DAY
				// + ", curDay = " + c.get(Calendar.DAY_OF_YEAR));
				if (DAY == c.get(Calendar.DAY_OF_YEAR)) {
					logInfo = logQueue.poll();
					if (logInfo != null) {
						switch (logInfo.level) {
						case GrgLog.DEBUG:
							if (logInfo.throwable == null) {
								Logger.getLogger(logInfo.tag).debug(logInfo.msg);
							} else {
								Logger.getLogger(logInfo.tag).debug(logInfo.msg, logInfo.throwable);
							}
							break;

						case GrgLog.INFO:
							if (logInfo.throwable == null) {
								Logger.getLogger(logInfo.tag).info(logInfo.msg);
							} else {
								Logger.getLogger(logInfo.tag).info(logInfo.msg, logInfo.throwable);
							}
							break;

						case GrgLog.WARN:
							if (logInfo.throwable == null) {
								Logger.getLogger(logInfo.tag).warn(logInfo.msg);
							} else {
								Logger.getLogger(logInfo.tag).warn(logInfo.msg, logInfo.throwable);
							}
							break;

						case GrgLog.ERROR:
							if (logInfo.throwable == null) {
								Logger.getLogger(logInfo.tag).error(logInfo.msg);
							} else {
								Logger.getLogger(logInfo.tag).error(logInfo.msg, logInfo.throwable);
							}
							break;

						default:
							break;
						}
					}
				} else {
					DAY = c.get(Calendar.DAY_OF_YEAR);
					int index = LOG_PATH.lastIndexOf("/");
					LOG_PATH = LOG_PATH.substring(0, index + 1) + new SimpleDateFormat(DATE_FORMAT).format(new Date());
					configureLog(LOG_PATH);
				}
			}
			logInfo = null;
			isAlive = false;
			super.run();
		}
	}

	/**
	 * 剩余空间（单位byte）
	 */
	private static long availableSpace() {
		long availableSize = 0;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File file = android.os.Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(file.getPath());
			long availableBlock = statfs.getAvailableBlocks();
			long blockSize = statfs.getBlockSize();
			availableSize = availableBlock * blockSize; // 单位byte
		}
		return availableSize;
	}

	/**
	 * 删除日志线程
	 *
	 * @author zjxin2
	 */
	private static class DeleteLogThread extends Thread {
		private String mFilePath = null;
		private int mMonths;

		/**
		 * 删除路径 filePath 里的日志，保留months到months+1个月的日志
		 *
		 * @param filePath
		 * @param months
		 */
		public DeleteLogThread(String filePath, int months) {
			this.mFilePath = filePath;
			this.mMonths = months;
		}

		@Override
		public void run() {
			// 删除mMonths个月之前或大于1M的文件日志
			deleteLog(this.mMonths);
			super.run();
		}

		/**
		 * 删除 monthInterval 月之前或大小超过 fileLength 的日志
		 *
		 * @param monthInterval
		 * @param fileLength
		 */
		private void deleteLog(int monthInterval) {
			File[] files = new File(this.mFilePath).listFiles();
			if (files == null)
				return;

			String[] paths = null;
			String fileDate = null;
			String[] fileDateSplit = null;
			String[] curDateSplit = null;
			int months = 0;
			for (int i = 0; i < files.length; i++) {
				paths = files[i].getPath().split("/");
				fileDate = paths[paths.length - 1];
				fileDateSplit = fileDate.split("-");

				if (fileDateSplit != null && fileDateSplit.length > 1) {
					curDateSplit = (new SimpleDateFormat(DATE_FORMAT)).format(new Date()).split("-");

					months = (Short.parseShort(curDateSplit[0]) - Short.parseShort(fileDateSplit[0])) * 12
							+ Short.parseShort(curDateSplit[1]) - Short.parseShort(fileDateSplit[1]);
					// 删除 monthInterval+1 月之前 或 当前时间之后 或大小超过 fileLength 的日志
					if (months > monthInterval || months < 0) {
						deleteFile(files[i]);
					}
				}
				// else { // 删除文件名不规则日志文件
				// if(files[i].exists() && files[i].length*() > fileLength) {
				// files[i].deleteOnExit();
				// }
				// }
			}
		}

		/**
		 * 删除文件（包括文件夹和文件）
		 *
		 * @param file
		 */
		private void deleteFile(File file) {
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

	}

	static class LogInfo {
		public int level = 0;
		public String tag = null;
		public String msg = null;
		public Throwable throwable = null;
	}

}

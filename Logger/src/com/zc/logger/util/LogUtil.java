package com.zc.logger.util;

import android.util.Log;

public final class LogUtil {

	private LogUtil() {
	}

	private static volatile boolean writeLogs = false;

	public static void writeLogs(boolean writeLogs) {
		LogUtil.writeLogs = writeLogs;
	}

	private static boolean showLogs() {
		return writeLogs;
	}

	public static void v(String tag, String msg) {
		if (showLogs()) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (showLogs()) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (showLogs()) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (showLogs()) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (showLogs()) {
			Log.e(tag, msg);
		}
	}

}

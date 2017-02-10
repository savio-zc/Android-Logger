package com.zc.logger;

import android.text.TextUtils;

import com.zc.logger.config.Config;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.LogOption;
import com.zc.logger.log.Logger;
import com.zc.logger.model.LogMessage;
import com.zc.logger.util.LogUtil;

public class LogManager {
	public static final String TAG = "LogManager";

//	private static final String ERROR_NOT_INIT = "LogManager must be init with non-null LogManagerConfig before using";

	private LogManagerConfig mConfig;

	private static final LogManager INSTANCE = new LogManager();

	public static LogManager getInstance() {
		return INSTANCE;
	}

	private LogManager() {
	}

	public void init(LogManagerConfig config) {
		mConfig = config;
	}

	public String getFilePath() {
//		checkConfig();
		if (!checkConfig()) {
			return null;
		}
		return mConfig.getFilePath();
	}

//	private void checkConfig() {
//		if (mConfig == null) {
//			throw new IllegalStateException(ERROR_NOT_INIT);
//		}
//	}
	
	private boolean checkConfig() {
		final boolean ok = mConfig != null;
		if (!ok) {
			LogUtil.e(TAG, "config is null");
		}
		return ok;
	}

	public void v(String tag, String text) {
		log(Config.LEVEL_VERBOSE, tag, text);
	}

	public void d(String tag, String text) {
		log(Config.LEVEL_DEBUG, tag, text);
	}

	public void i(String tag, String text) {
		log(Config.LEVEL_INFO, tag, text);
	}

	public void w(String tag, String text) {
		log(Config.LEVEL_WARN, tag, text);
	}

	public void e(String tag, String text) {
		log(Config.LEVEL_ERROR, tag, text);
	}

	private void log(int level, String tag, String text) {
//		checkConfig();
		if (!checkConfig()) {
			return;
		}
		LogOption local = new LogOption.Builder(tag, level).build();
		log(getMessage(text, local), local, null);
	}

	public void log(String text, LogOption local) {
		log(getMessage(text, local), local, null);
	}

	public void log(String text, LogOption local, LogManagerConfig global) {
		log(getMessage(text, local), local, global);
	}

	private LogMessage getMessage(String text, LogOption local) {
		if (TextUtils.isEmpty(text) || local == null) {
			LogUtil.w(TAG, "local config: " + local);
			return null;
		}
		final LogMessage message = new LogMessage();
		message.setTime(System.currentTimeMillis());
		message.setApplication(mConfig.getPackageName());
		message.setPID(android.os.Process.myPid());
		message.setTID(android.os.Process.myTid());
		message.setText(text);
		message.setTag(local.getTag());
		message.setLevel(local.getLevel());
		return message;
	}

	private void log(LogMessage message, LogOption local,
			LogManagerConfig global) {
		if (message == null || local == null) {
			LogUtil.w(TAG, "message: " + message + ", local config: " + local);
			return;
		}
		final LogManagerConfig cfg;
		if (global == null) {
			cfg = mConfig;
		} else {
			cfg = global;
		}
		final int count = cfg.getLoggerCount();
		for (int i = 0; i < count; i++) {
			final Logger logger = cfg.getLogger(i);
			if (logger != null) {
				logger.log(message, local, cfg);
			}
		}
	}

}

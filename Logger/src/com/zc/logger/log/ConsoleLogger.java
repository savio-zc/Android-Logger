package com.zc.logger.log;

import com.zc.logger.LogManager;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogOption;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.model.LogMessage;

import android.util.Log;

public class ConsoleLogger extends BaseLogger {
	public static final String TAG = LogManager.TAG + ":ConsoleLogger";

	@Override
	public boolean log(LogMessage message, LogOption local,
			LogManagerConfig global) {
		if (super.log(message, local, global)) {
			return true;
		}
		log(local.getTag(), local.getLevel(), message.getText());
		return true;
	}

	private static void log(String tag, int level, String msg) {
		log(tag, level, msg, null);
	}

	private static void log(String tag, int level, String msg, Throwable tr) {
		if (level >= Config.LEVEL_VERBOSE && level < Config.LEVEL_DEBUG) {
			Log.v(tag, msg, tr);
		} else if (level < Config.LEVEL_INFO) {
			Log.d(tag, msg, tr);
		} else if (level < Config.LEVEL_WARN) {
			Log.i(tag, msg, tr);
		} else if (level < Config.LEVEL_ERROR) {
			Log.w(tag, msg, tr);
		} else if (level < Config.LEVEL_ASSERT) {
			Log.e(tag, msg, tr);
		} else {
			Log.wtf(tag, msg, tr);
		}
	}

}
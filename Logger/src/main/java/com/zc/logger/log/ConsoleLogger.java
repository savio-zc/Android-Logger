package com.zc.logger.log;

import android.util.Log;

import com.zc.logger.LogManager;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.LogOption;
import com.zc.logger.model.LogMessage;

public class ConsoleLogger extends BaseLogger {
    private static final String TAG = LogManager.TAG + ":ConsoleLogger";

    @Override
    public boolean log(LogMessage message, LogOption local, LogManagerConfig global) {
        if (super.log(message, local, global)) {
            return true;
        }
        final int level = local.getLevel();
        final String tag = local.getTag();
        final String msg = message.getText();
        final Throwable t = message.getThrowable();
        if (level >= Config.LEVEL_VERBOSE && level < Config.LEVEL_DEBUG) {
            Log.v(tag, msg, t);
        } else if (level < Config.LEVEL_INFO) {
            Log.d(tag, msg, t);
        } else if (level < Config.LEVEL_WARN) {
            Log.i(tag, msg, t);
        } else if (level < Config.LEVEL_ERROR) {
            Log.w(tag, msg, t);
        } else if (level < Config.LEVEL_ASSERT) {
            Log.e(tag, msg, t);
        } else {
            Log.wtf(tag, msg, t);
        }
        return true;
    }

}
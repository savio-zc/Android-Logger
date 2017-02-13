package com.zc.logger;

import com.zc.logger.config.Config;
import com.zc.logger.config.LogOption;

/**
 * Created by zzc on 2017/2/11.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogManager.getInstance().log(ex, thread.toString(), new LogOption.Builder("[Crash]", Config.LEVEL_ASSERT).build(), null);
    }
}

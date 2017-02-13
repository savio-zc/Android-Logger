package com.zc.logger;

import com.zc.logger.config.Config;
import com.zc.logger.config.LogOption;

/**
 * Created by zzc on 2017/2/11.
 */

class CrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogOption local = new LogOption.Builder(Config.TAG_CRASH, Config.LEVEL_ASSERT).build();
        LogManager.getInstance().log(ex, thread.toString(), local, null);
    }
}

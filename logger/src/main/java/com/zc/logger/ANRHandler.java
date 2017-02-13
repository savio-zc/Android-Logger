package com.zc.logger;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogOption;

/**
 * Created by zzc on 2017/2/11.
 */

class ANRHandler implements ANRWatchDog.ANRListener {
    @Override
    public void onAppNotResponding(ANRError error) {
        LogOption local = new LogOption.Builder(Config.TAG_ANR, Config.LEVEL_ASSERT).build();
        LogManager.getInstance().log(error, Thread.currentThread().toString(), local, null);
    }
}

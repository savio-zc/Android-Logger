package com.zc.logger;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogOption;

/**
 * Created by zzc on 2017/2/11.
 */

public class ANRHandler implements ANRWatchDog.ANRListener {
    @Override
    public void onAppNotResponding(ANRError error) {
        LogManager.getInstance().log(error, null, new LogOption.Builder("[ANR]", Config.LEVEL_ASSERT).build(), null);
    }
}

package com.zc.logger.log;

import com.zc.logger.config.LogManagerConfig;

import java.io.File;

/**
 * Created by zzc on 2017/2/11.
 */

public class CrashFileLogger extends FileLogger {
    @Override
    protected String getRootPath(LogManagerConfig global) {
        return new File(global.getFilePath(), "crash").getAbsolutePath();
    }

    @Override
    protected String getFileName() {
        return "crash.log";
    }

    @Override
    protected String getZipFileName() {
        return "crash.zip";
    }
}

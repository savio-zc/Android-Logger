package com.zc.logger.config;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.github.anrwatchdog.ANRWatchDog;
import com.zc.logger.ANRHandler;
import com.zc.logger.CrashHandler;
import com.zc.logger.format.DefaultFormatter;
import com.zc.logger.format.Formatter;
import com.zc.logger.log.ANRFileLogger;
import com.zc.logger.log.CrashFileLogger;
import com.zc.logger.log.Logger;
import com.zc.logger.model.LogModule;
import com.zc.logger.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogManagerConfig {

    // System Level
    private final int mMinLevel;
    private final int mMaxLevel;

    // Modules
    private final Map<String, LogModule> mModules;
    private final boolean mEnableModuleFilter;

    // Formatter
    private final Formatter mFormatter;

    // Loggers
    private final List<Logger> mLoggers;

    // File Manager
    private final int mFileLevel;
    private final long mFileSize;
    private final List<OnFileFullListener> mFileListeners;
    private final Executor mExecutor;
    private final Handler mHandler;

    private final Context mContext;

    private LogManagerConfig(Builder builder) {
        mContext = builder.context;

        mMinLevel = builder.minLevel;
        mMaxLevel = builder.maxLevel;

        mModules = builder.modules;
        mEnableModuleFilter = builder.enableModuleFilter;

        mFormatter = builder.formatter;

        mLoggers = builder.loggers;

        mFileLevel = builder.fileLevel;
        mFileSize = builder.fileSize;
        mFileListeners = builder.fileListeners;
        mExecutor = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());

        LogUtil.writeLogs(builder.writeLogs);
    }

    public int getMinLevel() {
        return mMinLevel;
    }

    public int getMaxLevel() {
        return mMaxLevel;
    }

    public boolean enableModuleFilter() {
        return mEnableModuleFilter;
    }

    public int getModuleCount() {
        if (mModules == null) {
            return 0;
        }
        return mModules.size();
    }

    public LogModule getModule(String tag) {
        if (tag == null) {
            return null;
        }
        if (mModules == null || mModules.size() <= 0) {
            return null;
        }
        return mModules.get(tag);
    }

    public Formatter getFormatter() {
        return mFormatter;
    }

    public int getLoggerCount() {
        if (mLoggers == null) {
            return 0;
        }
        return mLoggers.size();
    }

    public Logger getLogger(int index) {
        if (index >= 0 && index < getLoggerCount()) {
            return mLoggers.get(index);
        }
        return null;
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }

    private static final String NAME_LOGGER = "AndroidLogger";

    public String getFilePath() {
        final String path = NAME_LOGGER + File.separator + mContext.getPackageName();
        final File pubFile = Environment.getExternalStoragePublicDirectory(path);
        if (pubFile != null) {
            return pubFile.getAbsolutePath();
        }
        final File exFile = mContext.getExternalFilesDir(path);
        if (exFile != null) {
            return exFile.getAbsolutePath();
        }
        final File inFile = mContext.getDir(path, Context.MODE_PRIVATE);
        if (inFile != null) {
            return inFile.getAbsolutePath();
        }
        return null;
    }

    public int getFileLevel() {
        return mFileLevel;
    }

    public long getFileSize() {
        return mFileSize;
    }

    public List<OnFileFullListener> getFileFullListeners() {
        return mFileListeners;
    }

    public Executor getExecutor() {
        return mExecutor;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public static class Builder {

        private int minLevel = Config.LEVEL_VERBOSE;
        private int maxLevel = Config.LEVEL_ASSERT;

        private Map<String, LogModule> modules = new HashMap<String, LogModule>();
        private boolean enableModuleFilter = false;

        private Formatter formatter = new DefaultFormatter();

        private List<Logger> loggers = new ArrayList<Logger>();

        private int fileLevel = Config.FILE_LEVEL_DEF;
        private long fileSize = Config.FILE_SIZE_DEF;
        private List<OnFileFullListener> fileListeners = new ArrayList<OnFileFullListener>();

        private boolean writeLogs = false;

        private final Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder minLevel(int level) {
            this.minLevel = level;
            return this;
        }

        public Builder maxLevel(int level) {
            this.maxLevel = level;
            return this;
        }

        public Builder addModule(LogModule module) {
            if (module != null && !this.modules.containsValue(module)) {
                this.modules.put(module.getTag(), module);
            }
            return this;
        }

        public Builder enableModuleFilter(boolean enable) {
            this.enableModuleFilter = enable;
            return this;
        }

        public Builder formatter(Formatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public Builder addLogger(Logger logger) {
            if (logger != null && !this.loggers.contains(logger)) {
                this.loggers.add(logger);
            }
            return this;
        }

        public Builder enableANR(boolean enable) {
            Logger anrLogger = null;
            for (Logger logger : this.loggers) {
                if (logger instanceof ANRFileLogger) {
                    anrLogger = logger;
                }
            }
            if (enable) {
                if (anrLogger == null) {
                    addLogger(new ANRFileLogger());
                    new ANRWatchDog(5000).setReportMainThreadOnly().setANRListener(new ANRHandler()).start();
                }
            } else {
                if (anrLogger != null) {
                    this.loggers.remove(anrLogger);
                }
            }
            return this;
        }

        public Builder enableCrash(boolean enable) {
            Logger crashLogger = null;
            for (Logger logger : this.loggers) {
                if (logger instanceof CrashFileLogger) {
                    crashLogger = logger;
                }
            }
            if (enable) {
                if (crashLogger == null) {
                    addLogger(new CrashFileLogger());
                    Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
                }
            } else {
                if (crashLogger != null) {
                    this.loggers.remove(crashLogger);
                    Thread.setDefaultUncaughtExceptionHandler(null);
                }
            }
            return this;
        }

        public Builder setFileLevel(int level) {
            if (level <= 0) {
                return this;
            }
            this.fileLevel = level;
            return this;
        }

        public Builder setFileSize(long size) {
            if (size <= 0) {
                return this;
            }
            this.fileSize = size;
            return this;
        }

        public Builder addOnFileFullListener(OnFileFullListener l) {
            if (l != null && !fileListeners.contains(l)) {
                fileListeners.add(l);
            }
            return this;
        }

        public Builder writeLogs(boolean write) {
            this.writeLogs = write;
            return this;
        }

        public LogManagerConfig build() {
            return new LogManagerConfig(this);
        }

    }
}

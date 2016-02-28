package com.zc.logger.log;

import com.zc.logger.LogManager;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.LogOption;
import com.zc.logger.model.LogMessage;
import com.zc.logger.model.LogModule;
import com.zc.logger.util.LogUtil;

public class BaseLogger implements Logger {
    public static final String TAG = LogManager.TAG + ":BaseLogger";

    @Override
    public boolean log(LogMessage message, LogOption local, LogManagerConfig global) {
        if (message == null || local == null || global == null) {
            LogUtil.w(TAG, "message: " + message + ", local: " + local + ", global: " + global);
            return true;
        }
        final int level = local.getLevel();
        if (global.enableModuleFilter()) {
            final int moduleCount = global.getModuleCount();
            if (moduleCount <= 0) {
                LogUtil.w(TAG, "moduleCount: " + moduleCount);
                return true;
            }
            final LogModule module = global.getModule(local.getTag());
            if (module == null) {
                LogUtil.w(TAG, "forbid module tag: " + local.getTag());
                return true;
            }
            if (level < module.getMinLevel() || level > module.getMaxLevel()) {
                LogUtil.w(TAG, "forbid module level: " + level);
                return true;
            }
        }
        if (level < global.getMinLevel() || level > global.getMaxLevel()) {
            LogUtil.w(TAG, "forbid system level: " + level);
            return true;
        }
        return false;
    }

}

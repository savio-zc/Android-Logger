package com.zc.logger.demo;

import android.app.Application;

import com.zc.logger.LogManager;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.OnFileFullListener;
import com.zc.logger.format.DefaultFormatter;
import com.zc.logger.log.ConsoleLogger;
import com.zc.logger.log.FileLogger;
import com.zc.logger.model.LogModule;

import java.io.File;

/**
 * Created by zzc on 16/2/28.
 */
public class HomeApplication extends Application {
    public static final String TAG = "HomeApplication";

    private static final boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        // init log manager before you use it to print log
        initLogManager();
        L.e(TAG, "onCreate");
    }

    private void initLogManager() {
        LogManagerConfig config = new LogManagerConfig.Builder(this)
                .minLevel(Config.LEVEL_VERBOSE) // default, lowest log level is v
                .maxLevel(Config.LEVEL_ASSERT) // default, highest log level is a
                .enableModuleFilter(!DEBUG) // if false, all logs are printed; if true, only added module logs are printed
                .addModule(new LogModule(HomeApplication.TAG))
                .addModule(new LogModule(HomeActivity.TAG))
                .writeLogs(true) // display Logger internal logs
                .setFileLevel(10) // create at most 10 log files
                .setFileSize(100000) // create every log file smaller than 100000Byte
                .addOnFileFullListener(new OnFileFullListener() {
                    @Override
                    public void onFileFull(File file) {
                        // called in ui thread when one log file is full, you can upload it to your log server
                    }
                })
                .formatter(new DefaultFormatter()) // log format is the same as adb logcat
                .addLogger(new FileLogger()) // print log to file
                .addLogger(new ConsoleLogger()) // print log to console
                .enableANR()//print anr log
                .enableCrash()//print crash log
                .build();
        LogManager.getInstance().init(config);
    }
}

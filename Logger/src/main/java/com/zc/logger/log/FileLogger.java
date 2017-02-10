package com.zc.logger.log;

import android.os.Handler;
import android.text.TextUtils;

import com.zc.logger.LogManager;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.LogOption;
import com.zc.logger.config.OnFileFullListener;
import com.zc.logger.format.Formatter;
import com.zc.logger.model.LogMessage;
import com.zc.logger.util.CompressUtil;
import com.zc.logger.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class FileLogger extends BaseLogger {
    public static final String TAG = LogManager.TAG + ":FileLogger";

    private static final String FILE_NAME = "logger.log";
    private static final String ZIP_FILE_NAME = "logger.zip";
    private static final String PATH_SINGLE = "single";
    private static final String PATH_MULTIPLE = "multiple";

    @Override
    public boolean log(final LogMessage message, final LogOption local, final LogManagerConfig global) {
        if (super.log(message, local, global)) {
            return true;
        }
        final String rootPath = global.getFilePath();
        if (TextUtils.isEmpty(rootPath)) {
            LogUtil.w(TAG, "filePath: " + rootPath);
            return true;
        }
        final Formatter formatter = global.getFormatter();
        if (formatter == null) {
            LogUtil.w(TAG, "formatter: " + formatter);
            return true;
        }
        final String line = formatter.format(message);
        if (TextUtils.isEmpty(line)) {
            LogUtil.w(TAG, "line: " + line);
            return true;
        }
        final Executor executor = global.getExecutor();
        if (executor == null) {
            LogUtil.w(TAG, "executor: " + executor);
            return true;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final int fileLevel = global.getFileLevel();
                final long fileSize = global.getFileSize();

                final int lineSize = line.getBytes().length;

                final File multipleDir = new File(rootPath, PATH_MULTIPLE);
                if (!multipleDir.exists()) {
                    multipleDir.mkdirs();
                }
                // delete useless files to make sure file level take effect
                ArrayList<File> remains = new ArrayList<File>();
                for (int i = 0; i < fileLevel; i++) {
                    remains.add(new File(multipleDir, FILE_NAME + "." + i));
                }
                File[] exits = multipleDir.listFiles();
                if (exits != null) {
                    ArrayList<File> deletes = new ArrayList<File>(Arrays.asList(exits));
                    deletes.removeAll(remains);
                    for (File file : deletes) {
                        file.delete();
                    }
                }
                // print log to multiple files
                final File firstFile = new File(multipleDir, FILE_NAME + "." + 0);
                if (firstFile.length() + lineSize <= fileSize) {
                    appendLine(firstFile, line);
                } else {
                    for (int i = 0; i < fileLevel; i++) {
                        final File file = new File(multipleDir, FILE_NAME + "." + i);
                        if (!file.exists() || (i == fileLevel - 1)) {
                            if (i == fileLevel - 1) {
                                file.delete();
                            }
                            for (int j = i - 1; j >= 0; j--) {
                                final File oldFile = new File(multipleDir, FILE_NAME + "." + j);
                                final File newFile = new File(multipleDir, FILE_NAME + "." + (j + 1));
                                oldFile.renameTo(newFile);
                            }
                            appendLine(firstFile, line);
                        }
                    }
                }

                // print log to a single log for upload
                final List<OnFileFullListener> listeners = global.getFileFullListeners();
                if (listeners == null || listeners.size() <= 0) {
                    return;
                }
                final Handler handler = global.getHandler();
                if (handler == null) {
                    return;
                }
                final File singleDir = new File(rootPath, PATH_SINGLE);
                if (!singleDir.exists()) {
                    singleDir.mkdirs();
                }
                final File singleFile = new File(singleDir, FILE_NAME);
                if (singleFile.length() + lineSize <= fileLevel * fileSize) {
                    appendLine(singleFile, line);
                } else {
                    final File zipFile = new File(rootPath, ZIP_FILE_NAME);
                    zipFile.delete();
                    CompressUtil.zip(zipFile, singleFile);
                    for (final OnFileFullListener l : listeners) {
                        if (l != null) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    l.onFileFull(zipFile);
                                }
                            });
                        }
                    }
                    singleFile.delete();
                    appendLine(singleFile, line);
                }
            }
        });
        return true;
    }

    private static void appendLine(File file, String line) {
        if (file == null || TextUtils.isEmpty(line)) {
            return;
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}

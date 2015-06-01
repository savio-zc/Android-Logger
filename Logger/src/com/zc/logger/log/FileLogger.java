package com.zc.logger.log;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Looper;

import com.zc.logger.LogManager;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogOption;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.OnFileFullListener;
import com.zc.logger.format.Formatter;
import com.zc.logger.model.LogMessage;
import com.zc.logger.util.CompressUtil;
import com.zc.logger.util.FileUtil;
import com.zc.logger.util.LogUtil;
import com.zc.logger.util.Util;

public class FileLogger extends BaseLogger {
	public static final String TAG = LogManager.TAG + ":FileLogger";

	private static final String FILE_NAME = "logger.log";
	private static final String ZIP_FILE_NAME = "logger.zip";
	private static final String PATH_SINGLE = "single";

	@Override
	public boolean log(final LogMessage message, final LogOption local,
			final LogManagerConfig global) {
		if (super.log(message, local, global)) {
			return true;
		}
		final Formatter formatter = global.getFormatter();
		if (formatter == null) {
			LogUtil.w(TAG, "formatter: " + formatter);
			return true;
		}
		final Executor executor = global.getFileExecutor();
		if (executor == null) {
			LogUtil.w(TAG, "executor: " + executor);
			return true;
		}
		executor.execute(new Runnable() {

			@Override
			public void run() {
				print(formatter.format(message), global.getFilePath(),
						global.getFileLevel(), global.getFileSize(),
						global.getFileFullListeners());
			}
		});
		return true;
	}

	private static String getFileName(String path, int index) {
		return FileUtil.concatPath(path, FILE_NAME + "." + index);
	}

	private static void print(final String line, final String filePath,
			final int fileLevel, final long fileSize,
			final List<OnFileFullListener> listeners) {
		if (Util.isEmpty(line) || Util.isEmpty(line.getBytes())
				|| Util.isEmpty(filePath)) {
			LogUtil.w(TAG, "line: " + line + ", filePath: " + filePath);
			return;
		}

		final File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		final int lineSize = line.getBytes().length;

		// delete useless files to make sure file level take effect
		for (int i = fileLevel; i < Config.FILE_LEVEL_MAX; i++) {
			final String name = getFileName(filePath, i);
			FileUtil.delete(name);
		}

		final String fileName = getFileName(filePath, 0);
		final long size = FileUtil.length(fileName) + lineSize;
		if (size <= fileSize) {
			FileUtil.appendLine(fileName, line);
		} else {
			for (int i = 0; i < fileLevel; i++) {
				final String name = getFileName(filePath, i);
				if (!FileUtil.exists(name) || (i == fileLevel - 1)) {
					if (i == fileLevel - 1) {
						FileUtil.delete(name);
					}
					for (int j = i - 1; j >= 0; j--) {
						final String oldName = getFileName(filePath, j);
						final String newName = getFileName(filePath, j + 1);
						FileUtil.rename(oldName, newName);
					}
					FileUtil.appendLine(fileName, line);
				}
			}
		}

		if (listeners != null && listeners.size() > 0) {
			final String rootPath = file.getParent();
			final String backupPath = FileUtil.concatPath(rootPath, PATH_SINGLE);

			final File backFile = new File(backupPath);
			if (!backFile.exists()) {
				backFile.mkdirs();
			}

			final String backupName = FileUtil.concatPath(backupPath, FILE_NAME);
			final long backSize = FileUtil.length(backupName);
			if (backSize + lineSize <= fileLevel * fileSize) {
				FileUtil.appendLine(backupName, line);
			} else {
				final File zipFile = new File(rootPath, ZIP_FILE_NAME);
				zipFile.delete();
				CompressUtil.zip(zipFile, new File(backupName));
				for (final OnFileFullListener l : listeners) {
					if (l != null) {
						Handler handler = new Handler(Looper.getMainLooper());
						handler.post(new Runnable() {

							@Override
							public void run() {
								l.onFileFull(zipFile);
							}
						});
					}
				}
				FileUtil.delete(backupName);
				FileUtil.appendLine(backupName, line);
			}
		}
	}
}

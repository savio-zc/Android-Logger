package com.zc.logger.config;

import java.io.File;

public interface OnFileFullListener {

	/**
	 * called in UI thread
	 * @param file
	 */
	public void onFileFull(File file);

}

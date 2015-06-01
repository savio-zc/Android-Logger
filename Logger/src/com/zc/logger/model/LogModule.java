package com.zc.logger.model;

import com.zc.logger.config.Config;

public class LogModule {

	// Module Level
	private int mMinLevel = Config.LEVEL_VERBOSE;
	private int mMaxLevel = Config.LEVEL_ASSERT;

	private final String mTag;

	public LogModule(String tag) {
		mTag = tag;
	}

	public LogModule(String tag, int min, int max) {
		mTag = tag;
		mMinLevel = min;
		mMaxLevel = max;
	}

	public String getTag() {
		return mTag;
	}

	public int getMinLevel() {
		return mMinLevel;
	}

	public int getMaxLevel() {
		return mMaxLevel;
	}

	public void setMinLevel(int level) {
		mMinLevel = level;
	}

	public void setMaxLevel(int level) {
		mMaxLevel = level;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LogModule)) {
			return false;
		}
		final LogModule module = (LogModule) o;
		if (mTag != null) {
			return mTag.equals(module.mTag);
		} else {
			if (module.mTag == null) {
				return true;
			} else {
				return false;
			}
		}
	}

}

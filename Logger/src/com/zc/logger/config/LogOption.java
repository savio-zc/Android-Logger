package com.zc.logger.config;

public class LogOption {

	// Level
	private final int mLevel;

	// Module tag
	private final String mTag;

	private LogOption(Builder builder) {
		mLevel = builder.level;
		mTag = builder.tag;
	}

	public int getLevel() {
		return mLevel;
	}

	public String getTag() {
		return mTag;
	}

	public static class Builder {

		private final int level;

		private final String tag;

		public Builder(String tag, int level) {
			this.tag = tag;
			this.level = level;
		}

		public LogOption build() {
			return new LogOption(this);
		}

	}

}

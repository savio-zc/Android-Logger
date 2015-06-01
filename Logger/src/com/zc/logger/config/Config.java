package com.zc.logger.config;

public class Config {

	// Level
	public static final int LEVEL_VERBOSE = 0;
	public static final int LEVEL_DEBUG = 100;
	public static final int LEVEL_INFO = 200;
	public static final int LEVEL_WARN = 300;
	public static final int LEVEL_ERROR = 400;
	public static final int LEVEL_ASSERT = 500;

	private static final String LEVEL_VERBOSE_S = "V";
	private static final String LEVEL_DEBUG_S = "D";
	private static final String LEVEL_INFO_S = "I";
	private static final String LEVEL_WARN_S = "W";
	private static final String LEVEL_ERROR_S = "E";
	private static final String LEVEL_ASSERT_S = "A";

	public static String levelString(int level) {
		if (level >= Config.LEVEL_VERBOSE && level < Config.LEVEL_DEBUG) {
			return LEVEL_VERBOSE_S;
		} else if (level < Config.LEVEL_INFO) {
			return LEVEL_DEBUG_S;
		} else if (level < Config.LEVEL_WARN) {
			return LEVEL_INFO_S;
		} else if (level < Config.LEVEL_ERROR) {
			return LEVEL_WARN_S;
		} else if (level < Config.LEVEL_ASSERT) {
			return LEVEL_ERROR_S;
		} else {
			return LEVEL_ASSERT_S;
		}
	}

	// File Level
	public static final int FILE_LEVEL_MIN = 1;
	public static final int FILE_LEVEL_MAX = 10;
	public static final int FILE_LEVEL_DEF = 10;

	// File Size
	public static final int FILE_SIZE_MIN = 10000;
	public static final int FILE_SIZE_MAX = 1000000;
	public static final int FILE_SIZE_DEF = 100000;

}

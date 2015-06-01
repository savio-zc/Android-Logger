package com.zc.logger.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarUtil {

	private CalendarUtil() {
	}

	public static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";

	public static String getCalendarString(long time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		Date result = new Date(time);
		return sdf.format(result);
	}

	public static String getCalendarString(long time) {
		return getCalendarString(time, LOG_DATE_FORMAT);
	}

}

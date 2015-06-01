package com.zc.logger.util;

import java.util.List;

public final class Util {

	private Util() {
	}

	public static boolean isEmpty(String s) {
		if (s == null || s.length() <= 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isEmpty(T[] array) {
		if (array == null || array.length <= 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isEmpty(List<T> list) {
		if (null == list || list.size() <= 0) {
			return true;
		}
		return false;
	}

}

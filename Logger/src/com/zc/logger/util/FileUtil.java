package com.zc.logger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil {

	private FileUtil() {
	}

	public static void appendLine(String fileName, String line) {
		if (Util.isEmpty(fileName) || Util.isEmpty(line)) {
			return;
		}
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fileName,
					true)));
			out.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static boolean exists(String fileName) {
		if (Util.isEmpty(fileName)) {
			return false;
		}
		return new File(fileName).exists();
	}

	public static long length(String fileName) {
		if (Util.isEmpty(fileName)) {
			return 0;
		}
		return new File(fileName).length();
	}

	public static boolean delete(String fileName) {
		if (Util.isEmpty(fileName)) {
			return true;
		}
		return new File(fileName).delete();
	}

	public static boolean rename(String oldName, String newName) {
		if (Util.isEmpty(oldName) || Util.isEmpty(newName)
				|| oldName.equals(newName)) {
			return true;
		}
		return new File(oldName).renameTo(new File(newName));
	}

	public static String concatPath(String path, String name) {
		if (Util.isEmpty(path) || Util.isEmpty(name)) {
			return path;
		}
		if (path.endsWith(File.separator)) {
			return path + name;
		} else {
			return path + File.separator + name;
		}
	}

}

package com.zc.logger.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class CompressUtil {

	private static final int BUFFER_SIZE = 1024;

	private CompressUtil() {
	}

	private static void zipFile(ZipOutputStream zos, File inFile) {
		if (zos == null) {
			return;
		}
		if (inFile == null || !inFile.exists() || inFile.isDirectory()) {
			return;
		}
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(inFile));
			zos.putNextEntry(new ZipEntry(inFile.getPath()));
			byte[] buffer = new byte[BUFFER_SIZE];
			int readLength = 0;
			while ((readLength = bis.read(buffer)) > 0) {
				zos.write(buffer, 0, readLength);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void zipDirectory(ZipOutputStream zos, File inFile) {
		if (zos == null) {
			return;
		}
		if (inFile == null || !inFile.exists() || !inFile.isDirectory()) {
			return;
		}
		File[] files = inFile.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file != null) {
				if (file.isFile()) {
					zipFile(zos, file);
				} else {
					zipDirectory(zos, file);
				}
			}
		}
	}

	public static void zip(String outPath, String inPath) {
		if (outPath == null || inPath == null) {
			return;
		}
		zip(new File(outPath), new File(inPath));
	}

	public static void zip(File outFile, File inFile) {
		if (outFile == null || outFile.isDirectory()) {
			return;
		}
		if (inFile == null || !inFile.exists()) {
			return;
		}
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(outFile));
			if (inFile.isFile()) {
				zipFile(zos, inFile);
			} else {
				zipDirectory(zos, inFile);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

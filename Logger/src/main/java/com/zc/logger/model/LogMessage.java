package com.zc.logger.model;

import com.zc.logger.config.Config;
import com.zc.logger.util.CalendarUtil;

public class LogMessage {

	private long mTime; // in ms
	private int mPID;
	private int mTID;
	private String mApplication;
	private String mTag;
	private String mText;

	private int mLevel;

	public String getText() {
		return mText;
	}

	public long getTime() {
		return mTime;
	}

	public String getTimeString() {
		return CalendarUtil.getCalendarString(getTime());
	}

	public int getPID() {
		return mPID;
	}

	public int getTID() {
		return mTID;
	}

	public String getApplication() {
		return mApplication;
	}

	public String getTag() {
		return mTag;
	}

	public int getLevel() {
		return mLevel;
	}

	public String getLevelString() {
		return Config.levelString(getLevel());
	}

	public void setText(String text) {
		mText = text;
	}

	public void setTime(long time) {
		mTime = time;
	}

	public void setPID(int pid) {
		mPID = pid;
	}

	public void setTID(int tid) {
		mTID = tid;
	}

	public void setApplication(String app) {
		mApplication = app;
	}

	public void setTag(String tag) {
		mTag = tag;
	}

	public void setLevel(int level) {
		mLevel = level;
	}

}

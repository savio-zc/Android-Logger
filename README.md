# Android-Logger
## 1 Overview
### 1.1 Background
Android System print all logs into centralized files via logcat. Developers can not pull their log files from android devices unless they have root previlege. But it is so common for a developer to find out the bugs only with the log files.

I developed the Android-Logger to solve the problem described above. For one thing, logs of an application are printed into files on sdcard with the directory of its own package name. For another thing, custom configurations can be made as you like.

### 1.2 Benefits
Using Android-Logger, you can:
1. print your logs into your own files on sdcard
2. pull your log files easily from any android device
3. upload your log files onto your servers for later analysis

### 1.3 TODOs
1. Encrypt the log files
2. View the logs on phones, such as CatLog

## 2 User manual
### 2.1 APIs
```java
public class LogManager {
    private static final LogManager INSTANCE = new LogManager();
	private LogManager() {
	}
	public static LogManager getInstance() {
		return INSTANCE;
	}
	public void init(LogManagerConfig config) {
		mConfig = config;
	}
	public void v(String tag, String text) {
	}
	public void d(String tag, String text) {
	}
	public void i(String tag, String text) {
	}
	public void w(String tag, String text) {
	}
	public void e(String tag, String text) {
	}
	public void log(String text, LogOption local) {
	}
	public void log(String text, LogOption local, LogManagerConfig global) {
	}
}
```

### 2.2 Global configuration
```java
 private void initLogManager() {
    LogManagerConfig config = new LogManagerConfig.Builder(this)
			.minLevel(Config.LEVEL_VERBOSE) // default
			.maxLevel(Config.LEVEL_ASSERT) // default
			.enableModuleFilter(true) // default
			.addModule(new LogModule("SampleActivity", Config.LEVEL_INFO, Config.LEVEL_ERROR))
			.addModule(new LogModule("SampleActivity2"))
			.formatter(new DefaultFormatter()) // default
			.setFileLevel(10) // default 10
			.setFileSize(10000) // default
			.writeLogs(false) // default, write logs of the library
			.build();
	LogManager.getInstance().init(config);
}
```
 
### 2.3 Sample
```java
public class LogUtil {
  	public static void d(String tag, String msg) {
		LogManager.getInstance().d(tag, msg);
	}
}
```

### 2.4 Notice
LogManager.getInstance().init(config) should be called before anything else when use.

## 3 Design description
### 3.1 System architecture  
TODO

### 3.2 Core components
#### 3.2.1 LogMessage: define the content of a log, including time, pid, tid, package name, module name, level and custom text.
```java
public class LogMessage {
    
	private long mTime; // in ms
	private int mPID;
	private int mTID;
	private String mApplication;
	private String mTag;
	private String mText;
	
	private int mLevel;
}
```

#### 3.2.2 Formatter: translate the log into formatted string.
```java
public interface Formatter {
    public String format(LogMessage message);
}
```
DefaultFormatter is offered, which looks like the android logcat format.
Custom formatter can be implemented as you like.

#### 3.2.3 Logger: print formatted string into console, file or anything.
```java
public interface Logger {

	/**
	 * This method should return immediately. Make sure it is thread-safe.
	 * 
	 * @param message
	 * @param local
	 * @param global
	 * @return true if log event has been consumed.
	 */
	public boolean log(LogMessage message, LogOption local,
			LogManagerConfig global);

}
```
ConsoleLogger is offered to print log into console. FileLogger is offered to print log into file which can be pulled from android devices without root previlege.
Custom logger can be implemented as you like.

#### 3.2.4 Log level：v, d, i, w, e, a
```java
public class Config {
    // Level
	public static final int LEVEL_VERBOSE = 0;
	public static final int LEVEL_DEBUG = 100;
	public static final int LEVEL_INFO = 200;
	public static final int LEVEL_WARN = 300;
	public static final int LEVEL_ERROR = 400;
	public static final int LEVEL_ASSERT = 500;
}
```
new level can be inserted into every two exsiting levels.

#### 3.2.5 Log module: describe a log belong to a certain module.
```java
public class LogModule {
    // Module Level
	private int mMinLevel = Config.LEVEL_VERBOSE;
	private int mMaxLevel = Config.LEVEL_ASSERT;
	private final String mTag;
}
```

### 3.3 Everything is configurable
#### 3.3.1 Global configuration：system max level, system min level, module name, module max level, module min level, formatter, loggers, log file numbers, log file size.
```java
public class LogManagerConfig {
  // System Level
	private final int mMinLevel;
	private final int mMaxLevel;
	// Modules
	private final Map<String, LogModule> mModules;
	private final boolean mEnableModuleFilter;
	// Formatter
	private final Formatter mFormatter;
	// Loggers
	private final List<Logger> mLoggers;
	// File Manager
	private final int mFileLevel;
	private final long mFileSize;
	private final Executor mFileExecutor;
	private final Context mContext;
}
```

#### 3.3.2 Local option: the level and module of the log to be printed.
```java
public class LogOption {
    // Level
	private final int mLevel;
	// Module tag
	private final String mTag;
}
```


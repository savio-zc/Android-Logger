# Android-Logger
## 1 Overview
### 1.1 Background
All logs are printed into centralized files via logcat by android system. Developers can not pull their log files from android devices unless they have root previlege. But it is so common for a developer to find out bugs only with log files.

I developed Android-Logger to solve the problem described above. For one thing, logs of an application are printed into files on sdcard with the directory of its own package name. For another thing, custom configurations can be made as you like.

### 1.2 Benefits
Using Android-Logger, you can:  
1. print your logs (including ANR and crash log) into your own files on sdcard;  
2. pull your log files easily from any android device;  
3. upload your log files onto your servers for later analysis.

### 1.3 TODOs
1. Encrypt the log files

## 2 Usage
### 2.1 Download
Use Gradle:
```groovy
dependencies {
    compile 'com.github.savio-zc:logger:1.1.0'
}
```
or Maven:
```xml
<dependency>
  <groupId>com.github.savio-zc</groupId>
  <artifactId>logger</artifactId>
  <version>1.1.0</version>
</dependency>
```

### 2.2 ProGuard
No need to add any rules.

### 2.3 How to use
First, config LogManager in your Application class:
```java
public class HomeApplication extends Application {

    private static final boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogManager();// init LogManager before you use it to print log
        LogUtil.writeLogs(DEBUG);
    }

    private void initLogManager() {
        LogManagerConfig config = new LogManagerConfig.Builder(this)
                .minLevel(Config.LEVEL_VERBOSE) // default, lowest log level is v
                .maxLevel(Config.LEVEL_ASSERT) // default, highest log level is a
                .enableModuleFilter(!DEBUG) // if false, all logs are printed; if true, only added module logs are printed
                .addModule(new LogModule(SMTApplication.TAG))
                .writeLogs(true) // display Logger internal logs
                .setFileLevel(10) // create at most 10 log files
                .setFileSize(100000) // create every log file smaller than 100000Byte
                .addOnFileFullListener(new OnFileFullListener() {
                    @Override
                    public void onFileFull(File file) {
                        // called in ui thread when one log file is full, you can upload it to your log server
                    }
                })
                .formatter(new DefaultFormatter()) // log format is the same as adb logcat
                .addLogger(new FileLogger()) // print log to file
                .addLogger(new ConsoleLogger()) // print log to console
                .build();
        LogManager.getInstance().init(config);
    }
}
```
Second, create a util class for log:
```java
public class LogUtil {

    private static volatile boolean writeLogs = true;

    public static void writeLogs(boolean writeLogs) {
        LogUtil.writeLogs = writeLogs;
    }

    private static boolean showLogs(String tag, String msg) {
        return writeLogs && !Util.isEmpty(tag) && !Util.isEmpty(msg);
    }

    public static void v(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().e(tag, msg);
        }
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


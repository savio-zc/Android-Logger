# Android-Logger

## 1 使用说明
1.1 全局配置
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
 
1.2 使用示例
```java
public class LogUtil {
  public static void d(String tag, String msg) {
		LogManager.getInstance().d(tag, msg);
	}
}
```

1.3 注意事项
LogManager.getInstance().init(config)必须在所有其它方法前调用

## 2 设计说明
2.1 系统架构
待补充
 
2.2 一切皆可配置，配置选项随意添加
2.2.1 日志级别：v, d, i, w, e, a（允许在任意两级别间新增级别）
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
2.2.2 全局配置：配置系统级的最大日志级别和最小日志级别，模块级的名称、最大日志级别、最小日志级别，使用什么Formatter，使用什么Logger(可同时使用多个)，日志文件的层级、大小
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
2.2.3 局部配置：要打印的该条日志所在的模块和级别
```java
public class LogOption {
    // Level
	private final int mLevel;
	// Module tag
	private final String mTag;
}
```
2.2.4 通过Builder方式保证配置的兼容性和扩展性
 
2.3 分级控制
系统级：日志级别（最高、高低）
模块级（LogModule）：日志级别（最高、最低），模块名
```java
public class LogModule {
    // Module Level
	private int mMinLevel = Config.LEVEL_VERBOSE;
	private int mMaxLevel = Config.LEVEL_ASSERT;
	private final String mTag;
}
```

2.4 LogMessage（定义日志内容）
时间、进程号、线程号、应用包名、模块名、日志信息、日志级别
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

2.5 Formatter（将日志内容转化为格式化字符串）
```java
public interface Formatter {
    public String format(LogMessage message);
}
```

2.6 Logger（将格式化字符串进行打印处理）
打印目的地：Console、File
对应的Logger实现：ConsoleLogger、FileLogger
```java
public interface Logger {
    /**
	 * This method should return immediately. Make sure it is thread-safe.
	 * @param message
	 * @param local
	 * @param global
	 */
	public void log(LogMessage message, LogOption local, LogManagerConfig global);
}
```

2.7 最终的API接口
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

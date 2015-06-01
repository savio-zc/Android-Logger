package com.zc.logger.log;

import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.LogOption;
import com.zc.logger.model.LogMessage;

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

package com.zc.logger.format;

import com.zc.logger.model.LogMessage;

public interface Formatter {

	public String format(LogMessage message);

}

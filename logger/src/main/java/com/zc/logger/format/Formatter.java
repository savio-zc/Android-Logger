package com.zc.logger.format;

import com.zc.logger.model.LogMessage;

public interface Formatter {

    String SEPARATOR = " ";

    String format(LogMessage message);

}

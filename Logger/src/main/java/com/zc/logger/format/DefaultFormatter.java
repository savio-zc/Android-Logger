package com.zc.logger.format;

import com.zc.logger.model.LogMessage;

public class DefaultFormatter implements Formatter {

    @Override
    public String format(LogMessage message) {
        if (message == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(message.getLevelString()).append(SEPARATOR);
        sb.append(message.getTimeString()).append(SEPARATOR);
        sb.append(message.getPID()).append(SEPARATOR);
        sb.append(message.getTID()).append(SEPARATOR);
        sb.append(message.getApplication()).append(SEPARATOR);
        sb.append(message.getTag()).append(SEPARATOR);
        sb.append(message.getText());
        return sb.toString();
    }

}

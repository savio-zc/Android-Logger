package com.zc.logger.format;

import com.zc.logger.model.LogMessage;

public class DefaultFormatter implements Formatter {

	private static final String SEPERATER = " ";

	@Override
	public String format(LogMessage message) {
		if (message == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(message.getLevelString()).append(SEPERATER);
		sb.append(message.getTimeString()).append(SEPERATER);
		sb.append(message.getPID()).append(SEPERATER);
		sb.append(message.getTID()).append(SEPERATER);
		sb.append(message.getApplication()).append(SEPERATER);
		sb.append(message.getTag()).append(SEPERATER);
		sb.append(message.getText());
		return sb.toString();
	}

}

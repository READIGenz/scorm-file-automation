package com.lh.xray;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class Log {

	/* Get actual class name to be printed on */
	private static Logger log = Logger.getLogger(Log.class.getName());

	// public static void main(String[] args)throws IOException,SQLException{
	// log.debug("Hello this is a debug message");
	// log.info("Hello this is an info message");
	// }

	public static void setLogger() {

		System.setProperty("log-directory", com.genz.config.PropertiesHandler.getLogsFolder());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		System.setProperty("currenttime", dateFormat.format(new Date()));
		info("Log configuration done. Log Dir:" + com.genz.config.PropertiesHandler.getLogsFolder());

	}

	public static void info(String str) {
		log.info(str);
	}

	public static void error(String str) {
		log.warn(str);
	}

	public static void debug(String str) {
		log.debug(str);
	}

	public static void fatal(String str) {
		log.fatal(str);
	}

}

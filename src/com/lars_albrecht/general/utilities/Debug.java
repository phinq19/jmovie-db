/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lalbrecht
 * 
 */
public class Debug {

	final public static Integer											LEVEL_ALL	= 0;
	final public static Integer											LEVEL_TRACE	= 1;
	final public static Integer											LEVEL_DEBUG	= 2;
	final public static Integer											LEVEL_INFO	= 3;
	final public static Integer											LEVEL_WARN	= 4;
	final public static Integer											LEVEL_ERROR	= 5;
	final public static Integer											LEVEL_FATAL	= 6;
	final public static Integer											LEVEL_OFF	= 7;

	public static Integer												loglevel	= Debug.LEVEL_DEBUG;

	private static final ConcurrentHashMap<Integer, ArrayList<String>>	logList		= new ConcurrentHashMap<Integer, ArrayList<String>>();
	public static ConcurrentHashMap<String, Long>						timerMap	= new ConcurrentHashMap<String, Long>();

	public static void log(final Integer level, final String msg) {
		if (level >= Debug.loglevel) {
			ArrayList<String> tempList = null;
			if (Debug.logList.containsKey(level)) {
				tempList = Debug.logList.get(level);
			} else {
				tempList = new ArrayList<String>();
			}
			tempList.add(msg);
			Debug.logList.put(level, tempList);
			String timeStr = null;
			TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
			final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN);
			timeStr = df.format(new Date().getTime());
			final String pre = "->\t" + timeStr + "\tMSG: ";
			if (Debug.loglevel > Debug.LEVEL_ERROR) {
				System.err.println(pre + msg);
			} else {
				System.out.println(pre + msg);
			}
		}
	}

	public static Boolean inDebugLevel(final Integer level) {
		return level >= Debug.loglevel;
	}

	public static ArrayList<String> getLogForCurrentLevel() {
		return Debug.logList.get(Debug.loglevel);
	}

	public static ArrayList<String> getLogForLevel(final Integer level) {
		return Debug.logList.get(level);
	}

	public static void printLogForCurrentLevel() {
		for (final String s : Debug.logList.get(Debug.loglevel)) {
			System.out.println(s);
		}
	}

	public static void printLogForLevel(final Integer level) {
		for (final String s : Debug.logList.get(level)) {
			System.out.println(s);
		}
	}

	public static void startTimer(final String name) {
		Debug.timerMap.put(name + "_start", System.currentTimeMillis());
	}

	public static void stopTimer(final String name) {
		Debug.timerMap.put(name + "_end", System.currentTimeMillis());
	}

	public static String getFormattedTime(final String name) {
		final Long time = Debug.getTime(name);
		if (time != null) {
			TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
			final SimpleDateFormat sdfTime = new SimpleDateFormat();
			sdfTime.applyPattern("HH:mm:ss");
			return sdfTime.format(time);
		} else {
			return null;
		}
	}

	public static ArrayList<String> getFormattedTimes() {
		final ArrayList<String> timerList = new ArrayList<String>();
		for (final Map.Entry<String, Long> entry : Debug.timerMap.entrySet()) {
			if (entry.getKey().endsWith("_start")
					&& Debug.timerMap.containsKey(entry.getKey().substring(0, entry.getKey().indexOf("_start")) + "_end")) {
				timerList.add(Debug.getFormattedTime(entry.getKey().substring(0, entry.getKey().indexOf("_start"))) + " - "
						+ entry.getKey().substring(0, entry.getKey().indexOf("_start")));
			}
		}

		return timerList;
	}

	public static Long getTime(final String name) {
		if (Debug.timerMap.containsKey(name + "_end") && Debug.timerMap.containsKey(name + "_start")) {
			return Debug.timerMap.get(name + "_end") - Debug.timerMap.get(name + "_start");
		}
		return null;
	}

}

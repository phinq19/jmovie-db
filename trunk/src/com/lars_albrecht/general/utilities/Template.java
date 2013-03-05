/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lalbrecht
 * 
 */
public class Template {

	public static Boolean containsMarker(final String content, final String marker) {
		Pattern pattern = null;
		if (marker == null) {
			pattern = Pattern.compile("\\{(.*)\\}");
		} else {
			pattern = Pattern.compile("\\{" + marker + "\\}");
		}
		final Matcher matcher = pattern.matcher(content);
		return matcher.find();
	}

	/**
	 * Returns the first markername.
	 * 
	 * @param content
	 * @return String
	 */
	public static String getNextMarkername(final String content) {
		final Pattern pattern = Pattern.compile("\\{(.*)\\}");
		final Matcher matcher = pattern.matcher(content);
		String name = null;
		if (matcher.find()) {
			name = matcher.group(1);
		}
		return name;
	}

	/**
	 * Replace a marker "markername" in "content" with "replacement".
	 * 
	 * @param content
	 * @param markername
	 * @param replacement
	 * @return String
	 */
	public static String replaceMarker(final String content, final String markername, final String replacement) {
		String inContent = content;
		if (replacement != null) {
			inContent = inContent.replaceFirst("(\\{" + markername + "\\})+", replacement);
		}
		return inContent;
	}

}

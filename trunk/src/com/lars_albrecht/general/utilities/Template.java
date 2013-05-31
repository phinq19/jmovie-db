/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.mdb.Main;

/**
 * @author lalbrecht
 * 
 */
public class Template {

	private String	content	= null;

	public Template() {

	}

	public Template(final String templateName) {
		this.loadTemplateFile(templateName);
	}

	public Template(final String templateName, final ConcurrentHashMap<String, String> markerReplacements) {
		this.loadTemplateFile(templateName);
		if (this.content != null && markerReplacements != null && markerReplacements.size() > 0) {
			for (final Entry<String, String> entry : markerReplacements.entrySet()) {
				this.replaceMarker(entry.getKey(), entry.getValue(), Boolean.FALSE);
			}
		}
	}

	/**
	 * Returns the content for a file. The file must be in /folder/
	 * 
	 * TODO replace this with helper if helper can read from input stream and
	 * filesystem in one function.
	 * 
	 * @param url
	 * @param folder
	 * @return String
	 */
	private String getFileContent(final String url, final String folder) {
		FileFinder.getInstance().addToPathList(new File("web/pages"), -1);
		FileFinder.getInstance().addToPathList(new File("web/pages/partials"), -1);
		FileFinder.getInstance().addToPathList(new File("trunk/web/pages"), -1);
		FileFinder.getInstance().addToPathList(new File("trunk/web/pages/partials"), -1);
		File file = null;
		if (url != null) {
			Debug.log(Debug.LEVEL_INFO, "Try to load file for template: " + url);
			final InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(folder + "/" + url);
			file = new File(url);
			try {
				String content = "";
				if (inputStream != null) {
					content = Helper.getInputStreamContents(inputStream, Charset.forName("UTF-8"));
					return content;
				} else if (file != null && (file = FileFinder.getInstance().findFile(new File(new File(url).getName()), false)) != null
						&& file.exists() && file.isFile() && file.canRead()) {
					content = Helper.getFileContents(file);
					return content;
				} else {
					Debug.log(Debug.LEVEL_ERROR, "InputStream == null && File == null: " + file);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public final void loadTemplateFile(final String templateName) {
		if (templateName != null && !templateName.equalsIgnoreCase("")) {
			this.content = this.getFileContent(templateName + ".page", "web");
			if (this.content == null) {
				this.content = this.getFileContent(templateName + ".partial", "web");
			}
			if (this.content != null) {
				Debug.log(Debug.LEVEL_INFO, "loaded template file: " + templateName);
			} else {
				Debug.log(Debug.LEVEL_ERROR, "Template could not be loaded: " + templateName);
			}
		} else {
			this.content = "no valid template file specified";
		}
	}

	/**
	 * @return the content
	 */
	public final String getContent() {
		return this.content;
	}

	public Boolean containsMarker(final String marker) {
		return Template.containsMarker(this.content, marker);
	}

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

	public String getNextMarkername() {
		return Template.getNextMarkername(this.content);
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

	public void replaceMarker(final String markername, final String replacement, final boolean replaceAll) {
		this.content = Template.replaceMarker(this.content, markername, replacement, replaceAll);
	}

	/**
	 * Replace a marker "markername" in "content" with "replacement".
	 * 
	 * @param content
	 * @param markername
	 * @param replacement
	 * @param replaceAll
	 * @return String
	 */
	public static String replaceMarker(String content, final String markername, final String replacement, final boolean replaceAll) {
		if (content != null && markername != null && replacement != null) {
			if (replaceAll) {
				content = content.replaceAll("(\\{" + markername + "\\})+", replacement);
			} else {
				content = content.replaceFirst("(\\{" + markername + "\\})+", replacement);
			}
		}
		return content;
	}

	public String getSubMarkerContent(final String markername) {
		return Template.getSubMarkerContent(this.content, markername);
	}

	public static String getSubMarkerContent(final String content, final String markername) {
		String markerContent = null;

		final String markerStart = "{" + markername + "-start}";
		final String markerEnd = "{" + markername + "-end}";
		final Pattern pattern = Pattern.compile(Pattern.quote(markerStart) + "(.*)?" + Pattern.quote(markerEnd));
		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			markerContent = matcher.group(1);
		}

		return markerContent;
	}

	public String getClearedContent() {
		return Template.getClearedContent(this.content);
	}

	public static String getClearedContent(String content) {
		content = content.replaceAll("(\\{([a-zA-Z0-9]+?-start)\\})(.*?)(\\{([a-zA-Z0-9]+?-end)\\})", "");
		content = content.replaceAll("(\\{(.*?)\\})", "");

		return content;
	}

}

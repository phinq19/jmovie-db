/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces.web.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.FileFinder;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.Main;
import com.lars_albrecht.mdb.main.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.main.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.main.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.main.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.AllPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.AttributesTagsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.BrowsePage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.DefaultErrorPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.FileDetailsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.HomePage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.LastFivePartial;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.SearchResultsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.SettingsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.ShowInfoControlPage;

/**
 * @author lalbrecht TODO Do better (Each "page" is an own Object/class and all
 *         inherit from one superclass)! Declare html not in class, load a file
 *         and replace marker (use Template class).
 * 
 */
public class WebServerHelper {

	private MainController	mainController			= null;
	private WebInterface	webInterface			= null;

	public final static int	SEARCHTYPE_MIXED		= 0;
	public final static int	SEARCHTYPE_TEXTALL		= 1;
	public final static int	SEARCHTYPE_ATTRIBUTE	= 2;

	public WebServerHelper(final MainController mainController, final WebInterface webInterface) {
		this.mainController = mainController;
		this.webInterface = webInterface;
	}

	/**
	 * Generate content for the given content, filename and parameters.
	 * 
	 * @param content
	 * @param filename
	 * @param request
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	private String
			generateContent(final String content, final String filename, final WebServerRequest request) throws UnsupportedEncodingException {
		String generatedContent = content;
		String contentMarkerReplacement = "";
		String pageTitle = "JMovieDB - Webinterface";
		String subTitle = "";

		if (filename.equalsIgnoreCase("index.html") || filename.equalsIgnoreCase("")) {
			String action = null;
			if (request.getGetParams().containsKey("action")) {
				action = request.getGetParams().get("action");
			} else {
				action = "index";
			}

			try {
				WebPage page = null;
				// TODO do dynamically
				if (action.equalsIgnoreCase("index")) {
					page = new HomePage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showInfoControl")) {
					page = new ShowInfoControlPage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showFileDetails")) {
					page = new FileDetailsPage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showSearchresults")) {
					page = new SearchResultsPage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showSettings")) {
					page = new SettingsPage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showBrowser")) {
					page = new BrowsePage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showAttributesTags")) {
					page = new AttributesTagsPage(action, request, this.mainController, this.webInterface);
				} else if (action.equalsIgnoreCase("showAll")) {
					page = new AllPage(action, request, this.mainController, this.webInterface);
				} else {
					page = new DefaultErrorPage("404", request, this.mainController, this.webInterface);
				}
				contentMarkerReplacement = page.getGeneratedContent();
				subTitle = page.getTitle();
				pageTitle = subTitle + " | " + pageTitle;
			} catch (final Exception e) {
				e.printStackTrace();
			}

			// replace contentmarker with "contentMarkerReplacement" if marker
			// exists.
			if (Template.containsMarker(generatedContent, "content")) {
				generatedContent = Template.replaceMarker(generatedContent, "content", contentMarkerReplacement, Boolean.FALSE);
				Debug.log(Debug.LEVEL_DEBUG, "marker content exist. Replace it");
			} else {
				Debug.log(Debug.LEVEL_ERROR, "marker content DOES NOT exist");
			}

			// replace "free" marker.
			if (Template.containsMarker(content, "searchTerm")) {
				if (request.getGetParams().containsKey("searchStr") && (request.getGetParams().get("searchStr") != null)) {
					generatedContent = Template.replaceMarker(generatedContent, "searchTerm", request.getGetParams().get("searchStr")
							.replaceAll("\"", "&quot;"), Boolean.FALSE);
				} else {
					generatedContent = Template.replaceMarker(generatedContent, "searchTerm", "", Boolean.FALSE);
				}
			}
			if (Template.containsMarker(generatedContent, "lastFiveAdded")) {
				try {
					final LastFivePartial lastFive = new LastFivePartial(null, null, this.mainController);
					generatedContent = Template.replaceMarker(generatedContent, "lastFiveAdded", lastFive.getGeneratedContent(), false);
				} catch (final Exception e) {
					e.printStackTrace();
				}
				// @SuppressWarnings("deprecation")
				// final String listOutput =
				// HTML.generateListOutput(lastFiveList, null, null, false);
				// generatedContent = Template.replaceMarker(generatedContent,
				// "lastFiveAdded", listOutput, Boolean.FALSE);
			}
			if (Template.containsMarker(generatedContent, "title")) {
				generatedContent = Template.replaceMarker(generatedContent, "title", pageTitle, Boolean.FALSE);
			}
			if (Template.containsMarker(generatedContent, "subTitle")) {
				generatedContent = Template.replaceMarker(generatedContent, "subTitle", subTitle, Boolean.FALSE);
			}
		}

		return generatedContent;
	}

	/**
	 * Returns the content of the file from the url. It is like "index.html".
	 * The file must be in /folder/
	 * 
	 * @param url
	 * @param folder
	 * @param request
	 * @return String
	 */
	public String getFileContent(String url, final String folder, final WebServerRequest request) {
		File file = null;
		if (url != null) {
			if (url.equalsIgnoreCase("")) {
				url = "index.html";
			}
			Debug.log(Debug.LEVEL_INFO, "Try to load file for web interface: " + url);
			final InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(folder + "/" + url);
			file = new File(url);
			try {
				String content = "";
				if (inputStream != null) {
					content = Helper.getInputStreamContents(inputStream, Charset.forName("UTF-8"));
					return content;
				} else if (file != null && (file = FileFinder.getInstance().findFile(new File(new File(url).getName()), false)) != null
						&& file.exists() && file.isFile() && file.canRead()) {
					content = this.generateContent(Helper.getFileContents(file), file.getName(), request);
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

	/**
	 * Returns the content of the file from the url. It is like "index.html".
	 * The file must be in /web/
	 * 
	 * @param url
	 * @param request
	 * @return String
	 */
	public String getAjaxContent(final String url, final WebServerRequest request, final boolean isJSON) {
		String content = null;
		if (url != null) {
			content = "";
			if (request.getGetParams() != null && request.getGetParams().size() > 0 && request.getGetParams().containsKey("action")
					&& request.getGetParams().get("action") != null) {
				final String action = request.getGetParams().get("action");
				if (action.equalsIgnoreCase("getStatus")) {
					if (this.mainController.getfController().getThreadList().size() > 0) {
						content += "<p>Finder läuft</p>";
					}
					if (this.mainController.getcController().getThreadList().size() > 0) {
						final String[] collectorNameList = new String[this.mainController.getcController().getThreadList().size()];

						int i = 0;
						for (final ThreadEx t : this.mainController.getcController().getThreadList()) {
							if (t.getInfo() != null && t.getInfo().length > 0 && t.getInfo()[0].equals("Collector")) {
								collectorNameList[i] = t.getName();
							}
							i++;
						}

						content += "<p>Collector läuft:" + "<ul>" + Helper.implode(collectorNameList, null, "<li>", "</li>") + "</ul>"
								+ "</p>";
					}

					if (content.equalsIgnoreCase("")) {
						content = "<p>Keine Aktivitäten</p>";
					}
				} else if (action.equalsIgnoreCase("autocompleteSearch") && request.getGetParams().get("term") != null) {
					if (request.getGetParams().get("term").contains("=")) {
						ArrayList<String> keyList = null;
						final String searchKey = request.getGetParams().get("term")
								.substring(0, request.getGetParams().get("term").indexOf("="));
						final String searchValue = request.getGetParams().get("term")
								.substring(request.getGetParams().get("term").indexOf("=") + 1);
						if (searchValue != null && !searchValue.equalsIgnoreCase("")) {
							keyList = this.mainController.getDataHandler().findAllValuesForKeyWithValuePart(searchKey, searchValue);
						} else {
							keyList = this.mainController.getDataHandler().findAllValuesForKey(searchKey);
						}
						final ArrayList<String> newKeyList = new ArrayList<String>();
						for (final String string : keyList) {
							if (string != null) {
								newKeyList.add(searchKey + "=" + string);
								System.out.println(searchKey + " - " + string);
							}
						}
						// TODO only show real value, but set with (example)
						// "type="
						content = ObjectHandler.stringListToJSON(newKeyList);
					} else {
						content = ObjectHandler.fileItemListToJSON(this.mainController.getDataHandler().findAllFileItemForStringInAll(
								request.getGetParams().get("term")));
					}
					if (content == null) {
						content = "";
					}
				}
			}
		}

		return content;
	}
}

/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.FileFinder;
import com.lars_albrecht.general.utilities.HTML;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.Main;
import com.lars_albrecht.mdb.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.core.interfaces.web.pages.FileDetailsPage;
import com.lars_albrecht.mdb.core.interfaces.web.pages.HomePage;
import com.lars_albrecht.mdb.core.interfaces.web.pages.SearchResultsPage;
import com.lars_albrecht.mdb.core.interfaces.web.pages.ShowInfoControlPage;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht TODO Do better (Each "page" is an own Object/class and all
 *         inherit from one superclass)! Declare html not in class, load a file
 *         and replace marker (use Template class).
 * 
 */
public class WebServerHelper {

	private MainController	mainController			= null;

	public final static int	SEARCHTYPE_MIXED		= 0;
	public final static int	SEARCHTYPE_TEXTALL		= 1;
	public final static int	SEARCHTYPE_ATTRIBUTE	= 2;

	public WebServerHelper(final MainController mainController) {
		this.mainController = mainController;
	}

	private String getPagenameForActionname(final String actionname) {
		String pagename = null;
		if (actionname.equalsIgnoreCase("") || actionname.equalsIgnoreCase("index")) {
			pagename = "home";
		} else if (actionname.equalsIgnoreCase("showBrowser")) {
			pagename = "browser";
		} else if (actionname.equalsIgnoreCase("showSettings")) {
			pagename = "settings";
		} else if (actionname.equalsIgnoreCase("showAttributes")) {
			pagename = "attributes";
		} else if (actionname.equalsIgnoreCase("showInfoControl")) {
			pagename = "infocontrol";
		} else if (actionname.equalsIgnoreCase("showSearchresults")) {
			pagename = "searchresults";
		} else if (actionname.equalsIgnoreCase("showFileDetails")) {
			pagename = "filedetails";
		} else {
			pagename = "404";
		}

		return pagename;
	}

	/**
	 * Generate content for the given content, filename and parameters.
	 * 
	 * @param content
	 * @param filename
	 * @param GETParams
	 * @param headerKeyValue
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	private String generateContent(final String content,
			final String filename,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) throws UnsupportedEncodingException {
		String generatedContent = content;
		String contentMarkerReplacement = "";
		// System.out.println("Params: " + GETParams);
		String pageTitle = "JMovieDB - Webinterface";
		String subTitle = "";

		if (filename.equalsIgnoreCase("index.html") || filename.equalsIgnoreCase("")) {
			String action = null;
			if (GETParams.containsKey("action")) {
				action = GETParams.get("action");
			} else {
				action = "index";
			}

			// currently unused
			final ConcurrentHashMap<String, String> contentMarkerReplacements = null;

			try {
				WebPage page = null;
				if (action.equalsIgnoreCase("index")) {
					page = new HomePage(action, GETParams, this.mainController);
				} else if (action.equalsIgnoreCase("showInfoControl")) {
					page = new ShowInfoControlPage(action, GETParams, this.mainController);
				} else if (action.equalsIgnoreCase("showFileDetails")) {
					page = new FileDetailsPage(action, GETParams, this.mainController);
				} else if (action.equalsIgnoreCase("showSearchresults")) {
					page = new SearchResultsPage(action, GETParams, this.mainController);
				}

				contentMarkerReplacement = page.getGeneratedContent();
				subTitle = page.getTitle();
				pageTitle += " | " + subTitle;
			} catch (final Exception e) {
				e.printStackTrace();
			}

			// current fallback
			if (contentMarkerReplacement.equals("")) {
				final String pagename = this.getPagenameForActionname(action);
				if (action.equalsIgnoreCase("showAttributes") || action.equalsIgnoreCase("showBrowser")
						|| action.equalsIgnoreCase("showSettings")) {
					contentMarkerReplacement = new Template(pagename, contentMarkerReplacements).getContent();
				}
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
				if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
					try {
						generatedContent = Template.replaceMarker(generatedContent, "searchTerm",
								URLDecoder.decode(GETParams.get("searchStr"), "utf-8"), Boolean.FALSE);
					} catch (final UnsupportedEncodingException e) {
						generatedContent = e.getMessage();
					}
				} else {
					generatedContent = Template.replaceMarker(generatedContent, "searchTerm", "", Boolean.FALSE);
				}
			}
			if (Template.containsMarker(generatedContent, "lastFiveAdded")) {
				final ArrayList<FileItem> lastFiveList = ObjectHandler.castObjectListToFileItemList(this.mainController.getDataHandler()
						.findAll(new FileItem(), 5));
				@SuppressWarnings("deprecation")
				final String listOutput = HTML.generateListOutput(lastFiveList, null, null, false);
				generatedContent = Template.replaceMarker(generatedContent, "lastFiveAdded", listOutput, Boolean.FALSE);
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
	 * @param GETParams
	 * @param headerKeyValue
	 * @return String
	 */
	public String getFileContent(String url,
			final String folder,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) {
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
					content = this.generateContent(Helper.getFileContents(file), file.getName(), GETParams, headerKeyValue);
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
	 * @param GETParams
	 * @param headerKeyValue
	 * @return String
	 */
	public String getAjaxContent(final String url,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue,
			final boolean isJSON) {
		String content = null;
		if (url != null) {
			content = "";
			if (GETParams != null && GETParams.size() > 0 && GETParams.containsKey("action") && GETParams.get("action") != null) {
				final String action = GETParams.get("action");
				if (action.equalsIgnoreCase("getStatus")) {
					if (this.mainController.getfController().getThreadList().size() > 0) {
						content += "<p>Finder is running</p>";
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

						content += "<p>Collector is running:" + "<ul>" + Helper.implode(collectorNameList, null, "<li>", "</li>") + "</ul>"
								+ "</p>";
					}

					if (content.equalsIgnoreCase("")) {
						content = "<p>No activities</p>";
					}
				} else if (action.equalsIgnoreCase("autocomplete") && GETParams.get("term") != null) {
					if (GETParams.get("term").contains("=")) {
						final String searchKey = GETParams.get("term").substring(0, GETParams.get("term").indexOf("="));
						final String searchValue = GETParams.get("term").substring(GETParams.get("term").indexOf("=") + 1);
						if(searchValue != null && !searchValue.equalsIgnoreCase(""){
							final ArrayList<String> keyList = this.mainController.getDataHandler().findAllValuesForKeyWithValuePart(searchKey, searchValue);
						} else {
							final ArrayList<String> keyList = this.mainController.getDataHandler().findAllValuesForKey(searchKey);
						}
						final ArrayList<String> newKeyList = new ArrayList<String>();
						for (final String string : keyList) {
							newKeyList.add(searchKey + "=" + string);
						}
						// TODO only show real value, but set with "type="
						content = ObjectHandler.stringListToJSON(newKeyList);
					} else {
						content = ObjectHandler.fileItemListToJSON(ObjectHandler.castObjectListToFileItemList(this.mainController
								.getDataHandler().findAllFileItemForStringInAll(GETParams.get("term"))));
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

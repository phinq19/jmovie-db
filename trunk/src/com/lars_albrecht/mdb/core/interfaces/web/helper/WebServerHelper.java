/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.FileFinder;
import com.lars_albrecht.general.utilities.HTML;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.Main;
import com.lars_albrecht.mdb.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.DataHandler;
import com.lars_albrecht.mdb.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.KeyValue;

/**
 * @author lalbrecht TODO Do better (Each "page" is an own Object? and all
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
		} else if (actionname.equalsIgnoreCase("showAttrtibutes")) {
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

	private String getTitleForActionname(final String actionname, final ConcurrentHashMap<String, String> GETParams) {
		String title = null;
		if (actionname.equalsIgnoreCase("") || actionname.equalsIgnoreCase("index")) {
			title = "";
		} else if (actionname.equalsIgnoreCase("showBrowser")) {
			title = "Browser";
		} else if (actionname.equalsIgnoreCase("showSettings")) {
			title = "Settings";
		} else if (actionname.equalsIgnoreCase("showAttrtibutes")) {
			title = "Attributes";
		} else if (actionname.equalsIgnoreCase("showInfoControl")) {
			title = "Info / Control";
		} else if (actionname.equalsIgnoreCase("showSearchresults")) {
			title = "Searchresults";
		} else if (actionname.equalsIgnoreCase("showFileDetails")) {
			if (GETParams.containsKey("fileId") && (GETParams.get("fileId") != null)) {
				final Integer fileId = Integer.parseInt(GETParams.get("fileId"));
				if ((fileId != null) && (fileId > 0)) {
					final FileItem tempFileItem = this.mainController.getDataHandler().findAllInfoForAllByFileId(fileId);
					if (tempFileItem != null) {
						title = "Detailansicht: " + tempFileItem.getName();
					} else {
						title = "Detailansicht: Keine gültige Datei gewählt";
					}
				} else {
					title = "Detailansicht: Keine gültige Datei gewählt";
				}
			} else {
				title = "Detailansicht: Keine Datei gewählt";
			}
		} else {
			title = "ERROR 404";
		}

		return title;
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
	public String generateContent(final String content,
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
			final String pagename = this.getPagenameForActionname(action);
			subTitle = this.getTitleForActionname(action, GETParams);
			Debug.log(Debug.LEVEL_DEBUG, "TITLE (subTitle): " + subTitle);
			pageTitle += " | " + subTitle;
			final ConcurrentHashMap<String, String> contentMarkerReplacements = null;

			if (action.equalsIgnoreCase("index") || action.equalsIgnoreCase("showAttributes") || action.equalsIgnoreCase("showBrowser")
					|| action.equalsIgnoreCase("showSettings")) {
				contentMarkerReplacement = new Template(pagename, contentMarkerReplacements).getContent();
			} else if (action.equalsIgnoreCase("showInfoControl")) {
				contentMarkerReplacement = this.generateInfoControlView(GETParams);
				pageTitle += " | " + subTitle;

			} else if (action.equalsIgnoreCase("showSearchresults")) {
				contentMarkerReplacement = this.generateSearchresults(GETParams);
				subTitle = this.getTitleForSearchresults(GETParams);

			} else if (action.equalsIgnoreCase("showFileDetails")) {
				contentMarkerReplacement = "showFileDetails";
				if (GETParams.containsKey("fileId") && (GETParams.get("fileId") != null)) {
					final Integer fileId = Integer.parseInt(GETParams.get("fileId"));

					if ((fileId != null) && (fileId > 0)) {
						final FileItem tempFileItem = this.mainController.getDataHandler().findAllInfoForAllByFileId(fileId);
						contentMarkerReplacement = this.generateDetailView(tempFileItem);
						subTitle = this.getTitleForDetailview(tempFileItem);
						pageTitle += " | " + subTitle;
					}

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

	public String generateDetailView(final FileItem item) {
		final Template detailViewTemplate = new Template("filedetails");

		// if file is set
		if ((item != null) && (item.getId() != null)) {
			// set default infos
			detailViewTemplate.replaceMarker("content", detailViewTemplate.getSubMarkerContent("file"), Boolean.FALSE);

			detailViewTemplate.replaceMarker("title", item.getName() + " (" + item.getId() + ")", Boolean.TRUE);
			detailViewTemplate.replaceMarker("path", item.getFullpath().replaceAll("\\\\", "\\\\\\\\"), Boolean.TRUE);

			if (item.getSize() != null) {
				String listWrapper = null;
				listWrapper = detailViewTemplate.getSubMarkerContent("listwrapper");
				listWrapper = Template.replaceMarker(listWrapper, "key", "Dir", Boolean.TRUE);
				listWrapper = Template.replaceMarker(listWrapper, "value", Helper.getHumanreadableFileSize(item.getSize()), Boolean.TRUE);

				detailViewTemplate.replaceMarker("listwrapperSize", listWrapper, Boolean.TRUE);
			}

			if (item.getSize() != null) {
				String listWrapper = null;
				listWrapper = detailViewTemplate.getSubMarkerContent("listwrapper");
				listWrapper = Template.replaceMarker(listWrapper, "key", "Added", Boolean.TRUE);
				listWrapper = Template.replaceMarker(listWrapper, "value",
						Helper.getFormattedTimestamp(item.getCreateTS().longValue(), null), Boolean.TRUE);

				detailViewTemplate.replaceMarker("listwrapperAdded", listWrapper, Boolean.TRUE);
			}

			// if file has attributes
			if ((item.getAttributes() != null) && (item.getAttributes().size() > 0)) {
				// get marker for attributes
				String attributes = detailViewTemplate.getSubMarkerContent("attributes");

				String currentInfoType = null;
				int i = 0;

				String attributesList = "";
				String sectionList = "";
				String attributeSectionList = "";
				// for each attribute ...
				for (final FileAttributeList attributeList : item.getAttributes()) {
					if ((currentInfoType == null)
							|| !currentInfoType.equalsIgnoreCase(attributeList.getKeyValues().get(0).getKey().getInfoType())) {
						if (i > 0) {
							// finish section and add to list
							attributeSectionList += Template.replaceMarker(attributesList, "sections", sectionList, Boolean.TRUE);
							attributesList = "";
							sectionList = "";
						}

						// create a new one for each infoType
						currentInfoType = attributeList.getKeyValues().get(0).getKey().getInfoType();
						attributesList = detailViewTemplate.getSubMarkerContent("attributesList");
						attributesList = Template.replaceMarker(attributesList, "title", currentInfoType, Boolean.FALSE);
						attributesList = Template.replaceMarker(attributesList, "id", currentInfoType, Boolean.FALSE);
					}

					// fill sectionlist
					if ((attributeList.getKeyValues() != null) && (attributeList.getKeyValues().size() > 0)) {
						sectionList += detailViewTemplate.getSubMarkerContent("attributeListSection");
						sectionList = Template.replaceMarker(sectionList, "sectionname", attributeList.getSectionName(), Boolean.TRUE);
						sectionList = Template.replaceMarker(sectionList, "keyTitle", "Key", Boolean.TRUE);
						sectionList = Template.replaceMarker(sectionList, "valueTitle", "Value", Boolean.TRUE);

						FileAttributeList attributeListCpy = null;
						try {
							attributeListCpy = (FileAttributeList) attributeList.clone();

							int evenOdd = 0;
							String rows = "";
							// fill rows
							for (final KeyValue<String, Object> keyValue : attributeList.getKeyValues()) {
								if (attributeListCpy.getKeyValues().contains(keyValue)) {
									rows += detailViewTemplate.getSubMarkerContent("attributesListSectionItem");
									rows = Template.replaceMarker(rows, "oddeven", ((evenOdd % 2) == 0 ? "even" : "odd"), Boolean.TRUE);
									rows = Template.replaceMarker(rows, "key", keyValue.getKey().getKey(), Boolean.TRUE);

									String value = "";
									final ArrayList<Object> tempList = this.getValuesForKey(attributeListCpy, keyValue.getKey().getKey());
									for (int j = 0; j < tempList.size(); j++) {
										if (j != 0) {
											value += ", ";
										}
										if (keyValue.getKey().getSearchable()) {
											value += "<a href=\"?"
													+ "action=showSearchresults&searchStr="
													+ URLEncoder.encode(keyValue.getKey().getKey() + "=" + "\"" + tempList.get(j) + "\"",
															"utf-8") + "\">" + tempList.get(j) + "</a>";
										} else {
											value += tempList.get(j);
										}
									}

									rows = Template.replaceMarker(rows, "value", value, Boolean.TRUE);
									attributeListCpy = this.removeKeysFromFileAttributeList(attributeListCpy, keyValue.getKey().getKey());
									evenOdd++;
								}
							}
							// replace row marker with real rows
							sectionList = Template.replaceMarker(sectionList, "rows", rows, Boolean.TRUE);
						} catch (final CloneNotSupportedException e) {
							e.printStackTrace();
						} catch (final UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					i++;
				}
				// add last attribute sections
				attributeSectionList += Template.replaceMarker(attributesList, "sections", sectionList, Boolean.TRUE);

				// add all attribute sections to attributes
				attributes = Template.replaceMarker(attributes, "attributesList", attributeSectionList, Boolean.TRUE);

				// add all attributes to template
				detailViewTemplate.replaceMarker("attributes", attributes, Boolean.TRUE);
			}
		} else {
			detailViewTemplate.replaceMarker("content", detailViewTemplate.getSubMarkerContent("nofile"), Boolean.TRUE);
			detailViewTemplate.replaceMarker("nofileString", "Keine Datei ausgewählt", Boolean.TRUE);
		}

		return detailViewTemplate.getClearedContent();
	}

	public String generateInfoControlView(final ConcurrentHashMap<String, String> GETParams) {
		String resultStr = "<div id=\"infoView\" class=\"contentPart\">";
		this.mainController.getDataHandler();
		this.mainController.getDataHandler().reloadData(DataHandler.RELOAD_ALL);
		final ConcurrentHashMap<String, Object> info = this.mainController.getDataHandler().getInfoFromDatabase();
		if (info != null) {
			resultStr += "<h2>Informationen</h2>";
			resultStr += "<h3 class=\"tableHeader\">Anzahl Einträge</h3>";
			resultStr += "<table>";
			resultStr += "<tr>";
			resultStr += "<th>Typ</th>";
			resultStr += "<th>Anzahl</th>";
			resultStr += "</tr>";
			resultStr += "<tr>";
			resultStr += "<td>File Count</td>";
			resultStr += "<td>" + info.get("fileCount") + "</td>";
			resultStr += "</tr>";
			resultStr += "<tr>";
			resultStr += "<td>Key Count</td>";
			resultStr += "<td>" + info.get("keyCount") + "</td>";
			resultStr += "</tr>";
			resultStr += "<tr>";
			resultStr += "<td>Value Count</td>";
			resultStr += "<td>" + info.get("valueCount") + "</td>";
			resultStr += "</tr>";
			resultStr += "<tr>";
			resultStr += "<td>Filetypes (count of files)</td>";
			resultStr += "<td>"
					+ Helper.implode((Map<?, ?>) info.get("filesWithFiletype"), ", ", null, null, " (", ")",
							"<span class=\"infoListEntry\">", "</span>", false) + "</td>";
			resultStr += "</tr>";
			resultStr += "</table>";
		} else {
			resultStr += "<p>Ein Fehler ist aufgetreten. Konnte keine Informationen sammeln.</p>";
		}
		resultStr += "</div>";

		resultStr += "<div id=\"controlView\" class=\"contentPart\">";
		resultStr += "<h3>Control</h3>";
		boolean isStartFinder = false;
		if (GETParams.containsKey("do") && (GETParams.get("do") != null) && GETParams.get("do").equalsIgnoreCase("startFinder")) {
			isStartFinder = true;
			resultStr += "<div id=\"statusArea\">";
			resultStr += "<p>" + "Files will be refreshed ..." + "</p>";
			resultStr += "</div>";
			this.mainController.startSearch();
		}

		boolean isStartCollectors = false;
		if (GETParams.containsKey("do") && (GETParams.get("do") != null) && GETParams.get("do").equalsIgnoreCase("startCollectors")) {
			isStartCollectors = true;
			resultStr += "<div id=\"statusArea\">";

			resultStr += "<p>" + "Collections will be refreshed ..." + "<br />";
			final ArrayList<FileItem> fileList = ObjectHandler.castObjectListToFileItemList(this.mainController.getDataHandler().findAll(
					new FileItem(), null));
			if ((fileList != null) && (fileList.size() > 0)) {
				resultStr += "Collections can be refreshed ... work in progress" + "</p>";
				this.mainController.getcController().collectInfos(fileList);
			} else {
				resultStr += "Collections cannot be refreshed" + (fileList.size() == 0 ? ", because no files are available" : "")
						+ ". Process stopped." + "</p>";
			}

			resultStr += "</div>";
		}
		resultStr += "<nav><ul>";
		resultStr += "<li><a href=\""
				+ (isStartFinder ? "javascript:void(0)\" class=\"disabled" : "?action=showInfoControl&do=startFinder") + "\">"
				+ "Start Finder" + "</a></li>";
		resultStr += "<li><a href=\""
				+ (isStartCollectors ? "javascript:void(0)\" class=\"disabled" : "?action=showInfoControl&do=startCollectors") + "\">"
				+ "Start Collectors" + "</a></li>";
		resultStr += "</ul></nav>";
		resultStr += "</div>";
		return resultStr;
	}

	/**
	 * Search with "=" for attributes must check if the search string is end
	 * after " " or if it is surrounded by """.
	 * 
	 * @param GETParams
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String generateSearchresults(final ConcurrentHashMap<String, String> GETParams) throws UnsupportedEncodingException {
		String resultStr = "<div id=\"searchresultsView\" class=\"contentPart\">";
		if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
			// get DATA for output
			final String searchStr = (URLDecoder.decode(GETParams.get("searchStr"), "utf-8")).replaceAll("[\"]", "");
			int searchType = WebServerHelper.SEARCHTYPE_TEXTALL;

			String searchKey = null;
			String searchValue = null;
			String[] searchStrList = null;
			final ArrayList<FileItem> foundList = new ArrayList<FileItem>();
			if (searchStr.contains(" ")) {
				searchStrList = searchStr.split(" ");
			} else {
				searchStrList = new String[] {
					searchStr
				};
			}

			for (final String searchStrItem : searchStrList) {
				searchType = WebServerHelper.SEARCHTYPE_TEXTALL;
				if (searchStrItem.contains("=")) {
					final String[] searchArr = searchStrItem.split("=");
					if (searchArr.length == 2) {
						searchKey = searchArr[0];
						searchValue = searchArr[1];
						if (this.mainController.getDataHandler().isKeyInKeyList(searchKey)) {
							searchType = WebServerHelper.SEARCHTYPE_ATTRIBUTE;
						}

					}
				}

				switch (searchType) {
					default:
					case SEARCHTYPE_TEXTALL:
						foundList.addAll(ObjectHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAll(searchStrItem))));
						break;
					case SEARCHTYPE_ATTRIBUTE:
						foundList.addAll(ObjectHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAttributesByKeyValue(searchKey, searchValue))));
						break;
				}
			}
			resultStr += HTML.generateListOutput(Helper.uniqueList(foundList), searchStrList,
					searchStrList.length > 1 ? WebServerHelper.SEARCHTYPE_MIXED : searchType, true);
		} else {
			resultStr += "<p>Suchen Sie mit hilfe der Suche</p>";
		}

		resultStr += "</div>";
		return resultStr;
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
					content = ObjectHandler.fileItemListToJSON(ObjectHandler.castObjectListToFileItemList(this.mainController
							.getDataHandler().findAllFileItemForStringInAll(GETParams.get("term"))));
					if (content == null) {
						content = "";
					}
				}
			}
		}

		return content;
	}

	public String getTitleForAttributesView() {
		return "Attributes";
	}

	public String getTitleForBrowserView() {
		return "Browser";
	}

	public String getTitleForSettingsView() {
		return "Settings";
	}

	public String getTitleForDetailview(final FileItem fileItem) {
		String titleStr = "Kein Titel gewählt";
		if (fileItem != null) {
			titleStr = fileItem.getName();
		}
		return "Detailansicht: " + titleStr;
	}

	public String getTitleForSearchresults(final ConcurrentHashMap<String, String> GETParams) {
		String searchStr = "";
		if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
			searchStr = GETParams.get("searchStr");
		}
		return "Suchergebnisse für: " + searchStr;
	}

	private ArrayList<Object> getValuesForKey(final FileAttributeList list, final String key) {
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if ((list != null) && (list.getKeyValues().size() > 0) && (key != null)) {
			for (final KeyValue<String, Object> keyValue : list.getKeyValues()) {
				if ((keyValue != null) && (keyValue.getKey() != null) && keyValue.getKey().getKey().equals(key)
						&& (keyValue.getValue() != null)) {
					resultList.add(keyValue.getValue().getValue());
				}
			}
		}
		return resultList;
	}

	private FileAttributeList
			removeKeysFromFileAttributeList(final FileAttributeList list, final String key) throws CloneNotSupportedException {
		final FileAttributeList resultList = (FileAttributeList) list.clone();
		if ((list != null) && (list.getKeyValues().size() > 0) && (key != null)) {
			for (final KeyValue<String, Object> keyValue : list.getKeyValues()) {
				if ((keyValue != null) && (keyValue.getKey() != null) && keyValue.getKey().getKey().equals(key)
						&& (keyValue.getValue() != null)) {
					resultList.getKeyValues().remove(keyValue);
				}
			}
		}
		return resultList;
	}

}

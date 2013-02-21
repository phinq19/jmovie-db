/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.helper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.HTML;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.TypeHandler;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.KeyValue;

/**
 * @author lalbrecht
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

	public String generateAttributesView(final ConcurrentHashMap<String, String> GETParams) {
		String resultStr = "<div id=\"attributesView\" class=\"contentPart\">";
		resultStr += "<p>Diese Anzeige ist aktuell nicht verfügbar.</p>";
		resultStr += "</div>";
		return resultStr;
	}

	/**
	 * Generate content for the given content, filename and parameters.
	 * 
	 * @param content
	 * @param filename
	 * @param GETParams
	 * @param headerKeyValue
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String generateContent(final String content,
			final String filename,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) throws UnsupportedEncodingException {
		String generatedContent = content;
		String contentMarkerReplacement = "";
		// System.out.println("Params: " + GETParams);
		String pageTitle = "MDB";
		if (filename.equalsIgnoreCase("index.html")) {
			String action = null;
			if (GETParams.containsKey("action")) {
				action = GETParams.get("action");
			} else {
				action = "index";
			}

			if (action.equalsIgnoreCase("index")) {
				contentMarkerReplacement = "Auf dieser Seite kann man vorhandene Filme suchen und sich verschiedene Informationen anzeigen lassen.";
			} else if (action.equalsIgnoreCase("showSearchresults")) {
				contentMarkerReplacement = this.generateSearchresults(GETParams);
				pageTitle += " | " + this.getTitleForSearchresults(GETParams);
			} else if (action.equalsIgnoreCase("showFileDetails")) {
				contentMarkerReplacement = "showFileDetails";
				if (GETParams.containsKey("fileId") && (GETParams.get("fileId") != null)) {
					final Integer fileId = Integer.parseInt(GETParams.get("fileId"));

					if ((fileId != null) && (fileId > 0)) {
						final FileItem tempFileItem = this.mainController.getDataHandler().findAllInfoForAllByFileId(fileId);
						contentMarkerReplacement = this.generateDetailView(tempFileItem);
						pageTitle += " | " + this.getTitleForDetailview(tempFileItem);
					}

				}
			} else if (action.equalsIgnoreCase("showInfoControl")) {
				contentMarkerReplacement = this.generateInfoControlView(GETParams);
				pageTitle += " | " + this.getTitleForInfoview();
			} else if (action.equalsIgnoreCase("showAttributes")) {
				contentMarkerReplacement = this.generateAttributesView(GETParams);
				pageTitle += " | " + this.getTitleForAttributesView();
			}

			// replace contentmarker with "contentMarkerReplacement" if marker
			// exists.
			if (Template.containsMarker(generatedContent, "content")) {
				generatedContent = Template.replaceMarker(generatedContent, "content", contentMarkerReplacement);
			}

			// replace "free" marker.
			if (Template.containsMarker(content, "searchTerm")) {
				if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
					try {
						generatedContent = Template.replaceMarker(generatedContent, "searchTerm",
								URLDecoder.decode(GETParams.get("searchStr"), "utf-8"));
					} catch (final UnsupportedEncodingException e) {
						generatedContent = e.getMessage();
					}
				} else {
					generatedContent = Template.replaceMarker(generatedContent, "searchTerm", "");
				}
			}
			if (Template.containsMarker(generatedContent, "lastFiveAdded")) {
				final ArrayList<FileItem> lastFiveList = TypeHandler.castObjectListToFileItemList(this.mainController.getDataHandler()
						.findAll(new FileItem(), false, 5));
				final String listOutput = HTML.generateListOutput(lastFiveList, null, null, false);
				generatedContent = Template.replaceMarker(generatedContent, "lastFiveAdded", listOutput);
			}
			if (Template.containsMarker(generatedContent, "title")) {
				generatedContent = Template.replaceMarker(generatedContent, "title", pageTitle);
			}
		}

		return generatedContent;
	}

	public String generateDetailView(final FileItem item) {
		String resultStr = "<div id=\"detailView\" class=\"contentPart\">";
		if ((item != null) && (item.getId() != null)) {
			resultStr += "<h2>" + item.getName() + " (" + item.getId() + ")" + "</h2>";
			resultStr += "<div class=\"path\">" + item.getFullpath().replaceAll("\\\\", "\\\\\\\\") + "</div>";
			resultStr += "<div class=\"listWrapper\"><div class=\"key\">Dir</div><div class=\"value\">"
					+ item.getDir().replaceAll("\\\\", "\\\\\\\\") + "</div></div>";

			if (item.getSize() != null) {
				resultStr += "<div class=\"listWrapper\"><div class=\"key\">Size</div><div class=\"value\">"
						+ Helper.getHumanreadableFileSize(item.getSize()) + "</div></div>";
			}
			if (item.getCreateTS() != null) {
				resultStr += "<div class=\"listWrapper\"><div class=\"key\">Added</div><div class=\"value\">"
						+ Helper.getFormattedTimestamp(item.getCreateTS().longValue(), null) + "</div></div>";
			}

			if ((item.getAttributes() != null) && (item.getAttributes().size() > 0)) {
				resultStr += "<hr />";
				resultStr += "<div id=\"attributes\">";

				resultStr += "<nav><ul><li><a href=\"#MediaInfo\">MediaInfo</a></li><li><a href=\"#themoviedb\">The Movie DB</a></li></ul></nav>";

				resultStr += "<h3>Attributes</h3>";
				String currentInfoType = null;
				int i = 0;
				for (final FileAttributeList attributeList : item.getAttributes()) {
					if ((currentInfoType == null)
							|| !currentInfoType.equalsIgnoreCase(attributeList.getKeyValues().get(0).getKey().getInfoType())) {
						currentInfoType = attributeList.getKeyValues().get(0).getKey().getInfoType();
						if (i > 0) {
							resultStr += "</div>";
						}
						resultStr += "<div class=\"infoSection\">";
						resultStr += "<h4>" + currentInfoType + "</h4>" + "<a name=\"" + currentInfoType + "\"></a>";
					}
					if ((attributeList.getKeyValues() != null) && (attributeList.getKeyValues().size() > 0)) {
						resultStr += "<div class=\"sectionSection\">";
						resultStr += "<h5 class=\"tableHeader\">" + attributeList.getSectionName() + "</h5>";
						resultStr += "<table>";
						resultStr += "<tr>";
						resultStr += "<th>" + "Key" + "</th>";
						resultStr += "<th>" + "Value" + "</th>";
						resultStr += "</tr>";
						// copy to reduce the count of loops in search for
						// values
						FileAttributeList attributeListCpy = null;
						try {
							attributeListCpy = (FileAttributeList) attributeList.clone();

							int evenOdd = 0;
							for (final KeyValue<String, Object> keyValue : attributeList.getKeyValues()) {
								if (attributeListCpy.getKeyValues().contains(keyValue)) {
									resultStr += "<tr class=\"" + ((evenOdd % 2) == 0 ? "even" : "odd") + "\">";
									resultStr += "<td>" + keyValue.getKey().getKey() + "</td>";
									if (!keyValue.getKey().getSearchable()
											|| this.getValuesForKey(attributeListCpy, keyValue.getKey().getKey()).size() > 1) {
										resultStr += "<td>"
												+ Helper.implode(this.getValuesForKey(attributeListCpy, keyValue.getKey().getKey()), ", ",
														null, null) + "</td>";
									} else {
										resultStr += "<td>"
												+ "<a href=\"?"
												+ "action=showSearchresults&searchStr="
												+ URLEncoder.encode(keyValue.getKey().getKey() + "=" + keyValue.getValue().getValue(),
														"utf-8") + "\">" + keyValue.getValue().getValue() + "</a>" + "</td>";
									}
									resultStr += "</tr>";
									attributeListCpy = this.removeKeysFromFileAttributeList(attributeListCpy, keyValue.getKey().getKey());
									evenOdd++;
								}
							}
						} catch (final CloneNotSupportedException e) {
							e.printStackTrace();
						} catch (final UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						resultStr += "</table>";
						resultStr += "</div>";
					}
					i++;
				}
				resultStr += "</div>";
			}

		} else {
			resultStr += "<p>Nichts gefunden</p>";
		}
		resultStr += "</div>";
		return resultStr;
	}

	public String generateInfoControlView(final ConcurrentHashMap<String, String> GETParams) {
		String resultStr = "<div id=\"infoView\" class=\"contentPart\">";
		this.mainController.getDataHandler().reloadData();
		final ConcurrentHashMap<String, Integer> info = this.mainController.getDataHandler().getInfoFromDatabase();
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
			final ArrayList<FileItem> fileList = TypeHandler.castObjectListToFileItemList(this.mainController.getDataHandler().findAll(
					new FileItem(), false, null));
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

	public String generateSearchresults(final ConcurrentHashMap<String, String> GETParams) throws UnsupportedEncodingException {
		String resultStr = "<div id=\"searchresultsView\" class=\"contentPart\">";
		if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
			// get DATA for output
			final String searchStr = URLDecoder.decode(GETParams.get("searchStr"), "utf-8");
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
						foundList.addAll(TypeHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAll(searchStrItem, false))));
						break;
					case SEARCHTYPE_ATTRIBUTE:
						foundList.addAll(TypeHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAttributesByKeyValue(searchKey, searchValue, false))));
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
	 * The file must be in /web/
	 * 
	 * @param url
	 * @param GETParams
	 * @param headerKeyValue
	 * @return
	 */
	public String getFileContent(final String url,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) {
		if (url != null) {
			final File file = (new File(new File("").getAbsolutePath() + "/trunk/web/" + url));
			// System.out.println("APATH: " + file.getAbsolutePath());
			try {
				if ((file != null) && file.exists()) {
					// System.out.println("load file: " +
					// file.getAbsolutePath());
					String content = "";
					content = Helper.getFileContents(file);
					content = this.generateContent(content, file.getName(), GETParams, headerKeyValue);

					return content;
				} else if ((file != null) && !file.exists()) {
					// System.out.println("cant load file: " +
					// file.getAbsolutePath());
				} else {
					// System.out.println("cant load file with url");
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public String getTitleForAttributesView() {
		return "Attributes";
	}

	public String getTitleForDetailview(final FileItem fileItem) {
		String titleStr = "Kein Titel gewählt";
		if (fileItem != null) {
			titleStr = fileItem.getName();
		}
		return "Detailansicht: " + titleStr;
	}

	public String getTitleForInfoview() {
		return "Infos";
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

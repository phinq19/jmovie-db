/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
 * @author albrela
 * 
 */
public class WebServerHelper {

	private MainController	mainController	= null;

	public WebServerHelper(final MainController mainController) {
		this.mainController = mainController;
	}

	public String generateContent(final String content,
			final String filename,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) {
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
				final String listOutput = HTML.generateListOutput(lastFiveList, null, false);
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
			resultStr += "<div class=\"listWrapper\"><div class=\"key\">Size</div><div class=\"value\">"
					+ Helper.getHumanreadableFileSize(item.getSize()) + "</div></div>";

			if ((item.getAttributes() != null) && (item.getAttributes().size() > 0)) {
				resultStr += "<hr />";
				resultStr += "<div id=\"attributes\">";

				resultStr += "<ul><li><a href=\"#MediaInfo\">MediaInfo</a></li><li><a href=\"#themoviedb\">The Movie DB</a></li></ul>";

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
									resultStr += "<td>"
											+ Helper.implode(this.getValuesForKey(attributeListCpy, keyValue.getKey().getKey()), ", ",
													null, null) + "</td>";
									resultStr += "</tr>";
									attributeListCpy = this.removeKeysFromFileAttributeList(attributeListCpy, keyValue.getKey().getKey());
									evenOdd++;
								}
							}
						} catch (final CloneNotSupportedException e) {
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
				resultStr += "Collections cannot be refreshed. Process stopped." + "</p>";
			}

			resultStr += "</div>";
		}
		resultStr += "<nav><ul>";
		resultStr += "<li><a href=\""
				+ (isStartCollectors ? "javascript:void(0)\" class=\"disabled" : "?action=showInfoControl&do=startCollectors") + "\">"
				+ "Start Collectors" + "</a></li>";
		resultStr += "</ul></nav>";
		resultStr += "</div>";
		return resultStr;
	}

	public String generateSearchresults(final ConcurrentHashMap<String, String> GETParams) {
		String resultStr = "<div id=\"searchresultsView\" class=\"contentPart\">";
		if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
			// get DATA for output
			String searchStr = GETParams.get("searchStr");
			searchStr = searchStr.replaceAll("\\+", " ");
			ArrayList<FileItem> foundList = TypeHandler.castObjectListToFileItemList(this.mainController.getDataHandler()
					.findAllFileItemForStringInAll(searchStr, false));
			foundList = Helper.unique(foundList);

			resultStr += HTML.generateListOutput(foundList, searchStr, true);
		} else {
			resultStr += "<p>Suchen Sie mit hilfe der Suche</p>";
		}

		resultStr += "</div>";
		return resultStr;
	}

	public String getContent(final String url,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) {
		if (url != null) {
			final File file = (new File(new File("").getAbsolutePath() + "/web/" + url));
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

/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.util.ArrayList;
import java.util.Arrays;

import com.lars_albrecht.mdb.core.interfaces.web.helper.WebServerHelper;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class HTML {

	/**
	 * Generate HTML Output from FileItem-List. <br />
	 * TODO do generic! <br />
	 * TODO make Arrays.toString unnecessary <br />
	 * 
	 * @param fileItemList
	 * @param searchTerms
	 * @param searchType
	 * @param printOutCount
	 * @return
	 */
	public static String generateListOutput(final ArrayList<FileItem> fileItemList,
			final String[] searchTerms,
			final Integer searchType,
			final boolean printOutCount) {
		String resultStr = "";
		if (fileItemList.size() > 0) {
			if (printOutCount && (searchTerms != null)) {
				switch (searchType) {
					default:
					case WebServerHelper.SEARCHTYPE_TEXTALL:
						resultStr += "<p><span class=\"searchResultCount\">" + fileItemList.size()
								+ "</span> Ergebnisse wurden für \"<span class=\"searchTerm\">" + Arrays.toString(searchTerms)
								+ "</span>\" gefunden</p>";
						break;
					case WebServerHelper.SEARCHTYPE_ATTRIBUTE:
						resultStr += "<p><span class=\"searchResultCount\">" + fileItemList.size()
								+ "</span> Ergebnisse wurden für das Attribut \"<span class=\"searchKey\">"
								+ Arrays.toString(searchTerms).split("=")[0] + "</span>\" mit dem Wert \"<span class=\"searchValue\">"
								+ Arrays.toString(searchTerms).split("=")[1] + "</span>\" gefunden</p>";
						break;
					case WebServerHelper.SEARCHTYPE_MIXED:
						resultStr += "<p><span class=\"searchResultCount\">" + fileItemList.size()
								+ "</span> Ergebnisse wurden gefunden. Folgendes wurde gesucht:";
						resultStr += "";

						int searchTermCount = 0;
						for (final String string : searchTerms) {
							if (string.contains("=")) {
								resultStr += "<ul><li>Für das Attribut <span class=\"searchKey\">" + string.split("=")[0]
										+ "</span> mit dem Wert <span class=\"searchValue\">" + string.split("=")[1] + "</span></li></ul>";
							} else {
								resultStr += "<ul><li>Für den Suchbegriff <span class=\"searchTerm\">" + string + "</span></li></ul>";
							}
							searchTermCount++;
							if (searchTermCount != searchTerms.length) {
								resultStr += " ODER ";
							}
						}
						resultStr += "</ul>";
						resultStr += "</p>";
						break;
				}
			} else if (printOutCount) {
				resultStr += "<p>" + fileItemList.size() + " Einträge gefunden</p>";
			} else if (searchTerms != null) {

				resultStr += "<p>" + searchTerms + " wurde gesucht</p>";
			}
			resultStr += "<div class=\"resulttable\">";
			resultStr += "<table>";
			resultStr += "<tr>";
			resultStr += "<th>Filename</th>";
			resultStr += "</tr>";

			for (final FileItem fileItem : fileItemList) {
				resultStr += "<tr>";
				resultStr += "<td><a href=\"?action=showFileDetails&fileId=" + fileItem.getId() + "\">" + fileItem.getName() + "</a></td>";
				resultStr += "</tr>";
			}
			resultStr += "</table>";
			resultStr += "</div>";

		} else {
			resultStr = "<p>Keine Ergebnisse für: " + Arrays.toString(searchTerms) + "</p>";
		}
		return resultStr;
	}
}

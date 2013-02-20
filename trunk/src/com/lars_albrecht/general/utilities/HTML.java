/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.interfaces.web.helper.WebServerHelper;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author ibsisini
 * 
 */
public class HTML {

	/**
	 * Generate HTML Output from FileItem-List.
	 * 
	 * @param fileItemList
	 * @param searchTerm
	 * @param searchType
	 * @param printOutCount
	 * @return
	 */
	public static String generateListOutput(final ArrayList<FileItem> fileItemList,
			final String searchTerm,
			final Integer searchType,
			final boolean printOutCount) {
		String resultStr = "";
		if (fileItemList.size() > 0) {
			if (printOutCount && (searchTerm != null)) {
				switch (searchType) {
					default:
					case WebServerHelper.SEARCHTYPE_TEXTALL:
						resultStr += "<p><span class=\"searchResultCount\">" + fileItemList.size()
								+ "</span>x wurde \"<span class=\"searchTerm\">" + searchTerm + "</span>\" gefunden</p>";
						break;
					case WebServerHelper.SEARCHTYPE_ATTRIBUTE:
						resultStr += "<p><span class=\"searchResultCount\">" + fileItemList.size()
								+ "</span>x wurde das Attribut \"<span class=\"searchKey\">" + searchTerm.split("=")[0]
								+ "</span>\" mit dem Wert \"<span class=\"searchValue\">" + searchTerm.split("=")[1]
								+ "</span>\" gefunden</p>";
						break;
				}
			} else if (printOutCount) {
				resultStr += "<p>" + fileItemList.size() + " Einträge gefunden</p>";
			} else if (searchTerm != null) {

				resultStr += "<p>" + searchTerm + " wurde gesucht</p>";
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
			resultStr = "<p>Keine Ergebnisse</p>";
		}
		return resultStr;
	}
}

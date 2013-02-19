/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.util.ArrayList;

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
	 * @return String
	 */
	public static String generateListOutput(final ArrayList<FileItem> fileItemList, final String searchTerm, final boolean printOutCount) {
		String resultStr = "";
		if (fileItemList.size() > 0) {
			if (printOutCount && searchTerm != null) {
				resultStr += "<p>" + fileItemList.size() + "x wurde \"" + searchTerm + "\" gefunden</p>";
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

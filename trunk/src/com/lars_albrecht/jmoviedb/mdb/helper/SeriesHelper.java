/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.helper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.mdb.main.core.handler.datahandler.AttributeHandler;
import com.lars_albrecht.mdb.main.core.handler.datahandler.abstracts.ADataHandler;
import com.lars_albrecht.mdb.main.core.models.FileAttributeList;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class SeriesHelper {

	public static Integer getSeasonFromFileItem(final FileItem fileItem) {
		Integer seasonNumber = null;
		if (fileItem != null) {
			@SuppressWarnings("unchecked")
			final ArrayList<FileAttributeList> attributesList = (ArrayList<FileAttributeList>) ADataHandler.getHandlerDataFromFileItem(
					fileItem, AttributeHandler.class);
			if ((attributesList != null) && (attributesList.size() > 0)) {
				for (final FileAttributeList fileAttributeList : attributesList) {
					if ((fileAttributeList.getValueByKey("season_number") != null)
							&& (fileAttributeList.getValueByKey("season_number") != "")) {
						seasonNumber = (Integer) fileAttributeList.getValueByKey("season_number");
						break;
					}
				}
			}
			if (seasonNumber == null) {
				final Pattern p = Pattern.compile("(S([0-9]{1,2})E[0-9]{1,2})");
				Matcher m = null;
				m = p.matcher(fileItem.getName());
				if (m.find()) {
					seasonNumber = Integer.parseInt(m.group(2));
				}
			}
		}

		return seasonNumber;
	}
}

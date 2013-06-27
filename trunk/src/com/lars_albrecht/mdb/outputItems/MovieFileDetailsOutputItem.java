/**
 * 
 */
package com.lars_albrecht.mdb.outputItems;

import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.interfaces.web.pages.abstracts.AbstractFileDetailsOutputItem;
import com.lars_albrecht.mdb.core.models.KeyValue;

/**
 * @author lalbrecht
 * 
 */
public class MovieFileDetailsOutputItem extends AbstractFileDetailsOutputItem {

	@Override
	public String getValue(final String infoType, final String sectionName, final KeyValue<String, Object> keyValue, final String value) {
		if (infoType.equalsIgnoreCase("themoviedb") && sectionName.equalsIgnoreCase("video")) {
			// create trailerlink
			// TODO create method
			final ArrayList<String> trailerParams = Helper.explode(value, ",");
			String trailerUrl = "";
			if (trailerParams.get(3).trim().equalsIgnoreCase("youtube")) {
				trailerUrl += "http://www.youtube.com/watch?v=" + trailerParams.get(2).trim();
			}
			return "<a href=\"" + trailerUrl + "\">" + trailerParams.get(0).trim() + " (" + trailerParams.get(1).trim() + ")</a>";

		} else {
			return this.getDefault(infoType, sectionName, keyValue, value);
		}
	}

}

/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.outputItems;

import java.util.ArrayList;
import java.util.Arrays;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.abstracts.AbstractFileDetailsOutputItem;
import com.lars_albrecht.mdb.main.core.models.KeyValue;

/**
 * @author lalbrecht
 * 
 */
public class MovieFileDetailsOutputItem extends AbstractFileDetailsOutputItem {

	@Override
	public String getValue(final String infoType, final String sectionName, final KeyValue<String, Object> keyValue, final String value) {
		if (infoType.equalsIgnoreCase("themoviedb")) {
			if (sectionName.equalsIgnoreCase("video")) {
				if (keyValue.getKey().getKey().equalsIgnoreCase("trailer")) {
					final ArrayList<String> trailerParams = Helper.explode(value, ",");
					String trailerUrl = null;
					if (Helper.explode(value, ",").get(3).trim().equalsIgnoreCase("youtube")) {
						trailerUrl = this.getYoutubeLink(trailerParams);
					}
					return "<a href=\"" + trailerUrl + "\">" + trailerParams.get(0).trim() + " (" + trailerParams.get(1).trim() + ")</a>";
				}
			} else if (sectionName.equalsIgnoreCase("facts")) {
				if (keyValue.getKey().getKey().equalsIgnoreCase("homepage")) {
					return (value != null && value.length() > 0) ? "<a href=\"" + value + "\">" + value + "</a>" : null;
				}
			}
		}

		if (sectionName.equalsIgnoreCase("general")) {
			if (keyValue.getKey().getKey().equalsIgnoreCase("imdb_id")) {
				return "<a href=\"http://www.imdb.com/title/" + value + "\">@ IMDB" + " (" + value + ")</a>";
			}
		}

		if (keyValue.getKey().getKey().equalsIgnoreCase("duration")) {
			return Helper.getFormattedTimestamp(Long.parseLong(value) / 1000, "HH:mm:ss");
		}

		return this.getDefaultValue(infoType, sectionName, keyValue, value);
	}

	/**
	 * Create a youtube link from videoParams (comma separated list of strings).
	 * 
	 * @param videoParams
	 * @return a youtube link
	 */
	private String getYoutubeLink(final ArrayList<String> videoParams) {
		return "http://www.youtube.com/watch?v=" + videoParams.get(2).trim();
	}

	@Override
	public boolean keyAllowed(final String infoType, final String sectionName, final KeyValue<String, Object> keyValue) {
		final String[] disallowList = {};
		if (Arrays.asList(disallowList).contains(keyValue.getKey().getKey())) {
			return false;
		}
		final Object[] disallowObjectValueList = {
				null, ""
		};
		if (Arrays.asList(disallowObjectValueList).contains(keyValue.getValue().getValue())) {
			return false;
		}

		return true;
	}

	@Override
	public String getKey(final String infoType, final String sectionName, final KeyValue<String, Object> keyValue) {
		final String key = this.translateKey(infoType, sectionName, keyValue);
		System.out.println(key);
		if (key == null) {
			return this.getDefaultKey(infoType, sectionName, keyValue);
		} else {
			return key;
		}

	}

	private String translateKey(final String infoType, final String sectionName, final KeyValue<String, Object> keyValue) {
		final String translateKey = "moviedb.filedetails." + infoType + "." + sectionName + "." + keyValue.getKey().getKey();
		System.out.println(translateKey);
		if (RessourceBundleEx.getInstance("mdb-trans").contains(translateKey)) {
			return RessourceBundleEx.getInstance("mdb-trans").getProperty(translateKey);
		} else {
			return null;
		}
	}
}

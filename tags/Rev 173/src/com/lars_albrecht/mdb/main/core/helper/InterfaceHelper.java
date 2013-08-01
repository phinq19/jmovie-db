/**
 * 
 */
package com.lars_albrecht.mdb.main.core.helper;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.main.core.handler.DataHandler;
import com.lars_albrecht.mdb.main.core.interfaces.web.helper.WebServerHelper;
import com.lars_albrecht.mdb.main.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class InterfaceHelper {

	public final static int	SEARCHTYPE_MIXED		= 0;
	public final static int	SEARCHTYPE_TEXTALL		= 1;
	public final static int	SEARCHTYPE_ATTRIBUTE	= 2;

	/**
	 * 
	 * @param searchText
	 * @param dataHandler
	 * @return Map<String, Object> resultlist (ArrayList<FileItem>)| searchtype
	 *         (int) | searchstrings (ArrayList<Entry<String,String>>)
	 */
	public static ConcurrentHashMap<String, Object> searchItems(final String searchText, final DataHandler dataHandler) {
		ConcurrentHashMap<String, Object> resultMap = null;
		ArrayList<FileItem> resultList = null;
		if (searchText != null && !searchText.equalsIgnoreCase("") && dataHandler != null) {
			resultMap = new ConcurrentHashMap<String, Object>();
			resultList = new ArrayList<FileItem>();

			int searchType = WebServerHelper.SEARCHTYPE_TEXTALL;

			final ArrayList<Entry<String, String>> searchStrList = new ArrayList<Entry<String, String>>();

			// (([\S])+=".*?(\ ){0,}")|(([\S])+=([\S])+)|([\S])+
			// finds attribute="value value"
			final String findTextWithAttributeMoreWords = "(([\\S])+=\".*?(\\ ){0,}\")";

			// finds attribute=value
			final String findTextWithAttributeOneWord = "(([\\S])+=([\\S])+)";

			// finds "value value"
			final String findTextWithQuotes = "(\".*?\")";

			// finds value
			final String findText = "([\\S])+";

			// concat all pattern to one with "or"
			final String strPattern = findTextWithAttributeMoreWords + "|" + findTextWithAttributeOneWord + "|" + findTextWithQuotes + "|"
					+ findText;

			final Pattern pattern = Pattern.compile(strPattern);
			final Matcher matcher = pattern.matcher(searchText);
			// Find all values and add them to searchStrList
			while (matcher.find()) {
				searchStrList.add(InterfaceHelper.getRealSearchValues(matcher.group()));
			}

			// TODO fix search and output from searchkeys/values

			for (final Entry<String, String> searchEntry : searchStrList) {
				searchType = WebServerHelper.SEARCHTYPE_TEXTALL;
				if (searchEntry.getKey() != null) {
					if (dataHandler.isKeyInKeyList(searchEntry.getKey())) {
						searchType = WebServerHelper.SEARCHTYPE_ATTRIBUTE;
					}
				}

				switch (searchType) {
					default:
					case SEARCHTYPE_TEXTALL:
						resultList.addAll(Helper.uniqueList(dataHandler.findAllFileItemForStringInAll(searchEntry.getValue())));
						break;
					case SEARCHTYPE_ATTRIBUTE:
						resultList.addAll(Helper.uniqueList(dataHandler.findAllFileItemForStringInAttributesByKeyValue(
								searchEntry.getKey(), searchEntry.getValue())));
						break;
				}
			}

			resultList = Helper.uniqueList(resultList);
			resultMap.put("resultlist", resultList); // ArrayList<FileItem>
			resultMap.put("searchtype", searchType); // int
			resultMap.put("searchstrings", searchStrList); // ArrayList<Entry<String,
															// String>>
		}

		return resultMap;
	}

	private static Entry<String, String> getRealSearchValues(final String input) {
		Entry<String, String> output = null;
		String key = null;
		String value = null;

		String[] keyValue = null;
		if (input.contains("=")) {
			keyValue = input.split("=");

			keyValue[1] = keyValue[1].replaceAll("\"", "");

			key = keyValue[0];
			value = keyValue[1];
		} else if (input.contains("\"")) {
			value = input.replaceAll("\"", "");
		} else {
			value = input;
		}

		output = new SimpleEntry<String, String>(key, value);

		return output;
	}

}

/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.core.interfaces.web.helper.WebServerHelper;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class SearchResultsPage extends WebPage {

	public final static int	SEARCHTYPE_MIXED		= 0;
	public final static int	SEARCHTYPE_TEXTALL		= 1;
	public final static int	SEARCHTYPE_ATTRIBUTE	= 2;

	public SearchResultsPage(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		super(actionname, request, mainController);

		this.setPageTemplate(this.generateSearchresults(this.getPageTemplate(), request.getGetParams()));
	}

	/**
	 * Search with "=" for attributes must check if the search string is end
	 * after " " or if it is surrounded by """.
	 * 
	 * @param GETParams
	 * @return Template
	 * @throws UnsupportedEncodingException
	 */
	private Template
			generateSearchresults(final Template searchResultsTemplate, final ConcurrentHashMap<String, String> GETParams) throws UnsupportedEncodingException {
		if (GETParams.containsKey("searchStr") && (GETParams.get("searchStr") != null)) {
			// get DATA for output
			String searchStr = null;
			try {
				/*
				 * java.lang.IllegalArgumentException: URLDecoder: Incomplete
				 * trailing escape (%) pattern -> 22.05.2013 17:00:09 MSG:
				 * UncaughtException thrown (URLDecoder: Incomplete trailing
				 * escape (%) pattern - java.lang.IllegalArgumentException:
				 * URLDecoder: Incomplete trailing escape (%) pattern) in Thread
				 * Thread-14729 (15501) at
				 * java.net.URLDecoder.decode(URLDecoder.java:187)
				 */
				searchStr = (URLDecoder.decode(GETParams.get("searchStr"), "utf-8"));
			} catch (final Exception e) {

			}
			int searchType = WebServerHelper.SEARCHTYPE_TEXTALL;

			final ArrayList<Entry<String, String>> searchStrList = new ArrayList<Entry<String, String>>();
			ArrayList<FileItem> foundList = new ArrayList<FileItem>();

			// (([\S])+=".*?(\ ){0,}")|(([\S])+=([\S])+)|([\S])+
			final String findTextWithAttributeMoreWords = "(([\\S])+=\".*?(\\ ){0,}\")";
			final String findTextWithAttributeOneWord = "(([\\S])+=([\\S])+)";
			final String findTextWithQuotes = "(\".*?\")";
			final String findText = "([\\S])+";

			final String strPattern = findTextWithAttributeMoreWords + "|" + findTextWithAttributeOneWord + "|" + findTextWithQuotes + "|"
					+ findText;

			final Pattern pattern = Pattern.compile(strPattern);
			final Matcher matcher = pattern.matcher(searchStr);
			// Check all occurance
			while (matcher.find()) {
				// System.out.print("Start index: " + matcher.start());
				// System.out.print(" End index: " + matcher.end() + " ");
				// System.out.println(matcher.group());
				searchStrList.add(this.getRealSearchValues(matcher.group()));
			}

			// TODO fix search and output from searchkeys/values

			for (final Entry<String, String> searchEntry : searchStrList) {
				searchType = WebServerHelper.SEARCHTYPE_TEXTALL;
				if (searchEntry.getKey() != null) {
					if (this.mainController.getDataHandler().isKeyInKeyList(searchEntry.getKey())) {
						searchType = WebServerHelper.SEARCHTYPE_ATTRIBUTE;
					}
				}

				switch (searchType) {
					default:
					case SEARCHTYPE_TEXTALL:
						foundList.addAll(ObjectHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAll(searchEntry.getValue()))));
						break;
					case SEARCHTYPE_ATTRIBUTE:
						foundList.addAll(ObjectHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAttributesByKeyValue(searchEntry.getKey(), searchEntry.getValue()))));
						break;
				}
			}

			foundList = Helper.uniqueList(foundList);

			if (foundList.size() > 0) {
				String searchResultContainer = searchResultsTemplate.getSubMarkerContent("results");
				String searchResultItemContainer = "";
				String searchResultItemContainerTemp = "";

				for (final FileItem fileItem : foundList) {
					searchResultItemContainerTemp = searchResultsTemplate.getSubMarkerContent("resultListItem");
					searchResultItemContainerTemp = Template.replaceMarker(searchResultItemContainerTemp, "url",
							"?action=showFileDetails&fileId=" + fileItem.getId(), false);
					searchResultItemContainerTemp = Template.replaceMarker(searchResultItemContainerTemp, "title", fileItem.getName(),
							false);

					searchResultItemContainer += searchResultItemContainerTemp;
				}

				searchResultContainer = Template.replaceMarker(searchResultContainer, "resultListItems", searchResultItemContainer, false);
				searchResultContainer = Template.replaceMarker(searchResultContainer, "searchResultCount",
						Integer.toString(foundList.size()), false);

				String searchTextContainer = null;
				switch (searchType) {
					default:
					case SEARCHTYPE_TEXTALL:
						searchTextContainer = searchResultsTemplate.getSubMarkerContent("resultTextTextAll");
						searchTextContainer = Template.replaceMarker(searchTextContainer, "searchTerm", searchStr, false);
						break;
					case SEARCHTYPE_ATTRIBUTE:
						searchTextContainer = searchResultsTemplate.getSubMarkerContent("resultTextAttribute");
						searchTextContainer = Template.replaceMarker(searchTextContainer, "searchKey",
								Arrays.toString(searchStrList.toArray()).split("=")[0].replaceAll("([\\[\\]])", ""), false);
						searchTextContainer = Template.replaceMarker(searchTextContainer, "searchValue",
								Arrays.toString(searchStrList.toArray()).split("=")[1].replaceAll("([\\[\\]])", ""), false);
						break;
				}
				searchResultContainer = Template.replaceMarker(searchResultContainer, "resultText", searchTextContainer, false);
				searchResultsTemplate.replaceMarker("content", searchResultContainer, false);
			} else {
				// no results
				searchResultsTemplate.replaceMarker("content", searchResultsTemplate.getSubMarkerContent("noResults"), false);
			}
		} else {
			searchResultsTemplate.replaceMarker("content", searchResultsTemplate.getSubMarkerContent("noInput"), false);
		}

		return searchResultsTemplate;
	}

	// TODO PUT TO OWN CLASS START

	private Entry<String, String> getRealSearchValues(final String input) {
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

	// TODO PUT TO OWN CLASS END

	@Override
	public String getTitle() {
		String searchStr = "";
		if (this.request.getGetParams().containsKey("searchStr") && (this.request.getGetParams().get("searchStr") != null)) {
			searchStr = this.request.getGetParams().get("searchStr");
		}
		return "Suchergebnisse für: " + searchStr;
	}

	@Override
	public String getTemplateName() {
		return "searchresults";
	}

}

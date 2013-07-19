/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces.web.pages;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.helper.InterfaceHelper;
import com.lars_albrecht.mdb.main.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.main.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.main.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.main.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class SearchResultsPage extends WebPage {

	/**
	 * Currently unused
	 */
	public final static int	SEARCHTYPE_MIXED		= 0;

	public final static int	SEARCHTYPE_TEXTALL		= 1;
	public final static int	SEARCHTYPE_ATTRIBUTE	= 2;

	public SearchResultsPage(final String actionname, final WebServerRequest request, final MainController mainController,
			final WebInterface webInterface) throws Exception {
		super(actionname, request, mainController, webInterface);

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
				 * 
				 * TODO propably fixed TODO move searchcode to extra
				 * class/method to reuse this for other interfaces
				 */
				searchStr = GETParams.get("searchStr");
				final ConcurrentHashMap<String, Object> searchResults = InterfaceHelper.searchItems(searchStr,
						this.mainController.getDataHandler());
				@SuppressWarnings("unchecked")
				final ArrayList<Entry<String, String>> searchStrList = (ArrayList<Entry<String, String>>) searchResults
						.get("searchstrings");
				@SuppressWarnings("unchecked")
				final ArrayList<FileItem> foundList = (ArrayList<FileItem>) searchResults.get("resultlist");
				final int searchType = ((Integer) searchResults.get("searchtype"));

				if (foundList.size() > 0) {
					String searchResultContainer = searchResultsTemplate.getSubMarkerContent("results");
					String searchResultItemContainer = "";
					String searchResultItemContainerTemp = "";
					int oddEven = 0;
					for (final FileItem fileItem : foundList) {
						searchResultItemContainerTemp = searchResultsTemplate.getSubMarkerContent("resultListItem");
						searchResultItemContainerTemp = Template.replaceMarker(searchResultItemContainerTemp, "fileid", fileItem.getId()
								.toString(), false);
						searchResultItemContainerTemp = Template.replaceMarker(searchResultItemContainerTemp, "title", fileItem.getName(),
								false);
						searchResultItemContainerTemp = Template.replaceMarker(searchResultItemContainerTemp, "oddeven",
								((oddEven % 2) == 0 ? "even" : "odd"), false);

						searchResultItemContainer += searchResultItemContainerTemp;
						oddEven++;
					}

					searchResultContainer = Template.replaceMarker(searchResultContainer, "resultListItems", searchResultItemContainer,
							false);
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
			} catch (final IllegalArgumentException e) {
				searchResultsTemplate.replaceMarker("content", searchResultsTemplate.getSubMarkerContent("noResults"), false);
			}
		} else {
			searchResultsTemplate.replaceMarker("content", searchResultsTemplate.getSubMarkerContent("noInput"), false);
		}

		return searchResultsTemplate;
	}

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

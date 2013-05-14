/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

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
			final String searchStr = (URLDecoder.decode(GETParams.get("searchStr"), "utf-8")).replaceAll("[\"]", "");
			int searchType = WebServerHelper.SEARCHTYPE_TEXTALL;

			String searchKey = null;
			String searchValue = null;
			String[] searchStrList = null;
			ArrayList<FileItem> foundList = new ArrayList<FileItem>();
			if (searchStr.contains(" ")) {
				searchStrList = searchStr.split(" ");
			} else {
				searchStrList = new String[] {
					searchStr
				};
			}

			for (final String searchStrItem : searchStrList) {
				searchType = WebServerHelper.SEARCHTYPE_TEXTALL;
				if (searchStrItem.contains("=")) {
					final String[] searchArr = searchStrItem.split("=");
					if (searchArr.length == 2) {
						searchKey = searchArr[0];
						searchValue = searchArr[1];
						if (this.mainController.getDataHandler().isKeyInKeyList(searchKey)) {
							searchType = WebServerHelper.SEARCHTYPE_ATTRIBUTE;
						}

					}
				}

				switch (searchType) {
					default:
					case SEARCHTYPE_TEXTALL:
						foundList.addAll(ObjectHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAll(searchStrItem))));
						break;
					case SEARCHTYPE_ATTRIBUTE:
						foundList.addAll(ObjectHandler.castObjectListToFileItemList(Helper.uniqueList(this.mainController.getDataHandler()
								.findAllFileItemForStringInAttributesByKeyValue(searchKey, searchValue))));
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
						searchTextContainer = Template.replaceMarker(searchTextContainer, "searchKey", Arrays.toString(searchStrList)
								.split("=")[0].replaceAll("([\\[\\]])", ""), false);
						searchTextContainer = Template.replaceMarker(searchTextContainer, "searchValue", Arrays.toString(searchStrList)
								.split("=")[1].replaceAll("([\\[\\]])", ""), false);
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

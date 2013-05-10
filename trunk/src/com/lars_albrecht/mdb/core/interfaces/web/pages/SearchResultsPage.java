/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;

/**
 * @author lalbrecht
 * 
 */
public class SearchResultsPage extends WebPage {

	public SearchResultsPage(final String actionname, final ConcurrentHashMap<String, String> GETParams, final MainController mainController)
			throws Exception {
		super(actionname, GETParams, mainController);
	}

	@Override
	public String getTitle() {
		String searchStr = "";
		if (this.GETParams.containsKey("searchStr") && (this.GETParams.get("searchStr") != null)) {
			searchStr = this.GETParams.get("searchStr");
		}
		return "Suchergebnisse für: " + searchStr;
	}

	@Override
	public String getTemplateName() {
		return "searchresults";
	}

}

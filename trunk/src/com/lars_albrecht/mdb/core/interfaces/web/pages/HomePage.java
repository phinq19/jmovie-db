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
public class HomePage extends WebPage {

	public HomePage(final String actionname, final ConcurrentHashMap<String, String> GETParams, final MainController mainController)
			throws Exception {
		super(actionname, GETParams, mainController);
	}

	@Override
	public String getTitle() {
		return "Home";
	}

	@Override
	public String getTemplateName() {
		return "home";
	}

}

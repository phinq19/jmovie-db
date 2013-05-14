/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;

/**
 * @author lalbrecht
 * 
 */
public class HomePage extends WebPage {

	public HomePage(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		super(actionname, request, mainController);
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

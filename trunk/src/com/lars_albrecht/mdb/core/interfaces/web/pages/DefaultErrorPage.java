/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;

/**
 * @author lalbrecht
 * 
 */
public class DefaultErrorPage extends WebPage {

	public DefaultErrorPage(final String actionname, final WebServerRequest request, final MainController mainController,
			final WebInterface webInterface) throws Exception {
		super(actionname, request, mainController, webInterface);
	}

	@Override
	public String getTitle() {
		if (this.actionname.equalsIgnoreCase("404")) {
			return "Page not found";
		} else {
			return "Server error";
		}
	}

	@Override
	public String getTemplateName() {
		if (this.actionname.equalsIgnoreCase("404")) {
			return "404";
		} else {
			return "500";
		}
	}

}

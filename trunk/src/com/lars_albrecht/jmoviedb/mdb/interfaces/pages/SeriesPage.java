/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.interfaces.pages;

import org.eclipse.jetty.server.Request;

import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.main.core.interfaces.web.abstracts.WebPage;

/**
 * @author lalbrecht
 * 
 */
public class SeriesPage extends WebPage {

	public SeriesPage(final String actionname, final Request request, final MainController mainController, final WebInterface webInterface)
			throws Exception {
		super(actionname, request, mainController, webInterface);
	}

	@Override
	public String getTemplateName() {
		return "series";
	}

	@Override
	public String getTitle() {
		return "Series";
	}

}

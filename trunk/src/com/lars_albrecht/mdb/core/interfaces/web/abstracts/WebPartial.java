/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.abstracts;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;

/**
 * @author lalbrecht
 * 
 */
public abstract class WebPartial {

	private Template			pageTemplate	= null;
	protected MainController	mainController	= null;
	protected WebServerRequest	request			= null;
	protected String			actionname		= null;

	public WebPartial(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		this.request = request;
		this.mainController = mainController;
		this.actionname = actionname;

		if (this.getTemplateName() != null) {
			this.pageTemplate = new Template(this.getTemplateName());
		} else {
			throw new Exception("Template is not set");
		}
	}

	final public String getGeneratedContent() {
		return this.pageTemplate.getClearedContent();
	}

	final protected Template getPageTemplate() {
		return this.pageTemplate;
	}

	final protected void setPageTemplate(final Template pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

	public abstract String getTemplateName();
}

/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.abstracts;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;

/**
 * @author lalbrecht
 * 
 */
public abstract class WebPage {

	private Template			pageTemplate	= null;
	protected MainController	mainController	= null;
	protected WebServerRequest	request			= null;
	protected String			actionname		= null;

	public WebPage(final String actionname, final WebServerRequest request, final MainController mainController,
			final WebInterface webInterface) throws Exception {
		this.request = request;
		this.mainController = mainController;
		this.actionname = actionname;

		if (this.getTemplateName() != null) {
			this.pageTemplate = new Template(this.getTemplateName());
		} else {
			throw new Exception("Template is not set");
		}
	}

	final protected void set404Error() {
		this.setPageTemplate(new Template("404"));
	}

	final protected void set500Error() {
		this.setPageTemplate(new Template("500"));
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

	public abstract String getTitle();

	public abstract String getTemplateName();
}

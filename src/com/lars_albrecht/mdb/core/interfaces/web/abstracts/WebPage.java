/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.abstracts;

import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;

/**
 * @author lalbrecht
 * 
 */
public abstract class WebPage {

	private Template							pageTemplate	= null;
	protected MainController					mainController	= null;
	protected ConcurrentHashMap<String, String>	GETParams		= null;

	public WebPage(final String actionname, final ConcurrentHashMap<String, String> GETParams, final MainController mainController)
			throws Exception {
		this.GETParams = GETParams;
		this.mainController = mainController;

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

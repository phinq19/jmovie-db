/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class BrowsePage extends WebPage {

	public BrowsePage(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		super(actionname, request, mainController);
		this.setPageTemplate(this.generateBrowseView());
	}

	private Template generateBrowseView() {
		Template browseTemplate = this.getPageTemplate();

		browseTemplate = this.fillNoInfoContainer(browseTemplate);

		return browseTemplate;
	}

	private Template fillNoInfoContainer(final Template browseTemplate) {
		final Template browseTemplateWithNoInfoContainer = browseTemplate;
		final ConcurrentHashMap<String, ArrayList<FileItem>> noInfoList = this.mainController.getDataHandler()
				.getAllFileItemsWithNoCollectorinfo();
		if (noInfoList.size() > 0) {
			String noInfoContainer = browseTemplate.getSubMarkerContent("noinformation");

			String noInfoItemList = "";
			String tempNoInfoItemList = null;

			int noItemCounter = 0;
			for (final Entry<String, ArrayList<FileItem>> entry : noInfoList.entrySet()) {
				// TODO create method replaceAllMarker to replace a bunch of
				// marker
				for (final FileItem fileItem : entry.getValue()) {
					tempNoInfoItemList = browseTemplate.getSubMarkerContent("noinformationItem");
					tempNoInfoItemList = Template.replaceMarker(tempNoInfoItemList, "noInfoItemId", fileItem.getId().toString(), true);
					tempNoInfoItemList = Template.replaceMarker(tempNoInfoItemList, "noInfoItemTitle", fileItem.getName(), false);
					tempNoInfoItemList = Template.replaceMarker(tempNoInfoItemList, "noInfoCollectorname", entry.getKey(), false);
					noInfoItemList += tempNoInfoItemList;
					noItemCounter++;
				}
			}

			noInfoContainer = Template.replaceMarker(noInfoContainer, "noinformationItems", noInfoItemList, false);
			noInfoContainer = Template.replaceMarker(noInfoContainer, "noinformationcounter", Integer.toString(noItemCounter), false);

			browseTemplateWithNoInfoContainer.replaceMarker("noinfocontainer", noInfoContainer, false);
		}

		return browseTemplateWithNoInfoContainer;
	}

	@Override
	public String getTitle() {
		return "Browse";
	}

	@Override
	public String getTemplateName() {
		return "browser";
	}

}

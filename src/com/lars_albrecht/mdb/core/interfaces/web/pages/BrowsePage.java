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

		browseTemplate = this.fillBrowseContainer(browseTemplate);
		browseTemplate = this.fillDuplicateContainer(browseTemplate);
		browseTemplate = this.fillNoInfoContainer(browseTemplate);

		return browseTemplate;
	}

	private Template fillBrowseContainer(final Template browseTemplate) {
		final Template browseTemplateWithBrowseContainer = browseTemplate;
		browseTemplateWithBrowseContainer.replaceMarker("browsercontainer",
				browseTemplateWithBrowseContainer.getSubMarkerContent("browser"), false);
		return browseTemplateWithBrowseContainer;
	}

	private Template fillDuplicateContainer(final Template browseTemplate) {
		final Template browseTemplateWithDuplicateContainer = browseTemplate;
		browseTemplateWithDuplicateContainer.replaceMarker("duplicatecontainer",
				browseTemplateWithDuplicateContainer.getSubMarkerContent("duplicate"), false);
		return browseTemplateWithDuplicateContainer;
	}

	private Template fillNoInfoContainer(final Template browseTemplate) {
		final Template browseTemplateWithNoInfoContainer = browseTemplate;
		final ConcurrentHashMap<String, ArrayList<FileItem>> noInfoList = this.mainController.getDataHandler()
				.getAllFileItemsWithNoCollectorinfo();
		if (noInfoList.size() > 0) {
			String noInfoContainer = browseTemplate.getSubMarkerContent("noinformation");

			String noInfoTables = "";
			String noInfoTable = "";

			String noInfoItemList = "";
			String tempNoInfoItemList = null;

			int noItemCounter = 0;
			for (final Entry<String, ArrayList<FileItem>> entry : noInfoList.entrySet()) {
				// TODO create method replaceAllMarker to replace a bunch of
				// marker
				noInfoTable = browseTemplate.getSubMarkerContent("noinformationTable");
				noInfoTable = Template.replaceMarker(noInfoTable, "collectorName", entry.getKey(), false);

				for (final FileItem fileItem : entry.getValue()) {
					tempNoInfoItemList = browseTemplate.getSubMarkerContent("noinformationItem");
					tempNoInfoItemList = Template.replaceMarker(tempNoInfoItemList, "noInfoItemId", fileItem.getId().toString(), true);
					tempNoInfoItemList = Template.replaceMarker(tempNoInfoItemList, "noInfoItemTitle", fileItem.getName(), false);
					tempNoInfoItemList = Template.replaceMarker(tempNoInfoItemList, "oddeven", ((noItemCounter % 2) == 0 ? "even" : "odd"),
							false);
					noInfoItemList += tempNoInfoItemList;
					noItemCounter++;
				}
				noInfoTable = Template.replaceMarker(noInfoTable, "noinformationItems", noInfoItemList, false);
				noInfoTables += noInfoTable;
			}

			noInfoContainer = Template.replaceMarker(noInfoContainer, "noinformationTables", noInfoTables, false);
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

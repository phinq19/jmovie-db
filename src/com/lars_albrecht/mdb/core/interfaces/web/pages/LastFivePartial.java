/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPartial;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class LastFivePartial extends WebPartial {

	public LastFivePartial(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		super(actionname, request, mainController);

		this.setPageTemplate(this.generateLastFivePartial());
	}

	private Template generateLastFivePartial() {
		final Template lastFivePartialTemplate = this.getPageTemplate();
		final ArrayList<FileItem> lastFiveList = ObjectHandler.castObjectListToFileItemList(this.mainController.getDataHandler().findAll(
				new FileItem(), 5, " ORDER BY fileInformation.createTS "));

		if (lastFiveList.size() > 0) {
			String listContainer = lastFivePartialTemplate.getSubMarkerContent("lastfivelist");
			String listItems = "";
			String tempListItem = "";
			for (final FileItem fileItem : lastFiveList) {
				tempListItem = lastFivePartialTemplate.getSubMarkerContent("lastfivelistitem");
				tempListItem = Template.replaceMarker(tempListItem, "fileid", fileItem.getId().toString(), false);
				tempListItem = Template.replaceMarker(tempListItem, "filetitle", fileItem.getName(), false);

				listItems += tempListItem;
			}
			listContainer = Template.replaceMarker(listContainer, "lastfivelistitems", listItems, false);
			lastFivePartialTemplate.replaceMarker("content", listContainer, false);

		} else {
			lastFivePartialTemplate.replaceMarker("content", lastFivePartialTemplate.getSubMarkerContent("nolastfive"), false);
		}

		return lastFivePartialTemplate;
	}

	@Override
	public String getTemplateName() {
		return "lastfive";
	}

}

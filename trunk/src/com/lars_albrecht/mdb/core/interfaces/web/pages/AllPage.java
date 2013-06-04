/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class AllPage extends WebPage {

	public AllPage(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		super(actionname, request, mainController);

		String sortOrder = null;
		if (request.getParams().containsKey("sortorder") && request.getParams().get("sortorder") != null) {
			sortOrder = request.getParams().get("sortorder");
		}
		this.setPageTemplate(this.fillAllContainer(this.getPageTemplate(), sortOrder));
	}

	private Template fillAllContainer(final Template allTemplate, final String sortOrder) {
		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().getFileItems();

		String fileList = allTemplate.getSubMarkerContent("allFileList");
		String fileListItems = "";
		String tempFileListItem = null;

		for (final FileItem fileItem : fileItems) {
			tempFileListItem = allTemplate.getSubMarkerContent("allFileListItem");
			tempFileListItem = Template.replaceMarker(tempFileListItem, "name", fileItem.getName(), false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "itemId", fileItem.getId().toString(), false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "type", fileItem.getFiletype(), false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "added",
					Helper.getFormattedTimestamp(fileItem.getCreateTS().longValue(), null), false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "updated",
					Helper.getFormattedTimestamp(fileItem.getUpdateTS().longValue(), null), false);
			fileListItems += tempFileListItem;
		}
		fileList = Template.replaceMarker(fileList, "fileListItems", fileListItems, false);

		allTemplate.replaceMarker("content", fileList, false);
		return allTemplate;
	}

	@Override
	public String getTitle() {
		return "Alle";
	}

	@Override
	public String getTemplateName() {
		return "all";
	}

}

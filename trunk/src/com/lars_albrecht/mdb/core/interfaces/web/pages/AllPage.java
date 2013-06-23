/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.OptionsHandler;
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

		String listOrderOption = (String) OptionsHandler.getOption("listSortOption");
		if (listOrderOption == null) {
			listOrderOption = "fileInformation.name";
			OptionsHandler.setOption("listSortOption", listOrderOption);
		}

		final Object maxItemsForListPagingOptionObj = OptionsHandler.getOption("maxItemsForListPagingOption");
		Integer maxItemsForListPagingOption = (maxItemsForListPagingOptionObj == null ? null
				: (maxItemsForListPagingOptionObj instanceof String ? Integer.parseInt((String) maxItemsForListPagingOptionObj)
						: (maxItemsForListPagingOptionObj instanceof Integer ? (Integer) maxItemsForListPagingOptionObj : null)));
		if (maxItemsForListPagingOption == null) {
			maxItemsForListPagingOption = 50;
			OptionsHandler.setOption("maxItemsForListPagingOption", maxItemsForListPagingOption);
		}

		String sortOrder = null;
		int page = 0;

		if (request.getParams().containsKey("sortorder") && request.getParams().get("sortorder") != null) {
			sortOrder = request.getParams().get("sortorder");
		}
		sortOrder = (sortOrder == null ? listOrderOption : sortOrder);

		if (request.getParams().containsKey("page") && request.getParams().get("page") != null
				&& request.getParams().get("page").matches("(\\d){1,}")) {
			page = Integer.parseInt(request.getParams().get("page"));
		}

		this.setPageTemplate(this.fillAllContainer(this.getPageTemplate(), sortOrder, page, maxItemsForListPagingOption));
	}

	private Template fillAllContainer(final Template allTemplate,
			final String sortOrder,
			final int page,
			final Integer maxItemsForPagingOption) {
		final int maxElems = maxItemsForPagingOption;
		final int startIndex = (page > 0 ? ((page * (maxElems > 0 ? maxElems : 0))) : 0);

		final int maxExistingElems = this.mainController.getDataHandler().getFileItems().size();

		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().getFileItemsForPaging(startIndex, maxElems, sortOrder);

		// pagination start
		final boolean showPagination = fileItems.size() > 0 && maxItemsForPagingOption > -1 ? true : false;
		if (showPagination) {
			final int pageCount = (int) Math.ceil(new Double(maxExistingElems) / new Double(maxElems));
			final int pageFirst = 0; // ever 0
			final int pagePrevious = (page > 0 ? page - 1 : -1);
			final String pageCurrentTitle = Integer.toString(page + 1);
			final int pageNext = (pageCount > page ? page + 1 : -1);
			final int pageLast = (pageCount > page ? pageCount - 1 : -1);

			String pagination = allTemplate.getSubMarkerContent("pagination");

			pagination = Template.replaceMarker(pagination, "currentPage", Integer.toString(page + 1), false);
			pagination = Template.replaceMarker(pagination, "maxPage", Integer.toString(pageCount), false);

			if (page > 0) {
				String paginationStart = allTemplate.getSubMarkerContent("paginationStart");
				paginationStart = allTemplate.getSubMarkerContent("paginationStart");
				paginationStart = Template.replaceMarker(paginationStart, "pageFirst", Integer.toString(pageFirst), false);
				paginationStart = Template.replaceMarker(paginationStart, "pagePrevious", Integer.toString(pagePrevious), false);

				pagination = Template.replaceMarker(pagination, "paginationStartContainer", paginationStart, false);
			}
			pagination = Template.replaceMarker(pagination, "pageCurrentTitle", pageCurrentTitle, false);

			if (pageCount > page + 1) {
				String paginationEnd = allTemplate.getSubMarkerContent("paginationEnd");
				paginationEnd = Template.replaceMarker(paginationEnd, "pageNext", Integer.toString(pageNext), false);
				paginationEnd = Template.replaceMarker(paginationEnd, "pageLast", Integer.toString(pageLast), false);

				pagination = Template.replaceMarker(pagination, "paginationEndContainer", paginationEnd, false);
			}
			allTemplate.replaceMarker("paginationContainer", pagination, false);
		}
		// pagination end

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

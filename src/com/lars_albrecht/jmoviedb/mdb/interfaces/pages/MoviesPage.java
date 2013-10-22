package com.lars_albrecht.jmoviedb.mdb.interfaces.pages;

import java.util.ArrayList;

import org.eclipse.jetty.server.Request;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.handler.OptionsHandler;
import com.lars_albrecht.mdb.main.core.handler.datahandler.AttributeHandler;
import com.lars_albrecht.mdb.main.core.handler.datahandler.MediaHandler;
import com.lars_albrecht.mdb.main.core.handler.datahandler.abstracts.ADataHandler;
import com.lars_albrecht.mdb.main.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.main.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.main.core.models.FileAttributeList;
import com.lars_albrecht.mdb.main.core.models.KeyValue;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;
import com.lars_albrecht.mdb.main.core.models.persistable.MediaItem;

/**
 * @author lalbrecht
 * 
 */
public class MoviesPage extends WebPage {

	public MoviesPage(final String actionname, final Request request, final MainController mainController, final WebInterface webInterface)
			throws Exception {
		super(actionname, request, mainController, webInterface);

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

		if ((request.getParameter("sortorder") != null) && (request.getParameter("sortorder") != null)) {
			sortOrder = request.getParameter("sortorder");
		}
		sortOrder = (sortOrder == null ? listOrderOption : sortOrder);

		if ((request.getParameter("page") != null) && (request.getParameter("page") != null)
				&& request.getParameter("page").matches("(\\d){1,}")) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		this.setPageTemplate(this.fillMoviesContainer(this.getPageTemplate(), sortOrder, page, maxItemsForListPagingOption));
	}

	private Template fillMoviesContainer(final Template moviesTemplate,
			final String sortOrder,
			final int page,
			final Integer maxItemsForPagingOption) {
		final int maxElems = maxItemsForPagingOption;
		final int startIndex = (page > 0 ? ((page * (maxElems > 0 ? maxElems : 0))) : 0);

		final int maxExistingElems = this.mainController.getDataHandler().getRowCount(new FileItem(), "filetype='movie'");

		final String[] additionalHandlerData = {
				new MediaHandler<>().getClass().getCanonicalName(), new AttributeHandler<>().getClass().getCanonicalName()
		};

		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().getFileItemsForPaging(startIndex, maxElems,
				"filetype='movie'", sortOrder, additionalHandlerData);

		// pagination start
		final boolean showPagination = (fileItems.size() > 0) && (maxItemsForPagingOption > 0)
				&& (fileItems.size() > maxItemsForPagingOption) ? true : false;
		if (showPagination) {
			final int pageCount = (int) Math.ceil(new Double(maxExistingElems) / new Double(maxElems));
			final int pageFirst = 0; // ever 0
			final int pagePrevious = (page > 0 ? page - 1 : -1);
			final String pageCurrentTitle = Integer.toString(page + 1);
			final int pageNext = (pageCount > page ? page + 1 : -1);
			final int pageLast = (pageCount > page ? pageCount - 1 : -1);

			String pagination = moviesTemplate.getSubMarkerContent("pagination");

			pagination = Template.replaceMarker(pagination, "currentPage", Integer.toString(page + 1), false);
			pagination = Template.replaceMarker(pagination, "maxPage", Integer.toString(pageCount), false);

			if (page > 0) {
				String paginationStart = moviesTemplate.getSubMarkerContent("paginationStart");
				paginationStart = moviesTemplate.getSubMarkerContent("paginationStart");
				paginationStart = Template.replaceMarker(paginationStart, "pageFirst", Integer.toString(pageFirst), false);
				paginationStart = Template.replaceMarker(paginationStart, "pagePrevious", Integer.toString(pagePrevious), false);

				pagination = Template.replaceMarker(pagination, "paginationStartContainer", paginationStart, false);
			}
			pagination = Template.replaceMarker(pagination, "pageCurrentTitle", pageCurrentTitle, false);

			if (pageCount > (page + 1)) {
				String paginationEnd = moviesTemplate.getSubMarkerContent("paginationEnd");
				paginationEnd = Template.replaceMarker(paginationEnd, "pageNext", Integer.toString(pageNext), false);
				paginationEnd = Template.replaceMarker(paginationEnd, "pageLast", Integer.toString(pageLast), false);

				pagination = Template.replaceMarker(pagination, "paginationEndContainer", paginationEnd, false);
			}
			moviesTemplate.replaceMarker("paginationContainer", pagination, false);
		}
		// pagination end

		String fileList = moviesTemplate.getSubMarkerContent("moviesFileList");
		String fileListItems = "";
		String tempFileListItem = null;

		// TODO extract / refactor this code block to a general class.
		String itemTitle = null;
		String extractedName = null;
		for (final FileItem fileItem : fileItems) {
			extractedName = this.getExtractedName(fileItem);
			itemTitle = extractedName != null ? extractedName : fileItem.getName();

			tempFileListItem = moviesTemplate.getSubMarkerContent("moviesFileListItem");
			tempFileListItem = Template.replaceMarker(tempFileListItem, "name", itemTitle, false);

			// TODO Refactor / Recode. This is only proof of concept.
			final ArrayList<MediaItem> fileMediaItems = ((MediaHandler<?>) ADataHandler.getDataHandler(MediaHandler.class))
					.getHandlerDataForFileItem(fileItem);
			String imageToShow = null;
			String bigImageToShow = null;
			if (fileMediaItems != null) {
				for (final MediaItem mediaItem : fileMediaItems) {
					if (mediaItem.getName().equalsIgnoreCase("poster")) {
						imageToShow = this.getUrlFromMediaItem(mediaItem, 1);
						bigImageToShow = this.getUrlFromMediaItem(mediaItem, 2);
						break;
					}
				}
			}

			tempFileListItem = Template.replaceMarker(tempFileListItem, "itemImageUrl", imageToShow, false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "itemImageUrlBig", bigImageToShow, false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "itemId", fileItem.getId().toString(), true);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "added",
					Helper.getFormattedTimestamp(fileItem.getCreateTS().longValue(), null), false);
			tempFileListItem = Template.replaceMarker(tempFileListItem, "updated",
					Helper.getFormattedTimestamp(fileItem.getUpdateTS().longValue(), null), false);
			fileListItems += tempFileListItem;
		}
		fileList = Template.replaceMarker(fileList, "fileListItems", fileListItems, false);

		moviesTemplate.replaceMarker("content", fileList, false);

		return moviesTemplate;
	}

	private String getExtractedName(final FileItem fileItem) {
		@SuppressWarnings("unchecked")
		final ArrayList<FileAttributeList> attributesList = (ArrayList<FileAttributeList>) ADataHandler.getHandlerDataFromFileItem(
				fileItem, AttributeHandler.class);
		for (final String type : this.mainController.gettController().getAvailableTypes()) {
			if ((type != null) && type.equalsIgnoreCase(fileItem.getFiletype())) {
				final String[] titleExtractionPath = this.mainController.getMdbConfig().getTitleExtractionForFileType(type);
				if ((titleExtractionPath != null) && (titleExtractionPath.length == 3) && (attributesList != null)) {
					for (final FileAttributeList fileAttributeList : attributesList) {
						if (fileAttributeList.getInfoType().equalsIgnoreCase(titleExtractionPath[0])
								&& fileAttributeList.getSectionName().equalsIgnoreCase(titleExtractionPath[1])
								&& (fileAttributeList.getKeyValues() != null)) {
							for (final KeyValue<String, Object> keyValue : fileAttributeList.getKeyValues()) {
								if (keyValue.getKey().getKey().equalsIgnoreCase(titleExtractionPath[2])) {
									return keyValue.getValue().getValue().toString();
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getTemplateName() {
		return "movies";
	}

	@Override
	public String getTitle() {
		return "Filme";
	}

	private String getUrlFromMediaItem(final MediaItem mediaItem, final Integer size) {
		if ((mediaItem.getOptions().get(MediaItem.OPTION_WEB_ISDIRECT) != null)
				&& (mediaItem.getOptions().get(MediaItem.OPTION_WEB_ISDIRECT) == Boolean.TRUE)) {
			return mediaItem.getUri().toString();
		} else {
			if (mediaItem.getOptions().get(MediaItem.OPTION_WEB_BASE_PATH) != null) {
				return mediaItem.getOptions().get(MediaItem.OPTION_WEB_BASE_PATH)
						+ ((ArrayList<?>) (Helper.explode((String) mediaItem.getOptions().get(MediaItem.OPTION_SIZES), ","))).get(size)
								.toString() + mediaItem.getUri().toString();
			} else {
				return mediaItem.getUri().toString();
			}
		}
	}

}

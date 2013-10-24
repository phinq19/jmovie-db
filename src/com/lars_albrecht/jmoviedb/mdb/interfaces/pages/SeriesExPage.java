package com.lars_albrecht.jmoviedb.mdb.interfaces.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.server.Request;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.jmoviedb.mdb.sorter.FileItemSortSeriesRegExName;
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
public class SeriesExPage extends WebPage {

	public SeriesExPage(final String actionname, final Request request, final MainController mainController, final WebInterface webInterface)
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

	private Template fillMoviesContainer(final Template seriesTemplate,
			final String sortOrder,
			final int page,
			final Integer maxItemsForPagingOption) {
		final int maxElems = maxItemsForPagingOption;
		@SuppressWarnings("unused")
		final int startIndex = (page > 0 ? ((page * (maxElems > 0 ? maxElems : 0))) : 0);

		final int maxExistingElems = this.mainController.getDataHandler().getRowCount(new FileItem(), "filetype='serie'");

		final String[] additionalHandlerData = {
				new MediaHandler<>().getClass().getCanonicalName(), new AttributeHandler<>().getClass().getCanonicalName()
		};

		// final ArrayList<FileItem> fileItems =
		// this.mainController.getDataHandler().getFileItemsForPaging(startIndex,
		// maxElems,
		// "filetype='serie'", sortOrder, additionalHandlerData);
		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().findAllFileItemForStringInAttributesByKey(
				"collection_id", true, "name", additionalHandlerData);

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

			String pagination = seriesTemplate.getSubMarkerContent("pagination");

			pagination = Template.replaceMarker(pagination, "currentPage", Integer.toString(page + 1), false);
			pagination = Template.replaceMarker(pagination, "maxPage", Integer.toString(pageCount), false);

			if (page > 0) {
				String paginationStart = seriesTemplate.getSubMarkerContent("paginationStart");
				paginationStart = seriesTemplate.getSubMarkerContent("paginationStart");
				paginationStart = Template.replaceMarker(paginationStart, "pageFirst", Integer.toString(pageFirst), false);
				paginationStart = Template.replaceMarker(paginationStart, "pagePrevious", Integer.toString(pagePrevious), false);

				pagination = Template.replaceMarker(pagination, "paginationStartContainer", paginationStart, false);
			}
			pagination = Template.replaceMarker(pagination, "pageCurrentTitle", pageCurrentTitle, false);
			if (pageCount > (page + 1)) {
				String paginationEnd = seriesTemplate.getSubMarkerContent("paginationEnd");
				paginationEnd = Template.replaceMarker(paginationEnd, "pageNext", Integer.toString(pageNext), false);
				paginationEnd = Template.replaceMarker(paginationEnd, "pageLast", Integer.toString(pageLast), false);

				pagination = Template.replaceMarker(pagination, "paginationEndContainer", paginationEnd, false);
			}
			seriesTemplate.replaceMarker("paginationContainer", pagination, false);
		}
		// pagination end

		// TODO extract / refactor this code block to a general class.
		String itemTitle = null;
		String extractedName = null;

		final ConcurrentHashMap<Integer, ArrayList<FileItem>> sortedList = new ConcurrentHashMap<Integer, ArrayList<FileItem>>();
		int collectionId = -1;
		for (final FileItem fileItem : fileItems) {
			collectionId = this.getCollectionId(fileItem);
			if (!sortedList.containsKey(collectionId)) {
				sortedList.put(collectionId, new ArrayList<FileItem>());
			}
			sortedList.get(collectionId).add(fileItem);
		}

		// sort sortedList for each series
		final FileItemSortSeriesRegExName sorter = new FileItemSortSeriesRegExName(FileItemSortSeriesRegExName.SORT_DESC);
		for (final Entry<Integer, ArrayList<FileItem>> entry : sortedList.entrySet()) {
			Collections.sort(entry.getValue(), sorter);
		}

		String seriesListContainer = "";
		String seriesList = seriesTemplate.getSubMarkerContent("seriesList");
		String seriesFileList = null;
		String fileListItems = "";
		String seriesFileListItem = null;

		boolean isSetSeriesTitle = false;
		boolean isSetSeriesBanner = false;
		for (final Entry<Integer, ArrayList<FileItem>> fileItemListEntry : sortedList.entrySet()) {
			isSetSeriesTitle = false;
			isSetSeriesBanner = false;

			seriesList = seriesTemplate.getSubMarkerContent("seriesList");
			seriesFileList = seriesTemplate.getSubMarkerContent("seriesFileList");
			fileListItems = "";
			for (final FileItem fileItem : fileItemListEntry.getValue()) {
				if (!isSetSeriesBanner) {
					@SuppressWarnings("unchecked")
					final ArrayList<MediaItem> itemFileMedia = (ArrayList<MediaItem>) ADataHandler.getHandlerDataFromFileItem(fileItem,
							MediaHandler.class);

					final MediaItem mediaItemBanner = MediaHandler.getMediaItemByName(itemFileMedia, "banner");
					if (mediaItemBanner != null) {
						mediaItemBanner.getUri().toString();
						String banner = seriesTemplate.getSubMarkerContent("seriesTitleBanner");
						banner = Template.replaceMarker(banner, "seriesBanner", mediaItemBanner.getUri().toString(), false);
						banner = Template.replaceMarker(banner, "seriesTitle",
								(String) AttributeHandler.getAttributeValueByKey(fileItem, "title"), true);
						seriesList = Template.replaceMarker(seriesList, "seriestitlebanner", banner, false);
						isSetSeriesBanner = true;
					}
				}
				if (!isSetSeriesTitle && !isSetSeriesBanner) {
					String title = seriesTemplate.getSubMarkerContent("seriesTitleStr");
					title = Template.replaceMarker(title, "seriesTitle",
							(String) AttributeHandler.getAttributeValueByKey(fileItem, "title"), false);
					seriesList = Template.replaceMarker(seriesList, "seriestitlebanner", title, false);

					isSetSeriesTitle = true;
				}

				extractedName = this.getExtractedName(fileItem);
				itemTitle = extractedName != null ? extractedName : fileItem.getName();

				seriesFileListItem = seriesTemplate.getSubMarkerContent("seriesFileListItem");
				seriesFileListItem = Template.replaceMarker(seriesFileListItem, "name", itemTitle, false);

				// TODO Refactor / Recode. This is only proof of concept.
				@SuppressWarnings("unchecked")
				final ArrayList<MediaItem> fileMediaItems = (ArrayList<MediaItem>) ADataHandler.getHandlerDataFromFileItem(fileItem,
						MediaHandler.class);

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

				seriesFileListItem = Template.replaceMarker(seriesFileListItem, "itemImageUrl", imageToShow, false);
				seriesFileListItem = Template.replaceMarker(seriesFileListItem, "itemImageUrlBig", bigImageToShow, false);
				seriesFileListItem = Template.replaceMarker(seriesFileListItem, "itemId", fileItem.getId().toString(), true);
				seriesFileListItem = Template.replaceMarker(seriesFileListItem, "added",
						Helper.getFormattedTimestamp(fileItem.getCreateTS().longValue(), null), false);
				seriesFileListItem = Template.replaceMarker(seriesFileListItem, "updated",
						Helper.getFormattedTimestamp(fileItem.getUpdateTS().longValue(), null), false);

				fileListItems += seriesFileListItem;
			}
			seriesFileList = Template.replaceMarker(seriesFileList, "fileListItems", fileListItems, false);

			seriesList = Template.replaceMarker(seriesList, "singleseries", seriesFileList, false);
			seriesListContainer += seriesList;
		}

		seriesTemplate.replaceMarker("content", seriesListContainer, false);

		return seriesTemplate;
	}

	private int getCollectionId(final FileItem fileItem) {
		final Object resultObj = AttributeHandler.getAttributeValueByKey(fileItem, "collection_id");
		if ((resultObj == null) || resultObj.equals("")) {
			return -1;
		} else {
			return Integer.parseInt((String) resultObj);
		}
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
		return "seriesex";
	}

	@Override
	public String getTitle() {
		return "Serien";
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

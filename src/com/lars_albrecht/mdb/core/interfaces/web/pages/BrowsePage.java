/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String browserContainer = browseTemplateWithBrowseContainer.getSubMarkerContent("browser");

		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().getFileItems();

		final TreeMap<String, ArrayList<FileItem>> fileList = new TreeMap<String, ArrayList<FileItem>>();
		final String[] keyList = {
				"0-9", "AÄ", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "OÖ", "P", "Q", "R", "S", "T", "UÜ", "V",
				"W", "X", "Y", "Z", "^\\d^\\w"
		};

		// TODO fix output of key (Create a map with key/values)
		Pattern pattern = null;
		Matcher matcher = null;
		for (final FileItem fileItem : fileItems) {
			for (final String patternKey : keyList) {
				pattern = Pattern.compile("[" + patternKey + "]");
				matcher = pattern.matcher(fileItem.getName().substring(0, 1));
				if (matcher.find()) {
					if (!fileList.containsKey(patternKey)) {
						fileList.put(patternKey, new ArrayList<FileItem>());
					}
					fileList.get(patternKey).add(fileItem);
					break;
				}
			}
		}

		String keyListItems = "";
		String tempKeyListItem = null;

		String fileListItemSingle = "";
		String fileListItemSingles = "";
		String tempFileListItemSingle = null;
		String tempFileListItemSingles = null;

		for (final Entry<String, ArrayList<FileItem>> entry : fileList.entrySet()) {
			fileListItemSingle = browseTemplateWithBrowseContainer.getSubMarkerContent("browserFileListItemSingle");
			fileListItemSingle = Template.replaceMarker(fileListItemSingle, "title", entry.getKey(), false);
			fileListItemSingle = Template.replaceMarker(fileListItemSingle, "class", "browseFileTable key_" + entry.getKey(), false);

			// set key/title
			tempKeyListItem = browseTemplateWithBrowseContainer.getSubMarkerContent("browserKeyListItem");
			tempKeyListItem = Template.replaceMarker(tempKeyListItem, "key", entry.getKey(), false);
			keyListItems += tempKeyListItem;

			// set files
			tempFileListItemSingles = "";
			for (final FileItem fileItem : entry.getValue()) {
				tempFileListItemSingle = browseTemplateWithBrowseContainer.getSubMarkerContent("browserFileListItem");
				tempFileListItemSingle = Template.replaceMarker(tempFileListItemSingle, "name", fileItem.getName(), false);
				tempFileListItemSingle = Template.replaceMarker(tempFileListItemSingle, "itemId", fileItem.getId().toString(), false);
				tempFileListItemSingles += tempFileListItemSingle;
			}
			fileListItemSingle = Template.replaceMarker(fileListItemSingle, "fileListItemSingles", tempFileListItemSingles, false);
			fileListItemSingles += fileListItemSingle;
		}

		browserContainer = Template.replaceMarker(browserContainer, "keyListItems", keyListItems, false);
		browserContainer = Template.replaceMarker(browserContainer, "fileListItems", fileListItemSingles, false);

		browseTemplateWithBrowseContainer.replaceMarker("browsercontainer", browserContainer, false);
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
		final ConcurrentHashMap<String, ArrayList<FileItem>> noInfoList = this.mainController.getDataHandler().getNoInfoFileItems();
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
				noInfoItemList = "";
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

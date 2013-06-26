/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.DataHandler;
import com.lars_albrecht.mdb.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class ShowInfoControlPage extends WebPage {

	public ShowInfoControlPage(final String actionname, final WebServerRequest request, final MainController mainController)
			throws Exception {
		super(actionname, request, mainController);

		this.setPageTemplate(this.generateInfoControlView());

	}

	private Template generateInfoControlView() {
		Template infoControlTemplate = this.getPageTemplate();

		infoControlTemplate = this.fillBasicInfoContainer(infoControlTemplate);
		infoControlTemplate = this.fillControlContainer(infoControlTemplate);
		infoControlTemplate = this.fillDuplicateContainer(infoControlTemplate);
		infoControlTemplate = this.fillNoInfoContainer(infoControlTemplate);
		infoControlTemplate = this.fillMissingContainer(infoControlTemplate);

		return infoControlTemplate;
	}

	private Template fillBasicInfoContainer(final Template template) {
		final Template templateWithBasicInfoContainer = template;
		final ConcurrentHashMap<String, Object> info = this.mainController.getDataHandler().getInfoFromDatabase();

		String informationContainer = templateWithBasicInfoContainer.getSubMarkerContent("information");
		informationContainer = Template.replaceMarker(informationContainer, "fileCount", Integer.toString((Integer) info.get("fileCount")),
				false);
		informationContainer = Template.replaceMarker(informationContainer, "keyCount", Integer.toString((Integer) info.get("keyCount")),
				false);
		informationContainer = Template.replaceMarker(informationContainer, "valueCount",
				Integer.toString((Integer) info.get("valueCount")), false);
		informationContainer = Template.replaceMarker(informationContainer, "missingCount",
				Integer.toString((Integer) info.get("missingCount")), false);
		informationContainer = Template.replaceMarker(informationContainer, "countOfFiletypes",
				Helper.implode((Map<?, ?>) info.get("filesWithFiletype"), ", ", null, null, " (", ")", "<span class=\"infoListEntry\">",
						"</span>", false), false);

		templateWithBasicInfoContainer.replaceMarker("basicInfoContainer", informationContainer, false);

		return templateWithBasicInfoContainer;
	}

	private Template fillControlContainer(final Template template) {
		final Template templateWithControlContainer = template;

		String statusAreaContainer = templateWithControlContainer.getSubMarkerContent("statusArea");
		final String statusMessageContainer = templateWithControlContainer.getSubMarkerContent("statusMessage");

		String controlViewContainer = templateWithControlContainer.getSubMarkerContent("controlView");
		String finderHrefString = "?action=showInfoControl&do=startFinder";
		String finderClassString = "";
		String collectorHrefString = "?action=showInfoControl&do=startCollectors";
		String collectorClassString = "";

		final ArrayList<String> statusMessages = new ArrayList<String>();

		// isStartFinder
		if (this.request.getGetParams().containsKey("do") && (this.request.getGetParams().get("do") != null)
				&& this.request.getGetParams().get("do").equalsIgnoreCase("startFinder")) {
			finderHrefString = "javascript:void(0)";
			finderClassString = "disabled";

			statusMessages.add("Files will be refreshed ...");
		}

		// isStartCollectors
		if (this.request.getGetParams().containsKey("do") && (this.request.getGetParams().get("do") != null)
				&& this.request.getGetParams().get("do").equalsIgnoreCase("startCollectors")) {
			collectorHrefString = "javascript:void(0)";
			collectorClassString = "disabled";

			statusMessages.add("Collections will be refreshed ...");

			final ArrayList<FileItem> fileList = ObjectHandler.castObjectListToFileItemList(this.mainController.getDataHandler().findAll(
					new FileItem(), null, null));
			if ((fileList != null) && (fileList.size() > 0)) {
				statusMessages.add("Collections can be refreshed ... work in progress");
				try {
					this.mainController.getcController().collectInfos(fileList);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else {
				statusMessages.add("Collections cannot be refreshed" + (fileList.size() == 0 ? ", because no files are available" : "")
						+ ". Process stopped.");
			}
		}

		controlViewContainer = Template.replaceMarker(controlViewContainer, "finderHref", finderHrefString, false);
		controlViewContainer = Template.replaceMarker(controlViewContainer, "finderClass", finderClassString, false);
		controlViewContainer = Template.replaceMarker(controlViewContainer, "collectorHref", collectorHrefString, false);
		controlViewContainer = Template.replaceMarker(controlViewContainer, "collectorClass", collectorClassString, false);

		String statusMessagesContainer = "";
		if (statusMessages.size() > 0) {
			for (final String statusMessage : statusMessages) {
				statusMessagesContainer += Template.replaceMarker(statusMessageContainer, "message", statusMessage, false);
			}
			statusAreaContainer = Template.replaceMarker(statusAreaContainer, "statusMessages", statusMessagesContainer, false);
		} else {
			statusAreaContainer = "";
		}

		controlViewContainer = Template.replaceMarker(controlViewContainer, "statusArea", statusAreaContainer, false);

		templateWithControlContainer.replaceMarker("controlView", controlViewContainer, false);

		return templateWithControlContainer;
	}

	private Template fillDuplicateContainer(final Template template) {
		final Template templateWithDuplicateContainer = template;
		templateWithDuplicateContainer.replaceMarker("duplicatecontainer", templateWithDuplicateContainer.getSubMarkerContent("duplicate"),
				false);
		return templateWithDuplicateContainer;
	}

	private Template fillNoInfoContainer(final Template template) {
		final Template templateWithNoInfoContainer = template;
		final ConcurrentHashMap<String, ArrayList<FileItem>> noInfoList = this.mainController.getDataHandler().getNoInfoFileItems(null);
		if (noInfoList.size() > 0) {
			String noInfoContainer = template.getSubMarkerContent("noinformation");

			String noInfoTables = "";
			String noInfoTable = "";

			String noInfoItemList = "";
			String tempNoInfoItemList = null;

			int noItemCounter = 0;
			for (final Entry<String, ArrayList<FileItem>> entry : noInfoList.entrySet()) {
				// TODO create method replaceAllMarker to replace a bunch of
				// marker
				noInfoTable = template.getSubMarkerContent("noinformationTable");
				noInfoTable = Template.replaceMarker(noInfoTable, "collectorName", entry.getKey(), false);
				noInfoItemList = "";
				for (final FileItem fileItem : entry.getValue()) {
					tempNoInfoItemList = template.getSubMarkerContent("noinformationItem");
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

			templateWithNoInfoContainer.replaceMarker("noinfocontainer", noInfoContainer, false);
		}

		return templateWithNoInfoContainer;
	}

	private Template fillMissingContainer(final Template template) {
		final Template templateWithMissingFileItemsContainer = template;
		this.mainController.getDataHandler().reloadData(DataHandler.RELOAD_MISSINGFILEITEMS);
		final ArrayList<FileItem> missingFilesList = this.mainController.getDataHandler().getMissingFileItems();
		if (missingFilesList.size() > 0) {
			String missingContainer = template.getSubMarkerContent("missing");

			String missingItemList = "";
			String tempMissingItemList = null;

			int missingCounter = 0;
			// TODO create method replaceAllMarker to replace a bunch of
			// marker
			for (final FileItem fileItem : missingFilesList) {
				tempMissingItemList = template.getSubMarkerContent("missingItem");
				tempMissingItemList = Template.replaceMarker(tempMissingItemList, "noInfoItemId", fileItem.getId().toString(), true);
				tempMissingItemList = Template.replaceMarker(tempMissingItemList, "noInfoItemTitle", fileItem.getName(), false);
				tempMissingItemList = Template.replaceMarker(tempMissingItemList, "oddeven", ((missingCounter % 2) == 0 ? "even" : "odd"),
						false);
				missingItemList += tempMissingItemList;
				missingCounter++;
			}
			missingContainer = Template.replaceMarker(missingContainer, "missingItems", missingItemList, false);
			missingContainer = Template.replaceMarker(missingContainer, "missingcounter", Integer.toString(missingCounter), false);
			templateWithMissingFileItemsContainer.replaceMarker("missingcontainer", missingContainer, false);
		}

		return templateWithMissingFileItemsContainer;
	}

	@Override
	public String getTitle() {
		return "Info / Control";
	}

	@Override
	public String getTemplateName() {
		return "infocontrol";
	}

}

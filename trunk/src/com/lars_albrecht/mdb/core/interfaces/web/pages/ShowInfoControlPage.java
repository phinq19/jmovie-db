/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
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

		final ConcurrentHashMap<String, Object> info = this.mainController.getDataHandler().getInfoFromDatabase();
		final Template infoControlViewTemplate = this.getPageTemplate();
		String informationContainer = infoControlViewTemplate.getSubMarkerContent("information");
		String controlViewContainer = infoControlViewTemplate.getSubMarkerContent("controlView");
		String statusAreaContainer = infoControlViewTemplate.getSubMarkerContent("statusArea");
		final String statusMessageContainer = infoControlViewTemplate.getSubMarkerContent("statusMessage");

		informationContainer = Template.replaceMarker(informationContainer, "fileCount", Integer.toString((Integer) info.get("fileCount")),
				false);
		informationContainer = Template.replaceMarker(informationContainer, "keyCount", Integer.toString((Integer) info.get("keyCount")),
				false);
		informationContainer = Template.replaceMarker(informationContainer, "valueCount",
				Integer.toString((Integer) info.get("valueCount")), false);
		informationContainer = Template.replaceMarker(informationContainer, "countOfFiletypes",
				Helper.implode((Map<?, ?>) info.get("filesWithFiletype"), ", ", null, null, " (", ")", "<span class=\"infoListEntry\">",
						"</span>", false), false);

		String finderHrefString = "?action=showInfoControl&do=startFinder";
		String finderClassString = "";
		String collectorHrefString = "?action=showInfoControl&do=startCollectors";
		String collectorClassString = "";

		final ArrayList<String> statusMessages = new ArrayList<String>();

		// isStartFinder
		if (request.getGetParams().containsKey("do") && (request.getGetParams().get("do") != null)
				&& request.getGetParams().get("do").equalsIgnoreCase("startFinder")) {
			finderHrefString = "javascript:void(0)";
			finderClassString = "disabled";

			statusMessages.add("Files will be refreshed ...");
		}

		// isStartCollectors
		if (request.getGetParams().containsKey("do") && (request.getGetParams().get("do") != null)
				&& request.getGetParams().get("do").equalsIgnoreCase("startCollectors")) {
			collectorHrefString = "javascript:void(0)";
			collectorClassString = "disabled";

			statusMessages.add("Collections will be refreshed ...");

			final ArrayList<FileItem> fileList = ObjectHandler.castObjectListToFileItemList(this.mainController.getDataHandler().findAll(
					new FileItem(), null));
			if ((fileList != null) && (fileList.size() > 0)) {
				statusMessages.add("Collections can be refreshed ... work in progress");
				this.mainController.getcController().collectInfos(fileList);
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

		infoControlViewTemplate.replaceMarker("content", informationContainer, false);
		infoControlViewTemplate.replaceMarker("controlView", controlViewContainer, false);

		this.setPageTemplate(infoControlViewTemplate);

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

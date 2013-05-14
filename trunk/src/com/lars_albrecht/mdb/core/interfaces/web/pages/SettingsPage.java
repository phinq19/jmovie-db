/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.OptionsHandler;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;

/**
 * @author lalbrecht
 * 
 */
public class SettingsPage extends WebPage {

	private ConcurrentHashMap<String, String>	searchOptionsList	= null;

	public SettingsPage(final String actionname, final WebServerRequest request, final MainController mainController) throws Exception {
		super(actionname, request, mainController);

		this.searchOptionsList = new ConcurrentHashMap<String, String>();
		this.searchOptionsList.put("Datei ID", "fileInformation.id");
		this.searchOptionsList.put("Dateiname", "fileInformation.name");
		this.searchOptionsList.put("Dateipfad", "fileInformation.fullpath");
		this.searchOptionsList.put("Dateigröße", "fileInformation.size");
		this.searchOptionsList.put("Dateityp", "fileInformation.filetype");
		this.searchOptionsList.put("Hinzugefügt am", "fileInformation.createdTS");

		if (request.getGetParams().containsKey("do") && request.getGetParams().get("do").equalsIgnoreCase("save")) {
			this.saveSettings(request);
		}

		this.setPageTemplate(this.generateSettingsView(this.getPageTemplate()));
	}

	/**
	 * Save settings from post params to database.
	 * 
	 * @param request
	 */
	private void saveSettings(final WebServerRequest request) {
		for (final Entry<String, String> entry : request.getPostParams().entrySet()) {
			OptionsHandler.setOption(entry.getKey(), entry.getValue());
		}
	}

	private Template generateSettingsView(final Template settingsTemplate) {
		String settingsFieldsContainer = settingsTemplate.getSubMarkerContent("settingsFields");

		// replace searchSettings
		settingsFieldsContainer = Template.replaceMarker(settingsFieldsContainer, "searchSettings",
				this.generateSearchSettingsView(settingsTemplate), false);

		// fill settings in contentmarker
		settingsTemplate.replaceMarker("content", settingsFieldsContainer, false);

		return settingsTemplate;
	}

	private String generateSearchSettingsView(final Template settingsTemplate) {
		String searchResultOrderOption = (String) OptionsHandler.getOption("sortOption");
		if (searchResultOrderOption == null) {
			searchResultOrderOption = "fileInformation.name";
			OptionsHandler.setOption("sortOption", searchResultOrderOption);
		}
		String searchSettingsContainer = settingsTemplate.getSubMarkerContent("searchSettings");
		String optionsContainer = "";
		String tempOptionContainer = null;
		for (final Entry<String, String> entry : this.searchOptionsList.entrySet()) {
			tempOptionContainer = settingsTemplate.getSubMarkerContent("selectOption");
			tempOptionContainer = Template.replaceMarker(tempOptionContainer, "optionValue", entry.getValue(), false);
			tempOptionContainer = Template.replaceMarker(tempOptionContainer, "optionTitle", entry.getKey(), false);
			if (entry.getValue().equalsIgnoreCase(searchResultOrderOption)) {
				tempOptionContainer = Template.replaceMarker(tempOptionContainer, "selected", "selected=\"selected\"", false);
			} else {
				tempOptionContainer = Template.replaceMarker(tempOptionContainer, "selected", "", false);
			}
			optionsContainer += tempOptionContainer;
		}

		searchSettingsContainer = Template.replaceMarker(searchSettingsContainer, "sortOptions", optionsContainer, false);

		return searchSettingsContainer;
	}

	@Override
	public String getTitle() {
		return "Einstellungen";
	}

	@Override
	public String getTemplateName() {
		return "settings";
	}

}

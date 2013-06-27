/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web.pages;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.Template;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerRequest;
import com.lars_albrecht.mdb.core.interfaces.web.abstracts.WebPage;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.KeyValue;

/**
 * @author lalbrecht
 * 
 */
public class FileDetailsPage extends WebPage {

	private WebInterface	webInterface	= null;

	public FileDetailsPage(final String actionname, final WebServerRequest request, final MainController mainController,
			final WebInterface webInterface) throws Exception {
		super(actionname, request, mainController, webInterface);
		this.webInterface = webInterface;

		if (request.getGetParams().containsKey("fileId") && (request.getGetParams().get("fileId") != null)) {
			final Integer fileId = Integer.parseInt(request.getGetParams().get("fileId"));

			if ((fileId != null) && (fileId > 0)) {
				final FileItem tempFileItem = this.mainController.getDataHandler().findAllInfoForAllByFileId(fileId);
				this.setPageTemplate(this.generateDetailView(tempFileItem));
			}
		} else {
			this.set404Error();
		}

	}

	private Template generateDetailView(final FileItem item) {
		final Template detailViewTemplate = this.getPageTemplate();

		// if file is set
		if ((item != null) && (item.getId() != null)) {
			// set default infos
			detailViewTemplate.replaceMarker("content", detailViewTemplate.getSubMarkerContent("file"), Boolean.FALSE);

			detailViewTemplate.replaceMarker("title", item.getName() + " (" + item.getId() + ")", Boolean.TRUE);
			detailViewTemplate.replaceMarker("path", item.getFullpath().replaceAll("\\\\", "\\\\\\\\\\\\\\\\"), Boolean.FALSE);

			if (item.getSize() != null) {
				String listWrapper = null;
				listWrapper = detailViewTemplate.getSubMarkerContent("listwrapper");
				listWrapper = Template.replaceMarker(listWrapper, "key", "Size", Boolean.TRUE);
				listWrapper = Template.replaceMarker(listWrapper, "value", Helper.getHumanreadableFileSize(item.getSize()), Boolean.TRUE);

				detailViewTemplate.replaceMarker("listwrapperSize", listWrapper, Boolean.TRUE);
			}

			if (item.getCreateTS() != null) {
				String listWrapper = null;
				listWrapper = detailViewTemplate.getSubMarkerContent("listwrapper");
				listWrapper = Template.replaceMarker(listWrapper, "key", "Added", Boolean.TRUE);
				listWrapper = Template.replaceMarker(listWrapper, "value",
						Helper.getFormattedTimestamp(item.getCreateTS().longValue(), null), Boolean.TRUE);

				detailViewTemplate.replaceMarker("listwrapperAdded", listWrapper, Boolean.TRUE);
			}

			if (item.getFiletype() != null) {
				String listWrapper = null;
				listWrapper = detailViewTemplate.getSubMarkerContent("listwrapper");
				listWrapper = Template.replaceMarker(listWrapper, "key", "Type", Boolean.TRUE);
				listWrapper = Template.replaceMarker(listWrapper, "value", item.getFiletype(), Boolean.TRUE);

				detailViewTemplate.replaceMarker("listwrapperType", listWrapper, Boolean.TRUE);
			}

			// if file has attributes
			if ((item.getAttributes() != null) && (item.getAttributes().size() > 0)) {
				// get marker for attributes
				String attributes = detailViewTemplate.getSubMarkerContent("attributes");

				String currentInfoType = null;
				int i = 0;

				String attributesList = "";
				String sectionList = "";
				String attributeSectionList = "";
				String images = "";
				String currentSection = null;
				// for each attribute ...
				for (final FileAttributeList attributeList : item.getAttributes()) {
					currentSection = attributeList.getSectionName();
					if ((currentInfoType == null)
							|| !currentInfoType.equalsIgnoreCase(attributeList.getKeyValues().get(0).getKey().getInfoType())) {
						if (i > 0) {
							// finish section and add to list
							attributeSectionList += Template.replaceMarker(attributesList, "sections", sectionList, Boolean.TRUE);
							attributesList = "";
							sectionList = "";
						}

						// create a new one for each infoType
						currentInfoType = attributeList.getKeyValues().get(0).getKey().getInfoType();
						attributesList = detailViewTemplate.getSubMarkerContent("attributesList");
						attributesList = Template.replaceMarker(attributesList, "infotype-title", currentInfoType, Boolean.FALSE);
						attributesList = Template.replaceMarker(attributesList, "id", currentInfoType, Boolean.FALSE);
					}

					// fill sectionlist
					if ((!currentSection.equalsIgnoreCase("images")) && (attributeList.getKeyValues() != null)
							&& (attributeList.getKeyValues().size() > 0)) {
						sectionList += detailViewTemplate.getSubMarkerContent("attributeListSection");
						sectionList = Template.replaceMarker(sectionList, "sectionname", attributeList.getSectionName(), Boolean.TRUE);
						sectionList = Template.replaceMarker(sectionList, "keyTitle", "Key", Boolean.TRUE);
						sectionList = Template.replaceMarker(sectionList, "valueTitle", "Value", Boolean.TRUE);

						FileAttributeList attributeListCpy = null;
						try {
							attributeListCpy = (FileAttributeList) attributeList.clone();

							int evenOdd = 0;
							String rows = "";
							// fill rows
							for (final KeyValue<String, Object> keyValue : attributeList.getKeyValues()) {
								if (attributeListCpy.getKeyValues().contains(keyValue)) {
									rows += detailViewTemplate.getSubMarkerContent("attributesListSectionItem");
									rows = Template.replaceMarker(rows, "oddeven", ((evenOdd % 2) == 0 ? "even" : "odd"), Boolean.TRUE);
									rows = Template.replaceMarker(rows, "key", keyValue.getKey().getKey(), Boolean.TRUE);

									String value = "";
									final ArrayList<Object> tempList = this.getValuesForKey(attributeListCpy, keyValue.getKey().getKey());
									for (int j = 0; j < tempList.size(); j++) {
										if (j != 0) {
											value += ", ";
										}
										if (keyValue.getKey().getSearchable()) {
											value += "<a href=\"?"
													+ "action=showSearchresults&searchStr="
													+ URLEncoder.encode(keyValue.getKey().getKey() + "=" + "\"" + tempList.get(j) + "\"",
															"utf-8") + "\">" + tempList.get(j) + "</a>";
										} else {
											value += this.webInterface.getFileDetailsOutputItem().getValue(currentInfoType,
													attributeList.getSectionName(), keyValue, (String) tempList.get(j));
										}
									}

									rows = Template.replaceMarker(rows, "value", value, Boolean.TRUE);
									attributeListCpy = this.removeKeysFromFileAttributeList(attributeListCpy, keyValue.getKey().getKey());
									evenOdd++;
								}
							}
							// replace row marker with real rows
							sectionList = Template.replaceMarker(sectionList, "rows", rows, Boolean.TRUE);
						} catch (final CloneNotSupportedException e) {
							e.printStackTrace();
						} catch (final UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} else if (attributeList.getSectionName().equalsIgnoreCase("images")) {
						String imageContainer = "";
						String tempImageContainer = null;
						for (final KeyValue<String, Object> keyValue : attributeList.getKeyValues()) {
							if (keyValue.getKey().getKey().equalsIgnoreCase("poster_path")) {
								tempImageContainer = detailViewTemplate.getSubMarkerContent("image");
								tempImageContainer = Template.replaceMarker(tempImageContainer, "imageSrc", (String) keyValue.getValue()
										.getValue(), false);
								tempImageContainer = Template.replaceMarker(tempImageContainer, "imageClass", "posterImage", false);
								tempImageContainer = Template.replaceMarker(tempImageContainer, "imageTitle", (String) keyValue.getValue()
										.getValue(), false);

								imageContainer += tempImageContainer;
							} else {
								// TODO create galley for the other pictures
							}
						}
						images = imageContainer;
					}
					i++;
				}

				// add last attribute sections
				attributeSectionList += Template.replaceMarker(attributesList, "sections", sectionList, Boolean.TRUE);

				// add all attribute sections to attributes
				attributes = Template.replaceMarker(attributes, "attributesList", attributeSectionList, Boolean.TRUE);

				detailViewTemplate.replaceMarker("images", images, Boolean.FALSE);

				// add all attributes to template
				detailViewTemplate.replaceMarker("attributes", attributes, Boolean.TRUE);
			}
		} else {
			detailViewTemplate.replaceMarker("content", detailViewTemplate.getSubMarkerContent("nofile"), Boolean.TRUE);
			detailViewTemplate.replaceMarker("nofileString", "Keine Datei ausgewählt", Boolean.TRUE);
		}

		return detailViewTemplate;
	}

	private ArrayList<Object> getValuesForKey(final FileAttributeList list, final String key) {
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if ((list != null) && (list.getKeyValues().size() > 0) && (key != null)) {
			for (final KeyValue<String, Object> keyValue : list.getKeyValues()) {
				if ((keyValue != null) && (keyValue.getKey() != null) && keyValue.getKey().getKey().equals(key)
						&& (keyValue.getValue() != null)) {
					resultList.add(keyValue.getValue().getValue());
				}
			}
		}
		return resultList;
	}

	private FileAttributeList
			removeKeysFromFileAttributeList(final FileAttributeList list, final String key) throws CloneNotSupportedException {
		final FileAttributeList resultList = (FileAttributeList) list.clone();
		if ((list != null) && (list.getKeyValues().size() > 0) && (key != null)) {
			for (final KeyValue<String, Object> keyValue : list.getKeyValues()) {
				if ((keyValue != null) && (keyValue.getKey() != null) && keyValue.getKey().getKey().equals(key)
						&& (keyValue.getValue() != null)) {
					resultList.getKeyValues().remove(keyValue);
				}
			}
		}
		return resultList;
	}

	@Override
	public String getTitle() {
		String title;
		if (this.request.getGetParams().containsKey("fileId") && (this.request.getGetParams().get("fileId") != null)) {
			final Integer fileId = Integer.parseInt(this.request.getGetParams().get("fileId"));
			if ((fileId != null) && (fileId > 0)) {
				final FileItem tempFileItem = this.mainController.getDataHandler().findAllInfoForAllByFileId(fileId);
				if (tempFileItem != null) {
					title = "Detailansicht: " + tempFileItem.getName();
				} else {
					title = "Detailansicht: Keine gültige Datei gewählt";
				}
			} else {
				title = "Detailansicht: Keine gültige Datei gewählt";
			}
		} else {
			title = "Detailansicht: Keine Datei gewählt";
		}
		return title;
	}

	@Override
	public String getTemplateName() {
		return "filedetails";
	}

}

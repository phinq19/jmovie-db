package com.lars_albrecht.moviedb.gui.splitview;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.components.imagepanel.JImage;
import com.lars_albrecht.moviedb.components.labeled.JLabeledList;
import com.lars_albrecht.moviedb.components.labeled.JLabeledTextArea;
import com.lars_albrecht.moviedb.components.labeled.JLabeledTextInput;
import com.lars_albrecht.moviedb.components.labeled.abstracts.JLabeled;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.KeyValue;
import com.lars_albrecht.moviedb.utilities.Helper;

@SuppressWarnings("serial")
public class TabView extends JTabbedPane {

	private Controller controller = null;

	private ConcurrentHashMap<String, Component> componentList = null;

	public TabView(final Controller controller) {
		this.controller = controller;
		this.addComponents(this);

		this.setVisible(true);
	}

	private void addComponents(final JTabbedPane panel) {
		this.componentList = new ConcurrentHashMap<String, Component>();
		this.addTabs(panel);
	}

	private void addTabs(final JTabbedPane panel) {
		final ConcurrentHashMap<String, Object> tempTab = new ConcurrentHashMap<String, Object>();
		final FieldList fl = Controller.flTabs;
		Collections.sort(fl);
		JPanel tempPanel = null;
		String tabName = null;
		int i = 1;
		for(final FieldModel fieldModel : fl) {
			tabName = fieldModel.getName();
			tempPanel = (JPanel) (tempTab.containsKey(tabName) ? tempTab.get(tabName) : new JPanel(new GridBagLayout()));
			i = (tempTab.containsKey(tabName + "Counter") ? (Integer) (tempTab.get(tabName + "Counter")) : 1);
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridy = i;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(10, 10, 10, 10);
			gbc.weightx = 0.1;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.gridheight = 1;
			// add item to panel
			final Dimension preferredLabelSize = new Dimension(100, 20);
			if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == Integer.class)))
					|| (fieldModel.getType() == ViewInTab.TYPE_INT)) {
				final JLabeledTextInput tfTemp = new JLabeledTextInput(fieldModel.getAs(), "", JLabeled.LABELPOSITION_LEFT,
						preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled(Boolean.FALSE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && ((fieldModel.getField().getType() == String.class) || (fieldModel
					.getField().getType() == File.class))))
					|| (fieldModel.getType() == ViewInTab.TYPE_INPUT)) {
				final JLabeledTextInput tfTemp = new JLabeledTextInput(fieldModel.getAs(), "", JLabeled.LABELPOSITION_LEFT,
						preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled(Boolean.FALSE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == ArrayList.class)))
					|| (fieldModel.getType() == ViewInTab.TYPE_SELECT)) {
				gbc.gridheight = 2;
				final DefaultListModel<KeyValue<Integer, String>> listModel = new DefaultListModel<KeyValue<Integer, String>>();
				final ArrayList<KeyValue<Integer, String>> kvList = Controller.keyValueLists.get(fieldModel.getField().getName());
				if(kvList != null) {
					for(final KeyValue<Integer, String> kv : kvList) {
						listModel.addElement(kv);
					}
					final JLabeledList<KeyValue<Integer, String>> tfTemp = new JLabeledList<KeyValue<Integer, String>>(fieldModel
							.getAs(), listModel, JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
					tfTemp.setEnabled(Boolean.FALSE);
					tempPanel.add(tfTemp, gbc);
					this.componentList.put(fieldModel.getField().getName(), tfTemp);
					i++;
				}
			} else if(fieldModel.getType() == ViewInTab.TYPE_AREA) {
				gbc.gridheight = 2;
				final JLabeledTextArea tfTemp = new JLabeledTextArea(fieldModel.getAs(), "", JLabeled.LABELPOSITION_LEFT,
						preferredLabelSize, new Dimension(100, 100), 10, 10);
				tfTemp.setEnabled(Boolean.FALSE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
				// i++ to increase the fieldsize to 2 rows
				i++;
			} else if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == Image.class)))
					|| (fieldModel.getType() == ViewInTab.TYPE_IMAGE)) {
				gbc.fill = GridBagConstraints.NONE;
				final JImage tfTemp = new JImage();
				tfTemp.setPreferredSize(new Dimension(140, 200));
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else {
				System.out.println("NOT FOUND: " + fieldModel.getType() + " - " + fieldModel.getAs());
			}

			// save made changes
			tempTab.put(tabName, tempPanel);
			tempTab.put(tabName + "Counter", ++i);
		}

		// add tabs
		i = 0;
		for(final Map.Entry<String, Object> tabEntry : tempTab.entrySet()) {
			if(tabEntry.getValue() instanceof JPanel) {
				final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
				final JPanel filledPanel = (JPanel) tabEntry.getValue();
				for(int j = 0; j < Controller.apiScraper.size(); j++) {
					if(Controller.apiScraper.get(j).getTabTitle().equals(tabEntry.getKey())) {
						final JButton bRefreshButton = new JButton("Refresh");
						bRefreshButton.addActionListener(this.controller);
						this.componentList.put("refresh" + Controller.apiScraper.get(j).getPluginName(), bRefreshButton);
						filledPanel.add(bRefreshButton, gbc);
						break;
					}
				}
				final JPanel t = new JPanel(new GridBagLayout());
				final GridBagConstraints gbcRootPanel = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
				t.add(filledPanel, gbcRootPanel);
				panel.addTab(Helper.ucfirst(tabEntry.getKey()), new JScrollPane(t));
				i++;
			}
		}
		// for (final Map.Entry<String, Component> tabEntry :
		// this.componentList.entrySet()) {
		// System.out.println(tabEntry.getKey() + " - " + tabEntry.getValue());
		// }

	}

	/**
	 * @return the componentList
	 */
	public synchronized final ConcurrentHashMap<String, Component> getComponentList() {
		return this.componentList;
	}

	/**
	 * @param componentList
	 *            the componentList to set
	 */
	public synchronized final void setComponentList(final ConcurrentHashMap<String, Component> componentList) {
		this.componentList = componentList;
	}

}

package com.lars_albrecht.moviedb.gui.splitview;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.lars_albrecht.general.components.imagepanel.JImage;
import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;
import com.lars_albrecht.general.types.KeyValue;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;
import com.lars_albrecht.moviedb.components.labeled.JLabeledComboBoxMovie;
import com.lars_albrecht.moviedb.components.labeled.JLabeledListMovie;
import com.lars_albrecht.moviedb.components.labeled.JLabeledTextAreaMovie;
import com.lars_albrecht.moviedb.components.labeled.JLabeledTextInputMovie;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.controller.MovieController;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

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
		for (final FieldModel fieldModel : fl) {
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
			if ((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == Integer.class))) || (fieldModel.getType() == ViewInTab.TYPE_INT)) {
				final JLabeledTextInputMovie tfTemp = new JLabeledTextInputMovie(fieldModel.getAs(), "", JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled(Boolean.FALSE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else if ((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && ((fieldModel.getField().getType() == String.class) || (fieldModel.getField().getType() == File.class))))
					|| (fieldModel.getType() == ViewInTab.TYPE_INPUT)) {
				final JLabeledTextInputMovie tfTemp = new JLabeledTextInputMovie(fieldModel.getAs(), "", JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled(Boolean.FALSE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else if ((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == ArrayList.class))) || (fieldModel.getType() == ViewInTab.TYPE_SELECT)) {
				gbc.gridheight = 2;
				final DefaultListModel<KeyValue<Integer, String>> listModel = new DefaultListModel<KeyValue<Integer, String>>();
				final ArrayList<KeyValue<Integer, String>> kvList = Controller.keyValueLists.get(fieldModel.getField().getName());
				if (kvList != null) {
					for (final KeyValue<Integer, String> kv : kvList) {
						listModel.addElement(kv);
					}
					final JLabeledListMovie<KeyValue<Integer, String>> tfTemp = new JLabeledListMovie<KeyValue<Integer, String>>(fieldModel.getAs(), listModel, JLabeled.LABELPOSITION_LEFT,
							preferredLabelSize, null, 10, 10);
					tfTemp.setEnabled(Boolean.FALSE);
					tempPanel.add(tfTemp, gbc);
					this.componentList.put(fieldModel.getField().getName(), tfTemp);
					i++;
				}
			} else if (fieldModel.getType() == ViewInTab.TYPE_AREA) {
				gbc.gridheight = 2;
				final JLabeledTextAreaMovie tfTemp = new JLabeledTextAreaMovie(fieldModel.getAs(), "", JLabeled.LABELPOSITION_LEFT, preferredLabelSize, new Dimension(100, 100), 10, 10);
				tfTemp.setEnabled(Boolean.FALSE);
				tfTemp.getTaText().setLineWrap(Boolean.TRUE);
				tfTemp.getTaText().setWrapStyleWord(Boolean.TRUE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
				// i++ to increase the fieldsize to 2 rows
				i++;
			} else if ((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == Image.class))) || (fieldModel.getType() == ViewInTab.TYPE_IMAGE)) {
				gbc.fill = GridBagConstraints.NONE;
				final JImage tfTemp = new JImage();
				tfTemp.addImageListener(this.controller);
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
		for (final Map.Entry<String, Object> tabEntry : tempTab.entrySet()) {
			if (tabEntry.getValue() instanceof JPanel) {
				final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
				final JPanel filledPanel = (JPanel) tabEntry.getValue();
				for (int j = 0; j < Controller.apiScraper.size(); j++) {
					if (Controller.apiScraper.get(j).getTabTitle().equals(tabEntry.getKey())) {
						final JButton bRefreshButton = new JButton("Refresh");
						bRefreshButton.addActionListener(this.controller);
						this.componentList.put("refresh" + Controller.apiScraper.get(j).getPluginName(), bRefreshButton);
						filledPanel.add(bRefreshButton, gbc);
						break;
					}
				}
				final JPanel t = new JPanel(new GridBagLayout());
				final GridBagConstraints gbcRootPanel = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
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
	 * 
	 * @param o
	 */
	public void refreshMovieApiScraper(final Object o) {
		final String pluginName = ((String) Helper.getKeyFromMapObject(this.componentList, o)).substring("refresh".length());
		for (final IApiScraperPlugin apiScraper : Controller.apiScraper) {
			if (apiScraper.getPluginName().equals(pluginName)) {
				try {
					//
					// TODO get movie from movie / threaded / if there is a
					// id, use it
					MovieModel m = apiScraper.getMovieFromStringYear((String) Controller.loadedMovie.get("maintitle"), (Integer) Controller.loadedMovie.get("year"));
					if (m == null) {
						m = apiScraper.getMovieFromStringYear((String) Controller.loadedMovie.get("maintitle") + " " + (String) Controller.loadedMovie.get("subtitle"),
								(Integer) Controller.loadedMovie.get("year"));
					}
					if (m != null) {
						final FieldList fieldlist = Helper.getDBFieldModelFromClass(m.getClass());
						for (final FieldModel fieldModel : fieldlist) {
							// System.out.println("AS: " + fieldModel.getAs());
							if (m.get(fieldModel.getField().getName()) != null) {
								// System.out.println(fieldModel.getField().getName());
								// System.out.println(m.get(fieldModel.getField().getName()));
								Controller.loadedMovie.set(fieldModel.getField().getName(), m.get(fieldModel.getField().getName()));
							}
							// System.out.println("----------");
						}
						// TODO set infos in tabview (reload movie)
						MovieController.updateMovie(Controller.loadedMovie);
						this.controller.getLv().getTable().repaint();
						this.refreshLoadedMovie();
					}
				} catch (final SecurityException e1) {
					e1.printStackTrace();
				} catch (final IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (final IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (final InvocationTargetException e1) {
					e1.printStackTrace();
				} catch (final NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (final SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
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

	/**
	 * 
	 */
	public void refreshLoadedMovie() {
		for (final Map.Entry<String, Component> entry : this.controller.getTv().getComponentList().entrySet()) {
			try {
				final Object result = Controller.loadedMovie.get(entry.getKey());
				Object resultForComponent = null;
				if (result != null) {
					// prepare selection / text for component
					if ((result instanceof ArrayList)) {
						resultForComponent = Helper.implode((ArrayList<?>) result, ", ", null, null);
					} else if ((result instanceof File)) {
						resultForComponent = ((File) result).getAbsolutePath();
					} else if ((result instanceof String)) {
						resultForComponent = result;
					} else if (result instanceof Integer) {
						resultForComponent = Integer.toString((Integer) result);
					} else if ((result instanceof Image)) {
						resultForComponent = result;
					}
				} else {
					// System.out.println(entry.getKey() + " is null");
				}

				// set selection / text to component
				System.out.println(entry.getValue());
				if ((entry.getValue() instanceof JLabeledTextInputMovie) || (entry.getValue() instanceof JLabeledTextAreaMovie)) {
					((IMovieTabComponent) entry.getValue()).select(resultForComponent);
				} else if ((entry.getValue() instanceof JLabeledComboBoxMovie) || (entry.getValue() instanceof JLabeledListMovie)) {
					((IMovieTabComponent) entry.getValue()).select(result);
				} else {
					if (entry.getValue() instanceof JImage) {
						((JImage) entry.getValue()).setImage((Image) resultForComponent);
					}
					// System.out.println(": " + entry.getValue().getClass());
				}

			} catch (final SecurityException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

}

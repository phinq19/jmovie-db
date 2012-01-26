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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.lars_albrecht.general.components.imagepanel.JImage;
import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;
import com.lars_albrecht.general.components.ratingbar.JRatingBar;
import com.lars_albrecht.general.types.KeyValue;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;
import com.lars_albrecht.moviedb.components.labeled.JLabeledComboBoxMovie;
import com.lars_albrecht.moviedb.components.labeled.JLabeledListMovie;
import com.lars_albrecht.moviedb.components.labeled.JLabeledRatingBar;
import com.lars_albrecht.moviedb.components.labeled.JLabeledTextAreaMovie;
import com.lars_albrecht.moviedb.components.labeled.JLabeledTextInputMovie;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.controller.MovieController;
import com.lars_albrecht.moviedb.exceptions.NoMovieIDException;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;
import com.lars_albrecht.moviedb.utilities.MovieHelper;

@SuppressWarnings("serial")
public class TabView extends JTabbedPane {

	private Controller controller = null;

	private ConcurrentHashMap<String, Component> componentList = null;

	/**
	 * 
	 * @param controller
	 *            Controller
	 */
	public TabView(final Controller controller) {
		this.controller = controller;
		this.addComponents(this);

		this.setVisible(true);
	}

	/**
	 * 
	 * @param panel
	 *            JTabbedPane
	 */
	private void addComponents(final JTabbedPane panel) {
		this.componentList = new ConcurrentHashMap<String, Component>();
		this.addTabs(panel);
	}

	/**
	 * 
	 * @param panel
	 *            JTabbedPane
	 */
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
			if(((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && ((fieldModel.getField().getType() == Integer.class) || (fieldModel
					.getField().getType() == Long.class)))))
					|| (fieldModel.getType() == ViewInTab.TYPE_INT)) {
				final JLabeledTextInputMovie tfTemp = new JLabeledTextInputMovie(fieldModel.getAs(), "",
						JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled((Boolean) fieldModel.getAdditional().get("editable"));
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && ((fieldModel.getField().getType() == String.class) || (fieldModel
					.getField().getType() == File.class))))
					|| (fieldModel.getType() == ViewInTab.TYPE_INPUT)) {
				final JLabeledTextInputMovie tfTemp = new JLabeledTextInputMovie(fieldModel.getAs(), "",
						JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled((Boolean) fieldModel.getAdditional().get("editable"));
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
					final JLabeledListMovie<KeyValue<Integer, String>> tfTemp = new JLabeledListMovie<KeyValue<Integer, String>>(
							fieldModel.getAs(), listModel, JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
					tfTemp.setEnabled((Boolean) fieldModel.getAdditional().get("editable"));
					tempPanel.add(tfTemp, gbc);
					this.componentList.put(fieldModel.getField().getName(), tfTemp);
					i++;
				}
			} else if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == Boolean.class)))
					|| (fieldModel.getType() == ViewInTab.TYPE_SELECT)) {
				// TODO replace with new LabeledCheckbox?
				final JCheckBox tfTemp = new JCheckBox(fieldModel.getAs());
				tfTemp.setEnabled((Boolean) fieldModel.getAdditional().get("editable"));
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else if(fieldModel.getType() == ViewInTab.TYPE_AREA) {
				gbc.gridheight = 2;
				final JLabeledTextAreaMovie tfTemp = new JLabeledTextAreaMovie(fieldModel.getAs(), "",
						JLabeled.LABELPOSITION_LEFT, preferredLabelSize, new Dimension(100, 100), 10, 10);
				tfTemp.setEnabled((Boolean) fieldModel.getAdditional().get("editable"));
				tfTemp.getTaText().setLineWrap(Boolean.TRUE);
				tfTemp.getTaText().setWrapStyleWord(Boolean.TRUE);
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
				// i++ to increase the fieldsize to 2 rows
				i++;
			} else if((((fieldModel.getType() == ViewInTab.TYPE_AUTO) && (fieldModel.getField().getType() == Image.class)))
					|| (fieldModel.getType() == ViewInTab.TYPE_IMAGE)) {
				gbc.fill = GridBagConstraints.NONE;
				final JImage tfTemp = new JImage();
				tfTemp.addImageListener(this.controller);
				tfTemp.setPreferredSize(new Dimension(140, 200));
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);

			} else if(fieldModel.getType() == ViewInTab.TYPE_RATER) {
				final JLabeledRatingBar tfTemp = new JLabeledRatingBar(fieldModel.getAs(), JRatingBar.DRAW_MODE_STARS, 5,
						JLabeled.LABELPOSITION_LEFT, preferredLabelSize, null, 10, 10);
				tfTemp.setEnabled((Boolean) fieldModel.getAdditional().get("editable"));
				tempPanel.add(tfTemp, gbc);
				this.componentList.put(fieldModel.getField().getName(), tfTemp);
			} else {
				// System.out.println("NOT FOUND: " + fieldModel.getType() +
				// " - " + fieldModel.getAs());
			}

			// save made changes
			tempTab.put(tabName, tempPanel);
			tempTab.put(tabName + "Counter", ++i);
		}

		// add tabs
		this.addTabsToTablist(tempTab, panel);

		// for (final Map.Entry<String, Component> tabEntry :
		// this.componentList.entrySet()) {
		// System.out.println(tabEntry.getKey() + " - " + tabEntry.getValue());
		// }

	}

	/**
	 * 
	 * @param tempTab
	 *            ConcurrentHashMap<String, Object>
	 * @param panel
	 *            JTabbedPane
	 */
	private void addTabsToTablist(final ConcurrentHashMap<String, Object> tempTab, final JTabbedPane panel) {
		for(final Map.Entry<String, Object> tabEntry : tempTab.entrySet()) {
			if(tabEntry.getValue() instanceof JPanel) {
				final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
				final JPanel filledPanel = (JPanel) tabEntry.getValue();
				if(tabEntry.getKey().equalsIgnoreCase("generalmovie")) {
					final JPanel tempPanel = new JPanel(new GridBagLayout());
					gbc.insets = new Insets(0, 0, 0, 10);
					gbc.gridx = 0;
					final JButton bSaveButton = new JButton(RessourceBundleEx.getInstance().getProperty(
							"application.panel.tabview.button.save.title"));
					bSaveButton.addActionListener(this.controller);
					this.componentList.put("save", bSaveButton);
					tempPanel.add(bSaveButton, gbc);

					gbc.insets = new Insets(0, 0, 0, 0);
					gbc.gridx = 1;
					final JButton bRefresh = new JButton(RessourceBundleEx.getInstance().getProperty(
							"application.panel.tabview.button.refresh.title"));
					bRefresh.addActionListener(this.controller);
					this.componentList.put("refresh", bRefresh);
					tempPanel.add(bRefresh, gbc);

					gbc.gridx = 0;
					gbc.insets = new Insets(10, 10, 5, 5);
					filledPanel.add(tempPanel, gbc);
				}
				for(int j = 0; j < Controller.apiScraper.size(); j++) {
					if(Controller.apiScraper.get(j).getTabTitle().equals(tabEntry.getKey())) {
						final JButton bRefreshButton = new JButton(RessourceBundleEx.getInstance().getProperty(
								"application.panel.tabview.apiscraper.button.refresh.title"));
						bRefreshButton.addActionListener(this.controller);
						this.componentList.put("refresh" + Controller.apiScraper.get(j).getPluginName(), bRefreshButton);
						gbc.insets = new Insets(10, 10, 5, 5);
						filledPanel.add(bRefreshButton, gbc);
						break;
					}
				}
				final JPanel t = new JPanel(new GridBagLayout());
				final GridBagConstraints gbcRootPanel = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
				t.add(filledPanel, gbcRootPanel);
				panel.addTab(Helper.ucfirst(tabEntry.getKey()), new JScrollPane(t));
			}
		}

		this.setSelectedIndex(this.getTabCount() - 1);
	}

	/**
	 * 
	 * @param o
	 *            Object
	 */
	public void clickRefreshMovieApiScraper(final Object o) {
		final String pluginName = ((String) Helper.getKeyFromMapObject(this.componentList, o)).substring("refresh".length());
		for(final IApiScraperPlugin apiScraper : Controller.apiScraper) {
			if(apiScraper.getPluginName().equals(pluginName)) {
				Controller.loadedMovie = this.refreshMovieApiScraper(apiScraper, Controller.loadedMovie);
				// TODO set infos in tabview (reload movie)
				try {
					MovieController.updateMovie(Controller.loadedMovie);
				} catch(final SecurityException e) {
					e.printStackTrace();
				} catch(final IllegalAccessException e) {
					e.printStackTrace();
				} catch(final IllegalArgumentException e) {
					e.printStackTrace();
				} catch(final InvocationTargetException e) {
					e.printStackTrace();
				} catch(final NoSuchMethodException e) {
					e.printStackTrace();
				} catch(final SQLException e) {
					e.printStackTrace();
				} catch(final NoMovieIDException e) {
					e.printStackTrace();
				}
				this.controller.getLv().getTable().repaint();
				this.refreshLoadedMovie();
				break;
			}
		}
	}

	/**
	 * Returns the given MovieModel "movie" with new informations from the given apiScraperPlugin.
	 * 
	 * @param apiScraper
	 *            IApiScraperPlugin
	 * @param movie
	 *            MovieModel
	 * @return MovieModel
	 */
	private MovieModel refreshMovieApiScraper(final IApiScraperPlugin apiScraper, MovieModel movie) {
		try {
			//
			// TODO threaded / (??? get movie from movie ???)
			MovieModel tempMovie = null;
			Integer year = null;
			if(movie.get("year") instanceof Integer) {
				year = (Integer) movie.get("year");
			} else if(movie.get("year") instanceof String) {
				year = Integer.parseInt((String) movie.get("year"));
			}
			final String originalName = (String) movie.get("originalName");
			final String mainTitle = (String) movie.get("maintitle");
			final String subTitle = (String) movie.get("subtitle");
			String key = null;
			if(movie.get(apiScraper.getIdFieldName()) instanceof String) {
				key = (String) movie.get(apiScraper.getIdFieldName());
			} else if(movie.get(apiScraper.getIdFieldName()) instanceof Integer) {
				key = Integer.toString((Integer) movie.get(apiScraper.getIdFieldName()));
			}
			if((key != null) && !key.equalsIgnoreCase("")) {
				tempMovie = apiScraper.getMovieFromKey(key);
			}
			if(tempMovie == null) {
				for(int i = 0; i < 2; i++) {
					// loop to do all scraper methods twice. With and without
					// year. If year is null, the loop will run only once.
					if(i == 1) {
						if(year == null) {
							break;
						} else {
							year = null;
						}
					}
					if(Helper.isValidString(originalName)) {
						tempMovie = apiScraper.getMovieFromStringYear(originalName, (Integer) movie.get("year"));
					}
					if((tempMovie == null) && Helper.isValidString((String) (movie.get("subtitle")))) {
						tempMovie = apiScraper.getMovieFromStringYear(mainTitle + " " + subTitle, year);
					}
					if(tempMovie == null) {
						tempMovie = apiScraper.getMovieFromStringYear(mainTitle, year);
					}
				}
			}
			if(tempMovie != null) {
				final FieldList fieldlist = MovieHelper.getDBFieldModelFromClass(tempMovie.getClass());
				for(final FieldModel fieldModel : fieldlist) {
					// System.out.println("AS: " + fieldModel.getAs());
					if(tempMovie.get(fieldModel.getField().getName()) != null) {
						// System.out.println(fieldModel.getField().getName());
						// System.out.println(m.get(fieldModel.getField().getName()));
						final Object oldValue = movie.get(fieldModel.getField().getName());
						final Object newValue = tempMovie.get(fieldModel.getField().getName());
						if(((oldValue != null) && (newValue != null) && (!oldValue.equals(newValue)) && (JOptionPane
								.showConfirmDialog(this, String.format(RessourceBundleEx.getInstance().getProperty(
										"application.dialog.refresh.replace.message"), fieldModel.getField().getName(), oldValue,
										newValue, apiScraper.getPluginName()), RessourceBundleEx.getInstance().getProperty(
										"application.dialog.refresh.replace.title"), JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE) == 0))
								|| ((oldValue == null) && (newValue != null))) {
							movie.set(fieldModel.getField().getName(), newValue);
						}
					}
					// System.out.println("----------");
				}
			} else {
				String str = null;
				if(((str = JOptionPane.showInputDialog(this, String.format(RessourceBundleEx.getInstance().getProperty(
						"application.dialog.refresh.id.message"), apiScraper.getPluginName()), RessourceBundleEx.getInstance()
						.getProperty("application.dialog.refresh.id.title"), JOptionPane.QUESTION_MESSAGE)) != null)
						&& !str.equalsIgnoreCase("")) {
					movie.set(apiScraper.getIdFieldName(), str);
					movie = this.refreshMovieApiScraper(apiScraper, movie);
				}
			}
		} catch(final SecurityException e1) {
			e1.printStackTrace();
		} catch(final IllegalAccessException e1) {
			e1.printStackTrace();
		} catch(final IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch(final InvocationTargetException e1) {
			e1.printStackTrace();
		}

		return movie;
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
	 * Checks the filepaths of loaded movies and checks if new movies are in stored folders and add them automatically.
	 */
	public void refreshLoadedMovie() {
		for(final Map.Entry<String, Component> entry : this.controller.getTv().getComponentList().entrySet()) {
			try {
				final Object result = Controller.loadedMovie.get(entry.getKey());
				Object resultForComponent = null;
				if(result != null) {
					// prepare selection / text for component
					if((result instanceof ArrayList<?>)) {
						resultForComponent = Helper.implode((ArrayList<?>) result, ", ", null, null);
					} else if((result instanceof File)) {
						resultForComponent = ((File) result).getAbsolutePath();
					} else if((result instanceof String)) {
						resultForComponent = result;
					} else if(result instanceof Integer) {
						resultForComponent = Integer.toString((Integer) result);
					} else if(result instanceof Long) {
						resultForComponent = Long.toString(((Long) result));
					} else if((result instanceof Image)) {
						resultForComponent = result;
					}

					// format item
					final FieldModel fm = MovieHelper.getTabFieldModelFromClass(MovieModel.class, Helper.lcfirst(entry.getKey()));
					if(fm != null) {
						switch((Integer) fm.getAdditional().get("format")) {
						case ViewInTab.FORMAT_AUTO:
							break;
						case ViewInTab.FORMAT_FILESIZE:
							if(result instanceof Long) {
								resultForComponent = Helper.getHumanreadableFileSize((Long) result);
							}
							break;
						}
					}
				} else {
					// System.out.println(entry.getKey() + " is null");
				}

				// set selection / text to component
				if((entry.getValue() instanceof JLabeledTextInputMovie) || (entry.getValue() instanceof JLabeledTextAreaMovie)) {
					((IMovieTabComponent) entry.getValue()).select(resultForComponent);
				} else if((entry.getValue() instanceof JLabeledComboBoxMovie<?>)
						|| (entry.getValue() instanceof JLabeledListMovie<?>)) {
					((IMovieTabComponent) entry.getValue()).select(result);
				} else if(entry.getValue() instanceof JImage) {
					((JImage) entry.getValue()).setImage((Image) resultForComponent);
				} else if(entry.getValue() instanceof JRatingBar) {
					((JRatingBar) entry.getValue()).setPoints((Integer) result);
				} else {
					// System.out.println(": " + entry.getValue().getClass());
				}

			} catch(final SecurityException e) {
				e.printStackTrace();
			} catch(final IllegalAccessException e) {
				e.printStackTrace();
			} catch(final IllegalArgumentException e) {
				e.printStackTrace();
			} catch(final InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public void saveMovie() {
		// System.out.println(this.getComponentList().size());
		for(final Entry<String, Component> entry : this.getComponentList().entrySet()) {
			if(entry.getValue().isEnabled()) {
				// System.out.println(entry.getKey());
			}
		}
	}
}

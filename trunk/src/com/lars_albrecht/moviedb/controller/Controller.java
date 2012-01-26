/**
 * 
 */
package com.lars_albrecht.moviedb.controller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.lars_albrecht.general.components.StatusBar;
import com.lars_albrecht.general.components.imagepanel.events.ImageEvent;
import com.lars_albrecht.general.components.imagepanel.events.ImageListener;
import com.lars_albrecht.general.types.KeyValue;
import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin;
import com.lars_albrecht.moviedb.apiscraper.rottentomatoes.RottenTomatoesScraper;
import com.lars_albrecht.moviedb.apiscraper.themoviedb.TMDbScraper;
import com.lars_albrecht.moviedb.components.InfoListView;
import com.lars_albrecht.moviedb.database.DB;
import com.lars_albrecht.moviedb.exceptions.NoMovieIDException;
import com.lars_albrecht.moviedb.exceptions.OptionsNotLoadedException;
import com.lars_albrecht.moviedb.exceptions.OptionsNotSavedException;
import com.lars_albrecht.moviedb.filter.MovieFilenameFilter;
import com.lars_albrecht.moviedb.gui.Platform;
import com.lars_albrecht.moviedb.gui.search.SearchListView;
import com.lars_albrecht.moviedb.gui.splitview.TabView;
import com.lars_albrecht.moviedb.gui.splitview.TableView;
import com.lars_albrecht.moviedb.gui.tablefilter.AdditionalFilter;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.Options;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;
import com.lars_albrecht.moviedb.thread.Parser;
import com.lars_albrecht.moviedb.utilities.MovieHelper;

/**
 * @author lalbrecht
 * 
 */
public class Controller implements ActionListener, ListDataListener, ListSelectionListener, WindowListener, MouseListener,
		ImageListener {

	private Platform pf = null;
	private TableView lv = null;
	private TabView tv = null;
	private SearchListView slv = null;
	private AdditionalFilter af = null;

	public static FieldList flTable = null;
	public static FieldList flTabs = null;
	public static FieldList flParse = null;
	public static FieldList flDB = null;
	public static FieldList flFilter = null;

	public static MovieModel loadedMovie = null;

	public static ArrayList<IApiScraperPlugin> apiScraper = null;

	public static ConcurrentHashMap<String, ArrayList<KeyValue<Integer, String>>> keyValueLists = new ConcurrentHashMap<String, ArrayList<KeyValue<Integer, String>>>();;

	public static Options options = null;

	private StatusBar sbStatus = null;

	public Controller() {
		this.setLookAndFeel();

		this.createTabbedPaneFromModel(MovieModel.class);
		this.init();
		this.sbStatus = new StatusBar();

		this.tv = new TabView(this);
		this.lv = new TableView(this);

		this.pf = new Platform(this);
	}

	private void init() {
		// init property reader
		RessourceBundleEx.getInstance().setPrefix("moviedb");

		// init fieldlists for reflection
		Controller.flTable = MovieHelper.getTableFieldModelFromClass(MovieModel.class);
		Controller.flTabs = MovieHelper.getTabFieldModelFromClass(MovieModel.class);
		Controller.flParse = MovieHelper.getParseFieldModelFromClass(MovieModel.class);
		Controller.flDB = MovieHelper.getDBFieldModelFromClass(MovieModel.class);
		Controller.flFilter = MovieHelper.getDBFieldModelFromClass(MovieModel.class);

		Controller.apiScraper = new ArrayList<IApiScraperPlugin>();
		Controller.apiScraper.add(new TMDbScraper());
		Controller.apiScraper.add(new RottenTomatoesScraper());

		for(final IApiScraperPlugin apiScraper : Controller.apiScraper) {
			Controller.flTable.addAll(MovieHelper.getTableFieldModelFromClass(apiScraper.getMovieModelInstance()));
			for(final FieldModel fieldModel : MovieHelper.getTabFieldModelFromClass(apiScraper.getMovieModelInstance())) {
				// System.out.println(fieldModel.getAs());
				fieldModel.setName(apiScraper.getTabTitle());
				Controller.flTabs.add(fieldModel);
			}
			Controller.flParse.addAll(MovieHelper.getParseFieldModelFromClass(apiScraper.getMovieModelInstance()));
			Controller.flDB.addAll(MovieHelper.getDBFieldModelFromClass(apiScraper.getMovieModelInstance()));
			Controller.flFilter = MovieHelper.getDBFieldModelFromClass(apiScraper.getMovieModelInstance());
		}

		this.initDB();

		for(final FieldModel field : Controller.flDB) {
			if(field.getType() == DatabaseOptions.TYPE_TABLE) {
				try {
					Controller.keyValueLists.put(field.getField().getName(), MovieController.getList(field.getAs()));
				} catch(final SQLException e) {
					Debug.log(Debug.LEVEL_ERROR, e.getMessage());
				}
			}
		}

		// for (final FieldModel fm : Controller.flTable) {
		// System.out.println(fm.getAs());
		// }

		// load options from database
		try {
			Controller.options = OptionController.loadOptions();
		} catch(final OptionsNotLoadedException e) {
			JOptionPane.showMessageDialog(this.pf, RessourceBundleEx.getInstance().getProperty(
					"application.dialog.options.notloaded.message"), RessourceBundleEx.getInstance().getProperty(
					"application.dialog.options.notloaded.title"), JOptionPane.ERROR_MESSAGE);
			Debug.log(Debug.LEVEL_ERROR, e.getMessage());
		}

	}

	/**
	 * Initilize the database. Create tables and fill them with default values.
	 * 
	 * @throws SQLException
	 */
	private void initDB() {
		// create movietables
		final FieldList fl = Controller.flDB;
		final DatabaseOptions classAnnotation = MovieModel.class.getAnnotation(DatabaseOptions.class);
		final String tableName = (classAnnotation != null ? classAnnotation.as() : MovieModel.class.getName());
		final ArrayList<String> sqlList = new ArrayList<String>();
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ";
		String subSql = null;
		String alterSql = null;
		// String tableFields = "";
		String fieldStr = null;
		final ArrayList<String> uniqueFields = new ArrayList<String>();
		final ArrayList<String> tableFields = new ArrayList<String>();

		ArrayList<String> availableColumns = null;

		for(final FieldModel field : fl) {
			if(field.getField().getType() == ArrayList.class) {
				subSql = "CREATE TABLE IF NOT EXISTS " + field.getAs()
						+ "(id INT IDENTITY, name VARCHAR(512) NOT NULL, UNIQUE (name))";
				sqlList.add(subSql);
				if(((String[]) field.getAdditional().get("defaultValues")).length > 0) {
					subSql = "INSERT INTO " + field.getAs() + " (name) VALUES "
							+ Helper.implode((String[]) field.getAdditional().get("defaultValues"), ", ", "('", "')") + "";
				}
				sqlList.add(subSql);
				subSql = "CREATE TABLE IF NOT EXISTS " + tableName + "_" + field.getAs() + " (id INT IDENTITY, " + tableName
						+ "_id INT NOT NULL, " + field.getAs() + "_id INT NOT NULL)";
				sqlList.add(subSql);
			} else {
				if(field.getField().getType() == Integer.class) {
					fieldStr = field.getAs() + " INT " + field.getAdditional().get("additionalType") + " ";
					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				} else if(field.getField().getType() == Float.class) {
					fieldStr = field.getAs() + " REAL " + field.getAdditional().get("additionalType") + " ";
					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				} else if(field.getField().getType() == Long.class) {
					fieldStr = field.getAs() + " BIGINT " + field.getAdditional().get("additionalType") + " ";
					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				} else if(field.getField().getType() == Boolean.class) {
					fieldStr = field.getAs() + " BOOLEAN " + field.getAdditional().get("additionalType") + " ";
					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				} else if(field.getField().getType() == String.class) {
					fieldStr = field.getAs()
							+ (field.getAdditional().get("additionalType").equals("") ? " VARCHAR(128) " : " "
									+ field.getAdditional().get("additionalType") + " ");

					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				} else if(field.getField().getType() == File.class) {
					fieldStr = field.getAs()
							+ (field.getAdditional().get("additionalType").equals("") ? " VARCHAR(512) " : " "
									+ field.getAdditional().get("additionalType") + " ");
					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				} else if(field.getField().getType() == Image.class) {
					fieldStr = field.getAs()
							+ (field.getAdditional().get("additionalType").equals("") ? " BLOB " : " "
									+ field.getAdditional().get("additionalType") + " ");
					if(!tableFields.contains(fieldStr)) {
						tableFields.add(fieldStr);
					}
					if((Boolean) field.getAdditional().get("isUnique")) {
						uniqueFields.add(field.getAs());
					}
				}
			}
		}
		sql = sql
				+ (!tableFields.equals("") ? "("
						+ Helper.implode(tableFields, ", ", null, null)
						+ (uniqueFields.size() > 0 ? " ,CONSTRAINT unique_" + tableName + " UNIQUE ("
								+ Helper.implode(uniqueFields, ", ", null, null) + ")" : "") + ")" : "");
		Debug.log(Debug.LEVEL_DEBUG, "sql");
		sqlList.add(0, sql);
		for(final String sqlStr : sqlList) {
			try {
				DB.update(sqlStr);
			} catch(final SQLIntegrityConstraintViolationException e) {
			} catch(final SQLException e) {
				Debug.log(Debug.LEVEL_FATAL, e.getMessage() + "\n\n" + sqlStr);
			}
		}

		try {
			DB.commit();
		} catch(final SQLException e) {
			e.printStackTrace();
		}

		try {
			availableColumns = DB.getColumnsFromTable(tableName);
		} catch(final SQLException e1) {
			e1.printStackTrace();
		}

		for(final FieldModel field : fl) {
			if(field.getField().getType() != ArrayList.class) {
				// if table exists
				// System.out.println(field.getAs());
				if(!availableColumns.contains(field.getAs().toUpperCase())) {
					// System.out.println(availableColumns);
					alterSql = "ALTER TABLE " + tableName + " ADD " + field.getAs() + " "
							+ Helper.getDatabaseTypeForType(field.getField().getType());

					try {
						DB.update(alterSql);
					} catch(final SQLException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}

		// TODO insert option
		final Boolean cleanUp = Boolean.FALSE;

		if(cleanUp) {
			for(final String string : availableColumns) {
				if(fl.fieldNameInAsList(string.toLowerCase()) == -1) {
					alterSql = "ALTER TABLE " + tableName + " DROP COLUMN " + string;
					try {
						DB.update(alterSql);
					} catch(final SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// create optiontables
		sql = "CREATE TABLE IF NOT EXISTS options (id INT IDENTITY, name VARCHAR(128) NOT NULL, values LONGVARCHAR NOT NULL, CONSTRAINT unique_option UNIQUE (name))";
		try {
			DB.update(sql);
		} catch(final SQLException e) {
			Debug.log(Debug.LEVEL_FATAL, e.getMessage() + "\n\n" + sql);
		}

		try {
			DB.commit();
		} catch(final SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param cClass
	 */
	private void createTabbedPaneFromModel(final Class<?> cClass) {
	}

	/**
	 * 
	 */
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("Table.alternateRowColor", new Color(212, 212, 212));
		} catch(final ClassNotFoundException e) {
			e.printStackTrace();
		} catch(final InstantiationException e) {
			e.printStackTrace();
		} catch(final IllegalAccessException e) {
			e.printStackTrace();
		} catch(final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch(final ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch(final InstantiationException e1) {
				e1.printStackTrace();
			} catch(final IllegalAccessException e1) {
				e1.printStackTrace();
			} catch(final UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private void removeAllMovies() {
		if(JOptionPane.showConfirmDialog(this.pf, RessourceBundleEx.getInstance().getProperty(
				"application.dialog.movies.removeall.message"), RessourceBundleEx.getInstance().getProperty(
				"application.dialog.movies.removeall.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
			try {
				final int movieCount = this.lv.getTable().getRowCount();
				MovieController.removeAllMovies();
				this.lv.getTableModel().getMovies().removeAllElements();
				this.lv.getTable().revalidate();
				this.sbStatus.setText(String.format(RessourceBundleEx.getInstance().getProperty(
						"application.status.movielist.removedall"), movieCount));
			} catch(final SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if(e.getSource() == this.pf.getMiAdd()) {
			// TODO add form to add a movie manually
		} else if(e.getSource() == this.pf.getMiSearch()) {
			this.slv = new SearchListView(this);
		} else if(e.getSource() == this.pf.getMiFindDuplicates()) {
			ConcurrentHashMap<Integer, MovieModel> duplicates = null;
			try {
				duplicates = MovieController.getDuplicates();
			} catch(final SecurityException e1) {
				e1.printStackTrace();
			} catch(final IllegalAccessException e1) {
				e1.printStackTrace();
			} catch(final IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch(final InvocationTargetException e1) {
				e1.printStackTrace();
			} catch(final SQLException e1) {
				e1.printStackTrace();
			} catch(final IOException e1) {
				e1.printStackTrace();
			}
			/**
			 * TODO do more here:<br />
			 * - Sort the list for the ilv<br />
			 * - Add titles and texts in translate file<br />
			 * - Add method to refactor this to TableView<br />
			 * - Add ability to jump directly to the entry in list<br />
			 * - Add good menu entries
			 */

			final InfoListView<MovieModel> ilv = new InfoListView<MovieModel>("Duplicates", "these are the duplicates");

			// init menu
			// final JPopupMenu infoListViewPopMenu = new JPopupMenu();
			// this.miFind = new JMenuItem("Relocate");
			// this.miFind.addActionListener(this.controller);
			// this.miRemoveFromList = new JMenuItem("Remove");
			// this.miRemoveFromList.addActionListener(this.controller);
			// infoListViewPopMenu.add(this.miFind);
			// infoListViewPopMenu.add(this.miRemoveFromList);
			// infoListViewPopMenu.addPopupMenuListener(new ListPopupMenuAdapter());
			// this.ilv.setPopupMenu(infoListViewPopMenu);
			ilv.fillList(new ArrayList<MovieModel>(duplicates.values()));
			ilv.setVisible(Boolean.TRUE);

		} else if(e.getSource() == this.pf.getMiClose()) {
			// TODO close
		} else if(e.getSource() == this.pf.getMiRemoveAll()) {
			this.removeAllMovies();
		} else if((this.slv != null) && (e.getSource() == this.slv.getbStartScan())) {
			this.slv.startScanMovies();
		} else if((this.slv != null) && (e.getSource() == this.slv.getbAddPath())) {
			this.slv.addPathToSearchView();
		} else if(e.getSource() == this.pf.getCbFilter()) {
			// System.out.println(this.pf.getCbFilter().getSelectedItem());
		} else if(e.getSource() == this.pf.getMiAbout()) {
			// TODO create about / info dialog
			JOptionPane.showMessageDialog(this.pf, "Iconset \"Fugue Icons\" from http://p.yusukekamiyamane.com/");
		} else if(e.getSource() == this.af.getCbUseAdvanced()) {
			this.af.toggleAdvancedSearchFilter();
		} else if(e.getSource() == this.pf.getbRefreshTable()) {
			this.lv.refreshTableItems();
		} else if((this.slv != null) && (e.getSource() == this.slv.getbSave())) {
			this.slv.saveSearchOptions();
		} else if((this.tv != null) && (this.tv.getComponentList() != null)
				&& this.tv.getComponentList().containsValue(e.getSource()) && (Controller.loadedMovie != null)
				&& ((String) Helper.getKeyFromMapObject(this.tv.getComponentList(), e.getSource())).startsWith("refresh")
				&& !((String) Helper.getKeyFromMapObject(this.tv.getComponentList(), e.getSource())).equalsIgnoreCase("refresh")) {
			this.tv.clickRefreshMovieApiScraper(e.getSource());
		} else if((this.tv != null) && (this.tv.getComponentList() != null)
				&& this.tv.getComponentList().containsValue(e.getSource()) && (Controller.loadedMovie != null)
				&& ((String) Helper.getKeyFromMapObject(this.tv.getComponentList(), e.getSource())).equalsIgnoreCase("save")) {
			this.tv.saveMovie();
		} else if((this.tv != null) && (this.tv.getComponentList() != null)
				&& this.tv.getComponentList().containsValue(e.getSource()) && (Controller.loadedMovie != null)
				&& ((String) Helper.getKeyFromMapObject(this.tv.getComponentList(), e.getSource())).equalsIgnoreCase("refresh")) {
			try {
				String regex = null;
				if(Helper.isValidString(Controller.options.getFilenameSeperator())) {
					regex = Controller.options.getFilenameSeperator();
				} else {
					// TODO test
					regex = (String) JOptionPane.showInputDialog(this.tv, RessourceBundleEx.getInstance().getProperty(
							"application.dialog.regex.message"), RessourceBundleEx.getInstance().getProperty(
							"application.dialog.regex.title"), JOptionPane.QUESTION_MESSAGE, null, null, "((?!-\\s).)+");
				}
				if(regex != null) {
					final Integer movieId = (Integer) Controller.loadedMovie.get("id");
					final Integer movieInList = this.lv.getTableModel().indexOf(Controller.loadedMovie);
					if(movieInList > -1) {
						Controller.loadedMovie = new Parser(ThreadController.getDBListItems(), Controller.flParse, regex)
								.parseMoviename((File) Controller.loadedMovie.get("file"));
						Controller.loadedMovie.set("id", movieId);
						this.lv.getTableModel().getMovies().set(movieInList, Controller.loadedMovie);
						MovieController.updateMovie(Controller.loadedMovie);
						this.tv.refreshLoadedMovie();
						this.getLv().refreshTableInfos();
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
			} catch(final NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch(final SQLException e1) {
				e1.printStackTrace();
			} catch(final NoMovieIDException e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == this.lv.getMiFind()) {
			// TODO try to find automatically
			final List<MovieModel> selectedMovies = this.lv.getIlv().getlList().getSelectedValuesList();
			final JFileChooser chooser = new JFileChooser();
			for(final MovieModel movieModel : selectedMovies) {
				try {
					chooser.setCurrentDirectory((File) movieModel.get("filepath"));
					chooser.setDialogTitle(String.format(RessourceBundleEx.getInstance().getProperty(
							"application.chooser.relocate.title"), movieModel.get("maintitle")));
					chooser.setFileFilter(new MovieFilenameFilter());
					if(chooser.showOpenDialog(this.lv) == JFileChooser.APPROVE_OPTION) {
						movieModel.set("file", chooser.getSelectedFile());
						movieModel.set("validPath", Boolean.TRUE);
						MovieController.updateMovie(movieModel);
						this.getLv().refreshTableInfos();
					}
				} catch(final SecurityException e1) {
					e1.printStackTrace();
				} catch(final IllegalAccessException e1) {
					e1.printStackTrace();
				} catch(final IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch(final InvocationTargetException e1) {
					e1.printStackTrace();
				} catch(final NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch(final SQLException e1) {
					e1.printStackTrace();
				} catch(final NoMovieIDException e1) {
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == this.lv.getMiRemoveFromList()) {
			// do something
			System.out.println("do something here");
		} else if(e.getSource() == this.lv.getMiOpenPath()) {
			// TODO try to find the latest existing path
			try {
				final int[] selectedRows = this.lv.getTable().getSelectedRows();
				MovieModel tempMovie = null;
				for(final int selectedRow : selectedRows) {
					tempMovie = this.lv.getTableModel().getMovie(this.lv.getTable().convertRowIndexToModel(selectedRow));
					if((tempMovie != null) && (tempMovie.get("file") != null) && ((File) tempMovie.get("file")).exists()) {
						tempMovie.set("validPath", Boolean.TRUE);
						this.lv.refreshTableInfos();
						Desktop.getDesktop().open(new File(((File) tempMovie.get("file")).getParent()));
					} else if((tempMovie != null) && (tempMovie.get("file") != null) && !((File) tempMovie.get("file")).exists()) {
						tempMovie.set("validPath", Boolean.FALSE);
						this.lv.refreshTableInfos();
					}
				}
			} catch(final IOException e1) {
				e1.printStackTrace();
			} catch(final SecurityException e1) {
				e1.printStackTrace();
			} catch(final IllegalAccessException e1) {
				e1.printStackTrace();
			} catch(final IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch(final InvocationTargetException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void contentsChanged(final ListDataEvent e) {
		if(e.getSource() == this.pf.getMcbmFilter()) {
			final String[] val = this.pf.getMcbmFilter().getSelectedItem().toString().split(" ");
			RowFilter<? super TableModel, ? super Integer> rf = null;
			// TODO replace with:
			// http://www.javalobby.org/java/forums/m91834162.html
			// final RowFilter<? super TableModel, ? super Integer> filter =
			// RowFilter.regexFilter(Pattern.compile(val,
			// Pattern.CASE_INSENSITIVE).toString(), 0, 1);

			final ArrayList<RowFilter<Object, Object>> rfs = new ArrayList<RowFilter<Object, Object>>(2);
			for(String string : val) {
				string = "(?i)" + string;
				rfs.add(RowFilter.regexFilter(string));
			}
			if(this.pf.getRbAndSearch().isSelected()) {
				rf = RowFilter.andFilter(rfs);
			} else {
				rf = RowFilter.orFilter(rfs);
			}

			try {
				// rf = (RowFilter.regexFilter(val, Pattern.CASE_INSENSITIVE));
			} catch(final java.util.regex.PatternSyntaxException ex) {
				return;
			}
			this.lv.getTableRowSorter().setRowFilter(rf);
			// this.lv.getTable().getSearchable().
			// SearchFactory.getInstance().showFindBar(this.af,
			// this.lv.getTable().getSearchable());
		} else if((this.slv != null) && (e.getSource() == this.slv.getFileListModel())) {
			if(this.slv.getFileListModel().getSize() > 0) {
				this.slv.getbStartScan().setEnabled(Boolean.TRUE);
			} else {
				this.slv.getbStartScan().setEnabled(Boolean.FALSE);
			}
		}

	}

	@Override
	public void valueChanged(final ListSelectionEvent e) {
		if(e.getSource() == this.lv.getTable().getSelectionModel()) {
			final int viewRow = this.lv.getTable().getSelectedRow();
			if(viewRow < 0) {
				// Selection got filtered away.
				Debug.log(Debug.LEVEL_DEBUG, "Selection got filtered away.");
			} else {
			}
		}

	}

	@Override
	public void intervalAdded(final ListDataEvent e) {
		if((this.slv != null) && (this.slv.getFileListModel() != null) && (e.getSource() == this.slv.getFileListModel())) {
			this.slv.getbSave().setEnabled(Boolean.TRUE);
			this.slv.getbStartScan().setEnabled(Boolean.TRUE);
		}
	}

	@Override
	public void intervalRemoved(final ListDataEvent e) {
	}

	/**
	 * @return the pf
	 */
	public synchronized final Platform getPf() {
		return this.pf;
	}

	/**
	 * @param pf
	 *            the pf to set
	 */
	public synchronized final void setPf(final Platform pf) {
		this.pf = pf;
	}

	/**
	 * @return the lv
	 */
	public synchronized final TableView getLv() {
		return this.lv;
	}

	/**
	 * @param lv
	 *            the lv to set
	 */
	public synchronized final void setLv(final TableView lv) {
		this.lv = lv;
	}

	/**
	 * @return the slv
	 */
	public synchronized final SearchListView getSlv() {
		return this.slv;
	}

	/**
	 * @param slv
	 *            the slv to set
	 */
	public synchronized final void setSlv(final SearchListView slv) {
		this.slv = slv;
	}

	/**
	 * @return the tv
	 */
	public synchronized final TabView getTv() {
		return this.tv;
	}

	/**
	 * @param tv
	 *            the tv to set
	 */
	public synchronized final void setTv(final TabView tv) {
		this.tv = tv;
	}

	/**
	 * @return the sbStatus
	 */
	public synchronized final StatusBar getSbStatus() {
		return this.sbStatus;
	}

	/**
	 * @param sbStatus
	 *            the sbStatus to set
	 */
	public synchronized final void setSbStatus(final StatusBar sbStatus) {
		this.sbStatus = sbStatus;
	}

	/**
	 * @return the af
	 */
	public synchronized final AdditionalFilter getAf() {
		return this.af;
	}

	/**
	 * @param af
	 *            the af to set
	 */
	public synchronized final void setAf(final AdditionalFilter af) {
		this.af = af;
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		try {
			DB.closeConnection();
			Controller.options.setWidthHeightMainWindow(new Point(this.pf.getWidth(), this.pf.getHeight()));
			Controller.options.setxYMainWindow(this.pf.getLocation());
			Controller.options.setSliderBottomPos(this.pf.getSplitPaneBottom().getDividerLocation());
			OptionController.saveOptions(Controller.options);
			this.pf.dispose();
		} catch(final SQLException ex) {
			ex.printStackTrace();
		} catch(final OptionsNotSavedException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	}

	@Override
	public void mouseClicked(final MouseEvent mouseEvent) {
		if((mouseEvent.getClickCount() == 2) && (mouseEvent.getSource() == this.lv.getTable())) {
			this.getLv().doTableDoubleClick();
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {

	}

	@Override
	public void mouseReleased(final MouseEvent e) {

	}

	@Override
	public void mouseEntered(final MouseEvent e) {

	}

	@Override
	public void mouseExited(final MouseEvent e) {

	}

	@Override
	public void imageAdded(final ImageEvent e) {
		if(this.tv.getComponentList().contains(e.getSource())) {
			try {
				if(e.getImage() != Controller.loadedMovie.get("cover")) {
					Controller.loadedMovie.set("cover", e.getImage());
					MovieController.updateMovie(Controller.loadedMovie);
					this.lv.getTable().repaint();
				}
			} catch(final SecurityException e1) {
				e1.printStackTrace();
			} catch(final IllegalAccessException e1) {
				e1.printStackTrace();
			} catch(final IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch(final InvocationTargetException e1) {
				e1.printStackTrace();
			} catch(final NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch(final SQLException e1) {
				e1.printStackTrace();
			} catch(final NoMovieIDException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void imageClear(final ImageEvent e) {
	}

	@Override
	public void imageGet(final ImageEvent e) {
	}

	@Override
	public void imageRemoved(final ImageEvent e) {
		if(this.tv.getComponentList().contains(e.getSource())) {
			try {
				if(e.getImage() == Controller.loadedMovie.get("cover")) {
					Controller.loadedMovie.set("cover", null, Image.class);
					MovieController.updateMovie(Controller.loadedMovie);
					this.lv.getTable().repaint();
				}
			} catch(final SecurityException e1) {
				e1.printStackTrace();
			} catch(final IllegalAccessException e1) {
				e1.printStackTrace();
			} catch(final IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch(final InvocationTargetException e1) {
				e1.printStackTrace();
			} catch(final NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch(final SQLException e1) {
				e1.printStackTrace();
			} catch(final NoMovieIDException e1) {
				e1.printStackTrace();
			}
		}
	}

}

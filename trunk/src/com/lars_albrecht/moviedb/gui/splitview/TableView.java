/**
 * 
 */
package com.lars_albrecht.moviedb.gui.splitview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.lars_albrecht.general.components.awt.ListPopupMenuAdapter;
import com.lars_albrecht.general.components.awt.TablePopupMenuAdapter;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.components.InfoListView;
import com.lars_albrecht.moviedb.components.movietablemodel.HidableTableColumnModel;
import com.lars_albrecht.moviedb.components.movietablemodel.MovieTableModel;
import com.lars_albrecht.moviedb.components.movietablemodel.celleditors.MovieCellEditor;
import com.lars_albrecht.moviedb.components.movietablemodel.cellrenderer.MovieValidRenderer;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.controller.MovieController;
import com.lars_albrecht.moviedb.controller.ThreadController;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class TableView extends JPanel {

	private Controller controller = null;

	/**
	 * @see "http://download.oracle.com/javase/tutorial/uiswing/components/table.html"
	 */
	private JTable table = null;
	private MovieTableModel tableModel = null;
	private JScrollPane tableScrollPane = null;
	private TableRowSorter<TableModel> tableRowSorter = null;
	private JMenuItem miFind = null;
	private JMenuItem miRemoveFromList = null;

	private JMenuItem miRemove = new JMenuItem("Remove");
	private JMenuItem miRemoveFile = new JMenuItem("Remove and delete file");
	private JMenuItem miOpenPath = new JMenuItem("Open Path");

	private InfoListView<MovieModel> ilv = null;

	public TableView(final Controller controller) {
		this.controller = controller;
		this.setLayout(new GridBagLayout());
		this.addComponents(this);

		try {
			this.addMoviesToTableModel();
		} catch(final IllegalArgumentException e) {
			e.printStackTrace();
		} catch(final SecurityException e) {
			e.printStackTrace();
		} catch(final IllegalAccessException e) {
			e.printStackTrace();
		} catch(final InvocationTargetException e) {
			e.printStackTrace();
		} catch(final NoSuchMethodException e) {
			e.printStackTrace();
		} catch(final ClassNotFoundException e) {
			e.printStackTrace();
		} catch(final SQLException e) {
			e.printStackTrace();
		} catch(final IOException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}

	/**
	 * 
	 * @param frame
	 */
	private void addComponents(final JPanel panel) {
		this.tableModel = new MovieTableModel();
		final GridBagConstraints gbc = new GridBagConstraints();
		this.tableRowSorter = new TableRowSorter<TableModel>(this.tableModel);
		this.table = new JTable(this.tableModel);

		// @see
		// "http://www.java-forum.org/awt-swing-swt/101885-jtable-spalten-ausblenden.html"
		// HidableTableColumnModel htcm = new
		// HidableTableColumnModel(table.getColumnModel());
		// table.setColumnModel(htcm);
		//
		// JPopupMenu popup = new JPopupMenu();
		// for (Action act : htcm.createColumnActions()) {
		// popup.add(new JCheckBoxMenuItem(act));
		// } // for
		// table.setComponentPopupMenu(popup);

		final MovieCellEditor mce = new MovieCellEditor();
		final Enumeration<TableColumn> columns = this.table.getColumnModel().getColumns();
		while(columns.hasMoreElements()) {
			final TableColumn col = columns.nextElement();
			col.setCellEditor(mce);
		}

		this.table.getColumnModel().getColumn(0).setCellRenderer(new MovieValidRenderer());
		this.table.setGridColor(Color.LIGHT_GRAY);
		this.table.setRowSorter(this.tableRowSorter);
		this.table.setRowHeight(50);
		this.table.getSelectionModel().addListSelectionListener(this.controller);
		this.table.addMouseListener(this.controller);

		final HidableTableColumnModel htcm = new HidableTableColumnModel(this.table.getColumnModel());
		final JPopupMenu tablePopmenu = new JPopupMenu();

		this.miRemove = new JMenuItem("Remove");
		this.miRemove.addActionListener(this.controller);
		this.miRemoveFile = new JMenuItem("Remove and delete file");
		this.miRemoveFile.addActionListener(this.controller);
		this.miOpenPath = new JMenuItem("Open Path");
		this.miOpenPath.addActionListener(this.controller);

		tablePopmenu.add(this.miRemove);
		tablePopmenu.add(this.miRemoveFile);
		tablePopmenu.add(this.miOpenPath);

		tablePopmenu.addSeparator();
		final Action[] columnActions = htcm.createColumnActions();
		for(final Action act : columnActions) {
			tablePopmenu.add(new JCheckBoxMenuItem(act));
		} // for
		// this.table.getTableHeader().setComponentPopupMenu(popup);
		this.table.setComponentPopupMenu(tablePopmenu);
		tablePopmenu.addPopupMenuListener(new TablePopupMenuAdapter());

		final TableColumn tc = this.table.getColumn(this.table.getColumnName(0));
		tc.setMinWidth(15);
		tc.setMaxWidth(15);

		this.tableScrollPane = new JScrollPane(this.table);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(this.tableScrollPane, gbc);
	}

	/**
	 * 
	 */
	public void doTableDoubleClick() {
		// System.out.println(this.lv.getTable().getValueAt(modelRow,
		// 1));
		// System.out.println(this.lv.getTableModel().getMovies().get(modelRow));
		// System.out.println(this.lv.getTable().getSelectionModel().getMinSelectionIndex());
		// System.out.println(this.lv.getTable().getSelectionModel().getMaxSelectionIndex());
		// System.out.println(String.format("Selected Row in view: %d. " +
		// "Selected Row in model: %d.", viewRow, modelRow));

		final int modelRow = this.table.convertRowIndexToModel(this.table.getSelectedRow());
		Controller.loadedMovie = this.tableModel.getMovies().get(modelRow);
		this.controller.getTv().refreshLoadedMovie();
	}

	/**
	 * 
	 */
	public void refreshTableInfos() {
		this.tableModel.callListenersChange(new TableModelEvent(this.tableModel, 0, this.tableModel.getMovies().size() - 1,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
	}

	/**
	 * 
	 */
	public void refreshTableItems() {
		// set new status
		final ArrayList<MovieModel> notAddedList = new ArrayList<MovieModel>();
		if(this.tableModel.getMovies().size() > 0) {
			for(final MovieModel movie : this.tableModel.getMovies()) {
				try {
					if(((File) movie.get("file")).getAbsoluteFile().exists()) {
						movie.set("validPath", Boolean.TRUE);
					} else {
						movie.set("validPath", Boolean.FALSE);
						notAddedList.add(movie);
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

			this.refreshTableInfos();
			if(notAddedList.size() > 0) {
				this.ilv = new InfoListView<MovieModel>(RessourceBundleEx.getInstance().getProperty(
						"application.panel.tableview.infolistview.listnotadded.title"), RessourceBundleEx.getInstance()
						.getProperty("application.panel.tableview.infolistview.listnotadded.message"));

				// init menu
				final JPopupMenu infoListViewPopMenu = new JPopupMenu();
				this.miFind = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
						"application.panel.tableview.menuitem.relocate.title"));
				this.miFind.addActionListener(this.controller);
				this.miRemoveFromList = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
						"application.panel.tableview.menuitem.removefromlist.title"));
				this.miRemoveFromList.addActionListener(this.controller);
				infoListViewPopMenu.add(this.miFind);
				infoListViewPopMenu.add(this.miRemoveFromList);
				infoListViewPopMenu.addPopupMenuListener(new ListPopupMenuAdapter());
				this.ilv.setPopupMenu(infoListViewPopMenu);
				this.ilv.fillList(notAddedList);
				this.ilv.setVisible(Boolean.TRUE);
			}

		}

		if((Controller.options.getPaths().size() > 0) && !Controller.options.getFilenameSeperator().equals("")) {
			// add new movies from defaultfilestack
			new ThreadController(this.controller, this.tableModel, null, Controller.options.getPaths());
		}
	}

	/**
	 * 
	 * @param tableModel
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 * @throws IOException
	 */
	private void addMoviesToTableModel() throws IllegalArgumentException, SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, ClassNotFoundException, SQLException, IOException {
		int i = 0;
		for(final MovieModel movie : MovieController.getMovies()) {
			i++;
			this.tableModel.addMovie(movie);
		}

		this.controller.getSbStatus().setText(
				String.format(RessourceBundleEx.getInstance().getProperty("application.status.movielist.loaded"), i));
	}

	/**
	 * @return the miRemoveFromList
	 */
	public synchronized final JMenuItem getMiRemoveFromList() {
		return this.miRemoveFromList;
	}

	/**
	 * @return the ilv
	 */
	public synchronized final InfoListView<MovieModel> getIlv() {
		return this.ilv;
	}

	/**
	 * @return the tableModel
	 */
	public synchronized final MovieTableModel getTableModel() {
		return this.tableModel;
	}

	/**
	 * @param tableModel
	 *            the tableModel to set
	 */
	public synchronized final void setTableModel(final MovieTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the table
	 */
	public synchronized final JTable getTable() {
		return this.table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public synchronized final void setTable(final JTable table) {
		this.table = table;
	}

	/**
	 * @return the tableRowSorter
	 */
	public synchronized final TableRowSorter<TableModel> getTableRowSorter() {
		return this.tableRowSorter;
	}

	/**
	 * @param tableRowSorter
	 *            the tableRowSorter to set
	 */
	public synchronized final void setTableRowSorter(final TableRowSorter<TableModel> tableRowSorter) {
		this.tableRowSorter = tableRowSorter;
	}

	/**
	 * @return the miFind
	 */
	public synchronized final JMenuItem getMiFind() {
		return this.miFind;
	}

	/**
	 * @return the miRemove
	 */
	public synchronized final JMenuItem getMiRemove() {
		return this.miRemove;
	}

	/**
	 * @return the miRemoveFile
	 */
	public synchronized final JMenuItem getMiRemoveFile() {
		return this.miRemoveFile;
	}

	/**
	 * @return the miOpenPath
	 */
	public synchronized final JMenuItem getMiOpenPath() {
		return this.miOpenPath;
	}

}

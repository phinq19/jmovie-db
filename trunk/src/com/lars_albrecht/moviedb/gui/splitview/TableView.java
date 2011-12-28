/**
 * 
 */
package com.lars_albrecht.moviedb.gui.splitview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.lars_albrecht.moviedb.components.MovieTableModel;
import com.lars_albrecht.moviedb.components.celleditors.MovieCellEditor;
import com.lars_albrecht.moviedb.components.cellrenderer.MovieValidRenderer;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.controller.MovieController;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;
import com.lars_albrecht.moviedb.utilities.PropertiesReader;

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

	public TableView(final Controller controller) {
		this.controller = controller;
		this.setLayout(new GridBagLayout());
		this.addComponents(this);

		try {
			this.addMoviesToTableModel();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
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
		while (columns.hasMoreElements()) {
			final TableColumn col = columns.nextElement();
			col.setCellEditor(mce);
		}

		this.table.getColumnModel().getColumn(0).setCellRenderer(new MovieValidRenderer());
		this.table.setGridColor(Color.LIGHT_GRAY);
		this.table.setRowSorter(this.tableRowSorter);
		this.table.setRowHeight(50);
		this.table.getSelectionModel().addListSelectionListener(this.controller);
		this.table.addMouseListener(this.controller);

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
	private void addMoviesToTableModel() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException,
			SQLException, IOException {
		int i = 0;
		for (final MovieModel movie : MovieController.getMovies()) {
			i++;
			this.tableModel.addMovie(movie);
		}

		this.controller.getSbStatus().setText(String.format(PropertiesReader.getInstance().getProperties("application.status.movielist.loaded"), i));
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

}

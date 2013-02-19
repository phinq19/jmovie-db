/**
 * 
 */
package com.lars_albrecht.moviedb.components.movietablemodel.celleditors;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;


/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class MovieCellEditor extends AbstractCellEditor implements TableCellEditor {

	@SuppressWarnings("unused")
	private final JButton accept = null;

	private MovieCellEditorWindow window = null;

	public MovieCellEditor() {
		this.window = new MovieCellEditorWindow();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
		// System.out.println("getTableCellEditorComponent");
		final Rectangle r = table.getCellRect(row, column, true);
		final Point xy = new Point(r.x, r.y);
		SwingUtilities.convertPointToScreen(xy, table);
		this.window.setBounds(xy.x, xy.y, 200, 200);
		// System.out.println(table.getColumnClass(column));
		// this.window.showView(table.getColumnClass(column));
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		System.out.println("getCellEditorValue");
		return "123";
	}

}

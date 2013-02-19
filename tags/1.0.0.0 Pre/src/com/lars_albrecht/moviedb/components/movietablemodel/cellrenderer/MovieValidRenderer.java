/**
 * 
 */
package com.lars_albrecht.moviedb.components.movietablemodel.cellrenderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author ibsisini
 * 
 */
@SuppressWarnings("serial")
public class MovieValidRenderer extends JPanel implements TableCellRenderer {

	public MovieValidRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		if (isSelected) {
			// cell (and perhaps other cells) are selected
		}

		if (hasFocus) {
			// this cell is the anchor and the table has the focus
		}
		// Boolean b = value;
		if (value == null) {
			this.setBackground(Color.GRAY);
		} else if ((Boolean) value) {
			this.setBackground(Color.GREEN);
		} else {
			this.setBackground(Color.RED);
		}
		return this;
	}

}

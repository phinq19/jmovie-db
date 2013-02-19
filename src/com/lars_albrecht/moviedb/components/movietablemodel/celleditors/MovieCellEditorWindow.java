/**
 * 
 */
package com.lars_albrecht.moviedb.components.movietablemodel.celleditors;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class MovieCellEditorWindow extends JWindow {

	private JPanel rootPanel = null;
	@SuppressWarnings("unused")
	private final Object oldValue = null;
	@SuppressWarnings("unused")
	private final Object newValue = null;

	public MovieCellEditorWindow() {
		super();

	}

	public void showView(final Class<?> c) {
		if(c == ArrayList.class) {
			this.showListEditView();
		}

		this.setVisible(true);
	}

	private void showListEditView() {
		this.rootPanel = new JPanel();
		final JComboBox<SimpleEntry<Integer, String>> list = new JComboBox<SimpleEntry<Integer, String>>();
		list.setEditable(true);
		list.addItem(new SimpleEntry<Integer, String>(1, "xxx"));
		list.addItem(new SimpleEntry<Integer, String>(2, "xxx"));
		this.rootPanel.add(list);
		this.rootPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(this.rootPanel);
	}
}

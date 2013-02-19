/**
 * 
 */
package com.lars_albrecht.moviedb.components.labeled;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ListModel;

import com.lars_albrecht.general.components.labeled.JLabeledList;
import com.lars_albrecht.general.types.KeyValue;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledListMovie<V> extends JLabeledList<V> implements IMovieTabComponent {

	public JLabeledListMovie(final String labelText, final Vector<? extends V> lV, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, lV, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	public JLabeledListMovie(final String labelText, final ListModel<V> lLm, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, lLm, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	public JLabeledListMovie(final String labelText, final Integer labelPosition, final Dimension labelWidthHeight,
			final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent#select (java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void select(final Object o) {
		// System.out.println(o);
		if(o == null) {
			this.getlList().setSelectedIndex(-1);
		} else {
			final ListModel<KeyValue<Integer, String>> listModel = (ListModel<KeyValue<Integer, String>>) this.getlList()
					.getModel();
			final int[] selectedIndices = new int[((ArrayList<String>) o).size()];
			int j = 0;
			for(int i = 0; i < listModel.getSize(); i++) {
				if(((ArrayList<String>) o).contains(listModel.getElementAt(i).getValue())) {
					selectedIndices[j] = i;
					j++;
				}
			}
			this.getlList().setSelectedIndices(selectedIndices);
		}
	}

}

/**
 * 
 */
package com.lars_albrecht.moviedb.components.labeled;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;

import com.lars_albrecht.general.components.labeled.JLabeledComboBox;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledComboBoxMovie<V> extends JLabeledComboBox<V> implements IMovieTabComponent {

	public JLabeledComboBoxMovie(final String labelText, final Vector<V> v, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx,
			final Integer paddingy) {
		super(labelText, v, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	public JLabeledComboBoxMovie(final String labelText, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	public JLabeledComboBoxMovie(final String labelText, final ComboBoxModel<V> cbmModel, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight,
			final Integer paddingx, final Integer paddingy) {
		super(labelText, cbmModel, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent#select
	 * (java.lang.Object)
	 */
	@Override
	public void select(final Object o) {
		if (o == null) {
			this.getCbBox().setSelectedIndex(-1);
		} else {
			this.getCbBox().setSelectedItem(o);
		}
	}

}

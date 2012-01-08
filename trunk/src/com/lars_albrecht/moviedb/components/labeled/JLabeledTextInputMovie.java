/**
 * 
 */
package com.lars_albrecht.moviedb.components.labeled;

import java.awt.Dimension;

import com.lars_albrecht.general.components.labeled.JLabeledTextInput;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledTextInputMovie extends JLabeledTextInput implements IMovieTabComponent {

	public JLabeledTextInputMovie(final String labelText, final String inputText, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx,
			final Integer paddingy) {
		super(labelText, inputText, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
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
			this.getTfText().setText("");
		} else if (o instanceof String) {
			this.getTfText().setText((String) o);
		}
	}

}

/**
 * 
 */
package com.lars_albrecht.moviedb.components.labeled;

import java.awt.Dimension;

import com.lars_albrecht.general.components.labeled.JLabeledTextArea;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledTextAreaMovie extends JLabeledTextArea implements IMovieTabComponent {

	public JLabeledTextAreaMovie(final String labelText, final String inputText, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, inputText, labelPosition, labelWidthHeight, widthHeight, paddingx, paddingy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent#select(java.lang.Object)
	 */
	@Override
	public void select(final Object o) {
		if(o == null) {
			this.getTaText().setText("");
		} else if(o instanceof String) {
			this.getTaText().setText((String) o);
		}
	}

}

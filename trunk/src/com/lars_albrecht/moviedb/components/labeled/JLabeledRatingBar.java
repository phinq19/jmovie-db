/**
 * 
 */
package com.lars_albrecht.moviedb.components.labeled;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;
import com.lars_albrecht.general.components.ratingbar.JRatingBar;
import com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledRatingBar extends JLabeled implements IMovieTabComponent {
	private JRatingBar ratingBar = null;
	private Integer points = null;

	public JLabeledRatingBar(final String labelText, final Integer drawMode, final Integer points, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {

		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);

		this.points = points != null ? points : 0;
		this.ratingBar = new JRatingBar(drawMode, this.points);
		this.add(this.ratingBar, BorderLayout.CENTER);
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.components.interfaces.IMovieTabComponent#select (java.lang.Object)
	 */
	@Override
	public void select(final Object o) {
		if(o == null) {
			this.ratingBar.setPoints(null);
		} else if(o instanceof String) {
			this.ratingBar.setPoints((Integer) o);
		}
	}

}

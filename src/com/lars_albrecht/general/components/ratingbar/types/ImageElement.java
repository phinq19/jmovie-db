/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.types;

import java.awt.Graphics;

import com.lars_albrecht.general.components.ratingbar.JRatingBar;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class ImageElement extends AbstractElement {

	public ImageElement(final JRatingBar parent, final int id) {
		super(parent, id);
	}

	@Override
	protected void drawEnabled(final Graphics g) {
	}

	@Override
	protected void drawDisabled(final Graphics g) {
	}

}

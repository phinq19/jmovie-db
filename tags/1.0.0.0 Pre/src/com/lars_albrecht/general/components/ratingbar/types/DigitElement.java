/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.types;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.lars_albrecht.general.components.ratingbar.JRatingBar;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class DigitElement extends AbstractElement {

	/**
	 * 
	 * @param parent
	 *            JRatingBar
	 * @param id
	 *            int
	 */
	public DigitElement(final JRatingBar parent, final int id) {
		super(parent, id);
	}

	@Override
	protected void drawEnabled(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.enabledColor);
		final Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.drawString(Byte.toString(this.getId()), g2.getFontMetrics().charWidth(Byte.toString(this.getId()).charAt(0)), g2
				.getFontMetrics().getHeight());
	}

	@Override
	protected void drawDisabled(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.disabledColor);
		final Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.drawString(Byte.toString(this.getId()), g2.getFontMetrics().charWidth(Byte.toString(this.getId()).charAt(0)), g2
				.getFontMetrics().getHeight());
	}

}

/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.types;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.lars_albrecht.general.components.ratingbar.JRatingBar;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class StarElement extends AbstractElement {

	// Koordinaten der Eckpunkte der Sterne
	private final int[] x = { 60, 72, 108, 79, 90, 60, 30, 41, 12, 48 }, y = { 10, 44, 45, 66, 100, 80, 100, 66, 45, 44 };

	public StarElement(final JRatingBar parent, final int id) {
		super(parent, id);

		// nachträgliche Anpassung der Koordinaten
		for(int i = 0; i < this.x.length; ++i) {
			// Verkleinerung
			this.x[i] -= 5;
			this.y[i] -= 5;
			// runden
			this.x[i] = (this.x[i] % 6 >= 3 ? this.x[i] / 6 + 1 : this.x[i] / 6);
			this.y[i] = (this.y[i] % 6 >= 3 ? this.y[i] / 6 + 1 : this.y[i] / 6);
		}
	}

	@Override
	protected void drawEnabled(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.enabledColor);
		g2.fillPolygon(this.x, this.y, this.x.length);

	}

	@Override
	protected void drawDisabled(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.disabledColor);
		g2.fillPolygon(this.x, this.y, this.x.length);
	}

}

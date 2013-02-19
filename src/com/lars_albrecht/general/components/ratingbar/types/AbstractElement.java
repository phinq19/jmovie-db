/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.types;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.lars_albrecht.general.components.ratingbar.JRatingBar;
import com.lars_albrecht.general.components.ratingbar.events.RaterEvent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractElement extends JPanel implements MouseListener {

	protected final JRatingBar parent;
	protected final byte id;

	private int size = 120;
	protected Color enabledColor = null;
	protected Color disabledColor = null;

	/**
	 * 
	 * @param parent
	 *            JRatingBar
	 * @param id
	 *            int
	 */
	public AbstractElement(final JRatingBar parent, final int id) {
		this.parent = parent;
		this.id = (byte) id;
		//
		this.addMouseListener(this);
		// Verkleinerung
		this.size = -10;
		// runden
		this.size = (this.size % 6 >= 3 ? this.size / 6 + 1 : this.size / 6);
		this.setSize(this.size, this.size);

		this.setVisible(true);
		this.repaint();
	}

	/**
	 * @return the enabledColor
	 */
	public synchronized final Color getEnabledColor() {
		return this.enabledColor;
	}

	/**
	 * @param enabledColor
	 *            the enabledColor to set
	 */
	public synchronized final void setEnabledColor(final Color enabledColor) {
		this.enabledColor = enabledColor;
	}

	/**
	 * @return the disabledColor
	 */
	public synchronized final Color getDisabledColor() {
		return this.disabledColor;
	}

	/**
	 * @param disabledColor
	 *            the disabledColor to set
	 */
	public synchronized final void setDisabledColor(final Color disabledColor) {
		this.disabledColor = disabledColor;
	}

	/**
	 * @return the id
	 */
	public synchronized final byte getId() {
		return this.id;
	}

	@Override
	final public void setEnabled(final boolean aFlag) {
		this.repaint();
	}

	protected void drawEnabled(final Graphics g) {
	}

	protected void drawDisabled(final Graphics g) {
	}

	@Override
	final public void paint(final Graphics g) {
		super.paint(g);
		if(this.parent.getPoints() >= this.id) {
			this.drawEnabled(g);
		} else {
			this.drawDisabled(g);
		}
	}

	// MouseListener-Schnittstelle

	@Override
	public void mouseEntered(final MouseEvent e) {
		this.parent.setPoints(this.id);
	}

	// Wenn die Maus die Stern-Fläche verlässt, wird er Grau

	@Override
	public void mouseExited(final MouseEvent e) {
		// parent.isActive() bedeutet, dass das Benutzer die RatingBar durch KLICK hinein aktiviert hat
		if((this.id == 1) && this.parent.isActive()) {
			this.setEnabled(false);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		// Ein Klick bedeutet, dass die RatingBar aktiviert wird, wenn sie zuvor nicht aktiv war und umgekehrt
		this.parent.toggleEnabled();
		this.parent.setPoints(this.id);
		this.parent.getMulticaster().raterSelected(new RaterEvent(this.parent, RaterEvent.RATER_SELECTED, (int) this.id));
	}

}

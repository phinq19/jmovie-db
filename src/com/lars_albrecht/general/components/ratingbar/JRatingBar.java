/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.lars_albrecht.general.components.ratingbar.events.RaterEventMulticaster;
import com.lars_albrecht.general.components.ratingbar.types.AbstractElement;
import com.lars_albrecht.general.components.ratingbar.types.DigitElement;
import com.lars_albrecht.general.components.ratingbar.types.StarElement;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JRatingBar extends JPanel {

	public static final Integer DRAW_MODE_STARS = 1;
	public static final Integer DRAW_MODE_DIGITS = 2;

	private RaterEventMulticaster multicaster = null;

	private AbstractElement[] elem = null;
	private final GridBagLayout gbl;
	private final GridBagConstraints gbc;
	private Color enabledColor = null;
	private Color disabledColor = null;
	private boolean active;
	private byte points;
	private Integer drawMode = null;

	/**
	 * 
	 * @param drawMode
	 * @param count
	 */
	public JRatingBar(final Integer drawMode, final Integer count) {
		super();
		this.multicaster = new RaterEventMulticaster();
		this.elem = new AbstractElement[count];
		// Positionierung der Einzelnen Element-Panels durch das GridBagLayout
		this.gbl = new GridBagLayout();
		this.setLayout(this.gbl);
		this.gbc = new GridBagConstraints();
		this.gbc.anchor = GridBagConstraints.CENTER;
		this.gbc.fill = GridBagConstraints.NONE;
		this.gbc.gridwidth = 1;
		this.gbc.gridheight = 1;
		this.gbc.weightx = 0.2;
		this.gbc.weighty = 1;
		this.gbc.gridy = 0;
		this.gbc.ipadx = 20;
		this.gbc.ipady = 20;
		this.enabledColor = new Color(205, 149, 12);
		this.disabledColor = Color.GRAY;
		this.drawMode = drawMode;

		this.draw();

		this.active = false;
	}

	/**
	 * 
	 */
	private void draw() {
		for(int i = 0; i < this.elem.length; ++i) {
			this.gbc.gridx = i;
			// Jeder Stern bekommt eine ID (Ist er der an weitesten rechts selektiert, ist seine ID gleichzeitig die aktuelle Punktzahl

			switch(this.drawMode) {
			case 1:
				this.elem[i] = new StarElement(this, i + 1);
				break;
			case 2:
				this.elem[i] = new DigitElement(this, i + 1);
			}

			this.elem[i].setEnabledColor(this.enabledColor);
			this.elem[i].setDisabledColor(this.disabledColor);
			this.gbl.setConstraints(this.elem[i], this.gbc);
			this.add(this.elem[i]);
		}
	}

	@Override
	public void repaint() {
		this.setPoints(this.points);
		super.repaint();
	}

	// Gesamtgröße der JRatingBar
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(70, 20);
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
		for(int i = 0; i < this.elem.length; ++i) {
			this.elem[i].setEnabledColor(this.enabledColor);
		}
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
		for(int i = 0; i < this.elem.length; ++i) {
			this.elem[i].setDisabledColor(this.disabledColor);
		}
	}

	/**
	 * 
	 * @param p
	 *            Integer
	 */
	public void setPoints(final Integer p) {
		this.setPoints((p != null ? p.byteValue() : 0));
	}

	/**
	 * 
	 * @param p
	 *            byte
	 */
	public void setPoints(final byte p) {
		if(this.active) {
			this.points = p;
			// enable or disable elements
			for(int i = 0; i < this.elem.length; ++i) {
				if(this.elem[i].getId() > p) {
					this.elem[i].setEnabled(false);
				} else {
					this.elem[i].setEnabled(true);
				}
			}
		}
	}

	/**
	 * 
	 * @return byte
	 */
	public byte getPoints() {
		return this.points;
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * 
	 */
	public void toggleEnabled() {
		this.active = (!this.active ? true : false);
	}

	/**
	 * @return the multicaster
	 */
	public synchronized final RaterEventMulticaster getMulticaster() {
		return this.multicaster;
	}

}

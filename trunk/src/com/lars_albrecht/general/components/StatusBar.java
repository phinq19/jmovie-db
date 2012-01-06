/**
 * 
 */
package com.lars_albrecht.general.components;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class StatusBar extends JLabel {

	/**
	 * 
	 */
	public StatusBar() {
		super();
		super.setPreferredSize(new Dimension(100, 16));
		super.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

	/**
	 * 
	 * @param message
	 */
	public void setMessage(final String message) {
		this.setText(" " + message);
	}
}

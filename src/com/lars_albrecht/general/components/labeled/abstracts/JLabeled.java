/**
 * 
 */
package com.lars_albrecht.general.components.labeled.abstracts;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public abstract class JLabeled extends JPanel {

	final public static Integer LABELPOSITION_TOP = 0;
	final public static Integer LABELPOSITION_RIGHT = 1;
	final public static Integer LABELPOSITION_BOTTOM = 2;
	final public static Integer LABELPOSITION_LEFT = 3;

	private JLabel label = null;
	private String labelText = null;

	/**
	 * 
	 * @param labelText
	 * @param labelPosition
	 * @param labelWidthHeight
	 * @param paddingx
	 * @param paddingy
	 * @param widthHeight
	 */
	public JLabeled(final String labelText, final Integer labelPosition, final Dimension labelWidthHeight, final Integer paddingx, final Integer paddingy, final Dimension widthHeight) {
		super();
		this.labelText = labelText != null ? labelText : "";
		this.setLayout(new BorderLayout(paddingx, paddingy));
		this.setPreferredSize(widthHeight);
		this.label = new JLabel(this.labelText);
		if (labelWidthHeight != null) {
			this.label.setPreferredSize(labelWidthHeight);
		}

		switch (labelPosition) {
		case 0:
			this.add(this.label, BorderLayout.NORTH);
			break;
		case 1:
			this.add(this.label, BorderLayout.EAST);
			break;
		case 2:
			this.add(this.label, BorderLayout.SOUTH);
			break;
		case 3:
			this.add(this.label, BorderLayout.WEST);
			break;
		}
	}

}

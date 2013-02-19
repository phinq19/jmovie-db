/**
 * 
 */
package com.lars_albrecht.general.components.labeled;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledTextArea extends JLabeled {

	private JScrollPane spWrap = null;
	private JTextArea taText = null;

	private String inputText = null;

	public JLabeledTextArea(final String labelText, final String inputText, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);

		this.inputText = inputText != null ? inputText : "";
		this.taText = new JTextArea(this.inputText);
		this.spWrap = new JScrollPane(this.taText);
		this.add(this.spWrap, BorderLayout.CENTER);
		this.setVisible(true);

	}

	/**
	 * @return the taText
	 */
	public synchronized final JTextArea getTaText() {
		return this.taText;
	}

	/**
	 * @param taText
	 *            the taText to set
	 */
	public synchronized final void setTaText(final JTextArea taText) {
		this.taText = taText;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.taText.setEnabled(enabled);
	}

}

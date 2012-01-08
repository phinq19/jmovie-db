/**
 * 
 */
package com.lars_albrecht.general.components.labeled;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTextField;

import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JLabeledTextInput extends JLabeled {

	private JTextField tfText = null;

	private String inputText = null;

	public JLabeledTextInput(final String labelText, final String inputText, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx,
			final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);

		this.inputText = inputText != null ? inputText : "";
		this.tfText = new JTextField(this.inputText);
		this.add(this.tfText, BorderLayout.CENTER);
		this.setVisible(true);

	}

	/**
	 * @return the tfText
	 */
	public synchronized final JTextField getTfText() {
		return this.tfText;
	}

	/**
	 * @param tfText
	 *            the tfText to set
	 */
	public synchronized final void setTfText(final JTextField tfText) {
		this.tfText = tfText;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.tfText.setEnabled(enabled);
	}

}

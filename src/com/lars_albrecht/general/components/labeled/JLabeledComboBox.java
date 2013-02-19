/**
 * 
 */
package com.lars_albrecht.general.components.labeled;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings( { "serial" })
public class JLabeledComboBox<V> extends JLabeled {

	protected JComboBox<V> cbBox = null;

	public JLabeledComboBox(final String labelText, final Integer labelPosition, final Dimension labelWidthHeight,
			final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);

		this.cbBox = new JComboBox<V>();
		this.init();
	}

	public JLabeledComboBox(final String labelText, final Vector<V> v, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);

		this.cbBox = new JComboBox<V>(v);
		this.init();
	}

	public JLabeledComboBox(final String labelText, final ComboBoxModel<V> cbmModel, final Integer labelPosition,
			final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);

		this.cbBox = new JComboBox<V>(cbmModel);
		this.init();
	}

	protected void init() {
		this.add(this.cbBox, BorderLayout.CENTER);
		this.setVisible(true);
	}

	/**
	 * @return the cbBox
	 */
	public synchronized final JComboBox<V> getCbBox() {
		return this.cbBox;
	}

	/**
	 * @param cbBox
	 *            the cbBox to set
	 */
	public synchronized final void setCbBox(final JComboBox<V> cbBox) {
		this.cbBox = cbBox;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.cbBox.setEditable(enabled);
	}

}

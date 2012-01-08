/**
 * 
 */
package com.lars_albrecht.general.components.labeled;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings({ "serial" })
public class JLabeledList<V> extends JLabeled {

	private JList<V> lList = null;

	public JLabeledList(final String labelText, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx, final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);
		this.lList = new JList<V>();
		this.init();
	}

	public JLabeledList(final String labelText, final ListModel<V> lLm, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx,
			final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);
		this.lList = new JList<V>(lLm);
		this.init();
	}

	public JLabeledList(final String labelText, final Vector<? extends V> lV, final Integer labelPosition, final Dimension labelWidthHeight, final Dimension widthHeight, final Integer paddingx,
			final Integer paddingy) {
		super(labelText, labelPosition, labelWidthHeight, paddingx, paddingy, widthHeight);
		this.lList = new JList<V>(lV);
		this.init();
	}

	private void init() {
		final JScrollPane sp = new JScrollPane(this.lList);
		this.add(sp, BorderLayout.CENTER);
		this.setVisible(true);
	}

	/**
	 * @return the lList
	 */
	public synchronized final JList<V> getlList() {
		return this.lList;
	}

	/**
	 * @param lList
	 *            the lList to set
	 */
	public synchronized final void setlList(final JList<V> lList) {
		this.lList = lList;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.lList.setEnabled(enabled);
	}

}

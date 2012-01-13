/**
 * 
 */
package com.lars_albrecht.moviedb.gui.tablefilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.lars_albrecht.general.components.labeled.JLabeledList;
import com.lars_albrecht.general.components.labeled.abstracts.JLabeled;
import com.lars_albrecht.general.types.KeyValue;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.model.FieldModel;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class AdditionalFilter extends JPanel {

	private Controller controller = null;

	private JCheckBox cbUseAdvanced = null;

	private JPanel pFieldSet = null;
	private JScrollPane spFieldSet = null;

	private JRadioButton rbAndSearch = null;
	private JRadioButton rbOrSearch = null;
	private JLabeledList<KeyValue<Integer, String>> useFieldList = null;

	public AdditionalFilter(final Controller controller) {
		super(new BorderLayout(0, 10));
		this.controller = controller;

		this.setPanelSettings(this);
		this.addComponents(this);

	}

	/**
	 * 
	 * @param p
	 */
	private void setPanelSettings(final JPanel p) {
		p.setMinimumSize(new Dimension(50, 25));
		p.setBackground(Color.LIGHT_GRAY);
		p.setBorder(BorderFactory.createRaisedBevelBorder());
	}

	/**
	 * 
	 * @param p
	 */
	private void addComponents(final JPanel p) {
		this.pFieldSet = new JPanel(new GridBagLayout());
		this.spFieldSet = new JScrollPane(this.pFieldSet);
		this.pFieldSet.setBorder(BorderFactory.createEtchedBorder());
		// this.pFieldSet.setVisible(Boolean.FALSE);

		final GridBagConstraints gbcFieldSet = new GridBagConstraints();

		final Box bAndOr = Box.createHorizontalBox();
		final ButtonGroup bgAndOr = new ButtonGroup();
		this.rbAndSearch = new JRadioButton(RessourceBundleEx.getInstance().getProperty(
				"application.panel.additionalfilter.radio.and.title"), Boolean.TRUE);
		this.rbOrSearch = new JRadioButton(RessourceBundleEx.getInstance().getProperty(
				"application.panel.additionalfilter.radio.or.title"));
		bgAndOr.add(this.rbAndSearch);
		bgAndOr.add(this.rbOrSearch);

		gbcFieldSet.insets = new Insets(10, 10, 10, 10);
		gbcFieldSet.anchor = GridBagConstraints.NORTHWEST;
		gbcFieldSet.fill = GridBagConstraints.NONE;
		gbcFieldSet.weightx = 0.1;
		gbcFieldSet.gridx = 0;
		gbcFieldSet.gridy = 0;
		bAndOr.add(this.rbAndSearch);
		gbcFieldSet.gridx = 1;
		gbcFieldSet.weightx = 0.1;
		bAndOr.add(this.rbOrSearch);
		this.pFieldSet.add(bAndOr, gbcFieldSet);

		final DefaultComboBoxModel<KeyValue<Integer, String>> dcbFieldLis = new DefaultComboBoxModel<KeyValue<Integer, String>>();
		this.useFieldList = new JLabeledList<KeyValue<Integer, String>>(RessourceBundleEx.getInstance().getProperty(
				"application.panel.additionalfilter.labeledlist.userows.title"), dcbFieldLis, JLabeled.LABELPOSITION_TOP,
				new Dimension(125, 20), new Dimension(125, 200), 0, 0);
		for(final FieldModel fieldModel : Controller.flTable) {
			if(!fieldModel.getAs().equals(" ")) {
				dcbFieldLis.addElement(new KeyValue<Integer, String>(fieldModel.getSort(), fieldModel.getAs()));
			}
		}

		gbcFieldSet.gridx = 0;
		gbcFieldSet.gridy = 1;
		gbcFieldSet.gridwidth = 2;
		gbcFieldSet.weighty = 1;
		gbcFieldSet.weightx = 1;
		gbcFieldSet.anchor = GridBagConstraints.NORTHWEST;
		this.pFieldSet.add(this.useFieldList, gbcFieldSet);

		final GridBagConstraints gbcP = new GridBagConstraints();
		gbcP.weightx = 1;
		gbcP.weighty = 0;
		gbcP.fill = GridBagConstraints.NONE;
		gbcP.anchor = GridBagConstraints.NORTH;
		this.cbUseAdvanced = new JCheckBox(RessourceBundleEx.getInstance().getProperty(
				"application.panel.additionalfilter.check.useadvanced.title"));
		this.cbUseAdvanced.addActionListener(this.controller);
		this.cbUseAdvanced.setBorder(BorderFactory.createEtchedBorder());
		this.cbUseAdvanced.setBackground(p.getBackground());
		p.add(this.cbUseAdvanced, BorderLayout.NORTH);

		gbcP.gridy = 1;
		gbcP.weighty = 1;
		gbcP.fill = GridBagConstraints.BOTH;
		gbcP.anchor = GridBagConstraints.NORTH;
		p.add(this.spFieldSet, BorderLayout.CENTER);
	}

	/**
	 * 
	 */
	public void toggleAdvancedSearchFilter() {
		if(this.cbUseAdvanced.isSelected()) {
			this.spFieldSet.setVisible(true);
			if(this.controller.getPf().getSplitPaneTop().getDividerLocation() < 30) {
				this.controller.getPf().getSplitPaneTop().setDividerLocation(1.0);
			}
		} else {
			this.spFieldSet.setVisible(false);
			if(this.controller.getPf().getSplitPaneTop().getDividerLocation() > 30) {
				this.controller.getPf().getSplitPaneTop().setDividerLocation(25);
			}
		}
	}

	/**
	 * @return the cbUseAdvanced
	 */
	public synchronized final JCheckBox getCbUseAdvanced() {
		return this.cbUseAdvanced;
	}

	/**
	 * @param cbUseAdvanced
	 *            the cbUseAdvanced to set
	 */
	public synchronized final void setCbUseAdvanced(final JCheckBox cbUseAdvanced) {
		this.cbUseAdvanced = cbUseAdvanced;
	}

	/**
	 * @return the pFieldSet
	 */
	public synchronized final JPanel getpFieldSet() {
		return this.pFieldSet;
	}

	/**
	 * @param pFieldSet
	 *            the pFieldSet to set
	 */
	public synchronized final void setpFieldSet(final JPanel pFieldSet) {
		this.pFieldSet = pFieldSet;
	}

	/**
	 * @return the spFieldSet
	 */
	public synchronized final JScrollPane getSpFieldSet() {
		return this.spFieldSet;
	}

	/**
	 * @param spFieldSet
	 *            the spFieldSet to set
	 */
	public synchronized final void setSpFieldSet(final JScrollPane spFieldSet) {
		this.spFieldSet = spFieldSet;
	}

}

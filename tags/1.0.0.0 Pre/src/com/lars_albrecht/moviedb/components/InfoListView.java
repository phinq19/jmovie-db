/**
 * 
 */
package com.lars_albrecht.moviedb.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.lars_albrecht.general.utilities.Helper;

/**
 * This frame show a list of information.
 * 
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class InfoListView<V> extends JFrame implements ActionListener {

	private JList<V> lList = null;
	private DefaultListModel<V> listModel = null;
	private String text = null;
	private JButton bClose = null;

	/**
	 * 
	 * @param title
	 *            String
	 * @param text
	 *            String
	 */
	public InfoListView(final String title, final String text) {
		super(title);
		this.text = text;
		this.setFrameSettings(this);
		this.addComponents(this);
	}

	/**
	 * 
	 * @param frame
	 *            JFrame
	 */
	private void setFrameSettings(final JFrame frame) {
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		final Point centerPos = Helper.getScreenCenterPoint(300, 300);
		frame.setBounds(centerPos.x, centerPos.y, 300, 300);
		frame.setLayout(new GridBagLayout());
		frame.setMinimumSize(new Dimension(200, 200));
	}

	/**
	 * 
	 * @param frame
	 *            JFrame
	 */
	private void addComponents(final JFrame frame) {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		// init text
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTH;

		if(Helper.isValidString(this.text)) {
			final JLabel l = new JLabel(this.text);
			frame.add(l, gbc);
		}

		// init list
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		this.listModel = new DefaultListModel<V>();

		this.lList = new JList<V>(this.listModel);
		final JScrollPane listScrollPane = new JScrollPane(this.lList);
		frame.add(listScrollPane, gbc);

		// init button
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		this.bClose = new JButton("Close");
		this.bClose.addActionListener(this);
		frame.add(this.bClose, gbc);

	}

	/**
	 * @return the lList
	 */
	public synchronized final JList<V> getlList() {
		return this.lList;
	}

	/**
	 * 
	 * @param menu
	 */
	public void setPopupMenu(final JPopupMenu menu) {
		this.lList.setComponentPopupMenu(menu);
	}

	/**
	 * 
	 * @param list
	 *            ArrayList<V>
	 */
	@SuppressWarnings("unchecked")
	public void fillList(final ArrayList<V> list) {
		for(final Object object : list) {
			this.listModel.add(this.listModel.getSize(), (V) object);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if(e.getSource() == this.bClose) {
			this.dispose();
		}
	}

}

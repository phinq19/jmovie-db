/**
 * 
 */
package com.lars_albrecht.moviedb.components.imagepanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.lars_albrecht.moviedb.components.imagepanel.events.IImageListener;
import com.lars_albrecht.moviedb.components.imagepanel.events.ImageEvent;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JImage extends JPanel implements ActionListener, MouseListener, IImageListener {

	private JImagePanel imagePanel = null;
	private JButton previousButton = null;
	private JButton nextButton = null;

	private ImageList images = null;

	private JPopupMenu pmMenu = null;
	private JMenuItem miAdd = null;
	private JMenuItem miRemove = null;
	private JMenuItem miRemoveAll = null;

	private Integer index = null;

	public JImage() {
		super(new GridBagLayout());
		this.setBorder(BorderFactory.createEtchedBorder());
		this.init();
	}

	public void init() {
		this.images = new ImageList();
		this.images.addImageListener(this);
		this.imagePanel = new JImagePanel();
		this.imagePanel.addMouseListener(this);

		// PopUp

		this.pmMenu = new JPopupMenu("ImageMenu");
		this.miAdd = new JMenuItem("Add");
		this.miRemove = new JMenuItem("Remove");
		this.miRemoveAll = new JMenuItem("Remove All");

		this.miAdd.addActionListener(this);
		this.miRemove.addActionListener(this);
		this.miRemoveAll.addActionListener(this);

		this.pmMenu.add(this.miAdd);
		this.pmMenu.add(this.miRemove);
		this.pmMenu.addSeparator();
		this.pmMenu.add(this.miRemoveAll);

		this.add(this.pmMenu);

		// Buttons

		this.previousButton = new JButton("<");
		this.previousButton.addActionListener(this);
		this.previousButton.setVisible(Boolean.FALSE);

		this.nextButton = new JButton(">");
		this.nextButton.addActionListener(this);
		this.nextButton.setVisible(Boolean.FALSE);

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = .1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		this.add(this.previousButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		this.add(this.imagePanel, gbc);

		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = .1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		this.add(this.nextButton, gbc);
	}

	public void addImage(final Image image) {
		if((image != null) && (this.images.indexOf(image) == -1)) {
			this.images.add(image);
		}

	}

	public void setImages(final ArrayList<Image> images) {
		this.images = (ImageList) images;
	}

	public void setImage(final Image image) {
		// this.images.clear();
		if((image != null) && (this.images.indexOf(image) == -1)) {
			this.images.add(image);
		}
	}

	private void refreshPanel() {
		this.repaint();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if(e.getSource() == this.nextButton) {
			this.index++;
			this.imagePanel.setImage(this.images.get(this.index));
		} else if(e.getSource() == this.previousButton) {
			this.index--;
			this.imagePanel.setImage(this.images.get(this.index));
		} else if(e.getSource() == this.miAdd) {
			// open dialog
		} else if(e.getSource() == this.miRemove) {
			this.images.remove((int) this.index);
		} else if(e.getSource() == this.miRemoveAll) {
			// open dialog
			this.index = null;
			this.images.clear();
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if((e.getButton() == 3) || e.isPopupTrigger()) { // right click or popuptrigger
			this.pmMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public void imageAdded(final ImageEvent e) {
		if(this.images.size() > 1) {
			this.nextButton.setVisible(Boolean.TRUE);
		} else {
			this.index = 0;
			this.imagePanel.setImage(this.images.get(this.index));
			this.refreshPanel();
		}
	}

	@Override
	public void imageRemoved(final ImageEvent e) {
		if(this.images.get(this.index) == e.getImage()) {
			this.imagePanel.setImage(null);
		}
		this.refreshButtonView(e.getImage());
	}

	@Override
	public void imageGet(final ImageEvent e) {
		this.index = this.images.indexOf(e.getImage());
		if(this.images.size() - 1 == this.index) {
			this.nextButton.setVisible(Boolean.FALSE);
		} else if(this.images.size() - 1 == this.index) {
			this.previousButton.setVisible(Boolean.FALSE);
		}
		this.refreshButtonView(e.getImage());

	}

	@Override
	public void imageClear(final ImageEvent e) {
		this.refreshPanel();
		this.imagePanel.setImage(null);
		this.refreshButtonView(e.getImage());
	}

	private void refreshButtonView(final Image image) {
		if((image != null) && (this.images.indexOf(image) < this.images.size() - 1)) {
			if(!this.nextButton.isVisible()) {
				this.nextButton.setVisible(Boolean.TRUE);
			}
		} else {
			if(!this.nextButton.isVisible()) {
				this.nextButton.setVisible(Boolean.FALSE);
			}
		}

		if((image != null) && (this.images.indexOf(image) > 0)) {
			if(!this.previousButton.isVisible()) {
				this.previousButton.setVisible(Boolean.TRUE);
			}
		} else {
			if(this.previousButton.isVisible()) {
				this.previousButton.setVisible(Boolean.FALSE);
			}
		}
	}
}

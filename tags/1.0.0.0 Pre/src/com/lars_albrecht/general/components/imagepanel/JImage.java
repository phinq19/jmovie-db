/**
 * 
 */
package com.lars_albrecht.general.components.imagepanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.lars_albrecht.general.components.imagepanel.events.ImageEvent;
import com.lars_albrecht.general.components.imagepanel.events.ImageListener;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JImage extends JPanel implements ActionListener, MouseListener, ImageListener {

	private JImagePanel imagePanel = null;
	private JButton previousButton = null;
	private JButton nextButton = null;

	private Image currentImage = null;

	private ImageList images = null;

	private JPopupMenu pmMenu = null;
	private JMenuItem miAdd = null;
	private JMenuItem miRemove = null;
	private JMenuItem miRemoveAll = null;

	private Integer index = null;

	/**
	 * 
	 */
	public JImage() {
		super(new GridBagLayout());
		this.setBorder(BorderFactory.createEtchedBorder());
		this.init();
	}

	/**
	 * 
	 */
	public void init() {
		this.images = new ImageList(this);
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

	/**
	 * Adds a new image if the image does not exists.
	 * 
	 * @param image
	 *            Image
	 */
	public void addImage(final Image image) {
		if((image != null) && (this.images.indexOf(image) == -1)) {
			this.images.add(image);
		}
	}

	/**
	 * Insert a list of images (all old will be removed).
	 * 
	 * @param images
	 *            ArrayList<Image>
	 */
	public void setImages(final ArrayList<Image> images) {
		this.images = (ImageList) images;
	}

	/**
	 * Clears the imagelist and adds an image to the imagelist.
	 * 
	 * @param image
	 *            Image
	 */
	public void setImage(final Image image) {
		this.images.clear();
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
			this.currentImage = this.images.get(this.index);
			this.imagePanel.setImage(this.currentImage);
		} else if(e.getSource() == this.previousButton) {
			this.index--;
			this.currentImage = this.images.get(this.index);
			this.imagePanel.setImage(this.currentImage);
		} else if(e.getSource() == this.miAdd) {
			// open dialog
			final JFileChooser fc = new JFileChooser();

			// TODO set options for fc (editable from outerclass)
			if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				try {
					// TODO if valid image?
					this.addImage(Toolkit.getDefaultToolkit().createImage(ImageIO.read(file).getSource()));
				} catch(final IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == this.miRemove) {
			this.images.remove((int) this.index);
		} else if(e.getSource() == this.miRemoveAll) {
			this.index = null;
			this.images.clear();
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if((e.getButton() == 3) || e.isPopupTrigger()) { // right click or popuptrigger

			// disable button if current image not set
			if((this.currentImage == null) || (this.images.size() == 0)) {
				this.miRemove.setEnabled(Boolean.FALSE);
			} else {
				this.miRemove.setEnabled(Boolean.TRUE);
			}

			// disable button if no items are available
			if(this.images.size() == 0) {
				this.miRemoveAll.setEnabled(Boolean.FALSE);
			} else {
				this.miRemoveAll.setEnabled(Boolean.TRUE);
			}
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
			this.currentImage = e.getImage();
		}
	}

	@Override
	public void imageRemoved(final ImageEvent e) {
		if((this.images.size() == 0) || (this.images.get(this.index) == e.getImage())) {
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

	/**
	 * 
	 * @param listener
	 */
	public void addImageListener(final ImageListener listener) {
		this.images.addImageListener(listener);
	}

}

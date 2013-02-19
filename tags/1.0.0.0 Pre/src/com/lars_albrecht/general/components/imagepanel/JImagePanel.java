/**
 * 
 */
package com.lars_albrecht.general.components.imagepanel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.lars_albrecht.general.utilities.Helper;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class JImagePanel extends JPanel {

	private Image image = null;

	public JImagePanel() {
	}

	@Override
	public void paintComponent(final Graphics g) {
		g.setColor(super.getBackground());
		g.fillRect(0, 0, super.getWidth(), super.getHeight());

		if(this.image != null) {
			final BufferedImage bImage = Helper.toBufferedImage(this.image);
			final Point widthHeight = Helper.getProportionalWidthHeightImage(bImage, new Double(super.getWidth()), new Double(
					super.getHeight()));
			g.drawImage(bImage.getScaledInstance(widthHeight.x, widthHeight.y, Image.SCALE_FAST), 0, 0, null); // see javadoc for more info on the parameters
		} else {
			g.drawString("No image", 10, 20);
		}
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public synchronized final void setImage(final Image image) {
		this.image = image;
		this.repaint();
	}

}

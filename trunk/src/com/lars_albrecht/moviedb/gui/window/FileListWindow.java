/**
 * 
 */
package com.lars_albrecht.moviedb.gui.window;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class FileListWindow extends JFrame {

	public FileListWindow() {
		super("FileListWindow");

		this.setFrameSettings(this);

		this.addComponents(this);
	}

	private void setFrameSettings(final JFrame f) {
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void addComponents(final JFrame f) {
	}

}

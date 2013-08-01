/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.interfaces.abstracts.AInterface;

/**
 * A simple icon in the tray to control some services.
 * 
 * @author lalbrecht
 * 
 */
public class SystemTrayInterface extends AInterface implements ActionListener {

	private MenuItem	exitItem		= null;
	private MenuItem	startFinderItem	= null;
	private MenuItem	openInterface	= null;

	public SystemTrayInterface(final MainController mainController, final IController controller) {
		super(mainController, controller);
		this.canOpened = false;
	}

	private TrayIcon getPopUpMenu() {
		final PopupMenu popup = new PopupMenu();
		// TODO change icon
		final TrayIcon trayIcon = new TrayIcon(SystemTrayInterface.createImage("trunk/tray/bulb.gif", "tray icon"));

		this.exitItem = new MenuItem("Exit");
		this.exitItem.addActionListener(this);

		this.startFinderItem = new MenuItem("Finder starten");
		this.startFinderItem.addActionListener(this);

		// Take every interface and check if it is openable and add a button to
		// the popupmenu/tray.
		for (final AInterface interfaze : this.mainController.getiController().getInterfaces()) {
			if (interfaze != null && interfaze.getClass() != this.getClass() && interfaze.canOpened) {
				this.openInterface = new MenuItem("Open " + interfaze.getClass().getSimpleName());
				this.openInterface.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						interfaze.openInterface();
					}
				});
				popup.add(this.openInterface);
			}
		}

		// Add components to pop-up menu
		popup.addSeparator();
		popup.add(this.startFinderItem);
		popup.addSeparator();
		popup.add(this.exitItem);

		trayIcon.setImageAutoSize(true);
		trayIcon.setPopupMenu(popup);

		return trayIcon;
	}

	@Override
	public void startInterface() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		try {
			SystemTray.getSystemTray().add(this.getPopUpMenu());
		} catch (final AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	// Obtain the image URL
	protected static final Image createImage(final String path, final String description) {
		final URL imageURL = SystemTrayInterface.class.getResource(path);
		if (imageURL != null) {
			return (new ImageIcon(imageURL, description)).getImage();
		}

		final File f = new File(path);
		if (f.exists() && f.isFile() && f.canRead()) {
			final Image image = Helper.readImage(new File(path));
			if (image != null) {
				return (new ImageIcon(image, description)).getImage();
			}
		}

		System.err.println("Resource not found: " + path);
		return null;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == this.exitItem) {
			this.mainController.exitProgram();
		} else if (e.getSource() == this.startFinderItem) {
			this.mainController.startSearch();
		}
	}

	@Override
	public void openInterface() {
		// TODO Auto-generated method stub

	}
}

/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.interfaces.abstracts.AInterface;
import com.lars_albrecht.mdb.core.interfaces.web.WebServerInterface;
import com.lars_albrecht.mdb.core.interfaces.web.pages.abstracts.AbstractFileDetailsOutputItem;

/**
 * @author lalbrecht
 * 
 */
public class WebInterface extends AInterface {

	final ArrayList<Thread>					threadList				= new ArrayList<Thread>();
	protected AbstractFileDetailsOutputItem	fileDetailsOutputItem	= null;

	public WebInterface(final MainController mainController, final IController controller) {
		super(mainController, controller);
	}

	@Override
	public void startInterface() {
		this.threadList.add(new Thread(new WebServerInterface(this.mainController, this)));
		this.threadList.get(this.threadList.size() - 1).start();
	}

	/**
	 * 
	 * @param fileDetailsOutputItem
	 */
	public void setFileDetailsOutputItem(final AbstractFileDetailsOutputItem fileDetailsOutputItem) {
		this.fileDetailsOutputItem = fileDetailsOutputItem;
	}

	/**
	 * @return the fileDetailsOutputItem
	 */
	public final AbstractFileDetailsOutputItem getFileDetailsOutputItem() {
		return this.fileDetailsOutputItem;
	}

}

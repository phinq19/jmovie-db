/**
 * 
 */
package com.lars_albrecht.mdb;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.database.DB;

/**
 * @author lalbrecht
 * 
 */
public class Main {

	public static void main(final String[] args) {
		new Main();
	}

	private MainController	controller	= null;

	public Main() {
		this.init();
		this.controller = new MainController();
		this.controller.run();
	}

	private void init() {
		Debug.loglevel = Debug.LEVEL_DEBUG;
		this.initDB();
	}

	private void initDB() {
		new DB().init();
	}

}

/**
 * 
 */
package com.lars_albrecht.mdb;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.database.DB;

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

		// try {
		// this.controller.getDataHandler().addFileTag(new FileTag(null, 1, new
		// Tag(null, "tesdfcstmxe"), false));
		// } catch (final Exception e) {
		// e.printStackTrace();
		// }

		this.controller.run();
	}

	private void init() {
		Debug.loglevel = Debug.LEVEL_INFO;
		this.initDB();
	}

	private void initDB() {
		try {
			new DB().init();
		} catch (final Exception e) {
			e.printStackTrace();
			System.err.println("SYSTEM SHUT DOWN");
			System.exit(-1);
		}
	}

}

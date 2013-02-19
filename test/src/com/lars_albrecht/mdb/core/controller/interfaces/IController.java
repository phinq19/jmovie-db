/**
 * 
 */
package com.lars_albrecht.mdb.core.controller.interfaces;

import java.util.ArrayList;

/**
 * @author ibsisini
 * 
 */
public interface IController {

	final ArrayList<Thread> threadList = new ArrayList<Thread>();

	public void run(final Object... params);

	public ArrayList<Thread> getThreadList();

}

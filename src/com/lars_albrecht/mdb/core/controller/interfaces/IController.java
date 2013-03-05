/**
 * 
 */
package com.lars_albrecht.mdb.core.controller.interfaces;

import java.util.ArrayList;

/**
 * @author lalbrecht
 * 
 */
public interface IController {

	final ArrayList<Thread>	threadList	= new ArrayList<Thread>();

	public ArrayList<Thread> getThreadList();

	public void run(final Object... params);

}

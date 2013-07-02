/**
 * 
 */
package com.lars_albrecht.mdb.main.core.controller.interfaces;

import java.util.ArrayList;

import com.lars_albrecht.mdb.main.core.abstracts.ThreadEx;

/**
 * @author lalbrecht
 * 
 */
public interface IController {

	public ArrayList<ThreadEx> getThreadList();

	public void run(final Object... params) throws Exception;

}

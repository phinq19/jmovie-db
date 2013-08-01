/**
 * 
 */
package com.lars_albrecht.mdb.main.core.abstracts;

/**
 * Class with metadata for threads.
 * 
 * @author lalbrecht
 */
public class ThreadEx extends Thread {

	private Object[]	info	= null;

	public ThreadEx() {
		super();
	}

	public ThreadEx(final Runnable target) {
		super(target);
	}

	public ThreadEx(final String name) {
		super(name);
	}

	public ThreadEx(final Runnable target, final String name) {
		super(target, name);
	}

	public ThreadEx(final Runnable target, final String name, final Object[] info) {
		super(target, name);
		this.info = info;
	}

	/**
	 * @return the info
	 */
	public Object[] getInfo() {
		return this.info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(final Object[] info) {
		this.info = info;
	}

	@Override
	public synchronized void start() {
		super.start();
	}

}

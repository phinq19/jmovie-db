/**
 * 
 */
package com.lars_albrecht.mdb;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.mdb.controller.ThreadController;
import com.lars_albrecht.mdb.core.finder.event.FinderEvent;
import com.lars_albrecht.mdb.core.finder.event.FinderListener;
import com.lars_albrecht.mdb.database.DB;

/**
 * @author albrela
 * 
 */
public class Main implements FinderListener {

	public static void main(final String[] args) {
		new Main();

	}

	private ThreadController	threadController	= null;

	public Main() {
		this.init();
		this.threadController = new ThreadController();
		this.threadController.addFinderEventListener(this);

		this.startSearch();
	}

	private void startSearch() {
		final ArrayList<File> tempList = new ArrayList<File>();
		tempList.add(new File("D:\\test\\"));
		this.threadController.findFiles(tempList);

	}

	private void startPersist() {

	}

	private void init() {
		this.initDB();
	}

	private void initDB() {
		new DB().init();
	}

	@Override
	public void finderFoundDir(final FinderEvent e) {
		System.out.println("Found Dir: " + e.getFiles().toString());
	}

	@Override
	public void finderFoundFile(final FinderEvent e) {
		System.out.println("Found File: " + e.getFiles().toString());
	}

	@Override
	public void finderPreAdd(final FinderEvent e) {
		System.out.println("PreAdd: " + e.getFiles().toString());
	}

	@Override
	public void finderAfterAdd(final FinderEvent e) {
		System.out.println("AfterAdd: " + e.getFiles().toString());
	}

	@Override
	public void finderAfterPersist(final FinderEvent e) {
		System.out.println("AfterPersist: " + e.getFiles().toString());
	}

	@Override
	public void finderAddFinish(final FinderEvent e) {
		System.out.println("AddFinish: " + e.getFiles().toString());
	}
}

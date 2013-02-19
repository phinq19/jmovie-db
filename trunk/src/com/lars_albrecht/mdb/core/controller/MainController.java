/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.mdb.core.finder.event.FinderEvent;
import com.lars_albrecht.mdb.core.finder.event.FinderListener;
import com.lars_albrecht.mdb.core.handler.DataHandler;
import com.lars_albrecht.mdb.core.handler.TypeHandler;

/**
 * @author albrela
 * 
 */
public class MainController implements FinderListener {

	private FinderController					fController	= null;
	private CollectorController					cController	= null;
	private DataHandler							dataHandler	= null;

	private ConcurrentHashMap<String, Object>	globalVars	= null;

	public MainController() {
		this.init();
	}

	private void init() {
		this.fController = new FinderController(this);
		this.fController.addFinderEventListener(this);

		this.cController = new CollectorController(this);
		this.dataHandler = new DataHandler(this);
		this.globalVars = new ConcurrentHashMap<String, Object>();

		final ArrayList<File> tempList = new ArrayList<File>();
		tempList.add(new File("D:\\test\\"));
		this.globalVars.put("searchPathList", tempList);
	}

	public void run() {
		this.startSearch();
	}

	@SuppressWarnings("unchecked")
	private void startSearch() {
		this.fController.findFiles((ArrayList<File>) this.globalVars
				.get("searchPathList"));
	}

	private void startCollect(final ArrayList<File> foundFilesList) {
		this.cController.collectInfos(TypeHandler
				.fileListToFileItemList(foundFilesList));
	}

	/**
	 * @return the fController
	 */
	public FinderController getfController() {
		return this.fController;
	}

	/**
	 * @return the cController
	 */
	public CollectorController getcController() {
		return this.cController;
	}

	/**
	 * @return the dataHandler
	 */
	public DataHandler getDataHandler() {
		return this.dataHandler;
	}

	/**
	 * @return the globalVars
	 */
	public ConcurrentHashMap<String, Object> getGlobalVars() {
		return this.globalVars;
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
	public void finderAddFinish(final FinderEvent e) {
		this.startCollect(e.getFiles());
	}

	// this.persistFoundFiles(e.getFiles());
	/*
	 * @Deprecated private void persistFoundFiles(final ArrayList<File>
	 * foundFilesList) { // persist FileItem tempFileItem = null; // TODO
	 * uncomment if "dh.persist()" can handle an arraylist of fileitem // final
	 * ArrayList<FileItem> fileItemList = new ArrayList<FileItem>(); for (final
	 * File item : foundFilesList) { tempFileItem = new FileItem(item.getName(),
	 * item.getAbsolutePath(), item.getParent(), item.length(),
	 * Helper.getFileExtension(item.getPath())); try {
	 * this.dataHandler.persist(tempFileItem); } catch (final Exception e1) {
	 * e1.printStackTrace(); } // fileItemList.add(tempFileItem); } //
	 * dh.persist(fileItemList); /*
	 * this.finderMulticaster.finderAfterPersist((new FinderEvent(this,
	 * FinderEvent.FINDER_AFTERPERSIST, foundFilesList)));
	 */
	/* } */

}

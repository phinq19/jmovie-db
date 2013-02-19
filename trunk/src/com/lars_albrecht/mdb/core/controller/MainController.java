/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.RessourceBundleEx;
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
	private InterfaceController					iController	= null;
	private DataHandler							dataHandler	= null;

	private ConcurrentHashMap<String, Object>	globalVars	= null;

	public MainController() {
		this.init();
	}

	private void init() {
		RessourceBundleEx.getInstance().setPrefix("mdb");
		System.out.println(RessourceBundleEx.getInstance().getProperty(
				"application.name")
				+ " ("
				+ RessourceBundleEx.getInstance().getProperty(
						"application.version") + ")");

		this.fController = new FinderController(this);
		this.fController.addFinderEventListener(this);

		this.iController = new InterfaceController(this);

		this.cController = new CollectorController(this);

		this.dataHandler = new DataHandler(this);
		this.globalVars = new ConcurrentHashMap<String, Object>();

		final ArrayList<?> tempList = TypeHandler
				.castStringListToFileList(RessourceBundleEx.getInstance()
						.getProperties("module.finder.path"));
		this.globalVars.put("searchPathList", tempList);
	}

	public void run() {
		this.startInterfaces();
		// this.startSearch();

		this.test();
	}

	private void test() {

		// System.out.println(this.dataHandler.findAllInfoForId(25));

	}

	private void startInterfaces() {
		this.iController.run();
	}

	@SuppressWarnings("unchecked")
	private void startSearch() {
		this.fController.run((ArrayList<File>) this.globalVars
				.get("searchPathList"));
	}

	private void startCollect(final ArrayList<File> foundFilesList) {
		this.cController
				.run(TypeHandler.fileListToFileItemList(foundFilesList));
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
	}

	@Override
	public void finderFoundFile(final FinderEvent e) {
	}

	@Override
	public void finderPreAdd(final FinderEvent e) {
	}

	@Override
	public void finderAfterAdd(final FinderEvent e) {
	}

	@Override
	public void finderAddFinish(final FinderEvent e) {
		this.startCollect(e.getFiles());
		System.out.println("Found " + e.getFiles().size() + " files");
	}

}

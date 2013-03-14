/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.mdb.core.collector.event.CollectorEvent;
import com.lars_albrecht.mdb.core.collector.event.ICollectorListener;
import com.lars_albrecht.mdb.core.finder.event.FinderEvent;
import com.lars_albrecht.mdb.core.finder.event.IFinderListener;
import com.lars_albrecht.mdb.core.handler.DataHandler;
import com.lars_albrecht.mdb.core.handler.ObjectHandler;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class MainController implements IFinderListener, ICollectorListener {

	private FinderController					fController	= null;
	private TypeController						tController	= null;
	private CollectorController					cController	= null;
	private InterfaceController					iController	= null;
	private DataHandler							dataHandler	= null;

	private ConcurrentHashMap<String, Object>	globalVars	= null;

	public MainController() {
		this.init();
	}

	@Override
	public void finderAddFinish(final FinderEvent e) {
		Debug.log(Debug.LEVEL_INFO, "Found " + e.getFiles().size() + " files. Type them and start to collect.");
		this.startCollect(this.startTyper(ObjectHandler.fileListToFileItemList(e.getFiles())));
	}

	@Override
	public void finderAfterAdd(final FinderEvent e) {
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
	 * @return the fController
	 */
	public FinderController getfController() {
		return this.fController;
	}

	/**
	 * @return the tController
	 */
	public TypeController gettController() {
		return this.tController;
	}

	/**
	 * @return the globalVars
	 */
	public ConcurrentHashMap<String, Object> getGlobalVars() {
		return this.globalVars;
	}

	private void init() {
		RessourceBundleEx.getInstance().setPrefix("mdb");
		Debug.log(Debug.LEVEL_INFO, RessourceBundleEx.getInstance().getProperty("application.name") + " ("
				+ RessourceBundleEx.getInstance().getProperty("application.version") + ")");

		this.tController = new TypeController(this);

		this.fController = new FinderController(this);
		this.fController.addFinderEventListener(this);

		this.iController = new InterfaceController(this);

		this.cController = new CollectorController(this);
		this.cController.addCollectorEventListener(this);

		this.dataHandler = new DataHandler(this);
		this.globalVars = new ConcurrentHashMap<String, Object>();

		final ArrayList<?> tempList = ObjectHandler.castStringListToFileList(RessourceBundleEx.getInstance().getProperties(
				"module.finder.path"));
		this.globalVars.put("searchPathList", tempList);

	}

	private ArrayList<FileItem> startTyper(final ArrayList<FileItem> fileItemList) {
		return this.tController.findOutType(fileItemList);
	}

	public void run() {
		this.startInterfaces();
		this.startSearch();
	}

	private void startCollect(final ArrayList<FileItem> fileItemList) {
		this.cController.run(fileItemList);
	}

	private void startInterfaces() {
		this.iController.run();
	}

	@SuppressWarnings("unchecked")
	public void startSearch() {
		this.fController.run((ArrayList<File>) this.globalVars.get("searchPathList"));
	}

	@Override
	public void collectorsEndAll(final CollectorEvent e) {
		Debug.log(Debug.LEVEL_INFO, "All collectors ended");
		Debug.log(Debug.LEVEL_DEBUG, "Times: ");
		for (final String string : Debug.getFormattedTimes()) {
			Debug.log(Debug.LEVEL_DEBUG, string);
		}
	}

	@Override
	public void collectorsEndSingle(final CollectorEvent e) {
		Debug.log(Debug.LEVEL_TRACE, "Collector " + e.getCollectorName() + " ends");
	}

}

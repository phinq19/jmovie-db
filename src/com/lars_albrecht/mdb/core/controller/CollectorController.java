/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.collector.ACollector;
import com.lars_albrecht.mdb.core.collector.MediaInfoCollector;
import com.lars_albrecht.mdb.core.collector.event.CollectorEventMulticaster;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author albrela
 * 
 */
public class CollectorController {

	private ArrayList<ACollector>		collectors				= null;
	@SuppressWarnings("unused")
	private CollectorEventMulticaster	collectorMulticaster	= null;
	private final ArrayList<Thread>		threadList				= new ArrayList<Thread>();
	private MainController				mainController			= null;

	/**
	 * 
	 * @param controller
	 * @param dir
	 */
	public CollectorController(final MainController mainController) {
		this.mainController = mainController;
		this.collectors = new ArrayList<ACollector>();
		this.collectorMulticaster = new CollectorEventMulticaster();
		this.initCollector();
	}

	private void initCollector() {
		this.collectors.add(new MediaInfoCollector(this.mainController, this));

	}

	public ArrayList<Thread> getThreadList() {
		return this.threadList;
	}

	public void collectInfos(final ArrayList<FileItem> fileItems) {
		for (final ACollector collector : this.collectors) {
			collector.setFileItems(fileItems);
			this.threadList.add(new Thread(collector));
			this.threadList.get(this.threadList.size() - 1).start();
		}
	}
}

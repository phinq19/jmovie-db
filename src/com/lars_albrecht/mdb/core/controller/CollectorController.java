/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.collector.MediaInfoCollector;
import com.lars_albrecht.mdb.core.collector.TheMovieDBCollector;
import com.lars_albrecht.mdb.core.collector.TheTVDBCollector;
import com.lars_albrecht.mdb.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.core.collector.event.CollectorEventMulticaster;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class CollectorController implements IController {

	private ArrayList<ACollector>		collectors				= null;

	@SuppressWarnings("unused")
	private CollectorEventMulticaster	collectorMulticaster	= null;
	private MainController				mainController			= null;

	/**
	 * 
	 * @param mainController
	 */
	public CollectorController(final MainController mainController) {
		this.mainController = mainController;
		this.collectors = new ArrayList<ACollector>();
		this.collectorMulticaster = new CollectorEventMulticaster();
		this.initCollector();
	}

	public void collectInfos(final ArrayList<FileItem> fileItems) {
		for (final ACollector collector : this.collectors) {
			collector.setFileItems(fileItems);
			IController.threadList.add(new Thread(collector));
			IController.threadList.get(IController.threadList.size() - 1).start();
		}
	}

	@Override
	public ArrayList<Thread> getThreadList() {
		return IController.threadList;
	}

	private void initCollector() {
		this.collectors.add(new MediaInfoCollector(this.mainController, this));
		this.collectors.add(new TheMovieDBCollector(this.mainController, this));
		this.collectors.add(new TheTVDBCollector(this.mainController, this));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(final Object... params) {
		if ((params.length == 1) && (params[0] instanceof ArrayList<?>)) {
			this.collectInfos((ArrayList<FileItem>) params[0]);
		}
	}
}

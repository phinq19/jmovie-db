/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.collector.MediaInfoCollector;
import com.lars_albrecht.mdb.core.collector.TheMovieDBCollector;
import com.lars_albrecht.mdb.core.collector.TheTVDBCollector;
import com.lars_albrecht.mdb.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.core.collector.event.CollectorEvent;
import com.lars_albrecht.mdb.core.collector.event.CollectorEventMulticaster;
import com.lars_albrecht.mdb.core.collector.event.ICollectorListener;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class CollectorController implements IController, ICollectorListener {

	private ArrayList<ACollector>		collectors				= null;

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
		this.collectorMulticaster.add(this);
		this.initCollector();
	}

	public void collectInfos(final ArrayList<FileItem> fileItems) {
		Thread tempThread = null;
		for (final ACollector collector : this.collectors) {
			collector.setFileItems(fileItems);
			tempThread = new Thread(collector);
			tempThread.setName("Thread " + collector.getInfoType());
			IController.threadList.add(tempThread);
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

	@Override
	public void collectorsEndAll(final CollectorEvent e) {
	}

	@Override
	public void collectorsEndSingle(final CollectorEvent e) {
		if (this.getThreadList().size() == 0) {
			this.collectorMulticaster.collectorsEndAll((new CollectorEvent(this, CollectorEvent.COLLECTOR_ENDALL_COLLECTOR, null)));
		}
	}

	public void addCollectorEventListener(final ICollectorListener listener) {
		this.collectorMulticaster.add(listener);
	}

	public CollectorEventMulticaster getCollectorMulticaster() {
		return this.collectorMulticaster;
	}

}

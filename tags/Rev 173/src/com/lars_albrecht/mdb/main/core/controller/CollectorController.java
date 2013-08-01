/**
 * 
 */
package com.lars_albrecht.mdb.main.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.main.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.main.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.main.core.collector.event.CollectorEvent;
import com.lars_albrecht.mdb.main.core.collector.event.CollectorEventMulticaster;
import com.lars_albrecht.mdb.main.core.collector.event.ICollectorListener;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.models.FileItem;

/**
 * 
 * @author lalbrecht
 * 
 */
public class CollectorController implements IController, ICollectorListener {

	final ArrayList<ThreadEx>			threadList				= new ArrayList<ThreadEx>();
	private ArrayList<ACollector>		collectors				= null;

	private CollectorEventMulticaster	collectorMulticaster	= null;
	@SuppressWarnings("unused")
	private MainController				mainController			= null;

	/**
	 * 
	 * @param mainController
	 * @throws Exception
	 */
	public CollectorController(final MainController mainController) {
		this.mainController = mainController;
		this.collectorMulticaster = new CollectorEventMulticaster();
		this.collectorMulticaster.add(this);
	}

	public void collectInfos(final ArrayList<FileItem> fileItems) throws Exception {
		if (this.collectors == null || this.collectors.size() == 0) {
			throw new Exception("Collector Controller collect failed. No collectors specified");
		}
		ThreadEx tempThread = null;
		final String[] info = {
			"Collector"
		};
		for (final ACollector collector : this.collectors) {
			collector.setMainController(this.mainController);
			collector.setController(this);
			collector.setFileItems(fileItems);
			tempThread = new ThreadEx(collector, collector.getInfoType(), info);
			this.threadList.add(tempThread);
			this.threadList.get(this.threadList.size() - 1).start();
		}
	}

	@Override
	public ArrayList<ThreadEx> getThreadList() {
		return this.threadList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(final Object... params) throws Exception {
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

	/**
	 * @param collectors
	 *            the collectors to set
	 */
	public final void setCollectors(final ArrayList<ACollector> collectors) {
		this.collectors = collectors;
	}

}

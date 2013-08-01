/**
 * 
 */
package com.lars_albrecht.mdb.main.core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.lars_albrecht.mdb.main.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter;
import com.lars_albrecht.mdb.main.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public class ExportController implements IController {

	@SuppressWarnings("unused")
	private MainController			mainController	= null;

	final ArrayList<ThreadEx>		threadList		= new ArrayList<ThreadEx>();
	private ArrayList<AExporter>	exporters		= null;

	public ExportController(final MainController mainController) {
		this.mainController = mainController;
	}

	@Override
	public ArrayList<ThreadEx> getThreadList() {
		return this.threadList;
	}

	@Override
	/**
	 * params[0] -> instanceof AExporter
	 * params[1] -> instanceof File
	 * params[2] -> instanceof ArrayList<FileItem> OR instanceof FileItem
	 * 
	 * @param params list of parameters
	 */
	public void run(final Object... params) throws Exception {
		if ((params.length >= 3) && (params[0] instanceof AExporter) && (this.exporters.contains(params[0])) && (params[1] instanceof File)
				&& ((params[2] instanceof ArrayList<?>) || (params[2] instanceof FileItem))) {
			final File exportFile = (File) params[1];
			if (params[2] instanceof ArrayList<?>) {
				@SuppressWarnings("unchecked")
				final ArrayList<FileItem> fileItems = (ArrayList<FileItem>) params[2];
				((AExporter) params[0]).exportList(exportFile, fileItems, Arrays.asList(params).subList(2, params.length));
			} else if (params[2] instanceof FileItem) {
				final FileItem fileItem = (FileItem) params[2];
				((AExporter) params[0]).exportItem(exportFile, fileItem, Arrays.asList(params).subList(2, params.length));
			} else {
				throw new Exception("Wrong parameter. params[2] should be an ArrayList<FileItem> or a single FileItem");
			}
		}
	}

	/**
	 * @param exporters
	 *            the exporters to set
	 */
	public final void setExporters(final ArrayList<AExporter> exporters) {
		this.exporters = exporters;
	}

	/**
	 * @return the exporters
	 */
	public final ArrayList<AExporter> getExporters() {
		return this.exporters;
	}

}

/**
 * 
 */
package com.lars_albrecht.mdb.main.core.exporter.abstracts;

import java.io.File;
import java.util.List;

import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.models.FileItem;

/**
 * @author lalbrecht
 * 
 */
public abstract class AExporter {

	protected MainController	mainController	= null;
	protected IController		controller		= null;

	public AExporter(final MainController mainController, final IController controller) {
		this.mainController = mainController;
		this.controller = controller;
	}

	public abstract void exportList(final File file, final List<FileItem> fileList, final List<Object> options);

	public abstract void exportItem(final File file, final FileItem fileItem, final List<Object> options);

	public abstract String getExporterName();

	public abstract String getExporterDescription();

}

/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.exporter;

import java.io.File;
import java.util.List;

import com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;

/**
 * @author lalbrecht
 * @see "http://wiki.xbmc.org/index.php?title=NFO_files"
 * 
 */
public class XBMCNFOExport extends AExporter {

	@Override
	public void exportItem(final File file, final FileItem fileItem, final List<Object> options) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exportList(final File file, final List<FileItem> fileList, final List<Object> options) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getExporterDescription() {
		return "Creates *.nfo-files for each video-file and save them besides the video files.";
	}

	@Override
	public String getExporterName() {
		return this.getClass().getSimpleName();
	}

}

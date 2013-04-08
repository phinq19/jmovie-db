/**
 * 
 */
package com.lars_albrecht.mdb.core.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.lars_albrecht.general.utilities.FileFinder;
import com.lars_albrecht.general.utilities.PropertiesEx;
import com.lars_albrecht.general.utilities.PropertiesExNotInitilizedException;
import com.lars_albrecht.general.utilities.RessourceBundleEx;

/**
 * @author lalbrecht TODO Build singleton
 * 
 */
public class ConfigurationHandler {

	private ArrayList<String>	configOptionModuleFinderPath						= null;
	private String				configOptionModuleCollectorMediainfoPathCli			= null;
	private String				configOptionModuleCollectorMediainfoPathTemplate	= null;

	public ConfigurationHandler() throws FileNotFoundException, IOException, PropertiesExNotInitilizedException {
		PropertiesEx.getInstance().init(
				FileFinder.getInstance().findFile(new File(RessourceBundleEx.getInstance().getProperty("config.ini")), false));

		if (PropertiesEx.getInstance().isInit()) {
			this.readConfigFile();
		} else {
			throw new FileNotFoundException("File \""
					+ new File(RessourceBundleEx.getInstance().getProperty("config.ini")).getAbsolutePath() + "\" not Found");
		}
	}

	private void readConfigFile() throws PropertiesExNotInitilizedException {
		this.configOptionModuleFinderPath = PropertiesEx.getInstance().getProperties("module.finder.path");
		this.configOptionModuleCollectorMediainfoPathCli = PropertiesEx.getInstance().getProperty("module.collector.mediainfo.path.cli");
		this.configOptionModuleCollectorMediainfoPathTemplate = PropertiesEx.getInstance().getProperty(
				"module.collector.mediainfo.path.template");
	}

	/**
	 * @return the configOptionModuleFinderPath
	 */
	public final ArrayList<String> getConfigOptionModuleFinderPath() {
		return this.configOptionModuleFinderPath;
	}

	/**
	 * @param configOptionModuleFinderPath
	 *            the configOptionModuleFinderPath to set
	 */
	public final void setConfigOptionModuleFinderPath(final ArrayList<String> configOptionModuleFinderPath) {
		this.configOptionModuleFinderPath = configOptionModuleFinderPath;
	}

	/**
	 * @return the configOptionModuleCollectorMediainfoPathCli
	 */
	public final String getConfigOptionModuleCollectorMediainfoPathCli() {
		return this.configOptionModuleCollectorMediainfoPathCli;
	}

	/**
	 * @param configOptionModuleCollectorMediainfoPathCli
	 *            the configOptionModuleCollectorMediainfoPathCli to set
	 */
	public final void setConfigOptionModuleCollectorMediainfoPathCli(final String configOptionModuleCollectorMediainfoPathCli) {
		this.configOptionModuleCollectorMediainfoPathCli = configOptionModuleCollectorMediainfoPathCli;
	}

	/**
	 * @return the configOptionModuleCollectorMediainfoPathTemplate
	 */
	public final String getConfigOptionModuleCollectorMediainfoPathTemplate() {
		return this.configOptionModuleCollectorMediainfoPathTemplate;
	}

	/**
	 * @param configOptionModuleCollectorMediainfoPathTemplate
	 *            the configOptionModuleCollectorMediainfoPathTemplate to set
	 */
	public final void setConfigOptionModuleCollectorMediainfoPathTemplate(final String configOptionModuleCollectorMediainfoPathTemplate) {
		this.configOptionModuleCollectorMediainfoPathTemplate = configOptionModuleCollectorMediainfoPathTemplate;
	}

}
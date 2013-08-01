/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author lalbrecht
 * 
 */
public class PropertiesEx {

	private static PropertiesEx	instance	= new PropertiesEx();

	private Properties			properties	= null;

	private PropertiesEx() {
	}

	/**
	 * 
	 * @return PropertiesEx
	 */
	public static PropertiesEx getInstance() {
		return PropertiesEx.instance;
	}

	public boolean isInit() {
		return (this.properties == null ? false : true);
	}

	public void init(final File configFile) throws FileNotFoundException, IOException {
		if (configFile != null && configFile.exists() && configFile.isFile() && configFile.canRead()) {
			this.properties = new Properties();
			this.properties.load(new FileInputStream(configFile));
		}
	}

	public String getProperty(final String key) throws PropertiesExNotInitilizedException {
		if (this.isInit()) {
			return this.properties.getProperty(key);
		} else {
			throw new PropertiesExNotInitilizedException("PropertiesEx not initilized");
		}
	}

	public ArrayList<String> getProperties(final String key) throws PropertiesExNotInitilizedException {
		if (this.isInit()) {
			final ArrayList<String> resultList = new ArrayList<String>();
			int i = 1;
			while (this.contains(key + "." + i)) {
				resultList.add(this.getProperty(key + "." + i));
				i++;
			}
			return resultList;
		} else {
			throw new PropertiesExNotInitilizedException("PropertiesEx not initilized");
		}
	}

	public Boolean contains(final String key) throws PropertiesExNotInitilizedException {
		if (this.isInit()) {
			return this.properties.containsKey(key);
		} else {
			throw new PropertiesExNotInitilizedException("PropertiesEx not initilized");
		}
	}

}

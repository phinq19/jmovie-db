/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.test;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.mdb.core.collector.TheMovieDBCollector;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.database.DB;

/**
 * @author lalbrecht
 * 
 */
public class TheMovieDBCollectorTest {

	public static void main(final String[] args) {
		new TheMovieDBCollectorTest();
	}

	private MainController	controller	= null;

	public TheMovieDBCollectorTest() {
		this.init();
		this.controller = new MainController();

		final TheMovieDBCollector test = new TheMovieDBCollector(this.controller, null);
		final ArrayList<FileItem> testList = new ArrayList<FileItem>();
		final FileItem testItem1 = new FileItem();
		testItem1.setName("Herr der Ringe - AC3 - 720p.avi");
		// testList.add(testItem1);
		test.setFileItems(testList);
		test.doCollect();

		final ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>> fileAttributeListToAdd = test.getFileAttributeListToAdd();
		for (final Entry<FileItem, ArrayList<FileAttributeList>> entry : fileAttributeListToAdd.entrySet()) {
			System.out.println(entry.getKey().getName());
			for (final FileAttributeList attributesList : entry.getValue()) {
				for (final KeyValue<String, Object> keyValue : attributesList.getKeyValues()) {
					System.out.println(keyValue.getKey() + " - " + keyValue.getValue());
				}
			}
			System.out.println("");
			System.out.println("");
		}

		System.exit(-1);
	}

	private void init() {
		Debug.loglevel = Debug.LEVEL_INFO;
		this.initDB();
	}

	private void initDB() {
		try {
			new DB().init();
		} catch (final Exception e) {
			e.printStackTrace();
			System.err.println("SYSTEM SHUT DOWN");
			System.exit(-1);
		}
	}

}

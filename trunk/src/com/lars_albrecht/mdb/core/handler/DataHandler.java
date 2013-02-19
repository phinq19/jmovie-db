/**
 * 
 */
package com.lars_albrecht.mdb.core.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.IPersistable;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.TypeInformation;
import com.lars_albrecht.mdb.core.models.Value;
import com.lars_albrecht.mdb.database.DB;

/**
 * @author albrela
 * 
 */
public class DataHandler {

	private ArrayList<Key<?>>			keys			= null;
	private ArrayList<Value<?>>			values			= null;
	private ArrayList<FileItem>			fileItems		= null;
	private ArrayList<TypeInformation>	typeInformation	= null;

	public DataHandler(final MainController mainController) {
		this.reloadData();
	}

	public void reloadData() {
		this.loadKeys();
		this.loadValues();
		this.loadFileItems(false);
		this.loadTypeInformation();
	}

	private DataHandler loadKeys() {
		this.keys = TypeHandler.castObjectListToKeyList(this.findAll(
				new Key<String>(), false));

		return this;
	}

	private DataHandler loadValues() {
		this.values = TypeHandler.castObjectListToValueList(this.findAll(
				new Value<Object>(), false));

		return this;
	}

	private DataHandler loadFileItems(final boolean withArguments) {
		this.fileItems = TypeHandler.castObjectListToFileItemList(this.findAll(
				new FileItem(), withArguments));

		return this;
	}

	private DataHandler loadTypeInformation() {
		this.typeInformation = TypeHandler
				.castObjectListToTypeInformationList(this.findAll(
						new TypeInformation(), false));

		return this;
	}

	public Integer keyPos(final Key<?> searchKey) {
		for (final Key<?> key : this.getKeys()) {
			if (key.getKey() == searchKey.getKey()
					&& key.getSection() == searchKey.getSection()
					&& key.getInfoType() == searchKey.getInfoType()) {
				return key.getId();
			}
		}

		return null;
	}

	public ArrayList<?> persist(final ArrayList<?> objects) throws Exception {
		final ArrayList<IPersistable> tempArrayList = new ArrayList<IPersistable>();
		for (final Object object : objects) {
			tempArrayList.add(this.persist((IPersistable) object));
		}

		return tempArrayList;
	}

	public IPersistable persist(final IPersistable object) throws Exception {
		final HashMap<String, Object> tempObject = object.toHashMap();
		final String databaseTable = object.getDatabaseTable();
		int result = -1;
		if (tempObject != null && tempObject.size() > 0) {
			final ConcurrentHashMap<Integer, Object> values = new ConcurrentHashMap<Integer, Object>();
			String sql = "";
			String valueStr = "";
			int i = 1;

			for (final Map.Entry<String, Object> entry : tempObject.entrySet()) {
				Object x = null;
				if (entry.getValue() == null) {
					x = "";
				} else {
					x = entry.getValue();
				}
				values.put(i, x);
				if (i != 1) {
					valueStr += ",";
				}
				valueStr += "?";

				i++;
			}
			sql = "INSERT OR IGNORE INTO '" + databaseTable + "' ("
					+ Helper.implode(tempObject.keySet(), ",", "'", "'")
					+ ") VALUES (" + valueStr + ");";

			result = DB.updatePS(sql, values);
			object.setId(result);
		}
		return object;
	}

	public ArrayList<Object> findAll(final IPersistable object,
			final Boolean withSubtypes) {
		HashMap<String, Object> tempMap = null;
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if (object != null) {
			String where = "";
			ResultSet rs = null;
			if (object.getId() != null) {
				where = " WHERE id=" + object.getId();
			}
			final String sql = "SELECT * FROM " + object.getDatabaseTable()
					+ where;
			try {
				rs = DB.query(sql);
				final ResultSetMetaData rsmd = rs.getMetaData();
				for (; rs.next();) {
					tempMap = new HashMap<String, Object>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						tempMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
					}
					resultList.add(object.fromHashMap(tempMap));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * @return the keys
	 */
	public ArrayList<Key<?>> getKeys() {
		if (this.keys == null) {
			this.loadKeys();
		}
		return this.keys;
	}

	/**
	 * @return the values
	 */
	public ArrayList<Value<?>> getValues() {
		return this.values;
	}

	/**
	 * @return the fileItems
	 */
	public ArrayList<FileItem> getFileItems() {
		if (this.fileItems == null) {
			this.loadFileItems(false);
		}
		return this.fileItems;
	}

	/**
	 * @return the typeInformation
	 */
	public ArrayList<TypeInformation> getTypeInformation() {
		return this.typeInformation;
	}

}

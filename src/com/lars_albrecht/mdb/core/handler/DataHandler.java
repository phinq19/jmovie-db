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
import com.lars_albrecht.mdb.core.models.KeyValue;
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
				new Key<String>(), false, null));

		return this;
	}

	private DataHandler loadValues() {
		this.values = TypeHandler.castObjectListToValueList(this.findAll(
				new Value<Object>(), false, null));

		return this;
	}

	private DataHandler loadFileItems(final boolean withArguments) {
		this.fileItems = TypeHandler.castObjectListToFileItemList(this.findAll(
				new FileItem(), withArguments, null));

		return this;
	}

	private DataHandler loadTypeInformation() {
		this.typeInformation = TypeHandler
				.castObjectListToTypeInformationList(this.findAll(
						new TypeInformation(), false, null));

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
			final Boolean withSubtypes, final Integer limit) {
		HashMap<String, Object> tempMap = null;
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if (object != null) {
			String where = "";
			String limitStr = "";
			ResultSet rs = null;
			if (object.getId() != null) {
				where = " WHERE id=" + object.getId();
			}
			if (limit != null) {
				limitStr = " LIMIT " + limit;
			}
			final String sql = "SELECT * FROM " + object.getDatabaseTable()
					+ where + limitStr;
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

	public ArrayList<Object> findAllByFileItemValue(final String fileItemValue,
			final Boolean withSubtypes) {
		final FileItem fileItem = new FileItem();
		HashMap<String, Object> tempMap = null;
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if (fileItem != null) {
			String where = "";
			ResultSet rs = null;
			where = " WHERE ((name || fullpath || dir|| size|| ext || filehash) LIKE '%"
					+ fileItemValue + "%')";

			final String sql = "SELECT * FROM " + fileItem.getDatabaseTable()
					+ where;
			System.out.println(sql);
			try {
				rs = DB.query(sql);
				final ResultSetMetaData rsmd = rs.getMetaData();
				for (; rs.next();) {
					tempMap = new HashMap<String, Object>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						tempMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
					}
					resultList.add(fileItem.fromHashMap(tempMap));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public ArrayList<KeyValue<Key<String>, Value<Object>>> findAllInfoForId(
			final Integer id) {
		final ArrayList<KeyValue<Key<String>, Value<Object>>> resultList = new ArrayList<KeyValue<Key<String>, Value<Object>>>();
		HashMap<String, Object> tempMapKey = null;
		HashMap<String, Object> tempMapValue = null;
		ResultSet rs = null;
		final String sql = "SELECT "
				+ "	tiKey.id AS 'keyId', tiKey.Key AS 'keyKey', tiKey.infoType AS 'keyInfoType', tiKey.section AS 'keySection', tiValue.id as 'valueId', tiValue.value as 'valueValue' "
				+ "FROM " + "	fileInformation as fi " + "LEFT JOIN "
				+ " 	typeInformation as ti " + "ON " + " 	ti.file_id = fi.id "
				+ " LEFT JOIN " + " 	typeInformation_key AS tiKey " + "ON "
				+ " 	tiKey.id = ti.key_id " + "LEFT JOIN "
				+ "	typeInformation_value AS tiValue " + "ON "
				+ "	tiValue.id = ti.value_id " + "WHERE " + "	fi.id = '" + id
				+ "' ";
		try {
			rs = DB.query(sql);
			final ResultSetMetaData rsmd = rs.getMetaData();
			for (; rs.next();) {
				tempMapKey = new HashMap<String, Object>();
				tempMapValue = new HashMap<String, Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					final String originalName = Helper.lcfirst(rsmd
							.getColumnLabel(i).replaceFirst("key", "")
							.replaceFirst("value", ""));
					if (rsmd.getColumnLabel(i).startsWith("key")) {
						tempMapKey.put(originalName, rs.getObject(i));
					} else if (rsmd.getColumnLabel(i).startsWith("value")) {
						tempMapValue.put(originalName, rs.getObject(i));
					}

				}
				resultList.add(new KeyValue((Key<String>) (new Key<String>())
						.fromHashMap(tempMapKey),
						((Value<Object>) (new Value<Object>())
								.fromHashMap(tempMapValue))));
				/*
				 * if(tempMapKey.size() > 0) resultList.put("key", tempMapKey);
				 */
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 
	 * @param searchStr
	 *            = fileItemValue
	 * @param withSubtypes
	 * @return
	 */
	public ArrayList<Object> findAllInfoForString(final String searchStr,
			final Boolean withSubtypes) {
		ArrayList<Object> tempList = null;
		tempList = this.findAllByFileItemValue(searchStr, withSubtypes);

		final FileItem fileItem = new FileItem();
		HashMap<String, Object> tempMap = null;
		final ArrayList<Object> tempResultList = new ArrayList<Object>();
		if (fileItem != null) {
			String where = "";
			ResultSet rs = null;
			where = " WHERE (tiValue.value LIKE '%" + searchStr + "%')";

			final String sql = "SELECT fi.* FROM '"
					+ fileItem.getDatabaseTable() + "' AS fi LEFT JOIN "
					+ " 	typeInformation as ti " + "ON "
					+ " 	ti.file_id = fi.id " + " LEFT JOIN "
					+ " 	typeInformation_key AS tiKey " + "ON "
					+ " 	tiKey.id = ti.key_id " + "LEFT JOIN "
					+ "	typeInformation_value AS tiValue " + "ON "
					+ "	tiValue.id = ti.value_id " + where;
			System.out.println(sql);
			try {
				rs = DB.query(sql);
				final ResultSetMetaData rsmd = rs.getMetaData();
				for (; rs.next();) {
					tempMap = new HashMap<String, Object>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						tempMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
					}
					tempResultList.add(fileItem.fromHashMap(tempMap));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		tempList.addAll(tempResultList);
		return tempList;
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

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
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.IPersistable;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.TypeInformation;
import com.lars_albrecht.mdb.core.models.Value;
import com.lars_albrecht.mdb.database.DB;

/**
 * @author lalbrecht
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

	/**
	 * currently UNUSED
	 * 
	 * @param fullpath
	 * @return
	 */
	public boolean fileItemFullpathPersisted(final String fullpath) {
		boolean result = false;
		ResultSet rs = null;
		if (!this.fileItems.contains(new FileItem(fullpath))) {
			final String sql = "SELECT fi.id FROM fileInformation AS fi WHERE fi.fullpath = " + fullpath + " LIMIT 1";

			try {
				rs = DB.query(sql);
				if (rs.next()) {
					result = true;
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public ArrayList<Object> findAll(final IPersistable object, final Boolean withSubtypes, final Integer limit) {
		HashMap<String, Object> tempMap = null;
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if (object != null) {
			String where = "";
			String limitStr = "";
			ResultSet rs = null;
			if (object.getId() != null) {
				where = " WHERE id=" + object.getId();
			}
			if ((limit != null) && (limit > 0)) {
				limitStr = " LIMIT " + limit;
			}
			final String sql = "SELECT * FROM " + object.getDatabaseTable() + where + limitStr;
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

	public ArrayList<Object> findAllByFileItemValue(final String fileItemValue, final Boolean withSubtypes) {
		final FileItem fileItem = new FileItem();
		HashMap<String, Object> tempMap = null;
		final ArrayList<Object> resultList = new ArrayList<Object>();
		if (fileItem != null) {
			String where = "";
			ResultSet rs = null;
			where = " WHERE ((name || fullpath || dir|| size|| ext || filehash) LIKE '%" + fileItemValue + "%')";

			final String sql = "SELECT * FROM " + fileItem.getDatabaseTable() + where;
			System.out.println("SQL: " + sql);
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

	/**
	 * Returns a list of FileItems which are searched by the search term
	 * searchStr.
	 * 
	 * @param searchStr
	 *            String
	 * @param withSubtypes
	 *            Boolean
	 * @return ArrayList<FileItem>
	 */
	public ArrayList<Object> findAllFileItemForStringInAll(final String searchStr, final Boolean withSubtypes) {
		ArrayList<Object> tempList = null;
		tempList = this.findAllByFileItemValue(searchStr, withSubtypes);

		final FileItem fileItem = new FileItem();
		HashMap<String, Object> tempMap = null;
		if (fileItem != null) {
			String where = "";
			ResultSet rs = null;
			where = " WHERE (tiValue.value LIKE '%" + searchStr + "%')";

			final String sql = "SELECT fi.* FROM '" + fileItem.getDatabaseTable() + "' AS fi LEFT JOIN " + " 	typeInformation as ti "
					+ "ON " + " 	ti.file_id = fi.id " + " LEFT JOIN " + " 	typeInformation_key AS tiKey " + "ON "
					+ " 	tiKey.id = ti.key_id " + "LEFT JOIN " + "	typeInformation_value AS tiValue " + "ON "
					+ "	tiValue.id = ti.value_id " + where;
			System.out.println("SQL: " + sql);
			try {
				rs = DB.query(sql);
				final ResultSetMetaData rsmd = rs.getMetaData();
				for (; rs.next();) {
					tempMap = new HashMap<String, Object>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						tempMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
					}
					tempList.add(fileItem.fromHashMap(tempMap));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return tempList;
	}

	public FileItem findAllInfoForAllByFileId(final Integer fileId) {
		HashMap<String, Object> tempMap = null;
		FileItem resultItem = new FileItem();
		if (fileId != null) {
			String where = "";
			ResultSet rs = null;
			where = " WHERE id = '" + fileId + "'";

			final String sql = "SELECT * FROM " + resultItem.getDatabaseTable() + where;
			System.out.println("SQL: " + sql);
			try {
				rs = DB.query(sql);
				final ResultSetMetaData rsmd = rs.getMetaData();
				for (; rs.next();) {
					tempMap = new HashMap<String, Object>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						tempMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
					}
					resultItem = (FileItem) resultItem.fromHashMap(tempMap);
					if (resultItem.getId() != null) {
						final ArrayList<FileAttributeList> attribList = this.findAllInfoForId(fileId);
						if ((attribList != null) && (attribList.size() > 0)) {
							resultItem.getAttributes().addAll(attribList);
						}
					}
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return resultItem;
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public ArrayList<FileAttributeList> findAllInfoForId(final Integer id) {
		final ArrayList<FileAttributeList> resultList = new ArrayList<FileAttributeList>();
		// ArrayList<KeyValue<Key<String>, Value<Object>>>
		HashMap<String, Object> tempMapKey = null;
		HashMap<String, Object> tempMapValue = null;
		ResultSet rs = null;
		final String sql = "SELECT "
				+ "	tiKey.id AS 'keyId', tiKey.Key AS 'keyKey', tiKey.infoType AS 'keyInfoType', tiKey.section AS 'keySection', tiValue.id as 'valueId', tiValue.value as 'valueValue' "
				+ "FROM " + "	fileInformation as fi " + "LEFT JOIN " + " 	typeInformation as ti " + "ON " + " 	ti.file_id = fi.id "
				+ " LEFT JOIN " + " 	typeInformation_key AS tiKey " + "ON " + " 	tiKey.id = ti.key_id " + "LEFT JOIN "
				+ "	typeInformation_value AS tiValue " + "ON " + "	tiValue.id = ti.value_id " + "WHERE " + "	fi.id = '" + id
				+ "' ORDER BY keyInfoType, keySection ";
		try {
			System.out.println("SQL: " + sql);
			rs = DB.query(sql);
			final ResultSetMetaData rsmd = rs.getMetaData();
			FileAttributeList tempFileAttributeList = null;
			for (; rs.next();) { // for each line
				KeyValue kv = null;
				tempMapKey = new HashMap<String, Object>();
				tempMapValue = new HashMap<String, Object>();
				String section = null;
				String infoType = null;
				for (int i = 1; i <= rsmd.getColumnCount(); i++) { // for each
																	// column
					final String originalName = Helper.lcfirst(rsmd.getColumnLabel(i).replaceFirst("key", "").replaceFirst("value", ""));
					if (rsmd.getColumnLabel(i).startsWith("key")) {
						if (originalName.equals("section")) {
							section = rs.getString("keySection");
						} else if (originalName.equals("infoType")) {
							infoType = rs.getString("keyInfoType");
						}
						tempMapKey.put(originalName, rs.getObject(i));
					} else if (rsmd.getColumnLabel(i).startsWith("value")) {
						tempMapValue.put(originalName, rs.getObject(i));
					}

				}

				final Key<String> key = (Key<String>) (new Key<String>()).fromHashMap(tempMapKey);
				final Value<Object> value = ((Value<Object>) (new Value<Object>()).fromHashMap(tempMapValue));
				if ((key != null) && (value != null) && (value.getValue() != null)) {
					kv = new KeyValue<String, Object>(key, value);

					int index = -1;
					if ((index = this.indexOfSectionInFileAttributeList(resultList, section, infoType)) > -1) {
						tempFileAttributeList = resultList.get(index);
						tempFileAttributeList.getKeyValues().add(kv);
						resultList.set(index, tempFileAttributeList);
					} else {
						tempFileAttributeList = new FileAttributeList();
						tempFileAttributeList.setSectionName(section);
						tempFileAttributeList.getKeyValues().add(kv);
						resultList.add(tempFileAttributeList);
					}
					// resultList.add();
					/*
					 * if(tempMapKey.size() > 0) resultList.put("key",
					 * tempMapKey);
					 */
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		// System.exit(-1);
		return resultList;
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

	public ConcurrentHashMap<String, Integer> getInfoFromDatabase() {
		final ConcurrentHashMap<String, Integer> resultMap = new ConcurrentHashMap<String, Integer>();

		resultMap.put("fileCount", this.getRowCount(new FileItem()));
		resultMap.put("keyCount", this.getRowCount(new Key<Object>()));
		resultMap.put("valueCount", this.getRowCount(new Value<Object>()));

		return resultMap;
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

	public Integer getRowCount(final IPersistable object) {
		Integer result = null;
		ResultSet rs = null;
		final String sql = "SELECT COUNT(id) AS count FROM " + object.getDatabaseTable();

		try {
			rs = DB.query(sql);
			if (rs.next()) {
				result = rs.getInt("count");
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @return the typeInformation
	 */
	public ArrayList<TypeInformation> getTypeInformation() {
		return this.typeInformation;
	}

	/**
	 * @return the values
	 */
	public ArrayList<Value<?>> getValues() {
		return this.values;
	}

	private int indexOfSectionInFileAttributeList(final ArrayList<FileAttributeList> fileAttribList,
			final String sectionName,
			final String infoType) {
		if ((infoType != null) && (sectionName != null)) {
			for (final FileAttributeList fileAttributeListItem : fileAttribList) {
				if ((fileAttributeListItem != null) && (fileAttributeListItem.getSectionName() != null)) {
					if (fileAttributeListItem.getSectionName().equalsIgnoreCase(sectionName)
							&& (fileAttribList.indexOf(fileAttributeListItem) > -1)) {
						final int pos = fileAttribList.indexOf(fileAttributeListItem);
						if ((pos > -1) && (fileAttribList.get(pos) != null) && (fileAttribList.get(pos).getKeyValues() != null)
								&& (fileAttribList.get(pos).getKeyValues().size() > 0)
								&& (fileAttribList.get(pos).getKeyValues().get(0).getKey() != null)
								&& fileAttribList.get(pos).getKeyValues().get(0).getKey().getInfoType().equalsIgnoreCase(infoType)) {
							return pos;
						}
					}
				}
			}
		}

		return -1;
	}

	public Integer keyPos(final Key<?> searchKey) {
		for (final Key<?> key : this.getKeys()) {
			if ((key.getKey() == searchKey.getKey()) && (key.getSection() == searchKey.getSection())
					&& (key.getInfoType() == searchKey.getInfoType())) {
				return key.getId();
			}
		}
		return null;
	}

	private DataHandler loadFileItems(final boolean withArguments) {
		this.fileItems = TypeHandler.castObjectListToFileItemList(this.findAll(new FileItem(), withArguments, null));
		return this;
	}

	private DataHandler loadKeys() {
		this.keys = TypeHandler.castObjectListToKeyList(this.findAll(new Key<String>(), false, null));
		return this;
	}

	private DataHandler loadTypeInformation() {
		this.typeInformation = TypeHandler.castObjectListToTypeInformationList(this.findAll(new TypeInformation(), false, null));
		return this;
	}

	private DataHandler loadValues() {
		this.values = TypeHandler.castObjectListToValueList(this.findAll(new Value<Object>(), false, null));

		return this;
	}

	public ArrayList<?> persist(final ArrayList<?> objects) throws Exception {
		final ArrayList<IPersistable> tempArrayList = new ArrayList<IPersistable>();
		if ((objects != null) && (objects.size() > 0)) {
			for (final Object object : objects) {
				tempArrayList.add(this.persist((IPersistable) object));
			}
		}
		return tempArrayList;
	}

	public IPersistable persist(final IPersistable object) throws Exception {
		final HashMap<String, Object> tempObject = object.toHashMap();
		final String databaseTable = object.getDatabaseTable();
		int result = -1;
		if ((tempObject != null) && (tempObject.size() > 0)) {
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
			// TODO check if h2 db -> if h2 db than use MERGE INTO instead of
			// INSERT OR IGNORE INTO.
			// TODO if h2 db, than use TRANSACTION_ID() as id to set instead of
			// nothing.
			sql = "INSERT OR IGNORE INTO "
					+ (DB.useQuotesForFields ? "'" : "")
					+ ""
					+ databaseTable
					+ ""
					+ (DB.useQuotesForFields ? "'" : "")
					+ " ("
					+ Helper.implode(tempObject.keySet(), ",", "" + (DB.useQuotesForFields ? "'" : "") + "", ""
							+ (DB.useQuotesForFields ? "'" : "") + "") + ") VALUES (" + valueStr + ");";

			result = DB.updatePS(sql, values);
			object.setId(result);
		}
		return object;
	}

	public void reloadData() {
		this.loadKeys();
		this.loadValues();
		this.loadTypeInformation();
		this.loadFileItems(false);
	}

}
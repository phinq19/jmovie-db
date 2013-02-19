/**
 * 
 */
package com.lars_albrecht.mdb.core.collector;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.mdb.core.controller.CollectorController;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.Value;
import com.lars_albrecht.mdb.database.DB;

/**
 * @author albrela
 * 
 */
public abstract class ACollector implements Runnable {

	protected MainController											mainController			= null;
	protected CollectorController										controller				= null;
	protected ArrayList<FileItem>										fileItems				= null;
	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	public ACollector(final MainController mainController,
			final CollectorController controller) {
		this.mainController = mainController;
		this.controller = controller;
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();
	}

	public abstract void doCollect();

	public abstract ArrayList<Key<String>> getKeysToAdd();

	public abstract ArrayList<Value<?>> getValuesToAdd();

	public abstract ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>> getFileAttributeListToAdd();

	@Override
	public final void run() {
		this.doCollect();
		this.keysToAdd = this.getKeysToAdd();
		this.valuesToAdd = this.getValuesToAdd();
		this.fileAttributeListToAdd = this.getFileAttributeListToAdd();
		this.persist();
		this.mainController.getDataHandler().reloadData();
		this.controller.getThreadList().remove(Thread.currentThread());
	}

	public final void setFileItems(final ArrayList<FileItem> fileItems) {
		this.fileItems = fileItems;
	}

	private void persistKeys() {
		for (final Key<String> key : this.keysToAdd) {
			try {
				this.mainController.getDataHandler().persist(key);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void persistValues() {
		for (final Value<?> value : this.valuesToAdd) {
			try {
				this.mainController.getDataHandler().persist(value);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void persistAttributes() {
		for (final Map.Entry<FileItem, ArrayList<FileAttributeList>> entry : this.fileAttributeListToAdd
				.entrySet()) {
			// persist fileItem:
			int itemId = -1;
			try {
				System.out.println(entry.getKey());
				if ((itemId = this.mainController.getDataHandler().persist(
						entry.getKey())) > -1) {
					entry.getKey().setId(itemId);

					// persist fileAttributeList
					for (final FileAttributeList fileAttributes : entry
							.getValue()) {
						for (final KeyValue<String, Object> keyValue : fileAttributes
								.getKeyValues()) {
							final int keyId = this.mainController
									.getDataHandler()
									.getKeys()
									.get(this.mainController.getDataHandler()
											.getKeys()
											.indexOf(keyValue.getKey()))
									.getId();
							final int valueId = this.mainController
									.getDataHandler()
									.getValues()
									.get(this.mainController.getDataHandler()
											.getValues()
											.indexOf(keyValue.getValue()))
									.getId();

							System.out.println("itemId: " + itemId);
							System.out.println("keyId: " + keyId);
							System.out.println("valueId: " + valueId);
							String sql = "";
							sql += "INSERT INTO 'typeInformation' (file_id, key_id, value_id) VALUES(?, ?, ?);";
							final ConcurrentHashMap<Integer, Object> values = new ConcurrentHashMap<Integer, Object>();
							values.put(1, itemId);
							values.put(2, keyId);
							values.put(3, valueId);
							DB.updatePS(sql, values);

						}
					}

					this.mainController.getDataHandler().getFileItems()
							.add(entry.getKey());
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void persist() {
		this.persistKeys();
		this.persistValues();
		this.persistAttributes();
	}

	public abstract String getInfoType();

	/*
	 * protected final void persistKeyValues(final Integer itemId, final
	 * ArrayList<KeyValue> keyValues) { if (itemId > 0 && keyValues != null) {
	 * System.out.println("Persist for fileItem: " + itemId); for (final
	 * KeyValue keyValue : keyValues) { } }
	 * 
	 * if (this.fileArguments != null && this.fileArguments.size() > 0) {
	 * System.out.println("Persist for fileItem: " + itemId); for (final
	 * Map.Entry<String, ArrayList<KeyValue<String, Object>>> item :
	 * this.fileArguments .entrySet()) { final ArrayList<KeyValue<String,
	 * Object>> tempList = item .getValue(); for (final KeyValue<String, Object>
	 * keyValue : tempList) { final DataHandler dh = new DataHandler();
	 * dh.persistKeyValue(itemId, keyValue, item.getKey()); } } }
	 * 
	 * }
	 */
}

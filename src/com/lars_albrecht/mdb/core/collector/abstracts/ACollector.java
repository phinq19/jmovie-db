/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.abstracts;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.handler.DataHandler;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.TypeInformation;
import com.lars_albrecht.mdb.core.models.Value;

/**
 * @author lalbrecht
 * 
 */
public abstract class ACollector implements Runnable {

	protected MainController											mainController			= null;
	protected IController												controller				= null;
	protected ArrayList<FileItem>										fileItems				= null;
	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	/**
	 * Default constructor.
	 * 
	 * @param mainController
	 * @param controller
	 */
	public ACollector(final MainController mainController, final IController controller) {
		this.mainController = mainController;
		this.controller = controller;
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();
	}

	/**
	 * Start the collect method.
	 */
	public abstract void doCollect();

	/**
	 * Returns a list of file attributes to add to the database.
	 * 
	 * @return ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>
	 */
	public abstract ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>> getFileAttributeListToAdd();

	/**
	 * Returns the name of the collector.
	 * 
	 * @return String
	 */
	public abstract String getInfoType();

	/**
	 * Returns the keys to add.
	 * 
	 * @return ArrayList<Key<String>>
	 */
	public abstract ArrayList<Key<String>> getKeysToAdd();

	/**
	 * Returns the values to add.
	 * 
	 * @return ArrayList<Value<?>>
	 */
	public abstract ArrayList<Value<?>> getValuesToAdd();

	private void persist() {
		System.out.println("persist now (" + this.getInfoType() + ")");
		this.persistKeys();
		this.persistValues();
		this.persistFileItemsAndAttributes();
		System.out.println("end persist (" + this.getInfoType() + ")");
	}

	/**
	 * 
	 * @param fileItemId
	 * @param fileAttributeListList
	 * @throws Exception
	 */
	private void persistAttributes(final int fileItemId, final ArrayList<FileAttributeList> fileAttributeListList) throws Exception {
		if (fileAttributeListList.size() > 0) {
			TypeInformation tempTypeInfo = null;
			final ArrayList<Key<?>> keys = this.mainController.getDataHandler().getKeys();
			final ArrayList<Value<?>> values = this.mainController.getDataHandler().getValues();
			final ArrayList<TypeInformation> typeInfo = this.mainController.getDataHandler().getTypeInformation();
			final ArrayList<TypeInformation> attributesToPersist = new ArrayList<TypeInformation>();
			for (final FileAttributeList fileAttributes : fileAttributeListList) {
				for (final KeyValue<String, Object> keyValue : fileAttributes.getKeyValues()) {
					// System.out.println(keyValue.getKey().getInfoType() +
					// " - " + keyValue.getKey().getSection());
					int keyPos = -1;
					int keyId = -1;
					if ((keyPos = keys.indexOf(keyValue.getKey())) > -1) {
						keyId = keys.get(keyPos).getId();
					} else {
						// System.out.println("KEY: " + keyValue.getKey());
					}

					int valuePos = -1;
					int valueId = -1;
					if ((valuePos = values.indexOf(keyValue.getValue())) > -1) {
						valueId = values.get(valuePos).getId();
					}

					tempTypeInfo = new TypeInformation(fileItemId, keyId, valueId);

					if ((fileItemId > -1) && (keyId > -1) && (valueId > -1) && (tempTypeInfo != null) && !typeInfo.contains(tempTypeInfo)) {
						attributesToPersist.add(tempTypeInfo);
					}
				}
			}
			this.mainController.getDataHandler().persist(attributesToPersist);
		}
	}

	private FileItem persistFileItem(final FileItem fileItem) throws Exception {
		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().getFileItems();
		int pos = -1;
		if ((pos = fileItems.indexOf(fileItem)) > -1) {
			return fileItems.get(pos);
		} else {
			final FileItem tempFileItem = (FileItem) this.mainController.getDataHandler().persist(fileItem);
			fileItems.add(tempFileItem);
			return tempFileItem;
		}

	}

	private void persistFileItemsAndAttributes() {
		FileItem tempItem = null;
		if ((this.fileAttributeListToAdd != null) && (this.fileAttributeListToAdd.size() > 0)) {
			for (final Map.Entry<FileItem, ArrayList<FileAttributeList>> entry : this.fileAttributeListToAdd.entrySet()) {
				try {
					if (((tempItem = this.persistFileItem(entry.getKey())) != null) && (tempItem.getId() > -1)) {
						this.persistAttributes(tempItem.getId(), entry.getValue());
						this.mainController.getDataHandler().getFileItems().add(tempItem);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void persistKeys() {
		try {
			this.mainController.getDataHandler().persist(this.keysToAdd);
			this.mainController.getDataHandler().reloadData(DataHandler.RELOAD_KEYS);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void persistValues() {
		try {
			this.mainController.getDataHandler().persist(this.valuesToAdd);
			this.mainController.getDataHandler().reloadData(DataHandler.RELOAD_VALUES);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void preparePersist() {
		this.keysToAdd = Helper.uniqueList(this.keysToAdd);
		this.valuesToAdd = Helper.uniqueList(this.valuesToAdd);
	}

	@Override
	public final void run() {
		this.doCollect();
		this.keysToAdd = this.getKeysToAdd();
		this.valuesToAdd = this.getValuesToAdd();
		this.fileAttributeListToAdd = this.getFileAttributeListToAdd();
		this.preparePersist();
		this.persist();
		this.mainController.getDataHandler().reloadData(DataHandler.RELOAD_ALL);
		this.controller.getThreadList().remove(Thread.currentThread());
	}

	public final void setFileItems(final ArrayList<FileItem> fileItems) {
		this.fileItems = fileItems;
	}

}

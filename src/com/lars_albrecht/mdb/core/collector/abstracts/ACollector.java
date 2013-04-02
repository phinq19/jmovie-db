/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.abstracts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.collector.event.CollectorEvent;
import com.lars_albrecht.mdb.core.collector.event.CollectorEventMulticaster;
import com.lars_albrecht.mdb.core.controller.CollectorController;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.handler.DataHandler;
import com.lars_albrecht.mdb.core.handler.OptionsHandler;
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
	private ArrayList<TypeInformation>									typeInformationToAdd	= null;
	private CollectorEventMulticaster									collectorMulticaster	= null;

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
		this.typeInformationToAdd = new ArrayList<TypeInformation>();
		if (controller instanceof CollectorController) {
			this.collectorMulticaster = ((CollectorController) controller).getCollectorMulticaster();
		}
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
		Debug.log(Debug.LEVEL_DEBUG, "persist now (" + this.getInfoType() + ")");
		this.persistKeys();
		this.persistValues();
		this.persistAttributes();
		Debug.log(Debug.LEVEL_DEBUG, "end persist (" + this.getInfoType() + ")");
	}

	/**
	 * 
	 * @param fileItemId
	 * @param fileAttributeListList
	 * @throws Exception
	 */
	private void
			transformToTypeInformation(final int fileItemId, final ArrayList<FileAttributeList> fileAttributeListList) throws Exception {
		if (fileAttributeListList.size() > 0) {
			TypeInformation tempTypeInfo = null;
			final ArrayList<Key<?>> keys = this.mainController.getDataHandler().getKeys();
			final ArrayList<Value<?>> values = this.mainController.getDataHandler().getValues();
			final ArrayList<TypeInformation> typeInfo = this.mainController.getDataHandler().getTypeInformation();
			for (final FileAttributeList fileAttributes : fileAttributeListList) {
				for (final KeyValue<String, Object> keyValue : fileAttributes.getKeyValues()) {
					int keyPos = -1;
					int keyId = -1;
					if ((keyPos = keys.indexOf(keyValue.getKey())) > -1) {
						keyId = keys.get(keyPos).getId();
					}

					int valuePos = -1;
					int valueId = -1;
					if ((valuePos = values.indexOf(keyValue.getValue())) > -1) {
						valueId = values.get(valuePos).getId();
					}

					tempTypeInfo = new TypeInformation(fileItemId, keyId, valueId);

					if ((fileItemId > -1) && (keyId > -1) && (valueId > -1) && (tempTypeInfo != null) && !typeInfo.contains(tempTypeInfo)) {
						this.typeInformationToAdd.add(tempTypeInfo);
					}
				}
			}
		}
	}

	/**
	 * Returns an id of a given fileItem.
	 * 
	 * @param fileItem
	 * @return int
	 * @throws Exception
	 */
	private int getFileItemId(final FileItem fileItem) throws Exception {
		final ArrayList<FileItem> fileItems = this.mainController.getDataHandler().getFileItems();
		int pos = -1;
		if ((pos = fileItems.indexOf(fileItem)) > -1) {
			this.mainController.getDataHandler().updateUpdateTSForFileItem(fileItems.get(pos).getId());
			return fileItems.get(pos).getId();
		} else {
			/*
			 * Should not be called.
			 */
			Debug.log(Debug.LEVEL_FATAL,
					"File item not found in DataHandler-FileItem-List: " + fileItem.getId() + " - " + fileItem.getFullpath());
			throw new Exception("File item not found in DataHandler-FileItem-List: " + fileItem.getId() + " - " + fileItem.getFullpath());
		}

	}

	/**
	 * Persist attributes (typeInformation). Take this.fileAttributeListToAdd
	 * which contains the fileItem and an ArrayList of FileAttributeList
	 * (ArrayList<FileAttributeList>). To persist the typeInformation, a new
	 * method called "persistTypeInformation" was created. In this method here,
	 * a method called "prepareAttributes" was created to add specific items to
	 * a general list.
	 * 
	 */
	private void persistAttributes() {
		int fileItemId = -1;
		if ((this.fileAttributeListToAdd != null) && (this.fileAttributeListToAdd.size() > 0)) {
			for (final Map.Entry<FileItem, ArrayList<FileAttributeList>> entry : this.fileAttributeListToAdd.entrySet()) {
				try {
					if (((fileItemId = this.getFileItemId(entry.getKey())) > -1)) {
						this.transformToTypeInformation(fileItemId, entry.getValue());
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		this.persistTypeInformation();
	}

	/**
	 * Persist typeInformation.
	 */
	private void persistTypeInformation() {
		try {
			this.mainController.getDataHandler().persist(this.typeInformationToAdd);
		} catch (final Exception e) {
			e.printStackTrace();
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

	/**
	 * Prepare keyList and valueList for persist. e.g. delete duplicated
	 * entries.
	 */
	private void preparePersist() {
		this.keysToAdd = Helper.uniqueList(this.keysToAdd);
		this.valuesToAdd = Helper.uniqueList(this.valuesToAdd);
	}

	/**
	 * Let only items in fileItems-list that are not collected already. Use the
	 * updateTS and the options (collectorEndRunLast) for this feature.
	 */
	private void prepareFileItems() {
		final Long lastRun = (Long) OptionsHandler.getOption("collectorEndRunLast" + Helper.ucfirst(this.getInfoType()));
		for (int i = 0; i < this.fileItems.size(); i++) {
			System.out.println(i);
			System.out.println(this.fileItems.get(i));
			System.out.println(this.fileItems.get(i).getUpdateTS());
			if (lastRun != null && this.fileItems.get(i).getUpdateTS() != null && lastRun > this.fileItems.get(i).getUpdateTS()) {
				Debug.log(Debug.LEVEL_DEBUG, "Element collected already: " + this.fileItems.get(i));
				this.fileItems.remove(i);
				i--;
			} else {
				Debug.log(Debug.LEVEL_TRACE, "Element not collected: " + this.fileItems.get(i));
			}
		}
	}

	@Override
	public final void run() {
		OptionsHandler.setOption("collectorStartRunLast" + Helper.ucfirst(this.getInfoType()), new Timestamp(System.currentTimeMillis()));
		Debug.startTimer("Collector collect time: " + this.getInfoType());
		this.prepareFileItems();
		this.doCollect();
		Debug.stopTimer("Collector collect time: " + this.getInfoType());
		this.keysToAdd = this.getKeysToAdd();
		this.valuesToAdd = this.getValuesToAdd();
		this.fileAttributeListToAdd = this.getFileAttributeListToAdd();
		this.preparePersist();
		Debug.startTimer("Collector persist time: " + this.getInfoType());
		this.persist();
		Debug.stopTimer("Collector persist time: " + this.getInfoType());
		this.mainController.getDataHandler().reloadData(DataHandler.RELOAD_ALL);
		this.controller.getThreadList().remove(Thread.currentThread());
		OptionsHandler.setOption("collectorEndRunLast" + Helper.ucfirst(this.getInfoType()), new Timestamp(System.currentTimeMillis()));
		this.collectorMulticaster.collectorsEndSingle((new CollectorEvent(this, CollectorEvent.COLLECTOR_ENDSINGLE_COLLECTOR, this
				.getInfoType())));
	}

	public final void setFileItems(final ArrayList<FileItem> fileItems) {
		this.fileItems = fileItems;
	}

}

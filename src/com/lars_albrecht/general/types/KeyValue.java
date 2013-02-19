/**
 * 
 */
package com.lars_albrecht.general.types;

/**
 * Key/Value-pair.
 * 
 * @author lalbrecht
 * 
 */
public class KeyValue<K, V> {

	private K key = null;
	private V value = null;

	public KeyValue() {
	}

	/**
	 * @param key
	 * @param value
	 */
	public KeyValue(final K key, final V value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public synchronized final K getKey() {
		return this.key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public synchronized final void setKey(final K key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public synchronized final V getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public synchronized final void setValue(final V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return (String) this.getValue();
	}

}

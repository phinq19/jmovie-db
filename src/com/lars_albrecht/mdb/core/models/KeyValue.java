/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

/**
 * 
 * This model class holds the key values.
 * 
 * @author lalbrecht
 * 
 */
public final class KeyValue<K, V> {

	private Key<K>		key		= null;
	private Value<V>	value	= null;

	/**
	 * @param key
	 * @param value
	 */
	public KeyValue(final Key<K> key, final Value<V> value) {
		super();
		this.key = key;
		this.value = value;
	}

	public Key<K> getKey() {
		return this.key;
	}

	public Value<V> getValue() {
		return this.value;
	}

	public void setKey(final Key<K> key) {
		this.key = key;
	}

	public void setValue(final Value<V> value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.key + " - " + this.value;
	}
}

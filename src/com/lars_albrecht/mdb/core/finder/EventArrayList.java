/**
 * 
 */
package com.lars_albrecht.mdb.core.finder;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.finder.event.ArrayListEvent;
import com.lars_albrecht.mdb.core.finder.event.ArrayListEventMulticaster;
import com.lars_albrecht.mdb.core.finder.event.ArrayListListener;

/**
 * @author albrela
 * 
 */
public class EventArrayList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= -2084366476048391943L;
	private ArrayListEventMulticaster	multicaster			= null;

	public EventArrayList() {
		super();
		this.multicaster = new ArrayListEventMulticaster();
	}

	@Override
	public boolean add(final E e) {
		final boolean result = super.add(e);
		this.multicaster.arrayListenerAdd((new ArrayListEvent(this, ArrayListEvent.FINDER_ADD)));
		return result;
	}

	@Override
	public void add(final int index, final E element) {
		super.add(index, element);
		this.multicaster.arrayListenerAdd((new ArrayListEvent(this, ArrayListEvent.FINDER_ADD)));
	}

	public void addArrayListEventListener(final ArrayListListener listener) {
		this.multicaster.add(listener);
	}

	@Override
	public void clear() {
		super.clear();
		this.multicaster.arrayListenerClear((new ArrayListEvent(this, ArrayListEvent.FINDER_CLEAR)));
	}

	@Override
	public E remove(final int index) {
		final E result = super.remove(index);
		this.multicaster.arrayListenerRemove((new ArrayListEvent(this, ArrayListEvent.FINDER_REMOVE)));
		return result;
	}

	@Override
	public boolean remove(final Object o) {
		final boolean result = super.remove(o);
		this.multicaster.arrayListenerRemove((new ArrayListEvent(this, ArrayListEvent.FINDER_REMOVE)));
		return result;
	}

	public void removeArrayListEventListener(final ArrayListListener listener) {
		this.multicaster.remove(listener);
	}
}

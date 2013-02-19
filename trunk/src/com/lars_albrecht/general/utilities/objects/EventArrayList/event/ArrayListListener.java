/**
 * 
 */
package com.lars_albrecht.general.utilities.objects.EventArrayList.event;

import java.util.EventListener;

/**
 * @author albrela
 * 
 */
public interface ArrayListListener extends EventListener {

	public void arrayListenerAdd(ArrayListEvent e);

	public void arrayListenerRemove(ArrayListEvent e);

	public void arrayListenerClear(ArrayListEvent e);

}

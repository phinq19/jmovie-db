/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.events;

import java.util.EventListener;

/**
 * @author lalbrecht
 * 
 */
public interface RaterListener extends EventListener {

	public void raterSelected(RaterEvent e);

}

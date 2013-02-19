/**
 * 
 */
package com.lars_albrecht.general.components.awt;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * @author lalbrecht
 * 
 */
public class TablePopupMenuAdapter implements PopupMenuListener {

	/**
	 * 
	 * @param e
	 *            PopupMenuEvent
	 */
	private void updateSelection(final PopupMenuEvent e) {
		final AWTEvent awtEvent = EventQueue.getCurrentEvent();
		final MouseEvent me;
		if(!(awtEvent instanceof MouseEvent) || !(me = (MouseEvent) awtEvent).isPopupTrigger()) {
			return;
		}
		final JPopupMenu menu = (JPopupMenu) e.getSource();
		final Component invoker = menu.getInvoker();

		if(!(invoker instanceof JTable)) {
			return;
		}
		final JTable table = (JTable) invoker;
		final Point p = me.getPoint();
		final int row = table.rowAtPoint(p);
		if(row == -1) {
			return;
		}

		final int[] indices = table.getSelectedRows();
		final int rowSelected = Arrays.binarySearch(indices, row);
		if((rowSelected < 0) && !Arrays.asList(indices).contains(row)) {
			table.clearSelection();
			table.setRowSelectionInterval(row, row);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
	 */
	@Override
	public void popupMenuCanceled(final PopupMenuEvent e) {
		this.updateSelection(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
	 */
	@Override
	public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
		this.updateSelection(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
	 */
	@Override
	public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
		this.updateSelection(e);
	}

}

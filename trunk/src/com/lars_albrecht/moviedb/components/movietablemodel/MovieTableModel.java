/**
 * 
 */
package com.lars_albrecht.moviedb.components.movietablemodel;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * 
 * @author lalbrecht
 * 
 */
public class MovieTableModel implements TableModel {

	private final Vector<MovieModel> movies = new Vector<MovieModel>();
	private final Vector<TableModelListener> listeners = new Vector<TableModelListener>();

	private TreeMap<Integer, Object[]> neededFields = null;

	public MovieTableModel() {
		super();
		this.neededFields = new TreeMap<Integer, Object[]>();
		for(final FieldModel fieldModel : Controller.flTable) {
			final Field field = fieldModel.getField();
			final Object[] value = { field, fieldModel.getAs().equals("") ? field.getName() : fieldModel.getAs() };
			this.neededFields.put(fieldModel.getSort(), value);
		}

	}

	/**
	 * 
	 * @param movie
	 * @return Integer
	 */
	public Integer indexOf(final MovieModel movie) {
		return this.movies.indexOf(movie);
	}

	/**
	 * 
	 * @param index
	 *            Integer
	 * @return MovieModel
	 */
	public MovieModel getMovie(final Integer index) {
		if(this.movies.size() >= index) {
			return this.movies.get(index.intValue());
		}
		return null;
	}

	public void addMovie(final MovieModel movie) {
		// index of movie
		final int index = this.movies.size();
		this.movies.add(movie);

		// call listeners
		// new event
		final TableModelEvent e = new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);

		this.callListenersChange(e);
	}

	public void callListenersChange(final TableModelEvent e) {
		// send event to listeners
		for(int i = 0, n = this.listeners.size(); i < n; i++) {
			this.listeners.get(i).tableChanged(e);
		}
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return column > 1 ? true : false;
	}

	@Override
	public void addTableModelListener(final TableModelListener l) {
		this.listeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		if(((Field) this.neededFields.get(Helper.getKeyFromMapPos(this.neededFields, columnIndex))[0]).getType() == Image.class) {
			return ImageIcon.class;
		}
		return ((Field) this.neededFields.get(Helper.getKeyFromMapPos(this.neededFields, columnIndex))[0]).getType();
	}

	@Override
	public int getColumnCount() {
		return this.neededFields.size();
	}

	@Override
	public String getColumnName(final int columnIndex) {
		return (String) this.neededFields.get(Helper.getKeyFromMapPos(this.neededFields, columnIndex))[1];
	}

	@Override
	public int getRowCount() {
		return this.movies.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		if(this.movies.size() > 0) {
			final MovieModel movie = this.movies.get(rowIndex);

			final Field field = ((Field) this.neededFields.get(Helper.getKeyFromMapPos(this.neededFields, columnIndex))[0]);

			Object o = null;
			try {
				o = movie.get(field.getName());
			} catch(final IllegalArgumentException e) {
				e.printStackTrace();
			} catch(final SecurityException e) {
				e.printStackTrace();
			} catch(final IllegalAccessException e) {
				e.printStackTrace();
			} catch(final InvocationTargetException e) {
				e.printStackTrace();
			}
			Object resultVal = null;
			if(o == null) {
				resultVal = null;
			} else {
				if(o.getClass() == String.class) {
					resultVal = o;
				} else if(o.getClass() == Integer.class) {
					resultVal = Integer.toString((Integer) o);
				} else if(o.getClass() == File.class) {
					resultVal = ((File) o).getAbsolutePath();
				} else if(o.getClass() == ArrayList.class) {
					resultVal = ((ArrayList<Object>) o).size() > 0 ? Helper.implode(((ArrayList<Object>) o), ", ", null, null)
							: null;
				} else if(o.getClass() == Boolean.class) {
					resultVal = o;
				} else if(o instanceof Image) {
					final Point widthHeight = Helper.getProportionalWidthHeightImage(Helper.toBufferedImage((Image) o), 50.0,
							50.0);
					resultVal = new ImageIcon(((Image) o).getScaledInstance(widthHeight.x, widthHeight.y, Image.SCALE_SMOOTH));
				} else {
					resultVal = null;
				}
			}

			return resultVal;
		}
		return null;
	}

	@Override
	public void removeTableModelListener(final TableModelListener l) {
		this.listeners.remove(l);

	}

	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
		Debug.log(Debug.LEVEL_DEBUG, "setValueAt " + aValue + " @ " + rowIndex + " : " + columnIndex);
	}

	public Vector<MovieModel> getMovies() {
		return this.movies;
	}
}

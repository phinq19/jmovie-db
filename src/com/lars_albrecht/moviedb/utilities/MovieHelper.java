/**
 * 
 */
package com.lars_albrecht.moviedb.utilities;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.annotation.FilterOptions;
import com.lars_albrecht.moviedb.annotation.ParseOptions;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.annotation.ViewInTable;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;

/**
 * @author lalbrecht
 * 
 */
public class MovieHelper {

	/**
	 * 
	 * @param cClass
	 *            Class<?>
	 * @param fieldName
	 *            String
	 * 
	 * @return FieldList
	 * 
	 * @throws SecurityException
	 */
	public static FieldModel getTabFieldModelFromClass(final Class<?> cClass, final String fieldName) throws SecurityException {
		FieldModel tempField = null;
		if(cClass != null) {
			Field field = null;
			try {
				field = cClass.getDeclaredField(fieldName);
			} catch(final NoSuchFieldException e) {
			}
			if(field != null) {
				final ViewInTab vit = field.getAnnotation(ViewInTab.class);
				if(vit != null) {
					final ConcurrentHashMap<String, Object> additionalList = new ConcurrentHashMap<String, Object>();
					additionalList.put("editable", vit.editable());
					additionalList.put("format", vit.format());
					tempField = new FieldModel(field, vit.as(), vit.type(), null, vit.sort(), vit.tabname(), additionalList);
				}

			}
		}
		return tempField;
	}

	/**
	 * 
	 * @param cClass
	 *            Class<?>
	 * @return FieldList
	 */
	public static FieldList getTableFieldModelFromClass(final Class<?> cClass) {
		final FieldList tempList = new FieldList();
		if(cClass != null) {
			final Field[] fields = cClass.getDeclaredFields();
			for(final Field field : fields) {
				final ViewInTable vit = field.getAnnotation(ViewInTable.class);
				if(vit != null) {
					tempList.add(new FieldModel(field, vit.as(), null, null, vit.sort(), null, null));
				}
			}
		}
		return tempList;
	}

	/**
	 * 
	 * @param cClass
	 *            Class<?>
	 * @return FieldList
	 */
	public static FieldList getFilterFieldModelFromClass(final Class<?> cClass) {
		final FieldList tempList = new FieldList();
		if(cClass != null) {
			final Field[] fields = cClass.getDeclaredFields();
			for(final Field field : fields) {
				final FilterOptions fo = field.getAnnotation(FilterOptions.class);
				if(fo != null) {
					tempList.add(new FieldModel(field, fo.as(), null, null, fo.sort(), null, null));
				}
			}
		}
		return tempList;
	}

	/**
	 * 
	 * @param cClass
	 *            Class<?>
	 * @return FieldList
	 */
	public static FieldList getParseFieldModelFromClass(final Class<?> cClass) {
		final FieldList tempList = new FieldList();
		if(cClass != null) {
			final Field[] fields = cClass.getDeclaredFields();
			for(final Field field : fields) {
				final ParseOptions po = field.getAnnotation(ParseOptions.class);
				if(po != null) {
					tempList.add(new FieldModel(field, po.as(), po.type(), po.typeConf(), null, null, null));
				}
			}
		}
		return tempList;
	}

	/**
	 * 
	 * @param cClass
	 *            Class<?>
	 * @return FieldList
	 */
	public static FieldList getDBFieldModelFromClass(final Class<?> cClass) {
		final FieldList tempList = new FieldList();
		if(cClass != null) {
			final Field[] fields = cClass.getDeclaredFields();
			for(final Field field : fields) {
				final DatabaseOptions dbo = field.getAnnotation(DatabaseOptions.class);
				if(dbo != null) {
					final ConcurrentHashMap<String, Object> additionalList = new ConcurrentHashMap<String, Object>();
					additionalList.put("additionalType", dbo.additionalType());
					additionalList.put("defaultValues", dbo.defaultValues());
					additionalList.put("isUnique", dbo.isUnique());
					tempList.add(new FieldModel(field, dbo.as(), dbo.type(), null, null, null, additionalList));
				}
			}
		}
		return tempList;
	}

	/**
	 * 
	 * @param cClass
	 *            Class<?>
	 * @return FieldList
	 */
	public static FieldList getTabFieldModelFromClass(final Class<?> cClass) {
		final FieldList tempList = new FieldList();
		if(cClass != null) {
			final Field[] fields = cClass.getDeclaredFields();
			for(final Field field : fields) {
				final ViewInTab vit = field.getAnnotation(ViewInTab.class);
				if(vit != null) {
					final ConcurrentHashMap<String, Object> additionalList = new ConcurrentHashMap<String, Object>();
					additionalList.put("editable", vit.editable());
					additionalList.put("format", vit.format());
					tempList.add(new FieldModel(field, vit.as(), vit.type(), null, vit.sort(), vit.tabname(), additionalList));
				}
			}
		}
		return tempList;
	}

}

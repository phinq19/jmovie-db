/**
 * 
 */
package com.lars_albrecht.mdb.main.core.collector.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter;
import com.lars_albrecht.mdb.main.core.models.FileItem;

/**
 * This is a simple PDF Export.
 * 
 * @author lalbrecht
 * 
 */
public class PDFExport extends AExporter {

	public PDFExport(final MainController mainController, final IController controller) {
		super(mainController, controller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter#exportList
	 * (java.io.File, java.util.List, java.util.List)
	 */
	@Override
	public void exportList(final File file, final List<FileItem> fileList, final List<Object> options) {
		try {
			final Document document = new Document();
			document.setPageSize(PageSize.A4);
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			document.add(this.generateMultiItemTable(fileList));
			document.close();

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private PdfPTable generateMultiItemTable(final List<FileItem> fileList) {
		// a table with four columns
		final PdfPTable table = new PdfPTable(4);
		table.setHeaderRows(3);
		table.setWidthPercentage(100);
		// the cell object to use
		PdfPCell cell = null;
		Font font = null;

		font = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);

		cell = new PdfPCell(new Phrase("MDB - FileItem export", font));
		cell.setColspan(4);
		cell.setBorderColor(BaseColor.WHITE);
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);

		table.getDefaultCell().setBackgroundColor(BaseColor.BLACK);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setBorderColor(BaseColor.WHITE);
		font = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
		table.addCell(new Phrase("Titel", font));
		table.addCell(new Phrase("Typ", font));
		table.addCell(new Phrase("Größe", font));
		table.addCell(new Phrase("Hinzugefügt am", font));

		font = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
		table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorderColor(BaseColor.BLACK);
		fileList.addAll(fileList);

		for (final FileItem fileItem : fileList) {
			table.addCell(new Phrase(fileItem.getName(), font));
			table.addCell(new Phrase(fileItem.getFiletype(), font));
			table.addCell(new Phrase(Helper.getHumanreadableFileSize(fileItem.getSize()), font));
			table.addCell(new Phrase(Helper.getFormattedTimestamp(fileItem.getCreateTS().longValue(), "dd-mm-yyyy"), font));
		}

		return table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter#exportItem
	 * (java.io.File, com.lars_albrecht.mdb.main.core.models.FileItem,
	 * java.util.List)
	 */
	@Override
	public void exportItem(final File file, final FileItem fileItem, final List<Object> options) {
		System.err.println("Currently not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter#getExporterName
	 * ()
	 */
	@Override
	public String getExporterName() {
		return this.getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.mdb.main.core.exporter.abstracts.AExporter#
	 * getExporterDescription()
	 */
	@Override
	public String getExporterDescription() {
		return "Returns a PDF file";
	}

}

/**
 * 
 */
package com.lars_albrecht.moviedb.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author lalbrecht
 * 
 *         TODO do more than that
 * 
 */
@SuppressWarnings("unused")
public class PDFCreator {

	private File file = null;
	private Font mainTitle = null;
	private Font subTitle = null;
	private Font defaultFont = null;

	private Document document = null;

	public PDFCreator(final File file) {
		this.file = file;

		this.init();
	}

	private void init() {
		this.mainTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		this.subTitle = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.GRAY);
		this.defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

		this.document = new Document();
		try {
			final PdfWriter pdf = PdfWriter.getInstance(this.document, new FileOutputStream(this.file));
			this.document.open();
			this.document.add(new Paragraph("JMovieDB-Export"));
			final PdfPTable table = new PdfPTable(6); // Code 1
			table.setHeaderRows(5);
			// Code 2
			table.addCell("1");
			table.addCell("2");

			// Code 3
			table.addCell("3");
			table.addCell("4");

			// Code 4
			table.addCell("5");
			table.addCell("6");

			// Code 5
			this.document.add(table);
			this.document.close();

		} catch(final FileNotFoundException e) {
			e.printStackTrace();
		} catch(final DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		new PDFCreator(new File("C:\\Users\\lalbrecht\\Documents\\test.pdf"));
	}

}

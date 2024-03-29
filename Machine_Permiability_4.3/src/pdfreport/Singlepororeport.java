package pdfreport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import application.DataStore;
import application.Myapp;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import data_read_write.DatareadN;

public class Singlepororeport {

	String notes;
	String companyname;
	String imgpath,imgpath1;
	List<String> graphs;
	Boolean darcy , gurley , frazier;

	DatareadN d;

	List<String> wpressur, wf, df;

	boolean isRow = false;
	boolean isHeaderadd = false;

	// Row Data
	BaseColor backcellcoltable = new BaseColor(130, 130, 130);
	BaseColor backcellcol = new BaseColor(230, 230, 230);
	Font tablemean = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD,
			new BaseColor(100, 100, 100));

	Font rowhed = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
			new BaseColor(255, 255, 255));

	Font unitlabrow = FontFactory.getFont(FontFactory.HELVETICA, 8,
			Font.NORMAL, new BaseColor(255, 255, 255));
	
	Font unitlabrownew = FontFactory.getFont(FontFactory.HELVETICA, 6,
			Font.NORMAL, new BaseColor(255, 255, 255));

	public static final String FONT = "/OpenSansCondensed-Light.ttf";
	public static final String FONTbb = "/OpenSansCondensed-Light.ttf";
	public static final String flegend = "/FiraSans-Regular.ttf";
	public static final String testnamefont = "/font/BebasNeue Book.ttf";
	public static final String coverptestname = "/font/Roboto-Regular.ttf";
	public static final String testtype = "/font/Roboto-Black.ttf";

	Font ftime = FontFactory.getFont(testtype, 10, Font.ITALIC, new BaseColor(
			0, 0, 0));
	Font comname = FontFactory.getFont(testtype, 12, Font.NORMAL,
			new BaseColor(0, 0, 0));

	Document document = new Document(PageSize.A4, 36, 36, 90, 36);
	PdfWriter writer;
	Font blueFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9,
			Font.NORMAL, new CMYKColor(100, 25, 0, 20));
	Font resultwhitecolorq = FontFactory.getFont(FontFactory.HELVETICA, 9,
			Font.NORMAL, new CMYKColor(1, 1, 1, 1));
	Font resultwhitecolora = FontFactory.getFont(FontFactory.HELVETICA, 9,
			Font.BOLD, new CMYKColor(1, 1, 1, 1));
	Font legendcircle = FontFactory.getFont(FontFactory.HELVETICA, 16,
			Font.NORMAL, new CMYKColor(1f, 0.34f, 0f, 0.29f));

	// First page new
	// Font filetypes = FontFactory.getFont(FontFactory.HELVETICA, 10,
	// Font.NORMAL, new CMYKColor(0f,0f,0f,0f));
	Font testname = FontFactory.getFont(coverptestname, 25, Font.BOLD,
			new CMYKColor(1f, 0.34f, 0f, 0.29f));

	Font fname = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL,
			new BaseColor(96, 96, 98));

	Font sampleinfoq = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.BOLD, new BaseColor(81, 81, 83));

	Font whitecol = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD,
			new BaseColor(255, 255, 255));

	Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(90, 90, 92));
	Font sampleinfoqh = FontFactory.getFont(FontFactory.HELVETICA, 15,
			Font.BOLD, new BaseColor(81, 81, 83));

	// NEW TABLE

	Font sampleinfola = FontFactory.getFont(FontFactory.HELVETICA, 10,
			Font.NORMAL, new BaseColor(255, 255, 255));
	Font sampleinfoans = FontFactory.getFont(FontFactory.HELVETICA, 20,
			Font.BOLD, new BaseColor(100, 100, 100));
	Font unitlab = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
			new BaseColor(100, 100, 100));

	Font graphtitle = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			getColor(14));
	Font legendtitle = FontFactory.getFont(FontFactory.HELVETICA, 12,
			Font.NORMAL, new BaseColor(96, 96, 98));
	Font legenddot = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);

	// Address
	Font addresslab = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(81, 81, 83));

	// Notes
	Font noteslab = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD,
			new BaseColor(81, 81, 83));
	
	Font resultlab = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD,
			new BaseColor(81, 81, 83));

	// Notes desc
	Font notesdeslab = FontFactory.getFont(FontFactory.HELVETICA, 11,
			Font.NORMAL, new BaseColor(81, 81, 83));

	List<String> clr = new ArrayList<String>();
	
	DatareadN dr1;

	public Singlepororeport() {
		clr.add("#000080");
		clr.add("#0000FF");
		clr.add("#00FFFF");
		clr.add("#008000");
		clr.add("#00FF00");
		clr.add("#FF00FF");
	}
	/* Main Function Create Report */
	public void Report(String path, DatareadN d, String notes, String comname,
			String imgpath, List<String> graphs, Boolean btabledata, Boolean bcoverpage,String imgpath1 , Boolean lbldarcy, Boolean lblgurley , Boolean lblfrazer) {

		this.companyname = comname;
		this.notes = notes;
		this.imgpath = imgpath;
		this.imgpath1 = imgpath1;
		this.graphs = graphs;
		this.d = d;
		
		this.darcy = lbldarcy;
		this.gurley = lblgurley;
		this.frazier = lblfrazer;

		try {

			writer = PdfWriter
					.getInstance(document, new FileOutputStream(path));

			// write to document
			document.open();

			if (bcoverpage == true) {
				coverpage(d);
			}
		

			document.newPage();
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			writer.setPageEvent(event);

			sampleinfo(d);

			document.newPage();

			File folder = new File("mypic");
			File[] listOfFiles = folder.listFiles();
			int nflag = 0;

			System.out.println("Selection :" + graphs);

			for (int i = 0; i < listOfFiles.length; i++) {

				System.out.println("File :" + listOfFiles[i].getName() + " : "
						+ graphs.get(i));

				if (graphs.get(i).equals("1")) {
					resultgraph(listOfFiles[i]);

					if (nflag % 2 == 1) {
						document.newPage();

					}

					nflag++;

				}

			}
			if (btabledata == true) {
				document.newPage();
				rowData();
			}
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/* RGB Color Code */
	BaseColor getColor(int i) {
		List<BaseColor> clrs = new ArrayList<>();
		clrs.add(new BaseColor(255, 00, 00));
		clrs.add(new BaseColor(00, 81, 212));
		clrs.add(new BaseColor(255, 204, 00));
		clrs.add(new BaseColor(143, 27, 145));
		clrs.add(new BaseColor(254, 153, 1));
		clrs.add(new BaseColor(00, 186, 00));
		clrs.add(new BaseColor(3, 179, 255));
		clrs.add(new BaseColor(255, 110, 137));
		clrs.add(new BaseColor(163, 144, 00));
		clrs.add(new BaseColor(47, 41, 73));
		clrs.add(new BaseColor(162, 134, 219));
		clrs.add(new BaseColor(104, 67, 51));
		clrs.add(new BaseColor(215, 167, 103));
		clrs.add(new BaseColor(27, 222, 222));
		clrs.add(new BaseColor(62, 64, 149));

		return clrs.get(i);

	}
	/* Cover Page */
	void coverpage(DatareadN d) {
		try {

			Image img = Image.getInstance("dddd (2).jpg");
			
			img.scaleAbsolute(800, 10);
			img.setAbsolutePosition(0f, 832f);
			document.add(img);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {

			Image imgs = Image.getInstance("logo1.png");
			imgs.scaleAbsolute(250f, 76f);
			// imgs.setAbsolutePosition(400f, 745f);
			imgs.setAbsolutePosition(330f, 735f);
			document.add(imgs);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {

			//Image img1 = Image.getInstance("f1.jpg");
			Image img1;
			if (!imgpath1.equals("")) {
			 img1 = Image.getInstance(imgpath1);
			}
			else
			{
				img1 = Image.getInstance("f1.jpg");
			}
			img1.scaleAbsolute(595, 500);
			img1.setAbsolutePosition(0f, 200f);
			document.add(img1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		Paragraph pp5 = new Paragraph(530);
		pp5.add(new Chunk("\n"));
		try {
			document.add(pp5);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PdfPTable resulttable = new PdfPTable(1); // 4 columns.
		resulttable.setWidthPercentage(120); // Width 100%
		try {
			resulttable.setWidths(new int[] { 100 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell r1 = new PdfPCell(new Paragraph("", graphtitle));
		r1.setBorder(1);
		r1.setBorderWidth(3f);
		r1.setBorder(r1.BOTTOM);
		r1.setBorderColor(getColor(14));
		r1.setPaddingTop(4);
		r1.setFixedHeight(20f);
		r1.setHorizontalAlignment(Element.ALIGN_LEFT);
		r1.setVerticalAlignment(Element.ALIGN_CENTER);
		resulttable.addCell(r1);

		try {
			document.add(resulttable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Paragraph pp6 = new Paragraph(-13);
		pp6.add(new Chunk("\n"));
		try {
			document.add(pp6);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PdfPTable filetype = new PdfPTable(1);
		filetype.setWidthPercentage(40);
		filetype.setHorizontalAlignment(Element.ALIGN_RIGHT);
		try {
			filetype.setWidths(new int[] { 100 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Front page

		Font testt = FontFactory.getFont(testtype, 10);// Main Report Title name
		testt.setColor(BaseColor.WHITE);

		Font date = FontFactory.getFont(testtype, 10);// Main Report Title name
		date.setColor(BaseColor.BLACK);

		PdfPCell r3 = new PdfPCell(
				new Paragraph("PERMEABILITY TEST REPORT", testt));
		r3.setBorder(0);
		r3.setBackgroundColor(getColor(14));
		r3.setFixedHeight(25f);
		r3.setHorizontalAlignment(Element.ALIGN_CENTER);
		r3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		filetype.addCell(r3);

		try {
			document.add(filetype);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Paragraph p = new Paragraph(20);
		p.add(new Chunk("\n"));
		try {
			document.add(p);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PdfPTable testnametab = new PdfPTable(2); // 2 columns.
		testnametab.setWidthPercentage(100); // Width 100%
		testnametab.setSpacingBefore(30f); // Space before table

		try {
			testnametab.setWidths(new int[] { 40, 60 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Font covertime = FontFactory.getFont(
				"./font/RobotoCondensed-Italic.ttf", BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED, 10);
		covertime.setColor(BaseColor.BLACK);

		PdfPCell t0 = new PdfPCell(new Paragraph("DATE : " + getCurrentData(),
				date));
		t0.setBorder(0);
		// t0.setBorder(t0.BOTTOM | t0.TOP);
		t0.setBorderWidth(3f);
		t0.setBorderColor(BaseColor.BLUE);
		t0.setFixedHeight(35f);
		t0.setHorizontalAlignment(Element.ALIGN_LEFT);
		t0.setVerticalAlignment(Element.ALIGN_BOTTOM);
		testnametab.addCell(t0);

		// Font titleFont=FontFactory.getFont(FONT, 45);//Main Report Title name
		// titleFont.setColor(getColor(6));

		// Font tt=FontFactory.getFont(testnamefont, 50);//Main Report Title
		// name
		// tt.setColor(getColor(6));

		Font testname = FontFactory.getFont("./font/BebasNeue Book.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 40);
		testname.setColor(getColor(14));

		PdfPCell t22 = new PdfPCell(new Paragraph(companyname, testname));
		t22.setBorder(0);
		// t22.setBorder(t22.BOTTOM | t22.TOP);
		t22.setBorderWidth(3f);
		t22.setFixedHeight(100f);
		t22.setRowspan(2);
		t22.setHorizontalAlignment(Element.ALIGN_RIGHT);
		t22.setVerticalAlignment(Element.ALIGN_TOP);
		testnametab.addCell(t22);

		PdfPCell t2 = new PdfPCell(new Paragraph("Sample ID : "
				+ d.data.get("sample"), date));
		t2.setBorder(0);
		// t2.setBorder(t2.BOTTOM | t2.TOP);
		t2.setBorderWidth(3f);
		t2.setBorderColor(BaseColor.BLUE);
		t2.setPaddingTop(0);
		t2.setFixedHeight(15f);
		t2.setHorizontalAlignment(Element.ALIGN_LEFT);
		t2.setVerticalAlignment(Element.ALIGN_CENTER);
		testnametab.addCell(t2);

		Font compname = FontFactory.getFont("./font/Roboto-Regular.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14);
		compname.setColor(BaseColor.BLACK);

		try {
			document.add(testnametab);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/* current Date */
	String getCurrentData() {
		String[] suffixes =
		// 0 1 2 3 4 5 6 7 8 9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 10 11 12 13 14 15 16 17 18 19
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				// 20 21 22 23 24 25 26 27 28 29
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 30 31
				"th", "st" };

		Date date = new Date();
		SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
		int day = Integer.parseInt(formatDayOfMonth.format(date));
		String dayStr = day + suffixes[day];

		DateFormat dateFormat = new SimpleDateFormat(" MMMM yyyy | HH:mm aa");
		Calendar cal = Calendar.getInstance();

		// System.out.println(dateFormat.format(cal.getTime()));

		return dayStr + dateFormat.format(cal.getTime());

	}
	/* Single Test File Information Display in Table */
	void sampleinfo(DatareadN d) {

		int r = 130;
		int g = 130;
		int b = 130;
		BaseColor backcellcoltable = new BaseColor(r, g, b);
		BaseColor backcellcoltable1 = new BaseColor(230, 230, 230);

		PdfPTable addresstable = new PdfPTable(1); // 4 columns.
		addresstable.setWidthPercentage(100); // Width 100%
		addresstable.setSpacingBefore(0f); // Space before table
		addresstable.setSpacingAfter(0f); // Space after table

		// Set Column widths
		try {
			addresstable.setWidths(new int[] { 100 });
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell a1 = new PdfPCell(new Paragraph("Material Intelligence Lab",
				addresslab));
		a1.setPaddingLeft(10);
		a1.setPaddingTop(1);
		a1.setBorder(0);
		// a1.setBorder(a1.LEFT | a1.RIGHT);
		a1.setFixedHeight(15f);
		// a1.setBackgroundColor(backcellcoltable1);
		a1.setBorderColor(new BaseColor(130, 130, 130));
		a1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell a2 = new PdfPCell(new Paragraph("www.m19.io", addresslab));
		a2.setPaddingLeft(10);
		a2.setPaddingTop(1);
		a2.setBorder(0);
		// a2.setBorder(a2.LEFT | a2.RIGHT);
		a2.setFixedHeight(15f);
		// a2.setBackgroundColor(backcellcoltable1);
		a2.setBorderColor(new BaseColor(130, 130, 130));
		a2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell a3 = new PdfPCell(new Paragraph("support@m19.io", addresslab));
		a3.setPaddingLeft(10);
		a3.setPaddingTop(1);
		a3.setBorder(0);
		// a3.setBorder(a3.LEFT | a3.RIGHT);
		a3.setFixedHeight(15f);
		// a3.setBackgroundColor(backcellcoltable1);
		a3.setBorderColor(new BaseColor(130, 130, 130));
		a3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell a4 = new PdfPCell(new Paragraph("+91 8140308833", addresslab));
		a4.setPaddingLeft(10);
		a4.setPaddingTop(1);
		a4.setBorder(0);
		// a3.setBorder(a3.LEFT | a3.RIGHT);
		a4.setFixedHeight(15f);
		// a3.setBackgroundColor(backcellcoltable1);
		a4.setBorderColor(new BaseColor(130, 130, 130));
		a4.setHorizontalAlignment(Element.ALIGN_RIGHT);
		a4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		addresstable.addCell(a1);
		addresstable.addCell(a2);
		addresstable.addCell(a3);
		//addresstable.addCell(a4);

		try {
			document.add(addresstable);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Paragraph p = new Paragraph(15);
		p.add(new Chunk("\n"));
		try {
			document.add(p);
		} catch (DocumentException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		PdfPTable tablem = new PdfPTable(2); // 4 columns.
		tablem.setWidthPercentage(100); // Width 100%
		tablem.setSpacingBefore(0f); // Space before table
		tablem.setSpacingAfter(0f); // Space after table

		// Set Column widths
		try {
			tablem.setWidths(new int[] { 50, 50 });
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell cell1h = new PdfPCell(new Paragraph("Sample Info", whitecol));
		cell1h.setBorder(1);
		cell1h.setBorder(cell1h.TOP);
		cell1h.setBorderColor(new BaseColor(255, 255, 255));
		cell1h.setBackgroundColor(backcellcoltable);
		cell1h.setPaddingLeft(10);
		cell1h.setPaddingTop(1);
		cell1h.setFixedHeight(28f);
		cell1h.setColspan(2);
		cell1h.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1h.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell1 = new PdfPCell(new Paragraph("Customer ID", sampleinfoq));
		cell1.setBorder(1);
		cell1.setBorder(cell1.TOP | cell1.LEFT | cell1.RIGHT);
		cell1.setBorderColor(new BaseColor(130, 130, 130));
		cell1.setPaddingLeft(10);
		cell1.setPaddingTop(8);
		// cell1.setBackgroundColor(backcellcoltable1);
		cell1.setFixedHeight(23f);
		cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell2 = new PdfPCell(new Paragraph(""
				+ d.data.get("customerid"), sampleinfoa));
		cell2.setPaddingLeft(10);
		cell2.setPaddingTop(8);
		cell2.setBorder(1);
		cell2.setFixedHeight(23f);
		cell2.setBorder(cell2.TOP | cell2.RIGHT);
		// cell2.setBackgroundColor(backcellcoltable1);
		cell2.setBorderColor(new BaseColor(130, 130, 130));
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell3 = new PdfPCell(new Paragraph("Test Date", sampleinfoq));
		cell3.setBorder(1);
		cell3.setBorder(cell3.LEFT | cell3.RIGHT);
		cell3.setPaddingLeft(10);
		cell3.setPaddingTop(1);
		cell3.setBackgroundColor(backcellcoltable1);
		cell3.setFixedHeight(23f);
		cell3.setBorderColor(new BaseColor(130, 130, 130));
		cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell4 = new PdfPCell(new Paragraph("" + d.data.get("testdate")
				+ " | " + d.data.get("testtime"), sampleinfoa));
		cell4.setBorder(1);
		cell4.setBorder(cell4.RIGHT);
		cell4.setBorderColor(new BaseColor(130, 130, 130));
		cell4.setPaddingLeft(10);
		cell4.setPaddingTop(1);
		cell4.setBackgroundColor(backcellcoltable1);
		cell4.setFixedHeight(23f);
		cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell5 = new PdfPCell(new Paragraph("Sample ID", sampleinfoq));
		// cell5 = new PdfPCell(new Phrase("jj"+d.starttime));
		cell5.setPaddingTop(1);
		cell5.setPaddingLeft(10);
		cell5.setBorder(1);
		cell5.setFixedHeight(23f);
		// cell5.setBackgroundColor(backcellcoltable1);
		cell5.setBorder(cell5.LEFT | cell5.RIGHT);
		cell5.setBorderColor(new BaseColor(130, 130, 130));
		cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell51 = new PdfPCell(new Paragraph("" + d.data.get("sample"),
				sampleinfoa));
		cell51.setBorder(1);
		cell51.setBorder(cell51.RIGHT);
		cell51.setBorderColor(new BaseColor(130, 130, 130));
		cell51.setPaddingLeft(10);
		// cell51.setBackgroundColor(backcellcoltable1);
		cell51.setPaddingTop(1);
		cell51.setFixedHeight(23f);
		cell51.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell51.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell c1 = new PdfPCell(new Paragraph("Sample Lot No ", sampleinfoq));
		c1.setPaddingLeft(10);
		c1.setPaddingTop(1);
		c1.setBorder(1);
		c1.setBorder(c1.LEFT | c1.RIGHT);
		c1.setBackgroundColor(backcellcoltable1);
		c1.setBorderColor(new BaseColor(130, 130, 130));
		c1.setFixedHeight(23f);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell c2 = new PdfPCell(new Paragraph("B545411", sampleinfoa));
		c2.setBorder(1);
		c2.setBorder(c2.RIGHT);
		c2.setBorderColor(new BaseColor(130, 130, 130));
		c2.setBackgroundColor(backcellcoltable1);
		c2.setPaddingTop(1);
		c2.setPaddingLeft(10);
		c2.setFixedHeight(23f);
		c2.setHorizontalAlignment(Element.ALIGN_LEFT);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell t1 = new PdfPCell(
				new Paragraph("Sample Diameter", sampleinfoq));
		t1.setPaddingLeft(10);
		t1.setPaddingTop(1);
		t1.setBorder(1);
		t1.setBorder(t1.LEFT | t1.RIGHT);
		t1.setFixedHeight(23f);
		t1.setBorderColor(new BaseColor(130, 130, 130));
		// t1.setBackgroundColor(backcellcoltable1);
		t1.setHorizontalAlignment(Element.ALIGN_LEFT);
		t1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell t2 = new PdfPCell(new Paragraph(""
				+ d.data.get("samplediameter") + " cm", sampleinfoa));
		t2.setBorder(1);
		t2.setBorder(t2.RIGHT);
		t2.setBorderColor(new BaseColor(130, 130, 130));
		t2.setPaddingLeft(10);
		t2.setFixedHeight(23f);
		// t2.setBackgroundColor(backcellcoltable1);
		t2.setPaddingTop(1);
		t2.setHorizontalAlignment(Element.ALIGN_LEFT);
		t2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell t3 = new PdfPCell(new Paragraph("Test Duration", sampleinfoq));
		t3.setPaddingLeft(10);
		t3.setPaddingTop(1);
		t3.setBorder(1);
		t3.setBorder(t3.LEFT | t3.RIGHT);
		t3.setFixedHeight(23f);
		t3.setBorderColor(new BaseColor(130, 130, 130));
		t3.setBackgroundColor(backcellcoltable1);
		t3.setHorizontalAlignment(Element.ALIGN_LEFT);
		t3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell t4 = new PdfPCell(new Paragraph("" + d.data.get("duration")
				+ " ", sampleinfoa));
		t4.setBorder(1);
		t4.setBorder(t4.RIGHT);
		t4.setBorderColor(new BaseColor(130, 130, 130));
		t4.setPaddingLeft(10);
		t4.setFixedHeight(23f);
		t4.setBackgroundColor(backcellcoltable1);
		t4.setPaddingTop(1);
		t4.setHorizontalAlignment(Element.ALIGN_LEFT);
		t4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		PdfPCell f1 = new PdfPCell(new Paragraph("Sample Thickness", sampleinfoq));
		f1.setPaddingLeft(10);
		f1.setPaddingTop(1);
		f1.setBorder(1);
		f1.setBorder(f1.LEFT | f1.RIGHT);
		f1.setFixedHeight(23f);
		// f1.setBackgroundColor(backcellcoltable1);
		f1.setBorderColor(new BaseColor(130, 130, 130));
		f1.setHorizontalAlignment(Element.ALIGN_LEFT);
		f1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f2 = new PdfPCell(new Paragraph("" + d.data.get("thikness")
				+ " cm", sampleinfoa));
		f2.setBorder(1);
		f2.setBorder(f2.RIGHT);
		f2.setBorderColor(new BaseColor(130, 130, 130));
		f2.setPaddingLeft(10);
		// f2.setBackgroundColor(backcellcoltable1);
		f2.setPaddingTop(1);
		f2.setFixedHeight(23f);
		f2.setHorizontalAlignment(Element.ALIGN_LEFT);
		f2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f3 = new PdfPCell(
				new Paragraph("Gas Viscosity", sampleinfoq));
		f3.setPaddingLeft(10);
		f3.setPaddingTop(1);
		f3.setBorder(1);
		f3.setBackgroundColor(backcellcoltable1);
		f3.setFixedHeight(23f);
		f3.setBorder(f3.LEFT | f3.RIGHT);
		f3.setBorderColor(new BaseColor(130, 130, 130));
		f3.setHorizontalAlignment(Element.ALIGN_LEFT);
		f3.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f4 = new PdfPCell(new Paragraph("" + d.data.get("fluidvalue")
				+ " cP ", sampleinfoa));
		f4.setBorder(1);
		f4.setBorder(f4.RIGHT);
		f4.setBorderColor(new BaseColor(130, 130, 130));
		f4.setPaddingLeft(10);
		f4.setBackgroundColor(backcellcoltable1);
		f4.setPaddingTop(1);
		f4.setFixedHeight(23f);
		f4.setHorizontalAlignment(Element.ALIGN_LEFT);
		f4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f11 = new PdfPCell(new Paragraph("Test Gas", sampleinfoq));
		f11.setPaddingLeft(10);
		// f11.setBackgroundColor(backcellcoltable1);
		f11.setPaddingTop(1);
		f11.setBorder(1);
		f11.setFixedHeight(23f);
		f11.setBorder(f11.LEFT | f11.RIGHT);
		f11.setBorderColor(new BaseColor(130, 130, 130));
		f11.setHorizontalAlignment(Element.ALIGN_LEFT);
		f11.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f22 = new PdfPCell(new Paragraph("" + d.data.get("fluidname"),
				sampleinfoa));
		f22.setBorder(1);
		f22.setBorder(f22.RIGHT);
		f22.setBorderColor(new BaseColor(130, 130, 130));
		f22.setPaddingLeft(10);
		// f22.setBackgroundColor(backcellcoltable1);
		f22.setPaddingTop(1);
		f22.setFixedHeight(23f);
		f22.setHorizontalAlignment(Element.ALIGN_LEFT);
		f22.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f33 = new PdfPCell(new Paragraph("Operator ID", sampleinfoq));
		f33.setPaddingLeft(10);
		f33.setPaddingTop(1);
		f33.setBorder(1);
		f33.setPaddingBottom(8);
		f33.setBackgroundColor(backcellcoltable1);
		f33.setFixedHeight(23f);
		f33.setBorder(f33.RIGHT | f33.LEFT);
		f33.setBorderColor(new BaseColor(130, 130, 130));
		f33.setHorizontalAlignment(Element.ALIGN_LEFT);
		f33.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f44 = new PdfPCell(new Paragraph("Rujul Raimagia", sampleinfoa));
		f44.setBorder(1);
		f44.setBorder(f44.RIGHT);
		f44.setBorderColor(new BaseColor(130, 130, 130));
		f44.setPaddingLeft(10);
		f44.setPaddingTop(1);
		f44.setBackgroundColor(backcellcoltable1);
		f44.setFixedHeight(23f);
		f44.setPaddingBottom(8);
		f44.setHorizontalAlignment(Element.ALIGN_LEFT);
		f44.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		/*Gurly Flow rate and Frazier*/
		
		PdfPCell f331 = new PdfPCell(new Paragraph("Gurley Flow rate (0.178 psi)", sampleinfoq));
		f331.setPaddingLeft(10);
		f331.setPaddingTop(1);
		f331.setBorder(1);
		f331.setPaddingBottom(8);
		//f331.setBackgroundColor(backcellcoltable1);
		f331.setFixedHeight(23f);
		f331.setBorder(f331.RIGHT | f331.LEFT);
		f331.setBorderColor(new BaseColor(130, 130, 130));
		f331.setHorizontalAlignment(Element.ALIGN_LEFT);
		f331.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell f441 = new PdfPCell(new Paragraph(d.data.get("gurleyflow")+" Flow Rate", sampleinfoa));
			f441.setBorder(1);
			f441.setBorder(f441.RIGHT);
			f441.setBorderColor(new BaseColor(130, 130, 130));
			f441.setPaddingLeft(10);
			f441.setPaddingTop(1);
		//	f441.setBackgroundColor(backcellcoltable1);
			f441.setFixedHeight(23f);
			f441.setPaddingBottom(8);
			f441.setHorizontalAlignment(Element.ALIGN_LEFT);
			f441.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
	
		
		PdfPCell f332 = new PdfPCell(new Paragraph("Frazier Flow rate (0.0182 psi)", sampleinfoq));
		f332.setPaddingLeft(10);
		f332.setPaddingTop(1);
		f332.setBorder(1);
		f332.setPaddingBottom(8);
		f332.setBackgroundColor(backcellcoltable1);
		f332.setFixedHeight(23f);
		f332.setBorder(f332.BOTTOM | f332.RIGHT | f332.LEFT);
		f332.setBorderColor(new BaseColor(130, 130, 130));
		f332.setHorizontalAlignment(Element.ALIGN_LEFT);
		f332.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell f414 = new PdfPCell(new Paragraph(d.data.get("frazierflow")+" Flow Rate", sampleinfoa));
		f414.setBorder(1);
		f414.setBorder(f414.BOTTOM | f414.RIGHT);
		f414.setBorderColor(new BaseColor(130, 130, 130));
		f414.setPaddingLeft(10);
		f414.setPaddingTop(1);
		f414.setBackgroundColor(backcellcoltable1);
		f414.setFixedHeight(23f);
		f414.setPaddingBottom(8);
		f414.setHorizontalAlignment(Element.ALIGN_LEFT);
		f414.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		
		tablem.addCell(cell1h);
		tablem.addCell(cell1);
		tablem.addCell(cell2);
		tablem.addCell(cell3);
		tablem.addCell(cell4);
		tablem.addCell(cell5);
		tablem.addCell(cell51);
		tablem.addCell(c1);
		tablem.addCell(c2);
		tablem.addCell(t1);
		tablem.addCell(t2);
		tablem.addCell(t3);
		tablem.addCell(t4);
		tablem.addCell(f1);
		tablem.addCell(f2);
		tablem.addCell(f3);
		tablem.addCell(f4);
		tablem.addCell(f11);
		tablem.addCell(f22);
		tablem.addCell(f33);
		tablem.addCell(f44);
		
		tablem.addCell(f331);
		tablem.addCell(f441);
		tablem.addCell(f332);
		tablem.addCell(f414);

		try {
			document.add(tablem);
		} catch (DocumentException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		PdfPTable resulttable = new PdfPTable(3); // 3 columns.
		resulttable.setWidthPercentage(100); // Width 100%
		resulttable.setSpacingBefore(0f); // Space before table
		resulttable.setSpacingAfter(0f); // Space after
		// Set Column widths
		try {
			resulttable.setWidths(new int[] {33,33,33});
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Font resultable = FontFactory.getFont("./font/Montserrat-SemiBold.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
		resultable.setColor(BaseColor.WHITE);

		BaseColor resultborder = new BaseColor(255, 255, 255);
		float border = 3f;
		
		PdfPCell titl4 = new PdfPCell(new Paragraph("Results", resultlab));
		titl4.setBorder(0);
		titl4.setBorderColor(new BaseColor(130, 130, 130));
		titl4.setPaddingLeft(10);
		titl4.setPaddingTop(1);
		titl4.setFixedHeight(35f);
		titl4.setColspan(3);
		titl4.setHorizontalAlignment(Element.ALIGN_CENTER);
		titl4.setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		PdfPCell r1h = new PdfPCell(new Paragraph("", sampleinfoqh));
		r1h.setBorder(0);
		r1h.setBorderColor(getColor(6));
		r1h.setPaddingLeft(10);
		r1h.setPaddingTop(4);
		r1h.setFixedHeight(30f);
		r1h.setColspan(4);
		r1h.setHorizontalAlignment(Element.ALIGN_LEFT);
		r1h.setVerticalAlignment(Element.ALIGN_MIDDLE);


		PdfPCell r1 = new PdfPCell(new Paragraph("Darcy constant ",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r1.setBorder(1);
		r1.setBackgroundColor(getColor(14));
		r1.setBorder(r1.RIGHT);
		r1.setBorderColor(resultborder);
		r1.setBorderWidth(border);
		//r1.setBackgroundColor(backcellcoltable);
		r1.setFixedHeight(30f);

		r1.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell r1r;
		if (darcy== true) {
			 r1r = new PdfPCell(new Paragraph(Myapp.getRound(
					Double.parseDouble("" + d.data.get("darcy avg")), 2),
					sampleinfoans));
			}
		else {
			
			 r1r = new PdfPCell(new Paragraph("-",sampleinfoans));
		}
		
		r1r.setBorder(1);
		r1r.setBorder(r1r.RIGHT);
		r1r.setBorderColor(resultborder);
		r1r.setBorderWidth(border);
		r1r.setBackgroundColor(backcellcoltable1);
		r1r.setFixedHeight(25f);
		r1r.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1r.setVerticalAlignment(Element.ALIGN_MIDDLE);

	
		PdfPCell u1;
		if (darcy== true) {
			 u1 = new PdfPCell(new PdfPCell(new Paragraph("(darcy)", unitlab)));
			}
		else {
			
			 u1 = new PdfPCell(new Paragraph("",sampleinfoans));
		}


		u1.setBorder(1);
		u1.setBorder(u1.RIGHT);
		u1.setBorderColor(resultborder);
		u1.setBorderWidth(3f);
		u1.setFixedHeight(20f);
		u1.setBackgroundColor(backcellcoltable1);
		u1.setHorizontalAlignment(Element.ALIGN_CENTER);
		u1.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell r1h2 = new PdfPCell(new Paragraph("", sampleinfoqh));
		r1h2.setBorder(0);
		r1h2.setBorderColor(getColor(6));
		r1h2.setPaddingLeft(10);
		r1h2.setPaddingTop(4);
		r1h2.setFixedHeight(30f);
		r1h2.setColspan(4);
		r1h2.setHorizontalAlignment(Element.ALIGN_LEFT);
		r1h2.setVerticalAlignment(Element.ALIGN_MIDDLE);


		PdfPCell r12 = new PdfPCell(new Paragraph("Gurley permeability ",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r12.setBorder(1);
		r12.setBackgroundColor(getColor(14));
		r12.setBorder(r1.RIGHT);
		r12.setBorderColor(resultborder);
		r12.setBorderWidth(border);
		//r12.setBackgroundColor(backcellcoltable);
		r12.setFixedHeight(30f);

		r12.setHorizontalAlignment(Element.ALIGN_CENTER);
		r12.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell r1r2;
		if (gurley== true) {
			r1r2 = new PdfPCell(new Paragraph(""+ DataStore.ConvertGurely("" + d.data.get("gurley")),
					sampleinfoans));
			}
		else {
			
			r1r2 = new PdfPCell(new Paragraph("-",sampleinfoans));
		}
		
		r1r2.setBorder(1);
		r1r2.setBorder(r1r.RIGHT);
		r1r2.setBorderColor(resultborder);
		r1r2.setBorderWidth(border);
		r1r2.setBackgroundColor(backcellcoltable1);
		r1r2.setFixedHeight(25f);
		r1r2.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1r2.setVerticalAlignment(Element.ALIGN_MIDDLE);

	
	

		PdfPCell u12;
		if (gurley== true) {
			u12 = new PdfPCell(new Paragraph(""+DataStore.getUnitegurely(), unitlab));
			}
		else {
			
			u12 = new PdfPCell(new Paragraph("",sampleinfoans));
		}
		
		
		u12.setBorder(1);
		u12.setBorder(u1.RIGHT);
		u12.setBorderColor(resultborder);
		u12.setBorderWidth(3f);
		u12.setFixedHeight(20f);
		u12.setBackgroundColor(backcellcoltable1);
		u12.setHorizontalAlignment(Element.ALIGN_CENTER);
		u12.setVerticalAlignment(Element.ALIGN_TOP);

		
		PdfPCell r1h3 = new PdfPCell(new Paragraph("", sampleinfoqh));
		r1h3.setBorder(0);
		r1h3.setBorderColor(getColor(6));
		r1h3.setPaddingLeft(10);
		r1h3.setPaddingTop(4);
		r1h3.setFixedHeight(30f);
		r1h3.setColspan(4);
		r1h3.setHorizontalAlignment(Element.ALIGN_LEFT);
		r1h3.setVerticalAlignment(Element.ALIGN_MIDDLE);


		PdfPCell r13 = new PdfPCell(new Paragraph("Frazier permeability ",
				sampleinfola));
		// r1 = new PdfPCell(new Phrase("jj"+d.starttime));
		r13.setBackgroundColor(getColor(14));
		r13.setBorder(1);
		r13.setBorder(r1.RIGHT);
		r13.setBorderColor(resultborder);
		r13.setBorderWidth(border);
//		r13.setBackgroundColor(backcellcoltable);
		r13.setFixedHeight(30f);

		r13.setHorizontalAlignment(Element.ALIGN_CENTER);
		r13.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell r1r3;
		if (frazier== true) {
			r1r3 = new PdfPCell(new Paragraph(""+ DataStore.ConvertFrazier("" + d.data.get("frazier")),
					sampleinfoans));
			}
		else {
			
			r1r3 = new PdfPCell(new Paragraph("-",sampleinfoans));
		}

		r1r3.setBorder(1);
		r1r3.setBorder(r1r.RIGHT);
		r1r3.setBorderColor(resultborder);
		r1r3.setBorderWidth(border);
		r1r3.setBackgroundColor(backcellcoltable1);
		r1r3.setFixedHeight(25f);
		r1r3.setHorizontalAlignment(Element.ALIGN_CENTER);
		r1r3.setVerticalAlignment(Element.ALIGN_MIDDLE);

	
	

		PdfPCell u13;
		if (frazier== true) {
			u13 = new PdfPCell(new Paragraph(""+DataStore.getUnitefrazier(), unitlab));
			}
		else {
			
			u13 = new PdfPCell(new Paragraph("",sampleinfoans));
		}

//		PdfPCell u13 = new PdfPCell(new Paragraph("( Cubic feet / Square foot - min )", unitlab));
		u13.setBorder(1);
		u13.setBorder(u1.RIGHT);
		u13.setBorderColor(resultborder);
		u13.setBorderWidth(3f);
		u13.setFixedHeight(20f);
		u13.setBackgroundColor(backcellcoltable1);
		u13.setHorizontalAlignment(Element.ALIGN_CENTER);
		u13.setVerticalAlignment(Element.ALIGN_TOP);
		
		resulttable.addCell(r1h);
	//resulttable.addCell(r1h2);
	//	resulttable.addCell(r1h3);
		
		
		resulttable.addCell(titl4);

		/* LABEL */

		resulttable.addCell(r1);
		resulttable.addCell(r12);
		resulttable.addCell(r13);
		



		/* RESULT */

		resulttable.addCell(r1r);
		resulttable.addCell(r1r2);
		resulttable.addCell(r1r3);
		



		/* Unit */

		resulttable.addCell(u1);
		resulttable.addCell(u12);
		resulttable.addCell(u13);
		



		try {
			document.add(resulttable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*Paragraph p88 = new Paragraph();
		p88.add(new Chunk("\n"));
		try {
			document.add(p88);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/

		
		// Notes and Image Table.

		if (!imgpath.equals("")) {
			PdfPTable notestable = new PdfPTable(2); // 4 columns.
			notestable.setWidthPercentage(100); // Width 100%
			notestable.setSpacingBefore(10f); // Space before table
			notestable.setSpacingAfter(0f); // Space after table

			// Set Column widths
			try {
				notestable.setWidths(new int[] { 50, 50 });
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			PdfPCell n11 = new PdfPCell(new Phrase());
			n11.setPaddingLeft(0);
			n11.setPaddingBottom(0);

			Image img = null;
			try {
				// img = Image.getInstance("img2.jpg");
				img = Image.getInstance(imgpath);
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// img.scalePercent(15);
			// img.scaleToFit(100, 50);
			img.scaleToFit(250, 100);

			n11.setBorder(0);
			n11.setPaddingLeft(20);
			n11.setHorizontalAlignment(Element.ALIGN_CENTER);
			n11.setVerticalAlignment(Element.ALIGN_TOP);
			n11.addElement(new Chunk(img, 0, 2, true));

			PdfPCell n2 = new PdfPCell(new Paragraph("Notes", noteslab));
			n2.setPaddingLeft(0);
			n2.setPaddingTop(0);
			n2.setBorder(0);
			// n2.setBorder(n2.LEFT | n2.RIGHT);
			n2.setFixedHeight(15f);
			// n2.setBackgroundColor(backcellcoltable1);
			n2.setBorderColor(new BaseColor(130, 130, 130));
			n2.setHorizontalAlignment(Element.ALIGN_CENTER);
			n2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			// PdfPCell n3 = new PdfPCell(new
			// Paragraph("The following test Procedure is based on ASTM F726-94 (Standard Test Method for Pore Size Characterization.)",notesdeslab));
			PdfPCell n3 = new PdfPCell(new Paragraph(notes, notesdeslab));

			n3.setPaddingLeft(0);
			n3.setPaddingRight(20);
			n3.setPaddingTop(1);
			n3.setBorder(0);

			// n3.setBorder(n3.BOTTOM);
			n3.setFixedHeight(120f);
			// n3.setBackgroundColor(backcellcoltable1);
			n3.setBorderColor(new BaseColor(130, 130, 130));
			n3.setHorizontalAlignment(Element.ALIGN_TOP);
			n3.setVerticalAlignment(Element.ALIGN_LEFT);

			n11.setRowspan(2);
			notestable.addCell(n11);
			notestable.addCell(n2);
			notestable.addCell(n3);

			try {
				document.add(notestable);
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		} else {
			PdfPTable notestable = new PdfPTable(1); // 4 columns.
			notestable.setWidthPercentage(100); // Width 100%
			notestable.setSpacingBefore(5f); // Space before table
			notestable.setSpacingAfter(0f); // Space after table

			// Set Column widths
			try {
				notestable.setWidths(new int[] { 100 });
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			PdfPCell n2 = new PdfPCell(new Paragraph("Notes", noteslab));
			n2.setPaddingLeft(0);
			n2.setPaddingTop(0);
			n2.setBorder(0);
			// n2.setBorder(n2.LEFT | n2.RIGHT);
			n2.setFixedHeight(16f);
			// n2.setBackgroundColor(backcellcoltable1);
			n2.setBorderColor(new BaseColor(130, 130, 130));
			n2.setHorizontalAlignment(Element.ALIGN_CENTER);
			n2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			// PdfPCell n3 = new PdfPCell(new
			// Paragraph("The following test Procedure is based on ASTM F726-94 (Standard Test Method for Pore Size Characterization.)",notesdeslab));
			PdfPCell n3 = new PdfPCell(new Paragraph(notes, notesdeslab));

			n3.setPaddingLeft(10);
			n3.setPaddingTop(1);
			n3.setBorder(0);

			// n3.setBorder(n3.BOTTOM);
			n3.setFixedHeight(120f);
			// n3.setBackgroundColor(backcellcoltable1);
			n3.setBorderColor(new BaseColor(130, 130, 130));
			n3.setHorizontalAlignment(Element.ALIGN_TOP);
			n3.setVerticalAlignment(Element.ALIGN_LEFT);

			notestable.addCell(n2);
			notestable.addCell(n3);

			try {
				document.add(notestable);
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}

		/*
		 * Paragraph p88=new Paragraph(190); p88.add(new Chunk("\n")); try {
		 * document.add(p88); } catch (DocumentException e2) { // TODO
		 * Auto-generated catch block e2.printStackTrace(); } //Footer conten
		 */

		PdfPTable disctable = new PdfPTable(1); // 4 columns.
		disctable.setWidthPercentage(100); // Width 100%
		disctable.setSpacingBefore(0f); // Space before table
		disctable.setSpacingAfter(0f); // Space after table

		// Set Column widths
		try {
			disctable.setWidths(new int[] { 100 });
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPCell d1 = new PdfPCell(new Paragraph(
				"* * ISO : 17025 Acceredited Laboratories * *", sampleinfoq));
		d1.setPaddingLeft(10);
		d1.setPaddingTop(1);
		d1.setBorder(1);
		d1.setBorder(d1.BOTTOM);
		d1.setFixedHeight(15f);
		// d1.setBackgroundColor(backcellcoltable1);
		d1.setBorderColor(new BaseColor(130, 130, 130));
		d1.setHorizontalAlignment(Element.ALIGN_CENTER);
		d1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell d2 = new PdfPCell(
				new Paragraph(
						"Sample specimen are not drawn by M19 Lab. Result relates to the sample tested. The report shall not be reproduced except in full, without the written approval of the laboratory. The report is strically confidential and technical inforamtion of client only. Not for advertisement, promotion, publicity or litigation.",
						addresslab));
		d2.setPaddingLeft(0);
		d2.setPaddingTop(1);
		d2.setBorder(0);
		// d2.setBorder(d2.LEFT | d2.RIGHT);
		d2.setFixedHeight(50f);
		// d2.setBackgroundColor(backcellcoltable1);
		d2.setBorderColor(new BaseColor(130, 130, 130));
		d2.setHorizontalAlignment(Element.ALIGN_CENTER);
		d2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		disctable.addCell(d1);
		disctable.addCell(d2);

		try {
			document.add(disctable);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}
	/* Display Graph */
	void resultgraph(File f) {
		try {

			Paragraph pp5 = new Paragraph(15);
			pp5.add(new Chunk("\n"));
			document.add(pp5);

			PdfPTable resulttable = new PdfPTable(4); // 4 columns.
			resulttable.setWidthPercentage(100); // Width 100%

			resulttable.setWidths(new int[] { 25, 25, 25, 25 });

			Font headerdate = FontFactory.getFont(
					"./font/Montserrat-SemiBold.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED, 14);
			headerdate.setColor(getColor(14));

			String imagefilename = f.getName().substring(0,
					f.getName().indexOf('.'));

			System.out.println("Image Name--------->" + imagefilename);

			PdfPCell r1 = new PdfPCell(new Paragraph(imagefilename.substring(0,
					imagefilename.length()), headerdate));
			r1.setBorder(0);
			r1.setColspan(4);
			r1.setBorder(0);
			r1.setPaddingTop(4);
			r1.setFixedHeight(20f);
			r1.setHorizontalAlignment(Element.ALIGN_CENTER);
			r1.setVerticalAlignment(Element.ALIGN_CENTER);

			resulttable.addCell(r1);

			document.add(resulttable);

			Image image = Image.getInstance(f.getAbsolutePath());
			image.scaleAbsolute(520, 300);

			document.add(image);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("File is created");

	}
	/* Row Data Table Header in Title name and unite */
	void addTableHeader(PdfPTable tablem) {
		tablem.setSpacingBefore(10f);
		String st = ""+ d.data.get("sample");
		
		Font headertestname = FontFactory.getFont(
				"./font/BebasNeue Regular.ttf", BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED, 15);
		headertestname.setColor(getColor(14));
		
		try {

			Chunk redText = new Chunk("Raw Data : " + st, headertestname);

			Paragraph p1 = new Paragraph(redText);
			
			document.add(p1);
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		PdfPCell cell1 = new PdfPCell(new Paragraph("Pressure Differential", rowhed));
		cell1.setBackgroundColor(backcellcoltable);
		cell1.setBorder(1);
		cell1.setBorder(cell1.LEFT);
		cell1.setBorderColor(new BaseColor(130, 130, 130));
		cell1.setPaddingLeft(0);
		cell1.setPaddingTop(0);
		cell1.setFixedHeight(30f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell2 = new PdfPCell(new Paragraph("Flow Rate", rowhed));
		cell2.setBackgroundColor(backcellcoltable);
		cell2.setBorder(0);
		// cell2.setBorder(cell2.TOP | cell2.BOTTOM | cell2.LEFT);
		cell2.setBorderColor(getColor(6));
		cell2.setPaddingLeft(0);
		cell2.setPaddingTop(0);
		cell2.setFixedHeight(30f);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell cell3 = new PdfPCell(new Paragraph("Average Darcy", rowhed));
		cell3.setBackgroundColor(backcellcoltable);
		cell3.setBorder(0);
		// cell3.setBorder(cell3.TOP | cell3.BOTTOM | cell3.LEFT);
		cell3.setBorderColor(getColor(6));
		cell3.setPaddingLeft(0);
		cell3.setPaddingTop(0);
		cell3.setFixedHeight(30f);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);



		// Units

		PdfPCell ucell1 = new PdfPCell(new Paragraph("("+DataStore.getUnitepressure()+")", unitlabrow));
		ucell1.setBackgroundColor(backcellcoltable);
		ucell1.setBorder(1);
		ucell1.setBorder(ucell1.LEFT);
		ucell1.setBorderColor(new BaseColor(130, 130, 130));
		ucell1.setPaddingLeft(0);
		ucell1.setPaddingTop(0);
		ucell1.setFixedHeight(15f);
		ucell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell1.setVerticalAlignment(Element.ALIGN_TOP);
		
		Chunk superscript =new Chunk("3");
		superscript.setTextRise(3f);
		superscript.setFont(unitlabrownew);
		
		Phrase phrase = new Phrase();

		phrase.add(new Chunk("(cm", unitlabrow));
		phrase.add( superscript ); 
		phrase.add(new Chunk("/s)", unitlabrow));
		
		

		PdfPCell ucell2 = new PdfPCell(new Paragraph("("+DataStore.getUniteflow()+")", unitlabrow));
		ucell2.setBackgroundColor(backcellcoltable);
		ucell2.setBorder(0);
		// ucell2.setBorder(ucell2.TOP | ucell2.BOTTOM | ucell2.LEFT);
		ucell2.setBorderColor(getColor(6));
		ucell2.setPaddingLeft(0);
		ucell2.setPaddingTop(0);
		ucell2.setFixedHeight(15f);
		ucell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell2.setVerticalAlignment(Element.ALIGN_TOP);

		PdfPCell ucell3 = new PdfPCell(new Paragraph("", unitlabrow));
		ucell3.setBackgroundColor(backcellcoltable);
		ucell3.setBorder(0);
		// ucell3.setBorder(ucell3.TOP | ucell3.BOTTOM | ucell3.LEFT);
		ucell3.setBorderColor(getColor(6));
		ucell3.setPaddingLeft(0);
		ucell3.setPaddingTop(0);
		ucell3.setFixedHeight(15f);
		ucell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		ucell3.setVerticalAlignment(Element.ALIGN_TOP);

	

		tablem.addCell(cell1);
		tablem.addCell(cell2);
		tablem.addCell(cell3);
	
		// unite
		tablem.addCell(ucell1);
		tablem.addCell(ucell2);
		tablem.addCell(ucell3);
	}
	/* Set Test Data in Table */
	void rowData() {

		wpressur = d.getValuesOf("" + d.data.get("dpressure"));
		wf = d.getValuesOf("" + d.data.get("dflow"));
		df = d.getValuesOf("" + d.data.get("darcy"));

		//System.out.println("DF size : "+df.size());
		//System.out.println("WF size : "+wf.size());
		
		Font tabledata = FontFactory.getFont("./font/Roboto-Light.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9);
		tabledata.setColor(new BaseColor(98, 98, 98));

		Font sampleinfoa = FontFactory.getFont(FontFactory.HELVETICA, 11,
				Font.NORMAL, new BaseColor(90, 90, 92));

		PdfPTable tablem = new PdfPTable(3); // 3 columns.
		tablem.setWidthPercentage(100); // Width 100%
		tablem.setSpacingBefore(0f); // Space before table
		tablem.setSpacingAfter(0f); // Space after table

		// Set Column widths
		float[] columnWidths = { 1f, 1f, 1f };

		try {
			tablem.setWidths(columnWidths);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addTableHeader(tablem);
		
		List<List<String>> data = new ArrayList<List<String>>();

		for (int k = 0; k < wpressur.size(); k++) {
			List<String> temp = new ArrayList<String>();

			String press = ""+DataStore.getconvertToSimpleNumber(DataStore.ConvertPressure(wpressur.get(k)));

			temp.add(press);

			String wetflow = ""+DataStore.ConvertFlow(wf.get(k));
			String dryflow = ""+Myapp.getRound(df.get(k),DataStore.getRoundOff());

			temp.add(wetflow);
			temp.add(dryflow);

			data.add(temp);
		}

		BaseColor bordercolor = new BaseColor(130, 130, 130);
		BaseColor backgroundcolor = new BaseColor(230, 230, 230);

		for (int j = 1; j < df.size(); j++) {

			if (j % 45 == 0 && j != 0) {

				j = j - 1;
				tablem.getRows().remove(tablem.getRows().size() - 1);

				// add last row

				addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, backgroundcolor, 14f, tabledata);

				j = j + 1;

				try {
					document.add(tablem);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				document.newPage();
				tablem = new PdfPTable(3); // 3 columns.
				tablem.setWidthPercentage(100); // Width 100%
				tablem.setSpacingBefore(0f); // Space before table
				tablem.setSpacingAfter(0f); // Space after table

				try {
					tablem.setWidths(columnWidths);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				addTableHeader(tablem);

			}

			if (j % 2 == 0) {

				addRowsToTable(tablem, data.get(j), 1, false, false, bordercolor, null, 14f, tabledata);

				// withoutbackcolor
			}

			else {
				addRowsToTable(tablem, data.get(j), 1, false, false, bordercolor, backgroundcolor, 14f, tabledata);

				// second column
				// backcolor

			}

			if (j == wpressur.size() - 1) {

				tablem.getRows().remove(tablem.getRows().size() - 1);

				if (j % 2 == 0) {
					addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, null, 14f, tabledata);
				} else {
					addRowsToTable(tablem, data.get(j), 1, false, true, bordercolor, backgroundcolor, 14f, tabledata);
				}

			}

		}
		try {
			document.add(tablem);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	void addRowsToTable(PdfPTable tablem, List<String> data, int borderwidth, boolean isTopBorder,
			boolean isBottomBorder, BaseColor bordercolor, BaseColor backgroundcolor, float rowheight, Font datafont) {

		for (int i = 0; i < data.size(); i++) {

			PdfPCell r11 = new PdfPCell(new Paragraph(data.get(i), datafont));
			r11.setBorder(1);

			if (i == 0) {
				if (isTopBorder) {
					r11.setBorder(r11.LEFT | r11.TOP);

				} else {
					r11.setBorder(r11.LEFT);
				}
				if (isBottomBorder) {
					r11.setBorder(r11.LEFT | r11.BOTTOM);
				} else {
					r11.setBorder(r11.LEFT);
				}
			} else if (i == data.size() - 1) {
				if (isTopBorder) {
					r11.setBorder(r11.RIGHT | r11.TOP);

				} else {
					r11.setBorder(r11.RIGHT);
				}
				if (isBottomBorder) {
					r11.setBorder(r11.RIGHT | r11.BOTTOM);
				} else {
					r11.setBorder(r11.RIGHT);
				}
			} else {
				if (isTopBorder) {
					r11.setBorder(r11.TOP);

				} else if (isBottomBorder) {
					r11.setBorder(r11.BOTTOM);
				} else {
					r11.setBorder(0);
				}
			}
			r11.setBorderColor(bordercolor);
			r11.setBackgroundColor(backgroundcolor);
			r11.setPaddingTop(0);
			r11.setFixedHeight(rowheight);
			r11.setHorizontalAlignment(Element.ALIGN_CENTER);
			r11.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tablem.addCell(r11);

		}

	}

	
	/* Header and Footer */
	class HeaderFooterPageEvent extends PdfPageEventHelper {
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 15,
				Font.BOLD, new CMYKColor(92, 17, 0, 15));
		Font titledate = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8,
				Font.ITALIC, new BaseColor(99, 99, 99));

		private PdfTemplate t;

		// private Image total;

		/*
		 * public void onOpenDocument(PdfWriter writer, Document document) { t =
		 * writer.getDirectContent().createTemplate(30, 16); try { total =
		 * Image.getInstance(t); total.setRole(PdfName.ARTIFACT); } catch
		 * (DocumentException de) { throw new ExceptionConverter(de); } }
		 */

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			addHeader(writer);
			addFooter(writer);

		}

		private void addHeader(PdfWriter writer) {
			try {
				// set defaults
				PdfPTable table = new PdfPTable(5);
				table.setTotalWidth(670);
				table.setLockedWidth(true);
				table.setWidths(new int[] { 2, 1, 1, 2, 2 });

				Font titleFont = FontFactory.getFont(testnamefont, 14);// Main
																		// Report
																		// Title
																		// name
				titleFont.setColor(getColor(14));

				Font headertestname = FontFactory.getFont(
						"./font/BebasNeue Regular.ttf", BaseFont.IDENTITY_H,
						BaseFont.EMBEDDED, 15);
				headertestname.setColor(getColor(14));

				PdfPCell cell;
				cell = new PdfPCell(new Phrase("ASTM D737-96", headertestname));
				cell.setBorder(1);
				cell.setBorder(cell.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(new BaseColor(150, 150, 150));
				cell.setPaddingBottom(3);
				table.addCell(cell);

				Font headerdate = FontFactory.getFont(
						"./font/RobotoCondensed-Italic.ttf",
						BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7.5f);
				headerdate.setColor(new BaseColor(98, 98, 98));

				cell = new PdfPCell(new Phrase("", headerdate));
				cell.setBorder(1);
				cell.setBorder(cell.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderColor(new BaseColor(150, 150, 150));
				cell.setPaddingBottom(2);
				cell.setColspan(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase());
				cell.setPaddingLeft(10);
				cell.setPaddingBottom(10);
				Image img = Image.getInstance("logo1.jpg");
				// img.scalePercent(15);
				// img.scaleToFit(100, 50);
				img.scaleToFit(50, 26);
				cell.setRowspan(2);
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.addElement(new Chunk(img, 0, 4, true));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.GRAY);
				table.addCell(cell);

				// write content
				table.writeSelectedRows(0, -1, 0, 803,
						writer.getDirectContent());
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			} catch (MalformedURLException e) {
				throw new ExceptionConverter(e);
			} catch (IOException e) {
				throw new ExceptionConverter(e);
			}
		}

		private void addFooter(PdfWriter writer) {
			PdfPTable footer = new PdfPTable(3);
			try {
				// set defaults
				footer.setWidths(new int[] { 24, 2, 1 });
				footer.setTotalWidth(527);
				footer.setLockedWidth(true);
				footer.getDefaultCell().setFixedHeight(20);
				footer.getDefaultCell().setBorder(0);
				// footer.getDefaultCell().setBorder(Rectangle.TOP);
				footer.getDefaultCell().setBorderColor(BaseColor.BLACK);

				// add copyright
				footer.addCell(new Phrase("", new Font(
						Font.FontFamily.HELVETICA, 12, Font.BOLD)));

				// add current page count
				footer.getDefaultCell().setHorizontalAlignment(
						Element.ALIGN_RIGHT);
				footer.addCell(new Phrase(String.format("Page %d",
						writer.getPageNumber()), new Font(
						Font.FontFamily.HELVETICA, 8, Font.NORMAL,
						BaseColor.BLACK)));

				// add placeholder for total page count
				PdfPCell totalPageCount = new PdfPCell();
				totalPageCount.setBorder(0);
				// totalPageCount.setBorder(Rectangle.TOP);
				// totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
				footer.addCell(totalPageCount);

				// write page
				PdfContentByte canvas = writer.getDirectContent();
				canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
				footer.writeSelectedRows(0, -1, 34, 50, canvas);
				canvas.endMarkedContentSequence();
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

		/*
		 * public void onCloseDocument(PdfWriter writer, Document document) {
		 * int totalLength = String.valueOf(writer.getPageNumber()).length();
		 * int totalWidth = totalLength * 5; ColumnText.showTextAligned(t,
		 * Element.ALIGN_RIGHT, new
		 * Phrase(String.valueOf(writer.getPageNumber()), new
		 * Font(Font.FontFamily.HELVETICA, 8)), totalWidth, 6, 0); }
		 */

	}
}

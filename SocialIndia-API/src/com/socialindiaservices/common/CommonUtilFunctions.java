package com.socialindiaservices.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gdata.data.DateTime;
import com.opensymphony.xwork2.ActionSupport;
import com.social.utils.Log;

public class CommonUtilFunctions extends ActionSupport {
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String getDateYYMMDDFormat() {
		String formatedDate = "";
		try {
			Date dt = new Date();
			String year = Integer.toString(dt.getYear() + 1900);
			String month = Integer.toString(dt.getMonth() + 1);
			if (month.length() == 1) {
				month = "0" + month;
			}
			String date = Integer.toString(dt.getDate());
			if (date.length() == 1) {
				date = "0" + date;
			}
			formatedDate = year + month + date;
		} catch (Exception e) {

		}
		return formatedDate;
	}
	  public Date getCurrentDateTime(String dateformattype) {
		    Date date = new Date();
		    try {
		      DateFormat dateFormat = new SimpleDateFormat(dateformattype);
		      Date dateFor = new Date();
		      date = dateFormat.parse(dateFormat.format(dateFor));
		    } catch (Exception ex) {
		      ex.printStackTrace();
		    }
		    return date;
		  }
	public Date stringTODate(String dtstring) {
		Date dt = new Date();
		try {
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			dt = df.parse(dtstring);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dt;
	}
	
	public Date stringTOModifyDate(String dtstring,String format) {
          Date dt = new Date();
          try {
                  DateFormat df = new SimpleDateFormat(format);
                  dt = df.parse(dtstring);
          } catch (Exception ex) {
                  ex.printStackTrace();
          }
          return dt;
        }

	public String dateToStringModifyDate(DateTime date,String format) {
		String strdate = "";
		try {
			//DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat df = new SimpleDateFormat(format);
			strdate = df.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strdate;
	}
	
	public String dateToString(Date date,String format) {
		String strdate = "";
		try {
			//DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat df = new SimpleDateFormat(format);
			strdate = df.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strdate;
	}

	public String getUniqStringueDateTime() {
		StringBuilder sb = new StringBuilder();
		try {
			String hur = "";
			String mint = "";
			String sec = "";

			Date date = new Date();
			int year = date.getYear() + 1900;
			String mont = "";
			int month = date.getMonth() + 1;
			if (month < 10) {
				mont = "0" + Integer.toString(month);
			} else {
				mont = Integer.toString(month);
			}
			String daye = "";
			int day = date.getDate();
			if (day < 10) {
				daye = "0" + Integer.toString(day);
			} else {
				daye = Integer.toString(day);
			}

			int hour = date.getHours();
			int minute = date.getMinutes();
			int second = date.getSeconds();
			if (hour < 10) {
				hur = "0" + Integer.toString(hour);
			} else {
				hur = Integer.toString(hour);
			}
			if (minute < 10) {
				mint = "0" + Integer.toString(minute);
			} else {
				mint = Integer.toString(minute);
			}
			if (second < 10) {
				sec = "0" + Integer.toString(second);
			} else {
				sec = Integer.toString(second);
			}

			sb.append(year);
			sb.append(mont);
			sb.append(daye);
			sb.append(hur);
			sb.append(mint);
			sb.append(sec);
		} catch (Exception ex) {

		}
		return sb.toString();
	}

	public static String readExcel(String xlsname, int usrid, int startrow,
			int columnlength, String extension) {
		Log log = null;
		JSONObject saveRow = null;
		try {
			log = new Log();
			saveRow = new JSONObject();
			FileInputStream file = new FileInputStream(new File(xlsname));
			if (extension.equalsIgnoreCase("xls")) {
				System.out.println("extension::xls:::::::: " + extension);
				HSSFWorkbook workbook = new HSSFWorkbook(file);
				HSSFSheet sheet = workbook.getSheetAt(0);
				FormulaEvaluator evaluator = workbook.getCreationHelper()
						.createFormulaEvaluator();
				DataFormatter formatter = new DataFormatter();
				String rowstr = "";
				StringBuilder sb = new StringBuilder();
				Iterator<Row> rowIterator = sheet.iterator();
				JSONArray json_data1 = null;
				JSONArray json_data = null;
				JSONObject saveColumn = new JSONObject();
				json_data1 = new JSONArray();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					int rownum = row.getRowNum();
					boolean flgr = false;
					if (rownum >= startrow) {
						saveColumn = new JSONObject();
						json_data = new JSONArray();
						for (int i = 0; i < columnlength; i++) {
							rowstr = "";
							Cell cell = row.getCell(i);

							if (cell != null) {
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:
									rowstr = formatter.formatCellValue(cell,
											evaluator);
									break;
								case Cell.CELL_TYPE_NUMERIC:
									rowstr = formatter.formatCellValue(cell);
									break;
								case Cell.CELL_TYPE_STRING:
									rowstr = cell.getStringCellValue();
									break;
								default:
									rowstr = "";
								}
								rowstr = rowstr.trim();
								System.out.println(i
										+ "----------rowstr--------------"
										+ rowstr);
								json_data.put(rowstr);
							} else {
								json_data.put(rowstr);
								flgr = true;
								break;
							}
						}
						if (!flgr) {
							saveColumn.put("column", json_data);
							json_data1.put(saveColumn);
						}

					}
				}
				saveRow.put("row", json_data1);
			} else if (extension.equalsIgnoreCase("xlsx")) {
				System.out.println("extension::::xlsx:--> " + xlsname);

				Workbook workBook = null;
				XSSFSheet xssfSheet = null;
				Sheet sheet;
				try {
					workBook = new XSSFWorkbook(file);
					// Read data at sheet 0
				} catch (Exception e) {
					System.out.println("read data xlsx fromat==> " + e);
				}
				sheet = workBook.getSheetAt(0);
				FormulaEvaluator evaluator = workBook.getCreationHelper()
						.createFormulaEvaluator();
				DataFormatter formatter = new DataFormatter();
				Iterator rowIteration = sheet.rowIterator();
				String rowstr = "";
				JSONArray json_data1 = null;
				JSONArray json_data = null;
				JSONObject saveColumn = new JSONObject();
				json_data1 = new JSONArray();
				boolean flgr = false;
				// Looping every row at sheet 0
				while (rowIteration.hasNext()) {
					XSSFRow xssfRow = (XSSFRow) rowIteration.next();
					Iterator cellIteration = xssfRow.cellIterator();
					int rownum = xssfRow.getRowNum();
					if (rownum >= startrow) {
						saveColumn = new JSONObject();
						json_data = new JSONArray();
						for (int i = 0; i < columnlength; i++) {
							rowstr = "";
							Cell cell = xssfRow.getCell(i);
							if (cell != null) {
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:
									rowstr = formatter.formatCellValue(cell,
											evaluator);
									break;
								case Cell.CELL_TYPE_NUMERIC:
									rowstr = formatter.formatCellValue(cell);
									break;
								case Cell.CELL_TYPE_STRING:
									rowstr = cell.getStringCellValue();
									break;
								default:
									rowstr = "";
								}
								rowstr = rowstr.trim();
								System.out.println(i
										+ "----------rowstr--------------"
										+ rowstr);
								rowstr = rowstr.trim();
								json_data.put(rowstr);
							} else {
								// Society Cell Empty check
								json_data.put(rowstr);
								System.out.println("i---------------" + i
										+ "-------null row-------------");
							}
						}
						if (!flgr) {
							saveColumn.put("column", json_data);
							json_data1.put(saveColumn);
						}
					}

				}
				saveRow.put("row", json_data1);
			} else if (extension.equalsIgnoreCase("csv")) {
				System.out.println("CSV FileFormat.....");
				File arr = new File(xlsname);
				if (arr.exists()) {
					BufferedReader products = new BufferedReader(
							new FileReader(arr));
					String line = "";
					String rowstr = "";
					JSONArray json_data1 = null;
					JSONArray json_data = null;
					JSONObject saveColumn = new JSONObject();
					json_data1 = new JSONArray();
					int j = 0;
					
					while ((line = products.readLine()) != null) {
						String fields = "";
						System.out.println("line-------"+line);
						if(!line.equalsIgnoreCase("")){
						String[] dataval = line.split(",");
						saveColumn = new JSONObject();
						json_data = new JSONArray();
						for (int i = 0; i < columnlength; i++) {
							String cellval = dataval[i];
							if (!cellval.equalsIgnoreCase("")) {
								fields = "" + dataval[i] + "";
							} else {
								fields = "";
							}
							fields = fields.trim();

							json_data.put(fields);
						}
						saveColumn.put("column", json_data);
						json_data1.put(saveColumn);
						j++;
						}
					}
					saveRow.put("row", json_data1);
					products.close();
				}
			}
			file.close();
			return saveRow.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception xlsx reading---> " + ex);
			log.write.error("ex == " + ex);
			return "";
		} finally {

		}

	}

	public boolean deleteIfFileExist(String filepath, String filename) {
		File lcMovefile = null;
		File directoryfile = null;
		try {
			lcMovefile = new File(filepath + "/" + filename);
			directoryfile = new File(filepath);
			if (lcMovefile.exists()) {
				lcMovefile.delete();
			}
			if (directoryfile.isDirectory() && directoryfile.list().length == 0) {
				directoryfile.delete();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public String randomString(int len) {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rand.nextInt(AB.length())));
		return sb.toString();
	}

	public String randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return Integer.toString(randomNum);
	}

	public Date addDays(Date date, int days) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public String roundTwoDecimalValue(String amount) {
		// TODO Auto-generated method stub
		Double myNumber = Double.parseDouble(amount);
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(myNumber).toString();
	}

	public boolean createDirIfNotExist(String dirPath) {
		boolean iscreated=false;
		try{
		File theDir = new File(dirPath);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + dirPath);

			try {
				theDir.mkdir();
				iscreated = true;
			} catch (SecurityException se) {
				// handle it
				se.printStackTrace();
			}
		}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return iscreated;
	}
	public String getFileExtension(String fileName){
		String extension="";
		try{
			 if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
				 extension=fileName.substring(fileName.lastIndexOf(".")+1);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return extension;
	}

	public String getFileSize(String filePath,String sizeIn){
		String size = "";
		try{
			File file = new File(filePath);
			Integer fileSize=(int) file.length();
			 System.out.println("fileSize-----------"+fileSize);
			 double m = fileSize;
			 DecimalFormat dec = new DecimalFormat("0.00");
			if(sizeIn!=null && sizeIn.equalsIgnoreCase("B")){
				size = dec.format(m).concat(" "+sizeIn);
			}else if(sizeIn!=null && sizeIn.equalsIgnoreCase("KB")){
				m=m/1024;
				size = dec.format(m).concat(" "+sizeIn);
				System.out.println("size in kb================"+size);
				if(m<1){
					m = fileSize;
					size = dec.format(m).concat(" B");
					}
			}else if(sizeIn!=null && sizeIn.equalsIgnoreCase("MB")){
				m=m/(1024*1024);
				System.out.println("size in Mb==========================="+m);
				size = dec.format(m).concat(" "+sizeIn);
				if(m<1){
					m = fileSize;
					m=m/1024;
					size = dec.format(m).concat(" KB");
					if(m<1){
						m = fileSize;
						size = dec.format(m).concat(" B");
						}
				}
			}
			System.out.println("size------------"+size);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return size;
	}
	
	public Date stringTODateParser(String dtstring,String dateformat) {
		Date dt = new Date();
		try {
			DateFormat df = new SimpleDateFormat(dateformat);
			dt = df.parse(dtstring);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dt;
	}
	
	public boolean isAlphaNumeric(String s){
	    String pattern= "^[a-zA-Z0-9]*$";
	        if(s.matches(pattern)){
	            return true;
	        }
	        return false;   
	}
	public String dateToStringModify(Date date,String format) {
		String strdate = "";
		try {
			//DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat df = new SimpleDateFormat(format);
			strdate = df.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strdate;
	}
	
	public boolean deleteallFileInDirectory(String filepath) {
		File lcMovefile = null;
		File directoryfile = null;
		try {
			directoryfile = new File(filepath);
			
			if (directoryfile.isDirectory()) {
				directoryfile.delete();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
	public JSONObject getdateAndTimeFromDateTime(String time){
		JSONObject timeObj=new JSONObject();
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dtToStr = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dtToStrTime = new SimpleDateFormat("HH:mm a");
			Date dat=simpleDateFormat.parse(time);
			timeObj.put("date", dtToStr.format(dat));
			timeObj.put("time", dtToStrTime.format(dat));
			System.out.println("dat-------------"+dat);
			
			System.out.println("getdate=---------"+dtToStr.format(dat));
			System.out.println("getdate=---------"+dtToStrTime.format(dat));
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return timeObj;
	}
	
	public String dbEventToMobiledate(String dbdate){
		String timeObj="";
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
			SimpleDateFormat dtToStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dat=simpleDateFormat.parse(dbdate);
			timeObj=dtToStr.format(dat);
			System.out.println("timeObj--------------"+timeObj);
			System.out.println("dat=---------"+dat);
		}catch (Exception e){
			e.printStackTrace();
		}
		return timeObj;
	}
	public String dbEventGreaterThanCurrDate(String dbdate){
		String timeObj="";
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
			SimpleDateFormat dtToStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dat=simpleDateFormat.parse(dbdate);
			Date currntDate=new Date();
			if(dat.after(currntDate)){
				timeObj="1";
			}else{
				timeObj="0";
			}
			System.out.println("timeObj--------------"+timeObj);
			System.out.println("dat=---------"+dat);
		}catch (Exception e){
			e.printStackTrace();
		}
		return timeObj;
	}
	
}

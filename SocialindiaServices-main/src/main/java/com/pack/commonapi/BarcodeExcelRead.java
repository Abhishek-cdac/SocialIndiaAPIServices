package com.pack.commonapi;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.social.utils.Log;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author PL0016
 */
public class BarcodeExcelRead {

    public static String readExcel(String xlsFile, String usrid, String supermart, String storeid, String prod, int batch_id) {
        String errRows = "";
        String insXLString = "";
        try {

            FileInputStream file = new FileInputStream(new File(xlsFile));

            HSSFWorkbook workbook = new HSSFWorkbook(file);

            HSSFSheet sheet = workbook.getSheetAt(0);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            String rowstr = "";
            StringBuilder sb = new StringBuilder();
            sb.append("insert into barcode_tbl "
                    + "(BATCH_ID, PROD_ID, SUPERMARKET_ID, STORE_ID, BARCODE_VALUE, STATUS, ENTRY_BY) "
                    + "values ");

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int rownum = row.getRowNum();

                if (rownum > 0) {


                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_FORMULA:
                                rowstr += "'" + formatter.formatCellValue(cell, evaluator) + "', ";
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                rowstr += "'" + formatter.formatCellValue(cell) + "', ";
                                break;
                            case Cell.CELL_TYPE_STRING:
                                rowstr += "'" + cell.getStringCellValue() + "', ";
                                break;
                        }
                    }                   
                  /*      rowstr = "(" + rowstr + "),";
                        int cnt = countOccurrences(rowstr, ',');
                        if (cnt == 7) {
                            sb.append(rowstr);
                        } else {
                            errRows += ", " + (rownum + 1);
                        }
                        
                    rowstr = "";*/
                }
            }
            errRows = errRows.replaceFirst(", ", "");
//            System.out.print("Row number " + errRows + " is error");
            insXLString = sb.toString();
            if (insXLString.endsWith("),")) {
                insXLString = insXLString.substring(0, insXLString.length() - 1);
            }
            file.close();
        } catch (Exception ex) {
            insXLString = "";
            Log log = new Log();
            log.write.error("ex == " + ex);
        }
        return insXLString + "<MPV>" + errRows;
    }

    private static int countOccurrences(String srcStr, char findchar) {
        int count = 0;
        for (int i = 0; i < srcStr.length(); i++) {
            if (srcStr.charAt(i) == findchar) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
    	//C:\Users\PL0038\Desktop
        String readExcel = readExcel("D:\\Testexcel.xls", "1", "1", "1", "1",1);
        System.out.println("readExcel ===   "+readExcel);
    }
}

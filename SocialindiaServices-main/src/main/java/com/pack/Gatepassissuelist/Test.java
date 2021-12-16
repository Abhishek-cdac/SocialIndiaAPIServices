package com.pack.Gatepassissuelist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException{
		String dateStr3 = "2017-08-22 14:56:50";
		DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	Date issuetime = formatter1.parse(dateStr3);
	 			formatter1 = new SimpleDateFormat("HH:mm:ss");
	 			System.out.println("issuedate3   "+formatter1.format(issuetime));
	 			formatter1.parse(formatter1.format(issuetime));
	}

}

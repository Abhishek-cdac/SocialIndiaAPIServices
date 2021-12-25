package com.mobi.apiinitiate;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.json.JSONException;

import com.pack.utilitypkg.Commonutility;

public class TestJava {
	public static String as(String ad) {
		if (ad.length() > 0) {
			ad = ad + ",";
		} else {
			ad = "";
		}
		return ad;
	}

	public static Date stringtoTimestamp(String timestamp) {
		Date date = new Date();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = dateFormat.parse(timestamp);
			System.out.println("-----");
			System.out.println(date.getHours());
			System.out.println(date.getMinutes());
		} catch (Exception ex) { // empty block
			date = null;
		}
		return date;
	}

	public static String getMothName(int monthNum) {
		String returnMonth = "";
		try {
			DateFormatSymbols dfs = new DateFormatSymbols();
			String[] months = dfs.getMonths();
			if (monthNum >= 0 && monthNum <= 11) {
				returnMonth = months[monthNum];
			}
		} catch (Exception ex) {

		}
		return returnMonth;
	}

	public static String getPostedDateTime(Date entryDate, Date modfyDate) {
		String postedDateTime = "";
		try {

			// #################################
			String dateformattype = "yyyy-MM-dd HH:mm:ss";
			DateFormat dateFormatw = new SimpleDateFormat(dateformattype);
			Date dateForw = new Date();
			Date toDayDateW = dateFormatw.parse(dateFormatw.format(dateForw));

			String dd = "2016-10-04 07:55:48";
			String ddf = "2016-10-04 20:17:02";
			String dateString = dateFormatw.format(toDayDateW);
			System.out.println("dateString:" + dateString);
			// System.out.println("dddddddddd:" + dd);
			Date dateen = dateFormatw.parse(ddf);
			System.out.println("----------:" + dateFormatw.format(dateen));
			getPostedDateTime(dateen, dateen);
			// #################################
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateFor = new Date();
			Date toDayDate = dateFormat.parse(dateFormat.format(dateFor));

			long diff = toDayDate.getTime() - entryDate.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.println("--------------" + entryDate.getDate() + " "
					+ getMothName(entryDate.getMonth()).substring(0, 3)
					+ " at " + entryDate.getHours() + ":"
					+ entryDate.getMinutes());

			if (diffDays != 0) {
				postedDateTime = entryDate.getDate() + " "
						+ getMothName(entryDate.getMonth()).substring(0, 3)
						+ " at " + entryDate.getHours() + ":"
						+ entryDate.getMinutes();
			} else if (diffHours != 0) {
				if (diffHours == 1) {
					postedDateTime = diffHours + " hr";
					System.out.println(postedDateTime);
				} else {
					postedDateTime = diffHours + " hrs";
					System.out.println(postedDateTime);
				}

			} else if (diffMinutes != 0) {
				postedDateTime = diffMinutes + " Mins";
				System.out.println(postedDateTime);
			} else if (diffSeconds != 0) {
				postedDateTime = diffSeconds + " Secs";
				System.out.println(postedDateTime);
			} else {
				System.out.println("Time else Block");
			}
		} catch (Exception ex) {

		}
		return postedDateTime;
	}

	private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
	static {
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "G");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "P");
		suffixes.put(1_000_000_000_000_000_000L, "E");
	}

	public static String format(long value) {
		// Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
		if (value == Long.MIN_VALUE)
			return format(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + format(-value);
		if (value < 1000)
			return Long.toString(value); // deal with easy case

		Entry<Long, String> e = suffixes.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10); // the number part of the
													// output times 10
		boolean hasDecimal = truncated < 100
				&& (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10)
				+ suffix;
	}

	public static void likecountFormat(Long ns) {
		long[] numbers = { 0, 5, 999, 1_000, -5_821, 10_500, -101_800,
				2_000_000, -7_800_000, 92_150_000, 123_200_000, 9_999_999,
				999_999_999_999_999_999L, 1_230_000_000_000_000L,
				Long.MIN_VALUE, Long.MAX_VALUE };
		String[] expected = { "0", "5", "999", "1k", "-5.8k", "10k", "-101k",
				"2M", "-7.8M", "92M", "123M", "9.9M", "999P", "1.2P", "-9.2E",
				"9.2E" };
		for (int i = 0; i < numbers.length; i++) {
			long n = numbers[i];
			String formatted = format(n);
			System.out.println(n + " => " + formatted);
			if (!formatted.equals(expected[i]))
				throw new AssertionError("Expected: " + expected[i]
						+ " but found: " + formatted);
		}
		System.out.println("###################");
		System.out.println(format(200000000));
	}

	public static void simpleJson() throws JSONException {
		// JSONArray list = new JSONArray();
		// JSONObject jsd = new JSONObject();
		// Object jsk = "lk";
		// Object jsv = "lk";
		//
		// jsd.put(jsk, jsv);
		//
		// System.out.println("jsd:" + jsd.toString());
		// list.add("msg 1");
		// list.add("msg 2");
		// list.add("msg 3");
		// System.out.println(list.toJSONString());
		// System.out.println("#############");

	}

	public static void getShortTime() throws ParseException {
		String input = "2014-04-25 7:03:13";
		DateFormat dateFormatw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		String shortTime = dateFormat.format(dateFormatw.parse(input));
		System.out.println("--shortTime--:" + shortTime);

	}

	public static void uniqueNoFromDate() {
		Date dateFor = new Date();
		DateFormat dateFormatD = new SimpleDateFormat("yyyyMMddhhmmssMs");
		String retValue = dateFormatD.format(dateFor);
		System.out.println("--longDateTime--:" + retValue);
	}

	public boolean c() {
		System.out.println(" hello");
		return true;
	}

	public boolean d() {
		System.out.print("Say");
		c();
		try {

		} catch (Exception ex) {

		} finally {
			try {
				super.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// StringBuilder sb = new StringBuilder();
		String sd = "[]";
		try {
			System.out.println(Commonutility.enteyUpdateInsertDateTime());
		} catch (Exception ex) {
			System.out.println("exxxx:" + ex);
		} finally {
		}

	}

}

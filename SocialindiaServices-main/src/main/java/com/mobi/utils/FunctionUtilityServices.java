package com.mobi.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONArray;

import com.mobi.message.CreateGroup;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.Log;

public class FunctionUtilityServices implements FunctionUtility {

	@Override
	public String getPostedDateTime(Date entryDate) {
		String postedDateTime = "";
		try {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateFor = new Date();
			Date toDayDate = dateFormat.parse(dateFormat.format(dateFor));
			System.out.println("toDayDate-------------"+toDayDate);
			System.out.println("entryDate-------------"+entryDate);
			long diff = toDayDate.getTime() - entryDate.getTime();
			System.out.println("diff---------"+diff);
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
					postedDateTime = diffHours + " Hr ago";
				} else {
					postedDateTime = diffHours + " Hrs ago";
				}
			} else if (diffMinutes != 0) {
				postedDateTime = diffMinutes + " Mins ago";
			} else if (diffSeconds != 0) {
				postedDateTime = diffSeconds + " Secs ago";
			} else if (diffSeconds == 0) {
				//postedDateTime = "0 Sec ago";
				postedDateTime = "Just now";
			} else {
				
				System.out.println("Time else Block");
			}
			System.out.println("postedDateTime:" + postedDateTime);
		} catch (Exception ex) {

		}
		System.out.println("########## :feed time:" + postedDateTime);
		return postedDateTime;
	}

	@Override
	public String getMothName(int monthNum) {
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

	private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
	static {
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "G");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "P");
		suffixes.put(1_000_000_000_000_000_000L, "E");
	}

	@Override
	public String likeCountFormat(Long value) {
		String retnval = "";
		try {
			if (value != null && value != 0) {
				if (value > 999) {
					retnval = likeFormat(value);
				} else {
					retnval = Long.toString(value);
				}

				
			}

		} catch (Exception ex) {
			System.out.println("Exception ex:" + ex);
		}
		return retnval;
	}

	@Override
	public String likeFormat(long value) {
		// Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
		if (value == Long.MIN_VALUE)
			return likeFormat(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + likeFormat(-value);
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

	@Override
	public String longToString(Long value) {
		String retValue = "";
		try {
			if (value != null && value != 0) {
				retValue = Long.toString(value);
			}
		} catch (Exception ex) {
		}
		return retValue;
	}

	@Override
	public String getShortTime(String timeStamp) {
		String retValue = "";
		try {
			System.out.println("Enter into short time");
			DateFormat dateFormatw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			retValue = dateFormat.format(dateFormatw.parse(timeStamp));
			System.out.println("--retValue--:" + retValue);
		} catch (Exception ex) {
			System.out.println("Exception found in getShortTime:" + ex);
		}
		return retValue;
	}

	@Override
	public String getLongTime(String timeStamp) {
		String retValue = "";
		try {
			System.out.println("Enter into Long time");
			DateFormat dateFormatw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat dateFormatD = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			retValue = dateFormatD.format(dateFormatw.parse(timeStamp)) + " "
					+ dateFormat.format(dateFormatw.parse(timeStamp));
			System.out.println("--longDateTime--:" + retValue);
		} catch (Exception ex) {
			System.out.println("Exception found in getLongTime:" + ex);
		}
		return retValue;
	}

	@Override
	public String uniqueNoFromDate() {
		String retValue = "";
		try {
			System.out.println("Enter into uniqueNoFromDate");
			Date dateFor = new Date();
			DateFormat dateFormatD = new SimpleDateFormat("yyyyMMddhhmmssMs");
			retValue = dateFormatD.format(dateFor);
			System.out.println("--longDateTime--:" + retValue);
		} catch (Exception ex) {
			System.out.println("Exception found in uniqueNoFromDate:" + ex);
		}
		return retValue;
	}

	@Override
	public String jsnArryIntoStrBasedOnComma(JSONArray jsnArryObj) {
		String retValue = "";
		try {
			System.out.println("Enter into jsnArryIntoStrBasedOnComma");
			if (jsnArryObj != null) {
				for (int i = 0; i < jsnArryObj.length(); i++) {
					int mem = jsnArryObj.getInt(i);
					retValue += mem + ",";
					System.out.println("Memeber id :" + mem);
				}
			}
			System.out.println("--return string--:" + retValue);
			if (retValue.contains(",")) {
				retValue = retValue.substring(0, retValue.length() - 1);
			}
			System.out.println("--final return string--:" + retValue);
		} catch (Exception ex) {
			retValue = null;
			System.out.println("Exception found in jsnArryIntoStrBasedOnComma:"
					+ ex);
		}
		return retValue;
	}

	@Override
	public String trimSplEscSrchdata(String srchdata) {
		try {
			srchdata = srchdata.trim();
			// CHECK SINGLE QUOTES
			if (srchdata.contains("'")) {
				srchdata = srchdata.replaceAll("\\'", "");
			}
			// CHECK COMMMA
			if (srchdata.contains(",")) {
				srchdata = srchdata.replaceAll(",", " ");
			}
			// CHECK QUESTION
			if (srchdata.contains("?")) {
				srchdata = srchdata.replaceAll("\\?", "");
			}
			// CHECK |
			if (srchdata.contains("|")) {
				srchdata = srchdata.replaceAll("\\|", "");
			}
			// CHECK -
			if (srchdata.contains("-")) {
				srchdata = srchdata.replaceAll("\\-", " ");
				// srchdata = datasplitbyHyphen(srchdata);
			}
			// CHECK SPACE
			// srchdata = Util.datasplitbyspace(srchdata);
			// CHECK PLUS
			if (srchdata.contains("+")) {
				srchdata = srchdata.replaceAll("\\+", "");
			}
			if (srchdata.contains("|")) {
				if (srchdata.startsWith("|")) {
					srchdata = srchdata.substring(1, srchdata.length() - 1);
				}
				if (srchdata.endsWith(",")) {
					srchdata = srchdata.substring(0, srchdata.length() - 1);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception found srchdata :" + e);			
			srchdata = "";
		}
		return srchdata;
	}

	@Override
	public String dataSplitbyHyphen(String data) {
		String dataval = "";
		try {
			data = data.trim();
			if (data.contains(" ")) {
				String[] spltval = data.split(" ");
				for (int i = 0; i < spltval.length; i++) {
					if (Commonutility.checkempty(spltval[i])) {
						if (i == spltval.length - 1) {
							// dataval += spltval[i];
							dataval += hypenadd(spltval[i]);

						} else {
							dataval += hypenadd(spltval[i]) + " ";
						}
					}
				}
				System.out.println("dataval:" + dataval);
			} else {
				// dataval = data;
				dataval = hypenadd(data);
			}
		} catch (Exception e) {
			System.out.println("Exception found in datasplitbyHyphen:" + e);
			Log log = new Log();
			log.write.error("Exception found in datasplitbyHyphen:" + e);
		}
		return dataval;
	}

	@Override
	public String dataSplitbyspace(String data) {
		String dataval = "";
		try {
			if (data.contains(" ")) {
				String[] spltval = data.split(" ");
				for (int i = 0; i < spltval.length; i++) {
					if (Commonutility.checkempty(spltval[i])) {
						if (i == spltval.length - 1) {
							dataval += spltval[i];
						} else {
							dataval += spltval[i] + "|";
						}
					}
				}
				// System.out.println("dataval:" + dataval);
			} else {
				dataval = data;
			}
		} catch (Exception e) {
			System.out.println("Exception found in :" + e);
		}
		return dataval;
	}

	@Override
	public String hypenadd(String data) {
		String retval = "";
		try {
			if (Commonutility.checkempty(data) && data.contains("-")) {
				String[] spl = data.split("-");
				if (spl.length == 2) {
					if (Commonutility.checkempty(spl[1])) {
						retval = "-" + spl[1];
					}
				}
			} else {
				if (Commonutility.checkempty(data)) {
					retval = data;
				}
			}
		} catch (Exception e) {
			Log log = new Log();
			log.write.error("Exception found in hypenadd " + e);
		}
		return retval;
	}

}

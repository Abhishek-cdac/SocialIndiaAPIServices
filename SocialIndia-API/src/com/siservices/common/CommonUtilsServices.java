package com.siservices.common;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CommonUtilsServices implements CommonUtils {
	
	@Override
	public Date getStrCurrentDateTime(String dateformattype) {
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
	
	 @Override
	  public String getRandomval(String type, int dataLength) {
	    char[] result = new char[dataLength];
	    try {
	      // final char[] CHARSET_aZ_09 =
	      // "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	      // .toCharArray();

	      final char[] charSetAz = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	          .toCharArray();
	      final char[] charSetAz09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	          .toCharArray();
	      final char[] charSetHex = "0123456789ABCDEF".toCharArray();
	      final char[] charSetSpecial = { '!', 'A', 'B' };
	      final char[] charSet09 = "0123456789".toCharArray();
	      final char[] charSetaZ09sp = "abcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+{}|[]?/><,.;':ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	          .toCharArray();

	      final char[] charSetAZaz09 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	          .toCharArray();
	      final char[] charSetAZazSP = "abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+{}|[]?/><,.;':ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	          .toCharArray();
	      final char[] charSet09SP = "0123456789~!@#$%^&*()_+{}|[]?/><,.;':"
	          .toCharArray();
	      final char[] charSetSP = "~!@#$%^&*()_+{}|[]?/><,.;':".toCharArray();

	      char[] characterSet = null;
	      if (type != null && !type.equalsIgnoreCase("")
	          && !type.equalsIgnoreCase("null")) {
	        if (type.equalsIgnoreCase("aZ")) {
	          characterSet = charSetAz;
	        } else if (type.equalsIgnoreCase("AZ_09")) {
	          characterSet = charSetAz09;
	        } else if (type.equalsIgnoreCase("09")) {
	          characterSet = charSet09;
	        } else if (type.equalsIgnoreCase("aZ_09")) {
	          characterSet = charSet09;
	        } else if (type.equalsIgnoreCase("aZ_09_sp")) {
	          characterSet = charSetaZ09sp;
	        } else if (type.equalsIgnoreCase("AZaz09")) {
	          characterSet = charSetAZaz09;
	        } else if (type.equalsIgnoreCase("AZazSP")) {
	          characterSet = charSetAZazSP;
	        } else if (type.equalsIgnoreCase("09SP")) {
	          characterSet = charSet09SP;
	        } else if (type.equalsIgnoreCase("SP")) {
	          characterSet = charSetSP;
	        }
	      }
	      Random random = new SecureRandom();

	      for (int i = 0; i < result.length; i++) {
	        // picks a random index out of character set > random character
	        int randomCharIndex = random.nextInt(characterSet.length);
	        result[i] = characterSet[randomCharIndex];
	      }

	    } catch (Exception ex) {
	      System.out.println("Exception found ger random number:" + ex);
	    }
	    return new String(result);
	  }
	
	 @Override
	  public String stringToMD5(String password) {
	    String result = null;
	    MessageDigest md;
	    try {
	      md = MessageDigest.getInstance("MD5");
	      md.update(password.getBytes(Charset.forName("UTF-8")));
	      result = String.format(Locale.ROOT, "%032x",
	          new BigInteger(1, md.digest()));
	    } catch (Exception ex) {	      
	    }

	    return result;
	  }
	 
}

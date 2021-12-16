package com.siservices.login;



import java.net.MalformedURLException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

public class EncDecrypt {
  private static final String ALGORITHM = "AES"; // AES 128 = 16 byte * 8 = 128
  private static byte[] keyValue = new byte[] { 'P', 'e', 'N', 'i', 'N', 'L',
      'o', 'G', 't', 'E', 'c', 'H', 'n', 'O', 'l', 'G' };// 16 bytes only allow

  public static HttpServletResponse response;

  /**
   * encrypt.
   * 
   * @param valueToEnc
   *          .
   * @return String
   * @throws Exception
   *           exce.
   */
  public static String encrypt(String valueToEnc) throws Exception {
    Key key = generateKey();
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    String encryptedValue = null;
    if (valueToEnc != null && !valueToEnc.equalsIgnoreCase("")
        && !valueToEnc.equalsIgnoreCase("null")) {
      byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
      //encryptedValue = new BASE64Encoder().encode(encValue);
      encryptedValue =Base64.encodeBase64String(encValue);
    }
    return encryptedValue;
  }

  /**
   * decrypt.
   * 
   * @param encryptedValue
   *          .
   * @return String
   * @throws Exception
   *           ex
   */
  public static String decrypt(String encryptedValue) throws Exception {
    Key key = generateKey();
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key);
   // byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
    byte[] decordedValue = Base64.decodeBase64(encryptedValue);
    byte[] decValue = cipher.doFinal(decordedValue);
    String decryptedValue = new String(decValue);   
    return decryptedValue;    
  }

  private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
    // key = key.generateSecret(new DESKeySpec(keyValue));
    return key;
  }

  /**
   * decrypt.
   * 
   * @param dataconcat
   *          .
   * @return String
   * @throws Exception
   *           ex.
   */
  public static String encrptDecrptReg(String dataconcat) {
    System.out.println("enter to encrptdecrept method");
    System.out.println("val---" + dataconcat);
    String encrptdata = "";
    try {
      // get URL content
      encrptdata = EncDecrypt.encrypt(dataconcat);
      System.out.println("--->" + encrptdata);
      encrptdata = "DdSqULGdci/QEX2dZyYkf2uBjHzUGZj5GHwP4DNUMw5V6LZ"
          + "iKsQAtEeohMkpythLRAvZTrehqCXKOuLrMfWUvvzOJn/bCkhpG1KPwx5PJrB0ozRvVaHY7S3Y2V9XMIU3";
      System.out.println("URL--->"
          + "---http://192.168.1.41:8086/paymentgateways/PaymentInit?payvale="
          + encrptdata);

      // response.sendRedirect("http://192.168.1.41:8086/paymentgateways/PaymentInit?payvale="+encrptdata);

      System.out.println("Done");
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return encrptdata;
  }

  /**
   * main.
   * 
   * @param args
   *          .
   */
  public static void main(String[] args) {
    try {
      
      String password1 = "10|||||sasi|kumar|||2nd street|chennai|tamilnadu"
          + "|600034|IN|mail2sasikumar.g@gmail.com|NI";
      String passwordEnc1 = EncDecrypt.encrypt(password1);
      // String
      String passwordDec1 = EncDecrypt.decrypt(passwordEnc1);
      // 7cs6KtEem+6WMxUqNbhMqEO0rSUhGp1DbSbCv6c/hhzODoqr6YIPNG0O91QfRYTehA7qEemX8gEo
      // Mogkur9mDjeyGaQETMt4QOUvPBgG/DErshIrlSVir45PrIzHtuzgY4XIjmU8HILXptbfFsAa1ZB+
      // NzyPWuC9TGTz1aQp24sOOoeSlE1KCo2EEcwprJn2slsisw0hwzsSd1PIo015CA==
      System.out.println("Plain Text : " + password1);
      System.out.println("Encrypted : " + passwordEnc1);
      System.out.println("Decrypted : " + passwordDec1);

      System.out
          .println(EncDecrypt
              .decrypt("7cs6KtEem+6WMxUqNbhMqEO0rSUhGp1DbSbCv6c/hhzODoqr6YIP"
                  + "NG0O91QfRYTehA7qEemX8gEoMogkur9mDjeyGaQETMt4QOUvPBgG/DErshIrlSVir45PrIzHtuzgY4"
                  + "XIjmU8HILXptbfFsAa1ZB+NzyPWuC9TGTz1aQp24sOOoeSlE1KCo2EE"
                  + "cwprJn2slsisw0hwzsSd1PIo015CA=="));

    } catch (Exception ex) {
      Logger.getLogger(EncDecrypt.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}

package com.pack.qrcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;


public class QRcodegenerator {

	public static void main(String ss[]){
		SimpleDateFormat lvrsmft = new SimpleDateFormat("yyyyMMddhhmmss");
	    String filePath = "D:/QRIMG/QRImg_"+lvrsmft.format(new Date())+".png";
		String lvrInputContent ="WELCOMETOALL"; 
	    try {
			 	generatorQRcode(lvrInputContent,filePath,200, 200);
				String tr=readQRCode(filePath);
				System.out.println(tr);
			} catch (Exception e) {			
				e.printStackTrace();
			} 
	}
	
	public static String generatorQRcode(String pinpcnt, String pFpath, int pimgWidth, int pimgHeight){
		Charset charset = null;
		CharsetEncoder encoder = null;
		byte[] b = null;
		String data = "";
		
		ByteBuffer bbuf = null;
		BitMatrix matrix = null;
		File file = null;
		try{
			charset = Charset.forName("ISO-8859-1");
			encoder = charset.newEncoder();
		   		   
			// Convert a string to ISO-8859-1 bytes in a ByteBuffer
			bbuf = encoder.encode(CharBuffer.wrap(pinpcnt));
			b = bbuf.array();
			data = new String(b, "ISO-8859-1");
		   

			// get a byte matrix for the data
			// int h = 100;
			// int w = 100;
		    com.google.zxing.Writer writer = new QRCodeWriter();		    
		    matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, pimgWidth, pimgHeight);
		   
		    //SimpleDateFormat lvrsmft = new SimpleDateFormat("yyyyMMddhhmmss");
		    //String filePath = "D:/QRIMG/QRImg_"+lvrsmft.format(new Date())+".png";
			file = new File(pFpath);
			MatrixToImageWriter.writeToFile(matrix, "PNG", file);
			System.out.println("QR Image printing to : " + file.getAbsolutePath());
		    		    		
		}catch(Exception e){
			System.err.println("Exception Found in QRcodegenerator.generatorQRcode() : "+e);
		} finally {
			charset = null;
			encoder = null;
			b = null;
			data = "";
			bbuf = null;
			matrix = null;
			file = null;
		}
		return "";
	}
	
	public static String readQRCode(String filePath) throws FileNotFoundException, IOException {
		Result qrCodeResult = null;
		BinaryBitmap binaryBitmap = null; 
		BufferedImageLuminanceSource lvrBimluminsrc = null;
		HybridBinarizer lvrHybrbinzr = null;
		try {   
				lvrBimluminsrc =  new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)));
				lvrHybrbinzr = new HybridBinarizer(lvrBimluminsrc);
				binaryBitmap = new BinaryBitmap(lvrHybrbinzr);
				qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
				return qrCodeResult.getText();
//				binaryBitmap = new BinaryBitmap(new HybridBinarizer(
//		        new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));									
			} catch (Exception e) {
				System.out.println("Exception Found in readQRCode : "+e);
				return "Error";
			}finally{
				qrCodeResult = null;
				binaryBitmap = null; 
				lvrBimluminsrc = null;
				lvrHybrbinzr = null;
			}
		    
		  }
}

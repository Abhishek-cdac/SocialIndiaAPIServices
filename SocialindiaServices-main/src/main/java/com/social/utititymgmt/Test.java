package com.social.utititymgmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Collections;


import net.sf.jasperreports.engine.JRException;

public class Test {
		static HashMap parameters=new HashMap();
		String t;
	 
		public  void toooo(){
		 	String t ="";
		 	System.out.println("tooo : " + t);
		 	System.out.println("tooo : " +this.t);
		}
	 
	public static void main(String[] args) throws JRException, IOException {
		try{
			 String t;
			String data="!_!";
			String[] de = data.split("!_!", -1);
			System.out.println("de---------------"+de.length);

			HashMap lvemp = new HashMap();
			lvemp.put("1", "P");lvemp.put("2", "R");lvemp.put("3", "A");lvemp.put("4", "B");lvemp.put("3", "A");lvemp.put(null, null);
			System.out.println("lvemp : "+lvemp);
			Iterator lvrSet =  lvemp.entrySet().iterator();
			System.out.println("-----Entry Set [Start]-------");
			while (lvrSet.hasNext()){
				Map.Entry mpentry = (Map.Entry) lvrSet.next();
				System.out.println(mpentry.getKey() + " : "+ mpentry.getValue());	
				System.out.println("hashCode :  "+mpentry.hashCode());
			}
			System.out.println("-----Entry Set [End]-------");
			
			System.out.println("-----Key Set [Start]-------");
			Iterator lvrKEYSet = 	lvemp.keySet().iterator();
			while (lvrKEYSet.hasNext()){
				String keyy = (String)lvrKEYSet.next();
				System.out.println(keyy +" : "+ lvemp.get(keyy));				
			}
			System.out.println("-----Key Set [End]-------");
			
			Hashtable lvrHTBL = new Hashtable();
			lvrHTBL.put("1", "M");lvrHTBL.put("2", "N");lvrHTBL.put("3", "O");lvrHTBL.put("4", "P");lvrHTBL.put("4", "P");
			System.out.println("lvrHTBL : "+lvrHTBL);			
			System.out.println("hashCode :  "+lvrHTBL.hashCode());			
			String Lis[] = { "PRABHU", "RAJA", "MANI" };
			ArrayList<String> lcstVr = new ArrayList<String>(Arrays.asList(Lis));
			System.out.println("ArrayList hascode :  " + lcstVr.hashCode());
			Collections.reverse(lcstVr);
			System.out.println("ArrayList reverse :  " + lcstVr);
			Collections.reverse(lcstVr);
			System.out.println("ArrayList :  " + lcstVr);

			new Test().toooo();
			
			String ab= "ab";
			String bc= "ab";
			String cs= "bc";
			System.out.println(cs);
			System.out.println(bc);
			System.out.println(ab);
			
			String aa="ABA";
			String bb = "ABAC";
			
			String personalLoan = new String("cheap personal loans");
			String homeLoan = new String("cheap personal loans");
			
			
			System.out.println("== compare :  " + (personalLoan == homeLoan));
			System.out.println("equals () :  " + personalLoan.equals(homeLoan));
			}catch (Exception e) {
				System.out.println("Exception  :::: "+e); 
			}
   }


}

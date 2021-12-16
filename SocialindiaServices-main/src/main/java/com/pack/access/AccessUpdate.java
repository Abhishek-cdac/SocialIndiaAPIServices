package com.pack.access;

import org.hibernate.Session;
import org.json.JSONObject;

import com.pack.access.persistance.AccessDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.Log;

public class AccessUpdate {
	public static JSONObject toAccessUpdate(JSONObject pDataJson) {
		JSONObject locFinalRTNObj=null;
		AccessDaoservice locaccessObj=null;
		String ipaddress=null;
		String uniquecode="";
		Log log = new Log();

		try {			
			System.out.println("Step 2 : Access update block enter");
			ipaddress = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "ipaddress");
			uniquecode=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "uniqueId");
			int uniqueid=Integer.parseInt(uniquecode);
			locaccessObj=new AccessDaoservice();	
			boolean ipval=locaccessObj.verificationUnique(ipaddress,uniqueid);
			System.out.println("Step 3 : Access update block Exit");
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toAccessUpdate() : "+e);
			log.logMessage("Exception :AccessUpdate", "error", AccessUpdate.class);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {

		}
	}
	
	
}

package com.pack.reconcile;

import com.pack.utilitypkg.Commonutility;

public class CyberPaygateCall extends Thread{
	String flg = null;
	String retval=null;
	CyberPaygateCall(String flgval){
		flg=flgval;
	}
	
	public void run() {
		if(flg!=null && flg.equalsIgnoreCase("cyberplat")){
			Commonutility.toWriteConsole("Step 1 : [Cyberplate] Enter ");
			CyberplatReconcile cybermethod=new CyberplatReconcile();
			retval=cybermethod.toStartCyberplatReconcile();
		}else if(flg!=null && flg.equalsIgnoreCase("paygate")){
			Commonutility.toWriteConsole("Step 2 : [Paygate] Enter.");
			PaygateReconcile paygate=new PaygateReconcile();
			retval=paygate.toStartPaygateReconcile();
		}else{
			System.out.println("Test --Error.");
		}
		if(retval!=null && retval.equalsIgnoreCase("SUCCESS") && this.isAlive()){
			Commonutility.toWriteConsole("Step 3 : [CyberPaygateCall] Thread Stoped.");
		this.stop();
	} else {
		this.stop();
	}
		}
}

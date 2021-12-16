package com.social.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class BioMetricContextListener implements ServletContextListener{

	BioMetricThread thread = null;
	
	@Override
	public void contextInitialized(ServletContextEvent arg){
		BiometricUtility.toWriteConsole("ReadBioMetricData: Before contextInitialized()");
		thread = new BioMetricThread();
		thread.start();
		BiometricUtility.toWriteConsole("ReadBioMetricData: After contextInitialized()");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg) {
		BiometricUtility.toWriteConsole("ReadBioMetricData: Before destroy()");
		BioMetricThread.stop = false;
		BiometricUtility.toWriteConsole("ReadBioMetricData: After destroy()");
	}
}

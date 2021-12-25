package com.social.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sisocial.load.HibernateUtil;
import com.socialindiaservices.vo.BiometricDatabaseInfoTblVO;
import com.socialindiaservices.vo.BiometricUserInfoTblVO;

public class BiometricUtility {
	
	public static void main(String[] args) {
		String SERVER_IP = "142.93.212.188";
		String PORT_NO = "1433";
		String USERNAME = "SA";
		String PASSWORD = "Social@123";
		String DATABASE_NAME = "eSSLSmartOffice";
		String socyteaId = "4";
		
		new BiometricUtility().biometricUtility(SERVER_IP,PORT_NO,USERNAME,PASSWORD,DATABASE_NAME,socyteaId);
	}
	
	public void biometricUtility(String SERVER_IP,String PORT_NO,String USERNAME,String PASSWORD,String DATABASE_NAME,String socyteaId){
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try{
			BiometricUtility.toWriteConsole("RemoteBiometricUtility.biometricUtility() - Start");
			
			connection = BiometricRemoteDBConnection.getInstance(SERVER_IP, PORT_NO, USERNAME, PASSWORD, DATABASE_NAME).getConnection();
			
			statement = connection.createStatement();
			
//			String query = "SELECT * FROM DeviceLogs_5_2018 INNER JOIN Employees ON DeviceLogs_5_2018.UserID=Employees.EmployeeCode WHERE Employees.EmployementType = 'Resident' ";
			String query = "SELECT * FROM DeviceLogs_5_2018 INNER JOIN Employees ON DeviceLogs_5_2018.UserID=Employees.EmployeeCode ";
			
			rs = statement.executeQuery(query);
			BiometricUserInfoTblVO tblVO = null;
			while (rs.next()) {
				
				tblVO = new BiometricUserInfoTblVO();
				tblVO.setBioDeviceId(rs.getString("DeviceId"));
				BiometricUtility.toWriteConsole(">>>>>>>>>>>>>>>>>>>>>>>>>>>"+rs.getString("EmployeeId"));
				tblVO.setBioDeviceLogId(rs.getString("DeviceLogId"));
				tblVO.setBioDuration(rs.getString("Duration"));
				tblVO.setBioEmail(rs.getString("Email"));
				tblVO.setBioIsSendPushNotification(rs.getString("IsSendPushNotification"));
				tblVO.setBioLocation(rs.getString("Location"));
				tblVO.setBioLogDate(rs.getString("LogDate"));
				tblVO.setBioMobileNo(rs.getString("ContactNo"));
				tblVO.setBioRecordStatus(rs.getString("RecordStatus"));
				tblVO.setBioResidentId(rs.getString("EmployeeId"));
				tblVO.setBioResidentName(rs.getString("EmployeeName"));
				tblVO.setBioStatusCode(rs.getString("StatusCode"));
				tblVO.setBioUserId(rs.getString("UserId"));
				tblVO.setBioVerificationMode(rs.getString("VerificationMode"));
				
				tblVO.setSocyteaId(socyteaId);
				
				insertData(tblVO);
			}
			rs.close();
			statement.close();
			connection.close();
		}
		catch (Exception e) {
			BiometricUtility.toWriteConsole("Step -1 : Exception found RemoteBiometricUtility.biometricUtility() : "+getStackTrace(e.getStackTrace()));
		}
		finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			if (connection!=null) {
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		BiometricUtility.toWriteConsole("RemoteBiometricUtility.biometricUtility() - End");
	}
	
	private void insertData(BiometricUserInfoTblVO tblVO){		
		Session session = null;
		Transaction tx = null;
		Log logwrite = new Log();
		try {
			BiometricUtility.toWriteConsole("RemoteBiometricUtility.insertData() - Start");
			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(tblVO);
			tx.commit();	
		} catch (Exception ex) {			
			if (tx != null) {
				tx.rollback();
			}
			BiometricUtility.toWriteConsole("Step -1 : Exception found RemoteBiometricUtility.insertData() : "+getStackTrace(ex.getStackTrace()));
			
		} finally {
			if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
			if (tx != null) {tx = null;}
		}
		
		BiometricUtility.toWriteConsole("RemoteBiometricUtility.insertData() - End");
		logwrite = null;
	}
	
	public List<BiometricDatabaseInfoTblVO> getBioMetricRemoteHost() {
		BiometricUtility.toWriteConsole("RemoteBiometricUtility.getBioMetricRemoteHost() - Start");
		
		List<BiometricDatabaseInfoTblVO> infoTblVO = new ArrayList<BiometricDatabaseInfoTblVO>();
		Session session = HibernateUtil.getSession();
		Query qy = null;
	    try {
	    	session = HibernateUtil.getSession();
	    	String qry = " from BiometricDatabaseInfoTblVO ";
	    	qy = session.createQuery(qry);
	    	infoTblVO = qy.list();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	infoTblVO = null;
	    	BiometricUtility.toWriteConsole("Exception RemoteBiometricUtility.getBioMetricRemoteHost() - ERROR:"+getStackTrace(ex.getStackTrace()));
	    } finally {
	    	if(session!=null){session.flush(); session.clear(); session.close(); session = null;}
	    	qy = null;
	    }
	    BiometricUtility.toWriteConsole("RemoteBiometricUtility.getBioMetricRemoteHost() - End");
	    return infoTblVO;
	}
	
	synchronized static public void toWriteConsole(String msg){
		FileOutputStream out = null;
		try{
			out = new FileOutputStream("C:\\nacter\\bio-log.txt", true);
			msg = "\n" + msg;
			out.write(msg.getBytes());
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				out.write(getStackTrace(e.getStackTrace()).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	synchronized static public String getStackTrace(StackTraceElement[] msg){
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < msg.length; i++) {
			StackTraceElement element = msg[i];
			str.append("\n");
			str.append(element.toString());
		}
		return str.toString();
	}
}
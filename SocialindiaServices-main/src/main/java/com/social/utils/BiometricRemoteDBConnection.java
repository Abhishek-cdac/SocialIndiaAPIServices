package com.social.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BiometricRemoteDBConnection {

	private static BiometricRemoteDBConnection instance;
    private Connection connection;

    private BiometricRemoteDBConnection(String SERVER_IP,String PORT_NO,String USERNAME,String PASSWORD,String DATABASE_NAME) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + SERVER_IP + ":" + PORT_NO + ";databaseName=" + DATABASE_NAME;
            this.connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            BiometricUtility.toWriteConsole("BiometricRemoteDBConnection Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static BiometricRemoteDBConnection getInstance(String SERVER_IP,String PORT_NO,String USERNAME,String PASSWORD,String DATABASE_NAME) throws SQLException {
        if (instance == null) {
            instance = new BiometricRemoteDBConnection(SERVER_IP,PORT_NO,USERNAME,PASSWORD,DATABASE_NAME);
        } else if (instance.getConnection().isClosed()) {
            instance = new BiometricRemoteDBConnection(SERVER_IP,PORT_NO,USERNAME,PASSWORD,DATABASE_NAME);
        }

        return instance;
    }
}

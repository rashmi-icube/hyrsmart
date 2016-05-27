package org.icube.hyrsmart.java.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.icube.hyrsmart.java.helper.UtilHelper;

public class DatabaseConnectionHelper {

	private static DatabaseConnectionHelper instance;

	public static synchronized DatabaseConnectionHelper getInstance() {
		if (instance == null)
			instance = new DatabaseConnectionHelper();
		return instance;
	}

	public Connection masterCon;
	public Map<Integer, Connection> companySqlConnectionPool;
	public Map<Integer, String> companyImagePath;

	private final static String MASTER_URL = UtilHelper.getConfigProperty("master_sql_url");
	private final static String MASTER_USER = UtilHelper.getConfigProperty("master_sql_user");
	private final static String MASTER_PASSWORD = UtilHelper.getConfigProperty("master_sql_password");

	private DatabaseConnectionHelper() {

		// master sql connection
		try {
			Class.forName("com.mysql.jdbc.Driver");
			masterCon = (masterCon != null && !masterCon.isValid(0)) ? masterCon : DriverManager.getConnection(MASTER_URL, MASTER_USER,
					MASTER_PASSWORD);
			org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).debug("Successfully connected to MySql with master database");

		} catch (SQLException | ClassNotFoundException e) {
			org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).error(
					"An error occurred while connecting to the master database on : " + MASTER_URL + " with user name : " + MASTER_USER, e);
		}
		companySqlConnectionPool = new HashMap<>();
		companyImagePath = new HashMap<>();
	}

	@Override
	public void finalize() {
		org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).debug("Shutting down databases ...");
		try {
			if (!masterCon.isClosed()) {
				try {
					masterCon.close();
					org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).debug("Connection to master database closed!!!!");
				} catch (SQLException e) {
					org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class)
							.error("An error occurred while closing the mysql connection", e);
				}
			}

			for (int companyId : companySqlConnectionPool.keySet()) {
				companySqlConnectionPool.get(companyId).close();
				org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).debug(
						"Connection to company sql for companyId : " + companyId + " is " + "closed!!!!");
			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).error("An error occurred while attempting to close db connections", e);
		}
	}

	/**
	 * Retrieves the company database connections
	 * @param companyId - The ID of the company for which the connections are required
	 */
	public void getCompanyConnection(int companyId) {
		try {
			String sqlUrl = "", sqlUserName = "", sqlPassword = "";
			// check if the sql connection pool contains the connection for the given company and if it is valid else call the db procedure for
			// connection details
			if (!companySqlConnectionPool.containsKey(companyId)
					|| (companySqlConnectionPool.containsKey(companyId) && !companySqlConnectionPool.get(companyId).isValid(0))) {
				// get company details

				CallableStatement cstmt = masterCon.prepareCall("{call getCompanyConfig(?)}");
				cstmt.setInt(1, companyId);
				ResultSet rs = cstmt.executeQuery();

				while (rs.next()) {
					sqlUrl = "jdbc:mysql://" + rs.getString("sql_server") + ":3306/" + rs.getString("comp_sql_dbname");
					sqlUserName = rs.getString("sql_user_id");
					sqlPassword = rs.getString("sql_password");
					companyImagePath.put(companyId, rs.getString("image_url"));
				}
			}

			// company sql connection
			try {
				if (!companySqlConnectionPool.containsKey(companyId)
						|| (companySqlConnectionPool.containsKey(companyId) && !companySqlConnectionPool.get(companyId).isValid(0))) {

					Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection(sqlUrl, sqlUserName, sqlPassword);
					companySqlConnectionPool.put(companyId, conn);
					org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).debug(
							"Successfully connected to company db with companyId : " + companyId);
				}
			} catch (SQLException | ClassNotFoundException e) {
				org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).error(
						"An error occurred while connecting to the sql db for companyId : " + companyId, e);
			}

		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(DatabaseConnectionHelper.class).error(
					"An error occurred while retrieving connection details for companyId : " + companyId, e);
		}
	}

}

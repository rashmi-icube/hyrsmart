package org.icube.hyrsmart.java.login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;
import org.icube.hyrsmart.java.helper.UtilHelper;
import org.icube.hyrsmart.java.user.User;

public class LoginHelper {

	/**
	 * Validates user name and password for login page
	 * @param emailId - email id of the user
	 * @param password - password of the user
	 * @param ipAddress - ip address of the machine from where the user logs in
	 * @return User object
	 * @throws Exception - thrown when provided with invalid credentials
	 */
	public User login(String emailId, String password, String ipAddress) throws Exception {
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		Connection companySqlCon = null;
		int index = emailId.indexOf('@');
		String companyDomain = emailId.substring(index + 1);
		int companyId = 0;
		User u = new User();
		try {
			CallableStatement cstmt = dch.masterCon.prepareCall("{call getCompanyDb(?)}");
			cstmt.setString(1, companyDomain);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				companyId = rs.getInt("comp_id");
				dch.getCompanyConnection(companyId);
				companySqlCon = dch.companySqlConnectionPool.get(companyId);
			}
			CallableStatement cstmt1 = companySqlCon.prepareCall("{call verifyLogin(?,?,?,?)}");
			cstmt1.setString("emailid", emailId);
			cstmt1.setString("pass", password);
			cstmt1.setTimestamp("curr_time", UtilHelper.convertJavaDateToSqlTimestamp(Date.from(Instant.now())));
			cstmt1.setString("ip", ipAddress);
			ResultSet res = cstmt1.executeQuery();
			while (res.next()) {
				if (res.getInt("emp_id") == 0) {
					org.apache.log4j.Logger.getLogger(LoginHelper.class).error("Invalid username/password");
					throw new Exception("Invalid credentials!!!");
				} else {
					u.setCompanyId(companyId);
					u.setUserId(res.getInt("user_id"));
					u.setDisplayName(res.getString("display_name"));
					u.setRoleId(res.getInt("role_id"));
					org.apache.log4j.Logger.getLogger(LoginHelper.class).debug("Successfully validated user with email ID : " + emailId);
				}
			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(LoginHelper.class).error("Exception while retrieving the company database", e);
		}
		return u;
	}

}

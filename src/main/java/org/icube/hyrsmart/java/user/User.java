package org.icube.hyrsmart.java.user;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;

public class User {

	private int companyId;
	private int userId;
	private int roleId;
	private String displayName;

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Retrieve user details of the userId provided
	 * @param companyId - company ID for db connection
	 * @param userId - userId for the user details
	 * @return user object
	 */
	public User getUser(int companyId, int userId) {
		User u = new User();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getUser(?)}");
			cstmt.setInt("userid", userId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				u.setUserId(userId);
				u.setCompanyId(companyId);
				u.setDisplayName(rs.getString("display_name"));
				u.setRoleId(rs.getInt("role_id"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(User.class).error("Exception while retrieving user details with user ID : " + userId, e);
		}
		return u;
	}

}

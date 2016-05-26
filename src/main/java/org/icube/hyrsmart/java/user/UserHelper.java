package org.icube.hyrsmart.java.user;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;
import org.icube.hyrsmart.java.test.SummaryHelper;

public class UserHelper {

	/**
	 * Get the sub admin list for the super admin
	 * @param companyId - company ID for database connection
	 * @param userId - user ID of the super admin
	 * @return list of users who have the role of sub admin
	 */
	public List<User> getSubAdminList(int companyId, int userId) {
		List<User> userList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getSubAdminList(?)}");
			cstmt.setInt("userid", userId);
			ResultSet rs = cstmt.executeQuery();

			while (rs.next()) {
				User u = new User();
				u.setCompanyId(companyId);
				u.setUserId(rs.getInt("user_id"));
				u.setDisplayName(rs.getString("display_name"));
				u.setRoleId(rs.getInt("role_id"));
				userList.add(u);
			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(SummaryHelper.class).error("Exception while retrieving sub admin list for userId : " + userId, e);
		}
		return userList;
	}
}

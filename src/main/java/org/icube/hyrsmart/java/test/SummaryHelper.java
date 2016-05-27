package org.icube.hyrsmart.java.test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;
import org.icube.hyrsmart.java.helper.UtilHelper;

public class SummaryHelper {

	/**
	 * Retrieve the test count list for every user - visible only to the super admin
	 * @param companyId - company ID for db connection
	 * @param userId - super admin user ID to get sub admins
	 * @return list of test count details
	 */
	public List<TestCount> getTestCountList(int companyId, int userId) {
		List<TestCount> result = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getTestCountList(?)}");
			cstmt.setInt("userid", userId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				TestCount tc = new TestCount();
				tc.setUserId(rs.getInt("user_id"));
				tc.setAllowed(rs.getInt("allowed"));
				tc.setBalance(rs.getInt("balance"));
				tc.setCompleted(rs.getInt("completed"));
				tc.setExited(rs.getInt("exited"));
				tc.setLive(rs.getInt("live"));
				result.add(tc);
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(SummaryHelper.class).error("Exception while retrieving test count list", e);
		}

		return result;
	}

	/**
	 * Retrieves the total count for the super admin to view
	 * @param companyId - company ID for db connection
	 * @param userId - super admin user ID to get sub admins
	 * @return TestCount object for the super admin with the total count
	 */
	public TestCount getTotalTestListCount(int companyId, int userId) {
		TestCount tc = new TestCount();
		List<TestCount> tcList = getTestCountList(companyId, userId);
		for (int i = 0; i < tcList.size(); i++) {
			TestCount t = tcList.get(i);
			tc.setAllowed(tc.getAllowed() + t.getAllowed());
			tc.setBalance(tc.getBalance() + t.getBalance());
			tc.setCompleted(tc.getCompleted() + t.getCompleted());
			tc.setExited(tc.getExited() + t.getExited());
			tc.setLive(tc.getLive() + t.getLive());
		}
		return tc;
	}

	/**
	 * Retrieves the list of tests for the filtered search
	 * 
	 * @param companyId - company ID for db connection
	 * @param userId - user ID of the logged in user
	 * @param candidateName - candidate name for the filter
	 * @param fromDate - tests can start on/after this date
	 * @param toDate - tests that fall within this date
	 * @param status - accepted / rejected
	 * @param pageNumber - for pagination
	 * @param pageSize - for pagination
	 * @param sortBy - sort by date / score
	 * @param orderBy - asc or desc order
	 * @return testInfo list
	 */
	public List<TestInfo> searchForTestDetails(int companyId, int userId, String candidateName, Date fromDate, Date toDate, TestStatus status,
			int pageNumber, int pageSize, SortBy sortBy, OrderBy orderBy) {
		List<TestInfo> testInfoList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			// TODO test for null values passed
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call searchForTestDetails(?,?,?,?,?)}");
			cstmt.setInt("userid", userId);
			cstmt.setString("candidate_name", candidateName);
			cstmt.setDate("from_date", UtilHelper.convertJavaDateToSqlDate(fromDate));
			cstmt.setDate("to_date", UtilHelper.convertJavaDateToSqlDate(toDate));
			cstmt.setString("teststatus", status.getValue());
			cstmt.setString("order_by_column", sortBy.getValue());
			cstmt.setString("direction", orderBy.getValue());
			cstmt.setInt("page_no", pageNumber);
			cstmt.setInt("page_size", pageSize);

			ResultSet rs = cstmt.executeQuery();
			List<TestInfo> result = new ArrayList<>();

			while (rs.next()) {
				TestInfo ti = new TestInfo();
				ti.setTestId(rs.getInt("test_id"));
				ti.setTestStatus(status);
				ti.setTestDate(rs.getDate("test_date"));
				ti.setCandidateFirstName(rs.getString("first_name"));
				ti.setCandidateMiddleName(rs.getString("middle_name"));
				ti.setCandidateLastName(rs.getString("last_name"));
				ti.setCandidateStream(rs.getString("stream"));
				ti.setCandidateMobile(rs.getString("mobile_no"));
				ti.setCandidateDegree(rs.getString("degree"));
				ti.setCandidateEmail(rs.getString("email_id"));
				ti.setScore(rs.getInt("test_score"));
				result.add(ti);
			}

			// pagination
			if (!result.isEmpty()) {
				Collections.sort(result, new Comparator<TestInfo>() {
					@Override
					public int compare(TestInfo obj1, TestInfo obj2) {
						return obj2.getTestDate().compareTo(obj1.getTestDate());
					}
				});

			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(SummaryHelper.class).error("Exception while retrieving test details list", e);
		}

		return testInfoList;
	}

	/**
	 * Get the count of result searched from the filter along with the number of pages where the result can be spanned through
	 * @param companyId - company ID for db connection
	 * @param userId - user ID of the logged in user
	 * @param candidateName - candidate name for the filter
	 * @param fromDate - tests can start on/after this date
	 * @param toDate - tests that fall within this date
	 * @param status - accepted / rejected
	 * @param pageSize - to return how many pages will be there to be shown
	 * @return count of the total results
	 */
	public Map<String, Integer> getTotalTestDetailsCountAndPages(int companyId, int userId, String candidateName, Date fromDate, Date toDate,
			TestStatus status, int pageSize) {
		Map<String, Integer> result = new HashMap<>();
		int testResultCount = 0;
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();

		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getTotalTestDetailsCount(?,?,?,?,?)}");
			cstmt.setInt("userid", userId);
			cstmt.setString("candidate_name", candidateName);
			cstmt.setDate("from_date", UtilHelper.convertJavaDateToSqlDate(fromDate));
			cstmt.setDate("to_date", UtilHelper.convertJavaDateToSqlDate(toDate));
			cstmt.setString("teststatus", status.getValue());
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				testResultCount = rs.getInt("test_result_count");
			}

			float fPages = testResultCount / Float.valueOf(pageSize);
			int pages = (int) Math.ceil(fPages);

			result.put("testResultCount", testResultCount);
			result.put("pages", pages);
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(SummaryHelper.class).error("Exception while retrieving test details list", e);
		}
		return result;
	}

}

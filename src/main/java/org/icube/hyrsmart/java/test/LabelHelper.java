package org.icube.hyrsmart.java.test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;

public class LabelHelper {
	/**
	 * Retrieve the labels in a specific language for the navigation in the test
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the labels
	 * @return map of navigation labels 
	 */
	public Map<String, String> getTestNavigationLabels(int companyId, int languageId) {
		Map<String, String> testNavigationLabelMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getTestNavigationLabels(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				testNavigationLabelMap.put(rs.getString("key"), rs.getString("value"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(LabelHelper.class).error("Exception while retrieving the navigation labels", e);
		}
		return testNavigationLabelMap;
	}

	/**
	 * Retrieve the labels in a specific language for the previous next labels in the test
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the labels
	 * @return map of previous and next labels 
	 */
	public Map<String, String> getPreviousNextLabelMap(int companyId, int languageId) {

		Map<String, String> previousNextLabelMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getPreviousNextLabels(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				previousNextLabelMap.put(rs.getString("key"), rs.getString("value"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(LabelHelper.class).error("Exception while retrieving the previous next labels", e);
		}
		return previousNextLabelMap;

	}

	/**
	 * Get the labels for the personal details in the language selected by the user
	 * @param companyId - company ID for db connection
	 * @param languageId - language that the labels need to be in
	 * @return labels map in the selected language
	 */
	public Map<String, String> getPersonalDetailsLabelMap(int companyId, int languageId) {
		Map<String, String> labelMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getPersonalDetailsLabelMap(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				labelMap.put(rs.getString("key"), rs.getString("value"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the personal details label map", e);
		}
		return labelMap;
	}
}

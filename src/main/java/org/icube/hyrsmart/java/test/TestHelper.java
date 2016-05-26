package org.icube.hyrsmart.java.test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;
import org.icube.hyrsmart.java.helper.UtilHelper;

public class TestHelper {

	/**
	 * Retrieve the language master for the test
	 * @param companyId - company ID for db connection
	 * @return language master map
	 */
	public Map<Integer, String> getLanguageMaster(int companyId) {
		Map<Integer, String> languageMasterMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getLanguageList()}");
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				languageMasterMap.put(rs.getInt("l_id"), rs.getString("language"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the language master map", e);
		}

		return languageMasterMap;
	}

	/**
	 * Retrieve the city master for the test
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the city list to be retrieved in
	 * @return city master map
	 */
	public Map<Integer, String> getCityMaster(int companyId, int languageId) {
		Map<Integer, String> cityMasterMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getCityList(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				cityMasterMap.put(rs.getInt("city_id"), rs.getString("city"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the city master map", e);
		}
		return cityMasterMap;
	}

	/**
	 * Get the user agreement for the test
	 * @param companyId - company ID for db connection
	 * @param languageId - language of the test giver
	 * @return user agreement string in html format
	 */
	public String getUserAgreement(int companyId, int languageId) {
		String userAgreement = "";
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getUserAgreement(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				userAgreement = rs.getString("value");
			}
			if (userAgreement.isEmpty()) {
				throw new NullPointerException("User agreement is empty for company ID : " + companyId + " and language ID : " + languageId);
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while retrieving the user agreement for language ID : " + languageId, e);
		}
		return userAgreement;
	}

	/**
	 * Insert the personal details of the candidate
	 * 
	 * @param companyId - company ID for db connection
	 * @param userId - unique user ID for the candidate 
	 * @param firstName - first name of the candidate
	 * @param middleName - middle name of the candidate
	 * @param lastName - last name of the candidate
	 * @param emailId - email address of the candidate
	 * @param mobileNo - mobile number of the candidate
	 * @param cityId - cityId of the candidate
	 * @param aadharNo - aadhar number of the candidate
	 * @param degree - degree of the candidate
	 * @param stream - stream of the candidate
	 * @return test info object with the test ID and candidate name for displaying purpose
	 */
	public TestInfo insertPersonalDetails(int companyId, int userId, String firstName, String middleName, String lastName, String emailId,
			String mobileNo, int cityId, String aadharNo, String degree, String stream) {
		TestInfo ti = new TestInfo();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call insertPersonalInfo(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString("firstname", firstName);
			cstmt.setString("middlename", middleName);
			cstmt.setString("lastname", lastName);
			cstmt.setString("emailid", emailId);
			cstmt.setString("mobileno", mobileNo);
			cstmt.setInt("cityid", cityId);
			cstmt.setString("aadharno", aadharNo);
			cstmt.setString("degreeip", degree);
			cstmt.setString("streamip", stream);
			cstmt.setInt("userid", userId);
			cstmt.setTimestamp("testDate", UtilHelper.convertJavaDateToSqlTimestamp(Date.from(Instant.now())));
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				ti.setTestId(rs.getInt("test_id"));
				ti.setCandidateFirstName(rs.getString("first_name"));
				ti.setCandidateMiddleName(rs.getString("middle_name"));
				ti.setCandidateLastName(rs.getString("last_name"));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while storing the personal details", e);
		}
		return ti;
	}

	/**
	 * Retrieve the test instructions 
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the instructions
	 * @param testId - test ID for the instructions
	 * @return test instructions string in html format
	 */
	public String getTestInstructions(int companyId, int languageId, int testId) {
		String testInstruction = "";
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getTestInstructions(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			Map<String, String> keyValuePairMap = new HashMap<>();
			while (rs.next()) {
				keyValuePairMap.put(rs.getString("key"), rs.getString("value"));
			}
			testInstruction = String.format(keyValuePairMap.get("test_direction"), testId, keyValuePairMap.get("total_questions"), keyValuePairMap
					.get("total_test_time"));
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the test instructions for test ID : " + testId, e);
		}
		return testInstruction;
	}

	/**
	 * Get the question ID list for the given test ID 
	 * @param companyId - company ID for db connection
	 * @param testId - test ID 
	 * @param languageId - language in which the questions are to be returned
	 * @return question ID list
	 */
	public List<Integer> getQuestionIdList(int companyId, int languageId, int testId) {
		List<Integer> qIdList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getQuestionList(?)}");
			cstmt.setInt("testid", testId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				qIdList.add(rs.getInt("ques_id"));
			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while retrieving the question list for test ID : " + testId, e);
		}
		return qIdList;
	}

	/**
	 * Get all the question details for the given question ID
	 * @param companyId - company ID for db connection
	 * @param languageId - language for the question
	 * @param questionId - question ID of the question
	 * @return question object
	 */
	public Question getQuestionDetails(int companyId, int languageId, int questionId) {
		Question q = new Question();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("call getQuestionDetails(?,?)");
			cstmt.setInt("lang_id", languageId);
			cstmt.setInt("quesid", questionId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				q.setQuestionId(questionId);
				q.setQuestionText(rs.getString("text"));
				q.setQuestionImageId(companyId + "_" + questionId);
				q.setSubQuestionList(getSubQuestionList(companyId, languageId, questionId));
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while retrieving the question details for question ID : " + questionId, e);
		}
		return q;
	}

	/**
	 * Get the sub question list for the specified question ID
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the question to be displayed
	 * @param questionId - question ID for the sub questions
	 * @return list of sub questions
	 */
	private List<SubQuestion> getSubQuestionList(int companyId, int languageId, int questionId) {
		List<SubQuestion> subQuestionList = new ArrayList<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getSubQuestionList(?,?)}");
			cstmt.setInt("lang_id", languageId);
			cstmt.setInt("quesid", questionId);
			ResultSet rs = cstmt.executeQuery();
			Map<Integer, List<Option>> subQuestionOptionListMap = getSubQuestionOptionListMap(companyId, languageId, questionId);
			while (rs.next()) {
				int subQuesId = rs.getInt("sub_ques_id");
				SubQuestion sq = new SubQuestion();
				sq.setQuestionId(questionId);
				sq.setSubQuestionId(subQuesId);
				sq.setText(rs.getString("text"));
				sq.setOptionList(subQuestionOptionListMap.get(subQuesId));
				org.apache.log4j.Logger.getLogger(TestHelper.class).debug("Retrieved sub question with ID " + subQuesId);
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while retrieving the sub question list for question ID : " + questionId, e);
		}
		return subQuestionList;
	}

	/**
	 * Get the options list for the sub question ID provided
	 * @param companyId - company ID for db connection
	 * @param languageId - language ID for the option to be displayed in
	 * @param subQuestionId - sub question ID for which the options need to be retrieved
	 * @return list of options for the sub question
	 */
	private Map<Integer, List<Option>> getSubQuestionOptionListMap(int companyId, int languageId, int questionId) {
		Map<Integer, List<Option>> subQuestionOptionListMap = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getOptionList(?,?)}");
			cstmt.setInt("lang_id", languageId);
			cstmt.setInt("quesid", questionId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				Option o = new Option();
				o.setOptionId(rs.getInt("option_id"));
				o.setQuestionId(rs.getInt("ques_id"));
				int subQuestionId = rs.getInt("sub_ques_id");
				o.setSubQuestionId(subQuestionId);
				o.setText(rs.getString("text"));
				org.apache.log4j.Logger.getLogger(TestHelper.class).debug("Retrieved option with ID " + rs.getInt("opt_id"));
				List<Option> subQuestionOptionList = subQuestionOptionListMap.get(subQuestionId) == null ? new ArrayList<>()
						: subQuestionOptionListMap.get(subQuestionId);
				subQuestionOptionList.add(o);
				subQuestionOptionListMap.put(subQuestionId, subQuestionOptionList);
			}

		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class)
					.error("Exception while retrieving the option list for question ID : " + questionId, e);
		}
		return subQuestionOptionListMap;
	}

	/**
	 * Updates the test status of the given test ID
	 * Should be called from the UI only once all the responses are successfully saved
	 * This will also do the result calculation once the test status has been successfully updated
	 * @param companyId - company ID for db connection
	 * @param testId - test ID for which the status needs to be updated
	 * @param status - status to be updated
	 * @return boolean value if the status was successfully updated
	 */
	public boolean updateTestStatus(int companyId, int testId, TestStatus status) {
		boolean statusUpdated = false;
		boolean resultUpdated = false;
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call updateTestStatus(?,?)}");
			cstmt.setInt("testid", testId);
			cstmt.setString("status", status.getValue());
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				statusUpdated = rs.getBoolean("status_updated");
			}

			if (statusUpdated) {
				CallableStatement cstmt1 = dch.companySqlConnectionPool.get(companyId).prepareCall("{call updateResult(?)}");
				cstmt1.setInt("testid", testId);
				ResultSet rs1 = cstmt.executeQuery();
				while (rs.next()) {
					resultUpdated = rs1.getBoolean("result_updated");
				}
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error("Exception while updating the status for test ID : " + testId, e);
		}
		return (statusUpdated || resultUpdated);
	}

	/**
	 * Saves the response for the test as a whole
	 * @param companyId - company ID for db connection
	 * @param responseList - response list for the whole test
	 * @return boolean value if the responses were saved or not
	 */
	public boolean saveResponse(int companyId, List<Response> responseList) {
		boolean responseSaved = false;
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		int subQuestionId = 0;
		try {
			dch.getCompanyConnection(companyId);

			for (Response r : responseList) {
				if (r.getOptionId() > 0) {
					subQuestionId = r.getSubQuestionId();
					CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call saveResponse(?,?,?,?)}");
					cstmt.setInt("testid", r.getTestId());
					cstmt.setInt("quesid", r.getQuestionId());
					cstmt.setInt("subquesid", r.getSubQuestionId());
					cstmt.setInt("optionid", r.getOptionId());
					ResultSet rs = cstmt.executeQuery();
					while (rs.next()) {
						responseSaved = rs.getBoolean("response_saved");
					}
				}
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class).error(
					"Exception while updating the responses for sub question ID : " + subQuestionId, e);
		}

		return responseSaved;
	}

	/**
	 * Retrieves the thank you text to be shown once the user has successfully completed the test
	 * @param companyId - company ID for db connection
	 * @param languageId - language in which the thank you text needs to be retrieved in
	 * @return thank you text in html format
	 */
	public String getThankYouText(int companyId, int languageId) {
		String thankYouText = "";
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		try {
			dch.getCompanyConnection(companyId);
			CallableStatement cstmt = dch.companySqlConnectionPool.get(companyId).prepareCall("{call getThankYouText(?)}");
			cstmt.setInt("lang_id", languageId);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				thankYouText = rs.getString("value");
			}
			if (thankYouText.isEmpty()) {
				throw new NullPointerException("Thank you is empty for company ID : " + companyId + " and language ID : " + languageId);
			}
		} catch (SQLException e) {
			org.apache.log4j.Logger.getLogger(TestHelper.class)
					.error("Exception while retrieving the thank you text for language ID" + languageId, e);
		}

		return thankYouText;
	}
}

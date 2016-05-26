package org.icube.hyrsmart.java.test;

import java.util.Date;

public class TestInfo {

	private int testId;
	private Date testDate;
	private TestStatus testStatus;
	private int score;
	private String candidateFirstName;
	private String candidateMiddleName;
	private String candidateLastName;
	private String candidateMobile;
	private String candidateEmail;
	private String candidateDegree;
	private String candidateStream;
	private String candidateAadhar;
	private int cityId;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public TestStatus getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(TestStatus testStatus) {
		this.testStatus = testStatus;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getCandidateFirstName() {
		return candidateFirstName;
	}

	public void setCandidateFirstName(String candidateFirstName) {
		this.candidateFirstName = candidateFirstName;
	}

	public String getCandidateMiddleName() {
		return candidateMiddleName;
	}

	public void setCandidateMiddleName(String candidateMiddleName) {
		this.candidateMiddleName = candidateMiddleName;
	}

	public String getCandidateLastName() {
		return candidateLastName;
	}

	public void setCandidateLastName(String candidateLastName) {
		this.candidateLastName = candidateLastName;
	}

	public String getCandidateMobile() {
		return candidateMobile;
	}

	public void setCandidateMobile(String candidateMobile) {
		this.candidateMobile = candidateMobile;
	}

	public String getCandidateEmail() {
		return candidateEmail;
	}

	public void setCandidateEmail(String candidateEmail) {
		this.candidateEmail = candidateEmail;
	}

	public String getCandidateDegree() {
		return candidateDegree;
	}

	public void setCandidateDegree(String candidateDegree) {
		this.candidateDegree = candidateDegree;
	}

	public String getCandidateStream() {
		return candidateStream;
	}

	public void setCandidateStream(String candidateStream) {
		this.candidateStream = candidateStream;
	}

	public String getCandidateAadhar() {
		return candidateAadhar;
	}

	public void setCandidateAadhar(String candidateAadhar) {
		this.candidateAadhar = candidateAadhar;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
}

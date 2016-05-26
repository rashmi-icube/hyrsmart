package org.icube.hyrsmart.java.test;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.icube.hyrsmart.java.database.DatabaseConnectionHelper;

public class Question {
	private int questionId;
	private String questionText;
	private String questionImageId;
	private List<SubQuestion> subQuestionList;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionImageId() {
		return questionImageId;
	}

	public void setQuestionImageId(String questionImageId) {
		this.questionImageId = questionImageId;
	}

	public List<SubQuestion> getSubQuestionList() {
		return subQuestionList;
	}

	public void setSubQuestionList(List<SubQuestion> subQuestionList) {
		this.subQuestionList = subQuestionList;
	}

	/**
	 * Get image for question
	 * @param companyId - company ID for db connection
	 * @param questionId - question ID for the image to be retrieved
	 * @return question image 
	 */
	public Image getImage(int companyId, int questionId) {
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getInstance();
		dch.getCompanyConnection(companyId);
		String imagePath = dch.companyImagePath.get(companyId);
		Image image = null;

		try {
			String filePath = imagePath + companyId + "_" + questionId + ".jpg";
			org.apache.log4j.Logger.getLogger(Question.class).debug("Path for retrieving the image for questionId : " + filePath);
			File sourceimage = new File(filePath);
			image = ImageIO.read(sourceimage);
			org.apache.log4j.Logger.getLogger(Question.class).debug("Successfully read the image for questionId " + questionId);
		} catch (IOException e) {
			org.apache.log4j.Logger.getLogger(Question.class).error("Exception while retrieving question image with questionId : " + questionId, e);
			return image;
		}

		return image;
	}
}

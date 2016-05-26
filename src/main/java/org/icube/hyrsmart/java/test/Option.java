package org.icube.hyrsmart.java.test;

public class Option {
	private int optionId;
	private int subQuestionId;
	private int questionId;
	private String text;

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public int getSubQuestionId() {
		return subQuestionId;
	}

	public void setSubQuestionId(int subQuestionId) {
		this.subQuestionId = subQuestionId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

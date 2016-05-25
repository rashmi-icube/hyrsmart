package org.icube.hyrsmart.java.test;

import java.util.List;

public class SubQuestion {

	private int subQuestionId;
	private int questionId;
	private String text;
	private List<Option> optionList;

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

	public List<Option> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<Option> optionList) {
		this.optionList = optionList;
	}
}

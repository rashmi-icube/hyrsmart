package org.icube.hyrsmart.java.test;

import java.util.HashMap;
import java.util.Map;

public enum SortBy {

	DATE("test_date"), SCORE("test_score");

	// Reverse-lookup map for getting a frequency from frequency ID
	private static final Map<String, SortBy> lookup = new HashMap<String, SortBy>();

	static {
		for (SortBy d : SortBy.values()) {
			lookup.put(d.getValue(), d);
		}
	}

	private String value;

	private SortBy(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static SortBy get(String value) {
		return lookup.get(value);
	}

}

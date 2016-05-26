package org.icube.hyrsmart.java.test;

import java.util.HashMap;
import java.util.Map;

public enum OrderBy {

	ASCENDING("asc"), DESCENDING("desc");

	// Reverse-lookup map for getting a frequency from frequency ID
	private static final Map<String, OrderBy> lookup = new HashMap<String, OrderBy>();

	static {
		for (OrderBy d : OrderBy.values()) {
			lookup.put(d.getValue(), d);
		}
	}

	private String value;

	private OrderBy(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static OrderBy get(String value) {
		return lookup.get(value);
	}

}

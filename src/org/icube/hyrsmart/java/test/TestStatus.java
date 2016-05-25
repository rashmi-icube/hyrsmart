package org.icube.hyrsmart.java.test;

import java.util.HashMap;
import java.util.Map;

public enum TestStatus {

	COMPLETED("Completed"), EXITED("Exited");

	// Reverse-lookup map for getting a frequency from frequency ID
	private static final Map<String, TestStatus> lookup = new HashMap<String, TestStatus>();

	static {
		for (TestStatus d : TestStatus.values()) {
			lookup.put(d.getValue(), d);
		}
	}

	private String value;

	private TestStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static TestStatus get(String value) {
		return lookup.get(value);
	}

}

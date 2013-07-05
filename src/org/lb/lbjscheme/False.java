package org.lb.lbjscheme;

public class False implements SchemeObject {
	private False() {
	}

	private static final False _instance = new False();

	public static False getInstance() {
		return _instance;
	}

	@Override
	public String toString() {
		return "#f";
	}
}

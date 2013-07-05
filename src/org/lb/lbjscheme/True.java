package org.lb.lbjscheme;

public class True implements SchemeObject {
	private True() {
	}

	private static final True _instance = new True();

	public static True getInstance() {
		return _instance;
	}

	@Override
	public String toString() {
		return "#t";
	}
}

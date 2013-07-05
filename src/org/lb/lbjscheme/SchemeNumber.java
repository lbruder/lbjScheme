package org.lb.lbjscheme;

public final class SchemeNumber implements SchemeObject {
	private final int _value;

	public SchemeNumber(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	@Override
	public String toString() {
		return Integer.toString(_value);
	}
}

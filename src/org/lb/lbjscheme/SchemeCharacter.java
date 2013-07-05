package org.lb.lbjscheme;

public final class SchemeCharacter implements SchemeObject {
	private final char _value;

	public SchemeCharacter(char value) {
		_value = value;
	}

	public char getValue() {
		return _value;
	}

	@Override
	public String toString() {
		switch (_value) {
		case '\\':
			return "\\\\";
		case '\n':
			return "#\\newline";
		case '\r':
			return "#\\cr";
		case '\t':
			return "#\\tab";
		default:
			return String.valueOf(_value);
		}
	}
}

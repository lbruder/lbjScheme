package org.lb.lbjscheme;

public final class SchemeString implements SchemeObject {
	private final char[] _characters;

	public SchemeString(String value) {
		_characters = value.toCharArray();
	}

	public SchemeString(int length) {
		_characters = new char[length];
	}

	public String getValue() {
		return String.valueOf(_characters);
	}

	public int getLength() {
		return _characters.length;
	}

	public char getAt(int position) {
		return _characters[position];
	}

	public void setAt(int position, char value) {
		_characters[position] = value;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("\"");
		for (char c : _characters) {
			switch (c) {
			case '\\':
				ret.append("\\\\");
				break;
			case '\n':
				ret.append("\\n");
				break;
			case '\r':
				ret.append("\\r");
				break;
			case '\t':
				ret.append("\\t");
				break;
			default:
				ret.append(c);
				break;
			}
		}
		ret.append("\"");
		return ret.toString();
	}
}

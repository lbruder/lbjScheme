// lbjScheme
// An experimental Scheme subset interpreter in Java, based on SchemeNet.cs
// Copyright (c) 2013, Leif Bruder <leifbruder@gmail.com>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

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

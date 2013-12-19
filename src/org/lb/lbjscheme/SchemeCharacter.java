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
		return toString(false);
	}

	@Override
	public String toString(boolean forDisplay) {
		if (forDisplay) return String.valueOf(_value);

		switch (_value) {
		case '\\':
			return "#\\\\";
		case ' ':
			return "#\\space";
		case '\n':
			return "#\\newline";
		case '\r':
			return "#\\cr";
		case '\t':
			return "#\\tab";
		default:
			return "#\\" + String.valueOf(_value);
		}
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		return _value;
	}
}

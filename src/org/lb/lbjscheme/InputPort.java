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

import java.io.IOException;

public final class InputPort implements SchemeObject {
	private final java.io.Reader _reader;
	private int _nextChar = -2;

	public InputPort(java.io.Reader reader) {
		_reader = reader;
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean forDisplay) {
		return "<input-port>";
	}

	public int peekChar() throws SchemeException {
		if (_nextChar == -2) _nextChar = readChar();
		return _nextChar;
	}

	public int readChar() throws SchemeException {
		int ret = _nextChar;
		if (ret != -2) {
			_nextChar = -2;
			return ret;
		}

		try {
			return _reader.read();
		} catch (IOException e) {
			throw new SchemeException("Error reading from input port: "
					+ e.getMessage());
		}
	}

	public boolean isCharReady() {
		return _nextChar != -2;
	}

	public void close() throws SchemeException {
		try {
			_reader.close();
		} catch (IOException e) {
			throw new SchemeException("Error closing input port: "
					+ e.getMessage());
		}
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		throw new SchemeException(
				"Input port cannot be converted into a plain Java object");
	}
}

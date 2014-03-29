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

import java.io.*;

public final class OutputPort extends SchemeObject {
	private final Writer _writer;

	public OutputPort(Writer writer) {
		_writer = writer;
	}

	public boolean isOutputPort() {
		return true;
	}

	@Override
	public String toString(boolean forDisplay) {
		return "<output-port>";
	}

	public void write(String string) throws SchemeException {
		try {
			_writer.write(string);
			_writer.flush();
		} catch (IOException e) {
			throw new SchemeException("Error writing to output port: "
					+ e.getMessage());
		}
	}

	public void close() throws SchemeException {
		try {
			_writer.close();
		} catch (IOException e) {
			throw new SchemeException("Error closing output port: "
					+ e.getMessage());
		}
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		throw new SchemeException(
				"Output port cannot be converted into a plain Java object");
	}
}

package org.lb.lbjscheme;

import java.io.*;

public class InputPort implements SchemeObject {
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
		if (_nextChar == -2)
			_nextChar = readChar();
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
}

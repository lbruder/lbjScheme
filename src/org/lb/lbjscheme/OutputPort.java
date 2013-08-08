package org.lb.lbjscheme;

import java.io.*;

public class OutputPort implements SchemeObject {
	private final Writer _writer;

	public OutputPort(Writer writer) {
		_writer = writer;
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean forDisplay) {
		return "<output-port>";
	}

	public void write(String string) throws SchemeException {
		try {
			_writer.write(string);
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
}

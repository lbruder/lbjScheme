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

public final class Reader {
	private static final Symbol _dot = Symbol.fromString(".");
	private static final Symbol _listEnd = Symbol.fromString(")");

	private final InputPort _input;
	private int _nextChar = -2;

	public Reader(InputPort input) {
		_input = input;
	}

	public SchemeObject read() throws SchemeException, EOFException {
		skipWhitespace();
		if (isEof())
			throw new EOFException();

		switch (peekChar()) {
		case ';':
			skipComment();
			return read();
		case '\'':
			readChar();
			return new Pair(Symbol.fromString("quote"), new Pair(read(),
					Nil.getInstance()));
		case '`':
			readChar();
			return new Pair(Symbol.fromString("quasiquote"), new Pair(read(),
					Nil.getInstance()));
		case ',':
			readChar();
			return new Pair(Symbol.fromString("unquote"), new Pair(read(),
					Nil.getInstance()));
		case '(':
			return readList();
		case '"':
			return readString();
		case '#':
			return readSpecial();
		default:
			return readSymbolOrNumber("");
		}
	}

	private void skipWhitespace() throws SchemeException {
		while (!isEof() && Character.isWhitespace(peekChar()))
			readChar();
	}

	private void skipComment() throws SchemeException {
		while (!isEof() && peekChar() != '\n')
			readChar();
	}

	private boolean isEof() throws SchemeException {
		ensureAtLeastOneCharRead();
		return _nextChar == -1;
	}

	private void ensureAtLeastOneCharRead() throws SchemeException {
		if (_nextChar == -2)
			_nextChar = _input.readChar();
	}

	private void assertNotEof() throws SchemeException {
		if (isEof())
			throw new SchemeException("Unexpected end of stream");
	};

	private char peekChar() throws SchemeException {
		assertNotEof();
		return (char) _nextChar;
	}

	private char readChar() throws SchemeException {
		assertNotEof();
		final char ret = (char) _nextChar;
		_nextChar = _input.readChar();
		return ret;
	}

	private SchemeObject readList() throws SchemeException, EOFException {
		readChar(); // Opening parenthesis
		Pair ret = null;
		Pair current = null;
		while (true) {
			final SchemeObject o = read();
			if (o == _listEnd)
				return (ret == null) ? Nil.getInstance() : ret; // )
			if (o == _dot) {
				if (current == null)
					throw new SchemeException("Invalid dotted list");
				current.setCdr(read());
				if (read() != _listEnd)
					throw new SchemeException("Invalid dotted list");
				return ret;
			}

			final Pair newPair = new Pair(o, Nil.getInstance());
			if (current == null) {
				ret = current = newPair;
			} else {
				current.setCdr(newPair);
				current = newPair;
			}
		}
	}

	private SchemeObject readString() throws SchemeException {
		readChar(); // Opening quote
		final StringBuilder sb = new StringBuilder();
		while (peekChar() != '"') {
			char c = readChar();
			if (c == '\\') {
				c = readChar();
				if (c == 'n')
					c = '\n';
				if (c == 'r')
					c = '\r';
				if (c == 't')
					c = '\t';
			}
			sb.append(c);
		}
		readChar(); // Closing quote
		return new SchemeString(sb.toString());
	}

	private SchemeObject readSpecial() throws SchemeException, EOFException {
		readChar(); // #
		if (peekChar() == '(')
			return new Vector((SchemeList) readList());
		if (peekChar() != '\\')
			return readSymbolOrNumber("#");
		readChar();
		return readCharacter();
	}

	private SchemeObject readCharacter() throws SchemeException {
		final char c = readChar();
		if (!Character.isLetter(c))
			return new SchemeCharacter(c);

		final StringBuilder sb = new StringBuilder();
		sb.append(c);
		while (!isEof() && peekChar() != ')'
				&& !Character.isWhitespace(peekChar()))
			sb.append(readChar());
		final String name = sb.toString();
		switch (name) {
		case "cr":
			return new SchemeCharacter('\r');
		case "newline":
			return new SchemeCharacter('\n');
		case "space":
			return new SchemeCharacter(' ');
		case "tab":
			return new SchemeCharacter('\t');
		default:
			if (name.length() == 1)
				return new SchemeCharacter(name.charAt(0));
			throw new SchemeException("Invalid character name: \\" + name);
		}
	}

	private SchemeObject readSymbolOrNumber(String init) throws SchemeException {
		if (init == "" && peekChar() == ')') {
			readChar();
			return _listEnd;
		}

		final StringBuilder sb = new StringBuilder();
		sb.append(init);

		while (!isEof() && peekChar() != ')'
				&& !Character.isWhitespace(peekChar()))
			sb.append(readChar());
		final String symbol = sb.toString();

		if (symbol.equals("#t"))
			return True.getInstance();
		if (symbol.equals("#f"))
			return False.getInstance();

		try {
			return SchemeNumber.fromString(symbol, 10);
		} catch (Exception ex) {
		}

		if (symbol.startsWith("#x"))
			return SchemeNumber.fromString(symbol.substring(2), 16);
		return Symbol.fromString(symbol);
	}
}

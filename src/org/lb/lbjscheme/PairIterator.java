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

import java.util.Iterator;

final class PairIterator implements Iterator<SchemeObject> {
	private SchemeObject _current;

	public PairIterator(SchemeObject current) {
		_current = current;
	}

	@Override
	public boolean hasNext() {
		return !(_current instanceof Nil);
	}

	@Override
	public SchemeObject next() {
		if (_current instanceof Nil) throw new UnsupportedOperationException();
		if (_current instanceof Pair) {
			final SchemeObject ret = ((Pair) _current).getCar();
			_current = ((Pair) _current).getCdr();
			return ret;
		}
		final SchemeObject ret = _current;
		_current = Nil.getInstance();
		return ret;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}

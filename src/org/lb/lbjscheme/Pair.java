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

import java.util.*;

public final class Pair implements SchemeList {
	private SchemeObject _car;
	private SchemeObject _cdr;

	public Pair(SchemeObject car, SchemeObject cdr) {
		_car = car;
		_cdr = cdr;
	}

	public SchemeObject getCar() {
		return _car;
	}

	public void setCar(SchemeObject value) {
		_car = value;
	}

	public SchemeObject getCdr() {
		return _cdr;
	}

	public void setCdr(SchemeObject value) {
		_cdr = value;
	}

	public static SchemeList fromIterable(Iterable<SchemeObject> values) {
		Pair ret = null;
		Pair current = null;

		for (SchemeObject o : values) {
			final Pair newPair = new Pair(o, Nil.getInstance());
			if (current == null) {
				ret = current = newPair;
			} else {
				current._cdr = newPair;
				current = newPair;
			}
		}
		return (ret == null) ? Nil.getInstance() : ret;
	}

	@Override
	public boolean isDottedList() {
		Pair i = this;
		while (true) {
			if (i._cdr instanceof Nil) return false;
			if (!(i._cdr instanceof Pair)) return true;
			i = (Pair) i._cdr;
		}
	}

	@Override
	public List<SchemeObject> toJavaList() {
		final ArrayList<SchemeObject> ret = new ArrayList<SchemeObject>();
		for (SchemeObject o : this)
			ret.add(o);
		return ret;
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean forDisplay) {
		final StringBuilder ret = new StringBuilder();

		ret.append("(");

		Pair i = this;
		while (true) {
			ret.append(i._car.toString(forDisplay));
			if (i._cdr instanceof Nil) break;
			ret.append(" ");

			if (!(i._cdr instanceof Pair)) {
				ret.append(". ");
				ret.append(i._cdr.toString(forDisplay));
				break;
			}
			i = (Pair) i._cdr;
		}

		ret.append(")");
		return ret.toString();
	}

	@Override
	public Iterator<SchemeObject> iterator() {
		return new PairIterator(this);
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		final List<Object> ret = new ArrayList<>();
		for (SchemeObject o : this)
			ret.add(o.toJavaObject());
		return ret;
	}
}

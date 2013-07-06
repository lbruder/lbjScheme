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

public final class Builtins {
	public static SchemeObject cons(SchemeObject o1, SchemeObject o2) {
		return new Pair(o1, o2);
	}

	public static SchemeObject car(SchemeObject o) throws SchemeException {
		if (o instanceof Pair)
			return ((Pair) o).getCar();
		throw new SchemeException(
				"car: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	public static SchemeObject cdr(SchemeObject o) throws SchemeException {
		if (o instanceof Pair)
			return ((Pair) o).getCdr();
		throw new SchemeException(
				"car: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	public static SchemeObject setCar(SchemeObject o, SchemeObject newCar)
			throws SchemeException {
		if (o instanceof Pair) {
			((Pair) o).setCar(newCar);
			return newCar;
		}
		throw new SchemeException(
				"set-car!: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	public static SchemeObject setCdr(SchemeObject o, SchemeObject newCdr)
			throws SchemeException {
		if (o instanceof Pair) {
			((Pair) o).setCdr(newCdr);
			return newCdr;
		}
		throw new SchemeException(
				"set-cdr!: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}
}

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

import java.util.List;

public abstract class Builtin implements SchemeObject {

	protected static final True _true = True.getInstance();
	protected static final False _false = False.getInstance();
	protected static final Symbol _undefined = Symbol.fromString("undefined");

	public abstract String getName();

	public abstract SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException;

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(boolean forDisplay) {
		return "Builtin procedure " + getName();
	}

	protected void assertParameterCount(int expected,
			List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected != got)
			throw new SchemeException(getName()
					+ ": Invalid parameter count; expected: " + expected
					+ ", got: " + got);
	}

	protected void assertParameterCountMin(int expected,
			List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected > got)
			throw new SchemeException(getName()
					+ ": Invalid parameter count; expected at least: "
					+ expected + ", got: " + got);
	}

	protected void assertParameterCountMax(int expected,
			List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected < got)
			throw new SchemeException(getName()
					+ ": Invalid parameter count; expected no more than: "
					+ expected + ", got: " + got);
	}

	protected void assertParameterType(SchemeObject o,
			Class<? extends SchemeObject> expected) throws SchemeException {
		final Class<? extends SchemeObject> got = o.getClass();
		if (expected.isAssignableFrom(got))
			return;
		throw new SchemeException(getName()
				+ ": Invalid parameter type; expected: "
				+ expected.getSimpleName() + ", got: " + got.getSimpleName());
	}

	protected SchemeNumber getNumber(SchemeObject o) throws SchemeException {
		assertParameterType(o, SchemeNumber.class);
		return (SchemeNumber) o;
	}

	protected int getFixnum(SchemeObject o) throws SchemeException {
		assertParameterType(o, Fixnum.class);
		return ((Fixnum) o).getValue();
	}
}
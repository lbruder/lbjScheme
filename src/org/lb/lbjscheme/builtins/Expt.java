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

package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Expt extends Builtin {
	private final Fixnum _one = new Fixnum(1);

	@Override
	public String getName() {
		return "expt";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(2, parameters);
		SchemeNumber n1 = getNumber(parameters.get(0));
		SchemeNumber n2 = getNumber(parameters.get(1));
		if (n2.isZero())
			return n2.isExact() ? _one : new Real(1);
		if (n1.isZero() && n1.isExact() && n2.lt(new Fixnum(0)))
			throw new SchemeException("Division by zero");
		if (n1.isExact() && n1.eq(_one))
			return new Fixnum(1);
		if (n1.isExact() && n2.isExact()) {
			// TODO: Can we return an exact value?
		}
		while (!(n1 instanceof Real))
			n1 = n1.promoteToLevel(4);
		while (!(n2 instanceof Real))
			n2 = n2.promoteToLevel(4);
		return new Real(
				Math.pow(((Real) n1).getValue(), ((Real) n2).getValue()));
	}
}

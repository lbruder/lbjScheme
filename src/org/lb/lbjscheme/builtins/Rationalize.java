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

public final class Rationalize extends Builtin {
	private final SchemeNumber _zero = Fixnum.valueOf(0);
	private final SchemeNumber _one = Fixnum.valueOf(1);

	@Override
	public String getName() {
		return "rationalize";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(2, parameters);
		final SchemeNumber number = getNumber(parameters.get(0));
		final SchemeNumber maxDiff = getNumber(parameters.get(1));

		for (SchemeNumber d = _one;; d = d.add(_one)) {
			final SchemeNumber n = number.mul(d).roundToNearestInteger();
			final SchemeNumber asRational = n.div(d);
			if (abs(asRational.sub(number)).le(maxDiff)) {
				if (number.isExact())
					return asRational;
				else
					return asRational.mul(new Real(1));
			}
		}
	}

	private SchemeNumber abs(SchemeNumber n) throws SchemeException {
		if (n.compareTo(_zero) >= 0)
			return n;
		else
			return _zero.sub(n);
	}
}

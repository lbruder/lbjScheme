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

public final class WriteChar extends Builtin {
	private final Evaluator _eval;

	public WriteChar(Evaluator eval) {
		_eval = eval;
	}

	@Override
	public String getName() {
		return "write-char";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCountMin(1, parameters);
		assertParameterCountMax(2, parameters);
		assertParameterType(parameters.get(0), SchemeCharacter.class);
		final char c = ((SchemeCharacter) parameters.get(0)).getValue();

		if (parameters.size() == 1) {
			if (_eval != null)
				_eval.getOutputPort().write(Character.toString(c));
		} else {
			assertParameterType(parameters.get(1), OutputPort.class);
			((OutputPort) parameters.get(1)).write(Character.toString(c));
		}
		return _undefined;
	}
}
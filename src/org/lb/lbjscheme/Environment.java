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

public final class Environment {
	private final Environment _outer;
	private final HashMap<Symbol, SchemeObject> _values = new HashMap<Symbol, SchemeObject>();

	public Environment() {
		_outer = null;
	}

	public Environment(Environment outer) {
		_outer = outer;
	}

	public SchemeObject get(Symbol name) throws SchemeException {
		if (_values.containsKey(name))
			return _values.get(name);
		if (_outer != null)
			return _outer.get(name);
		throw new SchemeException("Unknown symbol " + name.toString());
	}

	public void define(Symbol name, SchemeObject value) throws SchemeException {
		switch (name.toString()) {
		case "if":
		case "define":
		case "set!":
		case "lambda":
		case "quote":
		case "begin":
			throw new SchemeException("Symbol '" + name.toString()
					+ "' is constant and must not be changed");
		}
		_values.put(name, value);
	}

	public void set(Symbol name, SchemeObject value) throws SchemeException {
		if (_values.containsKey(name))
			_values.put(name, value);
		else if (_outer != null)
			_outer.set(name, value);
		else
			throw new SchemeException("Unknown symbol " + name.toString());
	}

	public void expand(List<Symbol> parameterNames, boolean hasRestParameter,
			List<SchemeObject> parameters) throws SchemeException {
		if (hasRestParameter) {
			if (parameterNames.size() - 1 > parameters.size())
				throw new SchemeException(
						"Invalid parameter count: Expected at least "
								+ (parameterNames.size() - 1) + ", got "
								+ parameters.size());
			for (int i = 0; i < parameterNames.size() - 1; ++i)
				define(parameterNames.get(i), parameters.get(i));
			define(parameterNames.get(parameterNames.size() - 1),
					Pair.fromIterable(parameters.subList(
							parameterNames.size() - 1, parameters.size())));
		} else {
			if (parameterNames.size() != parameters.size())
				throw new SchemeException("Invalid parameter count: Expected "
						+ parameterNames.size() + ", got " + parameters.size());
			for (int i = 0; i < parameters.size(); ++i)
				define(parameterNames.get(i), parameters.get(i));
		}
	}
}

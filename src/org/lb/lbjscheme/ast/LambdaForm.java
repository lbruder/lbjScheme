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

package org.lb.lbjscheme.ast;

import java.util.List;
import org.lb.lbjscheme.Symbol;

public final class LambdaForm extends SyntaxTreeObject {
	private final String _name;
	private final List<Symbol> _parameterNames;
	private final boolean _hasRestParameter;
	private final BeginForm _analyzedForms;

	public LambdaForm(String name, List<Symbol> parameterNames,
			boolean hasRestParameter, BeginForm analyzedForms) {
		_name = name;
		_parameterNames = parameterNames;
		_hasRestParameter = hasRestParameter;
		_analyzedForms = analyzedForms;
	}

	public String getName() {
		return _name;
	}

	public List<Symbol> getParameterNames() {
		return _parameterNames;
	}

	public boolean HasRestParameter() {
		return _hasRestParameter;
	}

	public BeginForm getAnalyzedForms() {
		return _analyzedForms;
	}
}

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

public class CompiledLambda extends SchemeObject {
	public final Environment captured;
	public final int pc;
	public final List<Symbol> parameterNames;
	public final boolean hasRestParameter;
	public final String name;

	public CompiledLambda(String name, Environment captured, int pc,
			List<Symbol> parameterNames, boolean hasRestParameter) {
		this.name = name;
		this.captured = captured;
		this.pc = pc;
		this.parameterNames = parameterNames;
		this.hasRestParameter = hasRestParameter;
	}

	@Override
	public boolean isProcedure() {
		return true;
	}

	@Override
	public String toString(boolean forDisplay) {
		return "<compiled procedure " + name + ">";
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		throw new SchemeException(
				"Compiled procedure cannot be converted into a plain Java object");
	}
}

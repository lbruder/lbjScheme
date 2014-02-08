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

public interface Emitter {
	public void emitCall();

	public void emitContinue();

	public void emitDefineVariable(final Symbol variable);

	public void emitGetVariable(final Symbol variable);

	public void emitInitArgs();

	public void emitJump(String doneLabel);

	public void emitJumpIfFalse(String label);

	public void emitLiteral(final SchemeObject value);

	public void emitMakeClosure(String string, String closureLabel,
			boolean hasRestParameter, List<Symbol> parameterNames);

	public void emitPopAll();

	public void emitPushAll();

	public void emitPushArg();

	public void emitSetArgsToValueRegister();

	public void emitSetContinuationRegisterToLabel(final String label);

	public void emitSetVariable(final Symbol variable);

	public void setLabelPositionToHere(final String label);
}

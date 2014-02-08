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

public final class NullEmitter implements Emitter {

	@Override
	public void emitCall() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitContinue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitDefineVariable(Symbol variable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitGetVariable(Symbol variable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitInitArgs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitJump(String doneLabel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitJumpIfFalse(String label) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitLiteral(SchemeObject value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitMakeClosure(String string, String closureLabel,
			boolean hasRestParameter, List<Symbol> parameterNames) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitPopAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitPushAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitPushArg() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitSetContinuationRegisterToLabel(String label) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitSetArgsToValueRegister() {
		// TODO Auto-generated method stub

	}

	@Override
	public void emitSetVariable(Symbol variable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLabelPositionToHere(String label) {
		// TODO Auto-generated method stub

	}

}

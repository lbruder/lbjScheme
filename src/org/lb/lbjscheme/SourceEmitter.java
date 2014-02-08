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

public final class SourceEmitter implements Emitter {
	@Override
	public void emitCall() {
		System.out.println("    CALL");
	}

	@Override
	public void emitContinue() {
		System.out.println("    CONTINUE");
	}

	@Override
	public void emitDefineVariable(final Symbol variable) {
		System.out.println("    DEFVAR " + variable);
	}

	@Override
	public void emitGetVariable(final Symbol variable) {
		System.out.println("    GETVAR " + variable);
	}

	@Override
	public void emitInitArgs() {
		System.out.println("    INITARGS");
	}

	@Override
	public void emitJump(final String doneLabel) {
		System.out.println("    JMP " + doneLabel);
	}

	@Override
	public void emitJumpIfFalse(final String label) {
		System.out.println("    BNE " + label);
	}

	@Override
	public void emitLiteral(final SchemeObject value) {
		System.out.println("    LITERAL " + value);
	}

	@Override
	public void emitMakeClosure(String name, String closureLabel,
			boolean hasRestParameter, List<Symbol> parameterNames) {
		System.out.println("    MAKECLOSURE " + name + " " + closureLabel + " "
				+ (hasRestParameter ? "#t " : "#f ")
				+ parameterNamesListToString(parameterNames));
	}

	private static String parameterNamesListToString(List<Symbol> parameterNames) {
		final StringBuilder ret = new StringBuilder();
		for (final Symbol i : parameterNames) {
			ret.append(i);
			ret.append(',');
		}
		if (ret.length() > 0) ret.setLength(ret.length() - 1);
		return ret.toString();
	}

	@Override
	public void emitPopAll() {
		System.out.println("    POPCONT");
	}

	@Override
	public void emitPushAll() {
		System.out.println("    PUSHCONT");
	}

	@Override
	public void emitPushArg() {
		System.out.println("    PUSHARGS");
	}

	@Override
	public void emitSetContinuationRegisterToLabel(final String label) {
		System.out.println("    SETCONT " + label);
	}

	@Override
	public void emitSetArgsToValueRegister() {
		System.out.println("    SETVAL ARGS");
	}

	@Override
	public void emitSetVariable(final Symbol variable) {
		System.out.println("    SETVAR " + variable);
	}

	@Override
	public void setLabelPositionToHere(final String label) {
		System.out.println(label + ":");
	}
}
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
import org.lb.lbjscheme.ast.*;

public final class Compiler {
	private final Emitter _emitter;

	public Compiler(final Emitter emitter) {
		_emitter = emitter;
	}

	public void compile(final SyntaxTreeObject form, final boolean tailPosition)
			throws SchemeException {
		if (form instanceof Apply) {
			compileApply((Apply) form, tailPosition);
		} else if (form instanceof BeginForm) {
			compileBeginForm((BeginForm) form, tailPosition);
		} else if (form instanceof CallccForm) {
			compileCallccForm((CallccForm) form);
		} else if (form instanceof DefineForm) {
			compileDefineForm((DefineForm) form);
		} else if (form instanceof Funcall) {
			compileFuncall((Funcall) form, tailPosition);
		} else if (form instanceof IfForm) {
			compileIfForm((IfForm) form, tailPosition);
		} else if (form instanceof LambdaForm) {
			compileLambdaForm((LambdaForm) form);
		} else if (form instanceof LiteralSymbol) {
			compileLiteralSymbol((LiteralSymbol) form);
		} else if (form instanceof SelfEvaluatingLiteral) {
			compileSelfEvaluatingLiteral((SelfEvaluatingLiteral) form);
		} else if (form instanceof SetForm) {
			compileSetForm((SetForm) form);
		} else {
			throw new SchemeException(
					"Internal error: Unknown syntax tree object");
		}
	}

	private void compileApply(final Apply form, final boolean tailPosition)
			throws SchemeException {
		if (!tailPosition) _emitter.emitPushAll();
		compile(form.getParameters(), false);
		_emitter.emitSetArgsToValueRegister();
		compile(form.getProcedure(), false);

		if (tailPosition) {
			_emitter.emitCall();
		} else {
			final String jumpLabel = newLabel();
			_emitter.emitSetContinuationRegisterToLabel(jumpLabel);
			_emitter.emitCall();
			_emitter.setLabelPositionToHere(jumpLabel);
			_emitter.emitPopAll();
		}
	}

	private void compileBeginForm(final BeginForm form,
			final boolean tailPosition) throws SchemeException {
		for (final SyntaxTreeObject i : form.getFormsWithoutLast())
			compile(i, false);
		compile(form.getLastForm(), tailPosition);
	}

	private void compileCallccForm(final CallccForm form)
			throws SchemeException {
		throw new SchemeException("TODO: call/cc form not compilable yet");
	}

	private void compileDefineForm(final DefineForm form)
			throws SchemeException {
		compile(form.getAnalyzedForm(), false);
		_emitter.emitDefineVariable(form.getTarget());
	}

	private void compileFuncall(final Funcall form, final boolean tailPosition)
			throws SchemeException {
		if (!tailPosition) _emitter.emitPushAll();
		_emitter.emitInitArgs();

		final List<SyntaxTreeObject> params = new ArrayList<SyntaxTreeObject>();
		for (final SyntaxTreeObject i : form.getParameters())
			params.add(0, i);
		for (final SyntaxTreeObject i : params) {
			compile(i, false);
			_emitter.emitPushArg();
		}
		compile(form.getProcedure(), false);

		if (tailPosition) {
			_emitter.emitCall();
		} else {
			final String jumpLabel = newLabel();
			_emitter.emitSetContinuationRegisterToLabel(jumpLabel);
			_emitter.emitCall();
			_emitter.setLabelPositionToHere(jumpLabel);
			_emitter.emitPopAll();
		}
	}

	private void compileIfForm(final IfForm form, final boolean tailPosition)
			throws SchemeException {
		final String falseLabel = newLabel();
		final String doneLabel = newLabel();
		compile(form.getCondition(), false);
		_emitter.emitJumpIfFalse(falseLabel);
		compile(form.getThenPart(), tailPosition);
		_emitter.emitJump(doneLabel);
		_emitter.setLabelPositionToHere(falseLabel);
		compile(form.getElsePart(), tailPosition);
		_emitter.setLabelPositionToHere(doneLabel);
	}

	private void compileLambdaForm(final LambdaForm form)
			throws SchemeException {
		final String closureLabel = newLabel();
		final String afterClosureLabel = newLabel();

		_emitter.emitMakeClosure(form.getName(), closureLabel,
				form.HasRestParameter(), form.getParameterNames());
		_emitter.emitJump(afterClosureLabel);
		_emitter.setLabelPositionToHere(closureLabel);
		compile(form.getAnalyzedForms(), true);
		_emitter.emitContinue();
		_emitter.setLabelPositionToHere(afterClosureLabel);

	}

	private void compileLiteralSymbol(final LiteralSymbol form) {
		_emitter.emitGetVariable(form.getSymbol());
	}

	private void compileSelfEvaluatingLiteral(final SelfEvaluatingLiteral form) {
		_emitter.emitLiteral(form.getValue());
	}

	private void compileSetForm(final SetForm form) throws SchemeException {
		compile(form.getValue(), false);
		_emitter.emitSetVariable(form.getTarget());
	}

	private int label = 1;

	private String newLabel() {
		return "L" + label++;
	}
}

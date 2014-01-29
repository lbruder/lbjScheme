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

import java.io.*;
import java.util.*;
import org.lb.lbjscheme.ast.*;

public final class AnalyzingEvaluator extends Evaluator {
	private final static Symbol _undefinedSymbol = Symbol
			.fromString("undefined");
	private final static Symbol _beginSymbol = Symbol.fromString("begin");

	private final Analyzer _analyzer;

	public AnalyzingEvaluator(InputPort defaultInputPort,
			OutputPort defaultOutputPort) throws SchemeException {
		super(defaultInputPort, defaultOutputPort);
		_analyzer = new Analyzer();
		analyzeBuiltinLambdas(getGlobalEnvironment());
	}

	private void analyzeBuiltinLambdas(Environment global)
			throws SchemeException {
		global.unlock();
		for (final Symbol sym : global.getDefinedSymbols()) {
			if (global.get(sym) instanceof Lambda) {
				Lambda l = (Lambda) global.get(sym);
				final BeginForm beginForm = (BeginForm) _analyzer
						.analyze(new Pair(_beginSymbol, l.getForms()));
				global.set(
						sym,
						new AnalyzedLambda(l.getName(), l.getParameterNames(),
								l.hasRestParameter(), beginForm, l
										.getCaptured()));
			}
		}
		global.lock();
	}

	@Override
	public SchemeObject eval(String commands) throws SchemeException {
		final Reader r = new Reader(new InputPort(new StringReader(commands)));
		SchemeObject ret = Symbol.fromString("undefined");
		while (true) {
			try {
				ret = eval(r.read());
			} catch (EOFException ex) {
				return ret;
			}
		}
	}

	@Override
	public SchemeObject eval(SchemeObject o, Environment env)
			throws SchemeException {
		return eval(_analyzer.analyze(o), env);
	}

	private SchemeObject eval(SyntaxTreeObject o, Environment env)
			throws SchemeException {
		tailCall: for (;;) {
			if (o instanceof BeginForm) {
				final BeginForm form = (BeginForm) o;
				for (SyntaxTreeObject i : form.getFormsWithoutLast())
					eval(i, env);
				o = form.getLastForm();
				continue tailCall;
			}

			if (o instanceof DefineForm) {
				final DefineForm form = (DefineForm) o;
				env.define(form.getTarget(), eval(form.getAnalyzedForm(), env));
				return _undefinedSymbol;
			}

			if (o instanceof Funcall) {
				final Funcall form = (Funcall) o;
				final SchemeObject procedure = eval(form.getProcedure(), env);
				if (procedure instanceof Nil)
					throw new SchemeException("Empty list can not be evaluated");
				if (procedure instanceof Vector)
					throw new SchemeException("Vectors must be quoted");
				final List<SchemeObject> parameters = new ArrayList<>(form
						.getParameters().size());
				for (SyntaxTreeObject i : form.getParameters())
					parameters.add(eval(i, env));

				if (procedure instanceof Builtin)
					return ((Builtin) procedure).apply(parameters);

				// Ugly hack: Can only happen on lambdas returned by (eval)
				if (procedure instanceof Lambda) {
					final Lambda l = (Lambda) procedure;
					o = _analyzer.analyze(new Pair(_beginSymbol, l.getForms()));
					env = new Environment(l.getCaptured());
					env.expand(l.getParameterNames(), l.hasRestParameter(),
							parameters);
					continue tailCall;
				}

				if (procedure instanceof AnalyzedLambda) {
					final AnalyzedLambda l = (AnalyzedLambda) procedure;

					env = new Environment(l.getCaptured());
					env.expand(l.getParameterNames(), l.hasRestParameter(),
							parameters);

					for (SyntaxTreeObject i : l.getForms()
							.getFormsWithoutLast())
						eval(i, env);
					o = l.getForms().getLastForm();
					continue tailCall;
				}

				throw new SchemeException(
						"Don't know how to call object of type "
								+ procedure.getClass());
			}

			if (o instanceof CallccForm)
				throw new SchemeException(
						"AnalyzingEvaluator doesn't support continuations");

			if (o instanceof Apply) {
				final Apply form = (Apply) o;
				final SchemeObject procedure = eval(form.getProcedure(), env);
				if (procedure instanceof Nil)
					throw new SchemeException("Empty list can not be applied");
				final SchemeObject parameterList = eval(form.getParameters(),
						env);
				if (!(parameterList instanceof SchemeList))
					throw new SchemeException(
							"Invalid apply form: Expected argument list, got "
									+ parameterList.getClass());
				final List<SchemeObject> parameters = ((SchemeList) parameterList)
						.toJavaList();

				if (procedure instanceof Builtin)
					return ((Builtin) procedure).apply(parameters);

				// Ugly hack: Can only happen on lambdas returned by (eval)
				if (procedure instanceof Lambda) {
					final Lambda l = (Lambda) procedure;
					o = _analyzer.analyze(new Pair(_beginSymbol, l.getForms()));
					env = new Environment(l.getCaptured());
					env.expand(l.getParameterNames(), l.hasRestParameter(),
							parameters);
					continue tailCall;
				}

				if (procedure instanceof AnalyzedLambda) {
					final AnalyzedLambda l = (AnalyzedLambda) procedure;

					env = new Environment(l.getCaptured());
					env.expand(l.getParameterNames(), l.hasRestParameter(),
							parameters);

					for (SyntaxTreeObject i : l.getForms()
							.getFormsWithoutLast())
						eval(i, env);
					o = l.getForms().getLastForm();
					continue tailCall;
				}

				throw new SchemeException(
						"Don't know how to call object of type "
								+ procedure.getClass());
			}

			if (o instanceof IfForm) {
				final IfForm form = (IfForm) o;
				o = eval(form.getCondition(), env) != False.getInstance() ? form
						.getThenPart() : form.getElsePart();
				continue tailCall;
			}

			if (o instanceof LambdaForm) {
				final LambdaForm form = (LambdaForm) o;
				return new AnalyzedLambda(form.getName(),
						form.getParameterNames(), form.HasRestParameter(),
						form.getAnalyzedForms(), env);
			}

			if (o instanceof LiteralSymbol)
				return env.get(((LiteralSymbol) o).getSymbol());

			if (o instanceof SelfEvaluatingLiteral)
				return ((SelfEvaluatingLiteral) o).getValue();

			if (o instanceof SetForm) {
				final SetForm form = (SetForm) o;
				env.set(form.getTarget(), eval(form.getValue(), env));
				return _undefinedSymbol;
			}

			throw new SchemeException(
					"Internal error: Don't know how to handle object of type "
							+ o.getClass());
		}
	}
}

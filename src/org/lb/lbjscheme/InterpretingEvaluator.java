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

public final class InterpretingEvaluator extends Evaluator {
	private static final Symbol _undefinedSymbol = Symbol
			.fromString("undefined");
	private static final Symbol _quoteSymbol = Symbol.fromString("quote");
	private static final Symbol _defineSymbol = Symbol.fromString("define");
	private static final Symbol _defmacroSymbol = Symbol.fromString("defmacro");
	private static final Symbol _setSymbol = Symbol.fromString("set!");
	private static final Symbol _ifSymbol = Symbol.fromString("if");
	private static final Symbol _beginSymbol = Symbol.fromString("begin");
	private static final Symbol _lambdaSymbol = Symbol.fromString("lambda");
	private static final Symbol _applySymbol = Symbol.fromString("sys:apply");
	private static final Symbol _callccSymbol = Symbol
			.fromString("sys:call/cc");

	public InterpretingEvaluator(InputPort defaultInputPort,
			OutputPort defaultOutputPort) throws SchemeException {
		super(defaultInputPort, defaultOutputPort);
	}

	public InterpretingEvaluator(Environment global,
			InputPort defaultInputPort, OutputPort defaultOutputPort) {
		super(global, defaultInputPort, defaultOutputPort);
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
		return eval(o, env, false);
	}

	public SchemeObject eval(SchemeObject o, Environment env,
			boolean doNotExecuteExpandedMacros) throws SchemeException {
		tailCall: for (;;) {
			if (o instanceof Nil)
				throw new SchemeException("Empty list can not be evaluated");
			if (o instanceof Vector)
				throw new SchemeException("Vectors must be quoted");
			if (o instanceof Symbol) return env.get((Symbol) o);
			if (o instanceof Pair) {
				final Pair p = (Pair) o;
				final SchemeObject car = p.getCar();
				if (car == _lambdaSymbol) return makeLambda(p.getCdr(), env);
				if (car == _defineSymbol) return define(p.getCdr(), env);
				if (car == _defmacroSymbol) return defmacro(p.getCdr(), env);
				final List<SchemeObject> form = p.toJavaList();
				if (car == _quoteSymbol) return ((Pair) p.getCdr()).getCar();
				if (car == _setSymbol) return set(form, env);
				if (car == _callccSymbol)
					throw new SchemeException(
							"InterpretingEvaluator doesn't support continuations");
				if (car == _applySymbol) {
					if (form.size() != 3)
						throw new SchemeException(
								"Invalid apply form: Expected 3 parameters, got "
										+ (form.size() - 1));
					final SchemeObject procedure = eval(form.get(1), env);
					final SchemeObject argsList = eval(form.get(2), env);
					if (!(argsList instanceof SchemeList))
						throw new SchemeException(
								"Invalid apply form: Expected argument list, got "
										+ argsList.getClass());
					final List<SchemeObject> parameters = ((SchemeList) argsList)
							.toJavaList();

					if (procedure instanceof Builtin)
						return ((Builtin) procedure).apply(parameters);

					if (procedure instanceof Lambda) {
						final Lambda l = (Lambda) procedure;
						o = new Pair(_beginSymbol, l.getForms());
						env = new Environment(l.getCaptured());
						env.expand(l.getParameterNames(), l.hasRestParameter(),
								parameters);
						continue tailCall;
					}

					throw new SchemeException(
							"Don't know how to call object of type "
									+ car.getClass());
				}
				if (car == _ifSymbol) {
					SchemeObject condition;
					SchemeObject thenPart;
					SchemeObject elsePart;

					if (form.size() == 3) {
						condition = form.get(1);
						thenPart = form.get(2);
						elsePart = False.getInstance();
					} else if (form.size() == 4) {
						condition = form.get(1);
						thenPart = form.get(2);
						elsePart = form.get(3);
					} else
						throw new SchemeException(
								"Invalid if form: Expected 3 or 4 parameters, got "
										+ (form.size() - 1));

					o = eval(condition, env) != False.getInstance() ? thenPart
							: elsePart;
					continue tailCall;
				}
				if (car == _beginSymbol) {
					if (form.size() == 1)
						throw new SchemeException("Invalid begin form: Empty");
					for (int i = 1; i < form.size(); ++i) {
						o = form.get(i);
						if (i == form.size() - 1) // Last form in (begin...)
							continue tailCall;
						eval(o, env);
					}
				}

				final SchemeObject procedure = eval(car, env);
				final ArrayList<SchemeObject> parameters = new ArrayList<>();

				if (procedure instanceof Macro) {
					final Macro m = (Macro) procedure;
					final Environment macroEnv = new Environment(
							m.getCaptured());
					for (int i = 1; i < form.size(); ++i)
						parameters.add(form.get(i));
					macroEnv.expand(m.getParameterNames(),
							m.hasRestParameter(), parameters);
					o = eval(new Pair(_beginSymbol, m.getForms()), macroEnv);

					if (doNotExecuteExpandedMacros) return o;
					continue tailCall;
				}

				for (int i = 1; i < form.size(); ++i)
					parameters.add(eval(form.get(i), env));

				if (procedure instanceof Builtin)
					return ((Builtin) procedure).apply(parameters);

				if (procedure instanceof Lambda) {
					final Lambda l = (Lambda) procedure;
					o = new Pair(_beginSymbol, l.getForms());
					env = new Environment(l.getCaptured());
					env.expand(l.getParameterNames(), l.hasRestParameter(),
							parameters);
					continue tailCall;
				}

				throw new SchemeException(
						"Don't know how to call object of type "
								+ car.getClass());
			}
			return o;
		}
	}

	private static SchemeObject makeLambda(SchemeObject form, Environment env)
			throws SchemeException {
		if (!(form instanceof Pair))
			throw new SchemeException(
					"Invalid lambda form: Expected at least a parameter list and one form");
		final Pair p1 = (Pair) form;
		final SchemeObject parameterNameObject = p1.getCar();
		if (!(p1.getCdr() instanceof Pair))
			throw new SchemeException(
					"Invalid lambda form: Expected at least a parameter list and one form");
		final Pair forms = (Pair) p1.getCdr();
		if (parameterNameObject instanceof Symbol) // (lambda x forms)
			return makeLambdaWithRestParameterOnly(env, parameterNameObject,
					forms);
		if (parameterNameObject instanceof SchemeList) // (lambda (a b) forms)
			return makeLambdaWithParameterList(env, parameterNameObject, forms);
		throw new SchemeException("Invalid lambda form");
	}

	private static SchemeObject makeLambdaWithRestParameterOnly(
			Environment env, SchemeObject parameterNameObject, Pair forms) {
		final List<Symbol> parameterNames = new ArrayList<>();
		parameterNames.add((Symbol) parameterNameObject);
		return new Lambda("lambda", parameterNames, true, forms, env);
	}

	private static SchemeObject makeLambdaWithParameterList(Environment env,
			SchemeObject parameterNameObject, Pair forms)
			throws SchemeException {
		final List<Symbol> parameterNames = new ArrayList<>();
		final boolean hasRestParameter = ((SchemeList) parameterNameObject)
				.isDottedList();
		for (SchemeObject o : (SchemeList) parameterNameObject) {
			if (o instanceof Symbol)
				parameterNames.add((Symbol) o);
			else
				throw new SchemeException(
						"Invalid lambda form: Only symbols allowed in parameter name list");
		}
		return new Lambda("lambda", parameterNames, hasRestParameter, forms,
				env);
	}

	private SchemeObject define(SchemeObject form, Environment env)
			throws SchemeException {
		if (!(form instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected target and value");
		final Pair p1 = (Pair) form;
		if (p1.getCar() instanceof Symbol) return defineValue(env, p1);
		if (p1.getCar() instanceof Pair) return defineProcedure(env, p1);

		throw new SchemeException(
				"Invalid define form: Expected symbol or list as target");
	}

	private SchemeObject defineValue(Environment env, Pair p1)
			throws SchemeException {
		final Symbol sym = (Symbol) p1.getCar();
		final SchemeObject valueObject = p1.getCdr();
		if (!(valueObject instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected target and value");
		if (!(((Pair) valueObject).getCdr() instanceof Nil))
			throw new SchemeException(
					"Invalid define form: Too many parameters");
		final SchemeObject value = eval(((Pair) valueObject).getCar(), env);
		env.define(sym, value);
		return _undefinedSymbol;
	}

	private static SchemeObject defineProcedure(Environment env, Pair p1)
			throws SchemeException {
		final SchemeList target = (SchemeList) p1.getCar();
		final SchemeObject forms = p1.getCdr();
		if (!(forms instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected lambda forms");
		Symbol sym = null;
		final List<Symbol> parameterNames = new ArrayList<>();
		for (SchemeObject o : target) {
			if (!(o instanceof Symbol))
				throw new SchemeException(
						"Invalid define form: Procedure and parameter names must be symbols");
			if (sym == null)
				sym = (Symbol) o;
			else
				parameterNames.add((Symbol) o);
		}
		env.define(
				sym,
				new Lambda(sym.toString(), parameterNames, target
						.isDottedList(), (Pair) forms, env));
		return _undefinedSymbol;
	}

	private static SchemeObject defmacro(SchemeObject obj, Environment env)
			throws SchemeException {
		if (!(obj instanceof Pair))
			throw new SchemeException("Invalid defmacro form: Empty form");
		final Pair p1 = (Pair) obj;
		final SchemeObject nameObj = p1.getCar();
		if (!(nameObj instanceof Symbol))
			throw new SchemeException(
					"Invalid defmacro form: Expected macro name");
		final Symbol name = (Symbol) nameObj;
		if (!(p1.getCdr() instanceof Pair))
			throw new SchemeException(
					"Invalid defmacro form: Expected parameter list and forms");
		final SchemeObject paramListObj = ((Pair) p1.getCdr()).getCar();
		final SchemeObject formsObj = ((Pair) p1.getCdr()).getCdr();
		if (!(formsObj instanceof Pair))
			throw new SchemeException("Invalid defmacro form: Expected forms");
		Pair forms = (Pair) formsObj;
		final List<Symbol> parameterNames = new ArrayList<>();
		boolean hasRestParameter;
		if (paramListObj instanceof Symbol) {
			hasRestParameter = true;
			parameterNames.add((Symbol) paramListObj);
		} else if (paramListObj instanceof SchemeList) {
			hasRestParameter = ((SchemeList) paramListObj).isDottedList();
			for (SchemeObject o : ((SchemeList) paramListObj)) {
				if (!(o instanceof Symbol))
					throw new SchemeException(
							"Invalid defmacro form: All parameter names must be symbols");
				parameterNames.add((Symbol) o);
			}
		} else
			throw new SchemeException(
					"Invalid defmacro form: Expected argument or argument list");
		env.define(name, new Macro(name.toString(), parameterNames,
				hasRestParameter, forms, env));
		return name;
	}

	private SchemeObject set(List<SchemeObject> form, Environment env)
			throws SchemeException {
		if (form.size() != 3)
			throw new SchemeException(
					"Invalid set! form: Expected 3 parameters, got "
							+ (form.size() - 1));
		if (!(form.get(1) instanceof Symbol))
			throw new SchemeException(
					"Invalid set! form: Expected symbol as target");

		final Symbol sym = (Symbol) form.get(1);
		final SchemeObject value = form.get(2);
		env.set(sym, eval(value, env));
		return _undefinedSymbol;
	}
}

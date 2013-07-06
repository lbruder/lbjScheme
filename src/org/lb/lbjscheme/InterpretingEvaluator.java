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

public final class InterpretingEvaluator extends EvaluatorBase {
	private static final Symbol undefinedSymbol = Symbol
			.fromString("undefined");
	private static final Symbol quoteSymbol = Symbol.fromString("quote");
	private static final Symbol defineSymbol = Symbol.fromString("define");
	private static final Symbol setSymbol = Symbol.fromString("set!");
	private static final Symbol ifSymbol = Symbol.fromString("if");
	private static final Symbol beginSymbol = Symbol.fromString("begin");
	private static final Symbol lambdaSymbol = Symbol.fromString("lambda");

	public InterpretingEvaluator() throws SchemeException {
		super();
	}

	@Override
	public SchemeObject eval(SchemeObject o, Environment env)
			throws SchemeException {
		tailCall: for (;;) {
			if (o instanceof Nil)
				throw new SchemeException("Empty list can not be evaluated");
			if (o instanceof Vector)
				throw new SchemeException("Vectors must be quoted");
			if (o instanceof Symbol)
				return env.get((Symbol) o);
			if (o instanceof Pair) {
				Pair p = (Pair) o;
				SchemeObject car = p.getCar();
				if (car == lambdaSymbol)
					return makeLambda(p.getCdr(), env);
				if (car == defineSymbol)
					return define(p.getCdr(), env);
				List<SchemeObject> form = p.toJavaList();
				if (car == quoteSymbol)
					return Builtins.car(p.getCdr());
				if (car == setSymbol)
					return set(form, env);
				if (car == ifSymbol) {
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
				if (car == beginSymbol) {
					if (form.size() == 1)
						throw new SchemeException("Invalid begin form: Empty");
					for (int i = 1; i < form.size(); ++i) {
						o = form.get(i);
						if (i == form.size() - 1) // Last form in (begin...)
							continue tailCall;
						else
							eval(o);
					}
				}

				SchemeObject procedure = eval(car, env);
				ArrayList<SchemeObject> parameters = new ArrayList<SchemeObject>();
				for (int i = 1; i < form.size(); ++i)
					parameters.add(eval(form.get(i), env));

				if (procedure instanceof Builtin)
					return ((Builtin) procedure).apply(parameters);

				if (procedure instanceof Lambda) {
					Lambda l = (Lambda) procedure;
					o = new Pair(beginSymbol, l.getForms());
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

	private SchemeObject makeLambda(SchemeObject form, Environment env)
			throws SchemeException {
		if (!(form instanceof Pair))
			throw new SchemeException(
					"Invalid lambda form: Expected at least a parameter list and one form");
		Pair p1 = (Pair) form;
		SchemeObject parameterNameObject = p1.getCar();
		if (!(p1.getCdr() instanceof Pair))
			throw new SchemeException(
					"Invalid lambda form: Expected at least a parameter list and one form");
		Pair forms = (Pair) p1.getCdr();
		if (parameterNameObject instanceof Symbol) // (lambda x forms)
			return makeLambdaWithRestParameterOnly(env, parameterNameObject,
					forms);
		if (parameterNameObject instanceof SchemeList) // (lambda (a b) forms)
			return makeLambdaWithParameterList(env, parameterNameObject, forms);
		throw new SchemeException("Invalid lambda form");
	}

	private SchemeObject makeLambdaWithRestParameterOnly(Environment env,
			SchemeObject parameterNameObject, Pair forms) {
		List<Symbol> parameterNames = new ArrayList<Symbol>();
		parameterNames.add((Symbol) parameterNameObject);
		return new Lambda("lambda", parameterNames, true, forms, env);
	}

	private SchemeObject makeLambdaWithParameterList(Environment env,
			SchemeObject parameterNameObject, Pair forms)
			throws SchemeException {
		List<Symbol> parameterNames = new ArrayList<Symbol>();
		boolean hasRestParameter = ((SchemeList) parameterNameObject)
				.isDottedList();
		for (SchemeObject o : (SchemeList) parameterNameObject) {
			if (o instanceof Symbol)
				parameterNames.add((Symbol) o);
			else
				throw new SchemeException(
						"Invalid lambda form: Only symbol allowed in parameter name list");
		}
		return new Lambda("lambda", parameterNames, hasRestParameter, forms,
				env);
	}

	private SchemeObject define(SchemeObject form, Environment env)
			throws SchemeException {
		if (!(form instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected target and value");
		Pair p1 = (Pair) form;
		if (p1.getCar() instanceof Symbol)
			return defineValue(env, p1);
		if (p1.getCar() instanceof Pair)
			return defineProcedure(env, p1);

		throw new SchemeException(
				"Invalid define form: Expected symbol or list as target");
	}

	private SchemeObject defineValue(Environment env, Pair p1)
			throws SchemeException {
		Symbol sym = (Symbol) p1.getCar();
		SchemeObject valueObject = p1.getCdr();
		if (!(valueObject instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected target and value");
		if (!(((Pair) valueObject).getCdr() instanceof Nil))
			throw new SchemeException(
					"Invalid define form: Too many parameters");
		SchemeObject value = eval(((Pair) valueObject).getCar());
		env.define(sym, value);
		return undefinedSymbol;
	}

	private SchemeObject defineProcedure(Environment env, Pair p1)
			throws SchemeException {
		SchemeList target = (SchemeList) p1.getCar();
		SchemeObject forms = p1.getCdr();
		if (!(forms instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected lambda forms");
		Symbol sym = null;
		List<Symbol> parameterNames = new ArrayList<Symbol>();
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
		return undefinedSymbol;
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

		Symbol sym = (Symbol) form.get(1);
		SchemeObject value = form.get(2);
		env.set(sym, eval(value));
		return undefinedSymbol;
	}
}

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

import java.util.ArrayList;
import java.util.List;

import org.lb.lbjscheme.ast.Apply;
import org.lb.lbjscheme.ast.BeginForm;
import org.lb.lbjscheme.ast.CallccForm;
import org.lb.lbjscheme.ast.DefineForm;
import org.lb.lbjscheme.ast.Funcall;
import org.lb.lbjscheme.ast.IfForm;
import org.lb.lbjscheme.ast.LambdaForm;
import org.lb.lbjscheme.ast.LiteralSymbol;
import org.lb.lbjscheme.ast.SelfEvaluatingLiteral;
import org.lb.lbjscheme.ast.SetForm;
import org.lb.lbjscheme.ast.SyntaxTreeObject;

public final class Analyzer {
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

	private final InterpretingEvaluator _macroEvaluator;
	private final Environment _macroEnvironment;

	public Analyzer() throws SchemeException {
		_macroEvaluator = new InterpretingEvaluator(
				Environment.newInteractionEnvironment(null), null, null);
		_macroEnvironment = _macroEvaluator.getGlobalEnvironment();
	}

	public SyntaxTreeObject analyze(SchemeObject obj) throws SchemeException {
		if (obj instanceof Nil)
			throw new SchemeException("Empty list can not be evaluated");
		if (obj instanceof Vector)
			throw new SchemeException("Vectors must be quoted");
		if (obj instanceof Symbol)
			return new LiteralSymbol((Symbol) obj);
		if (!(obj instanceof Pair))
			return new SelfEvaluatingLiteral(obj);

		final Pair p = (Pair) obj;
		final SchemeObject car = p.getCar();
		final List<SchemeObject> form = p.toJavaList();

		if (car instanceof Symbol
				&& _macroEnvironment.getDefinedSymbols().contains(car)
				&& _macroEnvironment.get((Symbol) car) instanceof Macro) {
			// System.out.println("Macro expansion:");
			// System.out.println(p.toString(false));
			// System.out.println("=>");
			final SchemeObject evald = _macroEvaluator.eval(p,
					_macroEnvironment, true);
			// System.out.println(evald.toString(false));
			// System.out.println("");
			return analyze(evald);
		}

		if (car == _lambdaSymbol)
			return analyzeLambdaForm(p.getCdr());
		if (car == _defineSymbol)
			return analyzeDefineForm(p.getCdr());
		if (car == _defmacroSymbol)
			return defmacro(p);
		if (car == _quoteSymbol)
			return new SelfEvaluatingLiteral(form.get(1));
		if (car == _setSymbol)
			return analyzeSetForm(form);
		if (car == _callccSymbol)
			return analyzeCallccForm(form);
		if (car == _applySymbol)
			return analyzeApplyForm(p);
		if (car == _ifSymbol)
			return analyzeIfForm(form);
		if (car == _beginSymbol)
			return analyzeBeginForm(form);

		return analyzeFuncall(p);
	}

	private LambdaForm analyzeLambdaForm(SchemeObject obj)
			throws SchemeException {
		if (!(obj instanceof Pair))
			throw new SchemeException(
					"Invalid lambda form: Expected at least a parameter list and one form");
		final Pair p1 = (Pair) obj;
		final SchemeObject parameterNameObject = p1.getCar();
		if (!(p1.getCdr() instanceof Pair))
			throw new SchemeException(
					"Invalid lambda form: Expected at least a parameter list and one form");

		final Pair forms = (Pair) p1.getCdr();
		final List<Symbol> parameterNames = new ArrayList<Symbol>();

		if (parameterNameObject instanceof Symbol) { // (lambda x forms)
			parameterNames.add((Symbol) parameterNameObject);
			return new LambdaForm(
					"lambda",
					parameterNames,
					true,
					analyzeBeginForm(new Pair(_beginSymbol, forms).toJavaList()));
		}
		if (parameterNameObject instanceof SchemeList) { // (lambda (a b) forms)
			final boolean hasRestParameter = ((SchemeList) parameterNameObject)
					.isDottedList();
			for (SchemeObject o : (SchemeList) parameterNameObject) {
				if (o instanceof Symbol)
					parameterNames.add((Symbol) o);
				else
					throw new SchemeException(
							"Invalid lambda form: Only symbols allowed in parameter name list");
			}
			return new LambdaForm(
					"lambda",
					parameterNames,
					hasRestParameter,
					analyzeBeginForm(new Pair(_beginSymbol, forms).toJavaList()));
		}
		throw new SchemeException("Invalid lambda form");
	}

	private DefineForm analyzeDefineForm(SchemeObject obj)
			throws SchemeException {
		if (!(obj instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected target and value");

		final Pair p1 = (Pair) obj;
		if (p1.getCar() instanceof Symbol)
			return analyzeDefineValue(p1);
		if (p1.getCar() instanceof Pair)
			return analyzeDefineProcedure(p1);

		throw new SchemeException(
				"Invalid define form: Expected symbol or list as target");
	}

	private DefineForm analyzeDefineValue(Pair p1) throws SchemeException {
		final Symbol sym = (Symbol) p1.getCar();
		final SchemeObject valueObject = p1.getCdr();
		if (!(valueObject instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected target and value");
		if (!(((Pair) valueObject).getCdr() instanceof Nil))
			throw new SchemeException(
					"Invalid define form: Too many parameters");

		return new DefineForm(sym, analyze(((Pair) valueObject).getCar()));
	}

	private DefineForm analyzeDefineProcedure(Pair p1) throws SchemeException {
		final SchemeList target = (SchemeList) p1.getCar();
		final SchemeObject forms = p1.getCdr();
		if (!(forms instanceof Pair))
			throw new SchemeException(
					"Invalid define form: Expected lambda forms");
		Symbol sym = null;
		final List<Symbol> parameterNames = new ArrayList<Symbol>();
		for (SchemeObject o : target) {
			if (!(o instanceof Symbol))
				throw new SchemeException(
						"Invalid define form: Procedure and parameter names must be symbols");
			if (sym == null)
				sym = (Symbol) o;
			else
				parameterNames.add((Symbol) o);
		}

		return new DefineForm(sym, new LambdaForm(sym.toString(),
				parameterNames, target.isDottedList(),
				analyzeBeginForm(new Pair(_beginSymbol, forms).toJavaList())));
	}

	private SelfEvaluatingLiteral defmacro(SchemeObject obj)
			throws SchemeException {
		Symbol macroName = (Symbol) _macroEvaluator.eval(obj);
		return new SelfEvaluatingLiteral(macroName);
	}

	private SetForm analyzeSetForm(List<SchemeObject> form)
			throws SchemeException {
		if (form.size() != 3)
			throw new SchemeException(
					"Invalid set! form: Expected 2 parameters, got "
							+ (form.size() - 1));
		if (!(form.get(1) instanceof Symbol))
			throw new SchemeException(
					"Invalid set! form: Expected symbol as target");

		return new SetForm((Symbol) form.get(1), analyze(form.get(2)));
	}

	private SyntaxTreeObject analyzeCallccForm(List<SchemeObject> form)
			throws SchemeException {
		if (form.size() != 2)
			throw new SchemeException(
					"Invalid call/cc form: Expected 1 parameter, got "
							+ (form.size() - 1));
		return new CallccForm(analyze(form.get(1)));
	}

	private SyntaxTreeObject analyzeApplyForm(Pair rawForm)
			throws SchemeException {
		final List<SchemeObject> form = rawForm.toJavaList();

		if (form.size() != 3)
			throw new SchemeException(
					"Invalid apply form: Expected 2 parameters, got "
							+ (form.size() - 1));

		final SyntaxTreeObject procedure = analyze(form.get(1));
		final SyntaxTreeObject parameterList = analyze(form.get(2));

		return new Apply(procedure, parameterList);
	}

	private IfForm analyzeIfForm(final List<SchemeObject> form)
			throws SchemeException {
		if (form.size() == 3)
			return new IfForm(analyze(form.get(1)), analyze(form.get(2)),
					analyze(False.getInstance()));
		if (form.size() == 4)
			return new IfForm(analyze(form.get(1)), analyze(form.get(2)),
					analyze(form.get(3)));
		throw new SchemeException(
				"Invalid if form: Expected 3 or 4 parameters, got "
						+ (form.size() - 1));
	}

	private BeginForm analyzeBeginForm(List<SchemeObject> form)
			throws SchemeException {
		if (form.size() == 1)
			throw new SchemeException("Invalid begin form: Empty");

		List<SyntaxTreeObject> analyzedForms = new ArrayList<SyntaxTreeObject>();
		for (SchemeObject o : form.subList(1, form.size()))
			analyzedForms.add(analyze(o));

		return new BeginForm(
				analyzedForms.subList(0, analyzedForms.size() - 1),
				analyzedForms.get(analyzedForms.size() - 1));
	}

	private SyntaxTreeObject analyzeFuncall(Pair rawForm)
			throws SchemeException {
		final SchemeObject procedure = rawForm.getCar();

		final List<SchemeObject> form = rawForm.toJavaList();
		final ArrayList<SyntaxTreeObject> parameters = new ArrayList<SyntaxTreeObject>();
		for (int i = 1; i < form.size(); ++i)
			parameters.add(analyze(form.get(i)));

		return new Funcall(analyze(procedure), parameters);
	}
}

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

// Class explosion or giant switch block. I'd rather have less classes for now.

public final class OldStyleBuiltins extends Builtin {
	private final Type _type;
	private final String _name;

	public enum Type {
		eqp, nullp, pairp, numberp, stringp, charp, booleanp, symbolp, procedurep, listp, vectorp, numeq, quotient, remainder, charToInteger, integerToChar, write, stringLength, stringRef, stringToSymbol, symbolToString, vectorLength, vectorRef, numberToString, display, integerp, rationalp, numerator, denominator, nullEnvironment, schemeReportEnvironment, interactionEnvironment, eval
	};

	public OldStyleBuiltins(Type type) {
		_type = type;
		_name = makeName(_type);
	}

	public static SchemeNumber asNumber(String procedure, SchemeObject o)
			throws SchemeException {
		if (o instanceof SchemeNumber)
			return ((SchemeNumber) o);
		else
			throw new SchemeException(procedure
					+ ": Invalid type conversion; expected Number, got "
					+ o.getClass());
	}

	public static int getFixnum(String procedure, SchemeObject o)
			throws SchemeException {
		if (o instanceof Fixnum)
			return ((Fixnum) o).getValue();
		else
			throw new SchemeException(procedure
					+ ": Invalid type conversion; expected Fixnum, got "
					+ o.getClass());
	}

	private static String makeName(Type type) {
		switch (type) {
		case interactionEnvironment:
			return "interaction-environment";
		case nullEnvironment:
			return "null-environment";
		case numeq:
			return "=";
		case schemeReportEnvironment:
			return "scheme-report-environment";
		case stringLength:
			return "string-length";
		case stringRef:
			return "string-ref";
		case vectorLength:
			return "vector-length";
		case vectorRef:
			return "vector-ref";
		default:
			final String name = type.name().replace("To", "->").toLowerCase();
			if (name.endsWith("p"))
				return name.substring(0, name.length() - 1) + "?";
			else
				return name;
		}
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		switch (_type) {
		case booleanp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof True)
					|| (parameters.get(0) instanceof False) ? _true : _false;
		case charp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeCharacter ? _true
					: _false;
		case charToInteger:
			assertParameterCount(1, parameters);
			SchemeObject o9 = parameters.get(0);
			if (o9 instanceof SchemeCharacter)
				return new Fixnum(((SchemeCharacter) o9).getValue());
			throw new SchemeException(
					"char->integer: Invalid parameter type; expected character, got "
							+ o9.getClass());
		case denominator:
			assertParameterCount(1, parameters);
			SchemeObject o14 = parameters.get(0);
			if (!(o14 instanceof SchemeNumber))
				throw new SchemeException(
						"denominator: Invalid parameter type; expected number, got "
								+ o14.getClass());
			return ((SchemeNumber) o14).getDenominator();
		case display:
			assertParameterCount(1, parameters); // HACK: For now
			System.out.print(parameters.get(0).toString(true));
			return parameters.get(0);
		case eval:
			assertParameterCount(2, parameters);
			SchemeObject env = parameters.get(1);
			if (!(env instanceof Environment))
				throw new SchemeException(
						"eval: Invalid parameter type; expected environment, got "
								+ env.getClass());
			return new InterpretingEvaluator((Environment) env).eval(parameters
					.get(0));
		case eqp:
			assertParameterCount(2, parameters);
			return parameters.get(0) == parameters.get(1) ? _true : _false;
		case integerp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof Fixnum)
					|| (parameters.get(0) instanceof Bignum) ? _true : _false;
		case integerToChar:
			assertParameterCount(1, parameters);
			return new SchemeCharacter((char) getFixnum("integer->char",
					parameters.get(0)));
		case interactionEnvironment:
			return Environment.newInteractionEnvironment();
		case listp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof SchemeList)
					&& (!((SchemeList) parameters.get(0)).isDottedList()) ? _true
					: _false;
		case nullEnvironment:
			assertParameterCount(1, parameters);
			return Environment.newNullEnvironment((Fixnum) (parameters.get(0)));
		case nullp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Nil ? _true : _false;
		case numerator:
			assertParameterCount(1, parameters);
			SchemeObject o13 = parameters.get(0);
			if (!(o13 instanceof SchemeNumber))
				throw new SchemeException(
						"numerator: Invalid parameter type; expected number, got "
								+ o13.getClass());
			return ((SchemeNumber) o13).getNumerator();
		case numberp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeNumber ? _true : _false;
		case numberToString:
			switch (parameters.size()) {
			case 1:
				return new SchemeString(asNumber("number->string",
						parameters.get(0)).toString(false));
			case 2:
				SchemeNumber num = asNumber("number->string", parameters.get(0));
				int base = getFixnum("number->string", parameters.get(1));
				return new SchemeString(num.toString(false, base));

			default:
				throw new SchemeException(
						"number->string: Expected 1 or 2 parameters, got "
								+ parameters.size());
			}
		case numeq:
			if (parameters.size() < 2)
				throw new SchemeException("=: Expected at least two parameters");

			final SchemeNumber last4 = asNumber("=", parameters.get(0));
			for (SchemeObject o8 : parameters.subList(1, parameters.size())) {
				SchemeNumber now = asNumber("=", o8);
				if (!last4.eq(now))
					return False.getInstance();
			}
			return True.getInstance();
		case pairp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Pair ? _true : _false;
		case procedurep:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof Lambda)
					|| (parameters.get(0) instanceof Builtin) ? _true : _false;
		case quotient:
			assertParameterCount(2, parameters);
			return asNumber("quotient", parameters.get(0)).idiv(
					asNumber("quotient", parameters.get(1)));
		case rationalp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof Fixnum)
					|| (parameters.get(0) instanceof Bignum)
					|| (parameters.get(0) instanceof Rational) ? _true : _false;
		case remainder:
			assertParameterCount(2, parameters);
			return asNumber("remainder", parameters.get(0)).mod(
					asNumber("remainder", parameters.get(1)));
		case schemeReportEnvironment:
			assertParameterCount(1, parameters);
			return Environment
					.newReportEnvironment((Fixnum) (parameters.get(0)));
		case stringLength:
			assertParameterCount(1, parameters);
			SchemeObject o10 = parameters.get(0);
			if (o10 instanceof SchemeString)
				return new Fixnum(((SchemeString) o10).getLength());
			throw new SchemeException(
					"string-length: Invalid parameter type; expected string, got "
							+ o10.getClass());
		case stringp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeString ? _true : _false;
		case stringRef:
			assertParameterCount(2, parameters);
			SchemeObject str = parameters.get(0);
			if (!(str instanceof SchemeString))
				throw new SchemeException(
						"string-ref: Invalid parameter type; expected string, got "
								+ str.getClass());
			return new SchemeCharacter(((SchemeString) str).getAt(getFixnum(
					"string-ref", parameters.get(1))));
		case stringToSymbol:
			assertParameterCount(1, parameters);
			SchemeObject str2 = parameters.get(0);
			if (!(str2 instanceof SchemeString))
				throw new SchemeException(
						"string->symbol: Invalid parameter type; expected string, got "
								+ str2.getClass());
			return Symbol.fromString(((SchemeString) str2).getValue());
		case symbolp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Symbol ? _true : _false;
		case symbolToString:
			assertParameterCount(1, parameters);
			SchemeObject sym = parameters.get(0);
			if (!(sym instanceof Symbol))
				throw new SchemeException(
						"symbol->string: Invalid parameter type; expected symbol, got "
								+ sym.getClass());
			return new SchemeString(((Symbol) sym).toString());
		case vectorLength:
			assertParameterCount(1, parameters);
			SchemeObject o12 = parameters.get(0);
			if (o12 instanceof Vector)
				return new Fixnum(((Vector) o12).getLength());
			throw new SchemeException(
					"vector-length: Invalid parameter type; expected vector, got "
							+ o12.getClass());
		case vectorp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Vector ? _true : _false;
		case vectorRef:
			assertParameterCount(2, parameters);
			SchemeObject vec = parameters.get(0);
			if (!(vec instanceof Vector))
				throw new SchemeException(
						"vector-ref: Invalid parameter type; expected vector, got "
								+ vec.getClass());
			return ((Vector) vec).getAt(getFixnum("vector-ref",
					parameters.get(1)));
		case write:
			assertParameterCount(1, parameters);
			System.out.print(parameters.get(0));
			return Symbol.fromString("undefined");
		default:
			throw new RuntimeException("Unknown builtin type " + _type);
		}
	}
}

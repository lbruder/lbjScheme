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
		eqp, nullp, pairp, numberp, stringp, charp, booleanp, symbolp, procedurep, listp, vectorp, add, sub, mul, div, lt, le, gt, ge, numeq, quotient, remainder, charToInteger, integerToChar, write, makeString, stringLength, stringRef, stringSet, stringToSymbol, symbolToString, makeVector, vectorLength, vectorRef, vectorSet, numberToString, stringToNumber, display, integerp, rationalp, numerator, denominator, nullEnvironment, schemeReportEnvironment, interactionEnvironment, eval
	};

	public OldStyleBuiltins(Type type) {
		_type = type;
		_name = makeName(_type);
	}

	private static String makeName(Type type) {
		switch (type) {
		case add:
			return "+";
		case div:
			return "/";
		case ge:
			return ">=";
		case gt:
			return ">";
		case interactionEnvironment:
			return "interaction-environment";
		case le:
			return "<=";
		case lt:
			return "<";
		case makeString:
			return "make-string";
		case makeVector:
			return "make-vector";
		case mul:
			return "*";
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
		case stringSet:
			return "string-set!";
		case sub:
			return "-";
		case vectorLength:
			return "vector-length";
		case vectorRef:
			return "vector-ref";
		case vectorSet:
			return "vector-set!";
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
		case add:
			return Builtins.add(parameters);
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
			return Builtins.charToInt(parameters.get(0));
		case denominator:
			assertParameterCount(1, parameters);
			return Builtins.denominator(parameters.get(0));
		case display:
			assertParameterCount(1, parameters); // HACK: For now
			System.out.print(parameters.get(0).toString(true));
			return parameters.get(0);
		case div:
			return Builtins.div(parameters);
		case eval:
			assertParameterCount(2, parameters);
			return Builtins.eval(parameters.get(0), parameters.get(1));
		case eqp:
			assertParameterCount(2, parameters);
			return parameters.get(0) == parameters.get(1) ? _true : _false;
		case ge:
			return Builtins.ge(parameters);
		case gt:
			return Builtins.gt(parameters);
		case integerp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof Fixnum)
					|| (parameters.get(0) instanceof Bignum) ? _true : _false;
		case integerToChar:
			assertParameterCount(1, parameters);
			return Builtins.intToChar(parameters.get(0));
		case interactionEnvironment:
			return Environment.newInteractionEnvironment();
		case listp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof SchemeList)
					&& (!((SchemeList) parameters.get(0)).isDottedList()) ? _true
					: _false;
		case le:
			return Builtins.le(parameters);
		case lt:
			return Builtins.lt(parameters);
		case makeString:
			return Builtins.makeString(parameters);
		case makeVector:
			return Builtins.makeVector(parameters);
		case mul:
			return Builtins.mul(parameters);
		case nullEnvironment:
			assertParameterCount(1, parameters);
			return Environment.newNullEnvironment((Fixnum) (parameters.get(0)));
		case nullp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Nil ? _true : _false;
		case numerator:
			assertParameterCount(1, parameters);
			return Builtins.numerator(parameters.get(0));
		case numberp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeNumber ? _true : _false;
		case numberToString:
			return Builtins.numberToString(parameters);
		case numeq:
			return Builtins.numeq(parameters);
		case pairp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Pair ? _true : _false;
		case procedurep:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof Lambda)
					|| (parameters.get(0) instanceof Builtin) ? _true : _false;
		case quotient:
			assertParameterCount(2, parameters);
			return Builtins.quotient(parameters.get(0), parameters.get(1));
		case rationalp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof Fixnum)
					|| (parameters.get(0) instanceof Bignum)
					|| (parameters.get(0) instanceof Rational) ? _true : _false;
		case remainder:
			assertParameterCount(2, parameters);
			return Builtins.remainder(parameters.get(0), parameters.get(1));
		case schemeReportEnvironment:
			assertParameterCount(1, parameters);
			return Environment
					.newReportEnvironment((Fixnum) (parameters.get(0)));
		case stringLength:
			assertParameterCount(1, parameters);
			return Builtins.stringLength(parameters.get(0));
		case stringp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeString ? _true : _false;
		case stringRef:
			assertParameterCount(2, parameters);
			return Builtins.stringRef(parameters.get(0), parameters.get(1));
		case stringSet:
			assertParameterCount(3, parameters);
			return Builtins.stringSet(parameters.get(0), parameters.get(1),
					parameters.get(2));
		case stringToNumber:
			return Builtins.stringToNumber(parameters);
		case stringToSymbol:
			assertParameterCount(1, parameters);
			return Builtins.stringToSymbol(parameters.get(0));
		case sub:
			return Builtins.sub(parameters);
		case symbolp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Symbol ? _true : _false;
		case symbolToString:
			assertParameterCount(1, parameters);
			return Builtins.symbolToString(parameters.get(0));
		case vectorLength:
			assertParameterCount(1, parameters);
			return Builtins.vectorLength(parameters.get(0));
		case vectorp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Vector ? _true : _false;
		case vectorRef:
			assertParameterCount(2, parameters);
			return Builtins.vectorRef(parameters.get(0), parameters.get(1));
		case vectorSet:
			assertParameterCount(3, parameters);
			return Builtins.vectorSet(parameters.get(0), parameters.get(1),
					parameters.get(2));
		case write:
			assertParameterCount(1, parameters);
			return Builtins.write(parameters.get(0));
		default:
			throw new RuntimeException("Unknown builtin type " + _type);
		}
	}
}

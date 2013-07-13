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

public final class Builtin implements SchemeObject {
	private final Type _type;
	private static final True _true = True.getInstance();
	private static final False _false = False.getInstance();

	public enum Type {
		cons, car, cdr, setCar, setCdr, eqp, nullp, pairp, numberp, stringp, charp, booleanp, symbolp, procedurep, listp, add, sub, mul, div, lt, le, gt, ge, numeq, quotient, remainder, charToInt, intToChar, write, makeString, stringLength, stringRef, stringSet, stringToSymbol, symbolToString
	};

	public Builtin(Type type) {
		_type = type;
	}

	public String getName() {
		switch (_type) {
		case add:
			return "+";
		case booleanp:
			return "boolean?";
		case car:
			return "car";
		case cdr:
			return "cdr";
		case charp:
			return "char?";
		case charToInt:
			return "char->integer";
		case cons:
			return "cons";
		case div:
			return "/";
		case eqp:
			return "eq?";
		case ge:
			return ">=";
		case gt:
			return ">";
		case intToChar:
			return "integer->char";
		case listp:
			return "list?";
		case makeString:
			return "make-string";
		case mul:
			return "*";
		case nullp:
			return "null?";
		case le:
			return "<=";
		case lt:
			return "<";
		case numberp:
			return "number?";
		case numeq:
			return "=";
		case pairp:
			return "pair?";
		case procedurep:
			return "procedure?";
		case quotient:
			return "quotient";
		case remainder:
			return "remainder";
		case setCar:
			return "set-car!";
		case setCdr:
			return "set-cdr!";
		case stringLength:
			return "string-length";
		case stringp:
			return "string?";
		case stringRef:
			return "string-ref";
		case stringSet:
			return "string-set!";
		case stringToSymbol:
			return "string->symbol";
		case sub:
			return "-";
		case symbolp:
			return "symbol?";
		case symbolToString:
			return "symbol->string";
		case write:
			return "write";
		default:
			throw new RuntimeException("Unknown builtin type " + _type);
		}
	}

	@Override
	public String toString() {
		return "Builtin procedure " + getName();
	}

	protected void assertParameterCount(int expected,
			List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected != got)
			throw new SchemeException(getName()
					+ ": Invalid parameter count; expected: " + expected
					+ ", got: " + got);
	}

	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		switch (_type) {
		case add:
			return Builtins.add(parameters);
		case booleanp:
			assertParameterCount(1, parameters);
			return (parameters.get(0) instanceof True)
					|| (parameters.get(0) instanceof False) ? _true : _false;
		case car:
			assertParameterCount(1, parameters);
			return Builtins.car(parameters.get(0));
		case cdr:
			assertParameterCount(1, parameters);
			return Builtins.cdr(parameters.get(0));
		case charp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeCharacter ? _true
					: _false;
		case charToInt:
			assertParameterCount(1, parameters);
			return Builtins.charToInt(parameters.get(0));
		case cons:
			assertParameterCount(2, parameters);
			return Builtins.cons(parameters.get(0), parameters.get(1));
		case div:
			return Builtins.div(parameters);
		case eqp:
			assertParameterCount(2, parameters);
			return parameters.get(0) == parameters.get(1) ? _true : _false;
		case ge:
			return Builtins.ge(parameters);
		case gt:
			return Builtins.gt(parameters);
		case intToChar:
			assertParameterCount(1, parameters);
			return Builtins.intToChar(parameters.get(0));
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
		case mul:
			return Builtins.mul(parameters);
		case nullp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof Nil ? _true : _false;
		case numberp:
			assertParameterCount(1, parameters);
			return parameters.get(0) instanceof SchemeNumber ? _true : _false;
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
		case remainder:
			assertParameterCount(2, parameters);
			return Builtins.remainder(parameters.get(0), parameters.get(1));
		case setCar:
			assertParameterCount(2, parameters);
			return Builtins.setCar(parameters.get(0), parameters.get(1));
		case setCdr:
			assertParameterCount(2, parameters);
			return Builtins.setCdr(parameters.get(0), parameters.get(1));
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
		case write:
			assertParameterCount(1, parameters);
			return Builtins.write(parameters.get(0));
		default:
			throw new RuntimeException("Unknown builtin type " + _type);
		}
	}
}

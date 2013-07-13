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

public final class Builtins {
	public static SchemeObject cons(SchemeObject o1, SchemeObject o2) {
		return new Pair(o1, o2);
	}

	public static SchemeObject car(SchemeObject o) throws SchemeException {
		if (o instanceof Pair)
			return ((Pair) o).getCar();
		throw new SchemeException(
				"car: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	public static SchemeObject cdr(SchemeObject o) throws SchemeException {
		if (o instanceof Pair)
			return ((Pair) o).getCdr();
		throw new SchemeException(
				"car: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	public static SchemeObject setCar(SchemeObject o, SchemeObject newCar)
			throws SchemeException {
		if (o instanceof Pair) {
			((Pair) o).setCar(newCar);
			return newCar;
		}
		throw new SchemeException(
				"set-car!: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	public static SchemeObject setCdr(SchemeObject o, SchemeObject newCdr)
			throws SchemeException {
		if (o instanceof Pair) {
			((Pair) o).setCdr(newCdr);
			return newCdr;
		}
		throw new SchemeException(
				"set-cdr!: Invalid parameter type; expected Pair, got "
						+ o.getClass());
	}

	private static int toNumber(String procedure, SchemeObject o)
			throws SchemeException {
		if (o instanceof SchemeNumber)
			return ((SchemeNumber) o).getValue();
		else
			throw new SchemeException(procedure
					+ ": Invalid type conversion; expected Number, got "
					+ o.getClass());
	}

	public static SchemeObject add(List<SchemeObject> parameters)
			throws SchemeException {
		int ret = 0;
		for (SchemeObject o : parameters)
			ret += toNumber("+", o);
		return new SchemeNumber(ret);
	}

	public static SchemeObject sub(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() == 0)
			throw new SchemeException("-: Expected at least one parameter");

		int ret = toNumber("-", parameters.get(0));
		if (parameters.size() == 1)
			return new SchemeNumber(-ret);
		for (SchemeObject o : parameters.subList(1, parameters.size()))
			ret -= toNumber("-", o);
		return new SchemeNumber(ret);
	}

	public static SchemeObject mul(List<SchemeObject> parameters)
			throws SchemeException {
		int ret = 1;
		for (SchemeObject o : parameters)
			ret *= toNumber("*", o);
		return new SchemeNumber(ret);
	}

	public static SchemeObject div(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() == 0)
			throw new SchemeException("/: Expected at least one parameter");

		int ret = toNumber("/", parameters.get(0));
		if (parameters.size() == 1)
			return new SchemeNumber(1 / ret); // TODO Rationals
		for (SchemeObject o : parameters.subList(1, parameters.size()))
			ret /= toNumber("/", o); // TODO Rationals
		return new SchemeNumber(ret);
	}

	public static SchemeObject lt(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException("<: Expected at least two parameters");

		int last = toNumber("<", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			int now = toNumber("<", o);
			if (last >= now)
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject le(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException("<=: Expected at least two parameters");

		int last = toNumber("<=", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			int now = toNumber("<=", o);
			if (last > now)
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject gt(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException(">: Expected at least two parameters");

		int last = toNumber(">", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			int now = toNumber(">", o);
			if (last <= now)
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject ge(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException(">=: Expected at least two parameters");

		int last = toNumber(">=", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			int now = toNumber(">=", o);
			if (last < now)
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject numeq(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException("=: Expected at least two parameters");

		final int last = toNumber("=", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			int now = toNumber("=", o);
			if (last != now)
				return False.getInstance();
		}
		return True.getInstance();
	}

	public static SchemeObject quotient(SchemeObject p0, SchemeObject p1)
			throws SchemeException {
		return new SchemeNumber(toNumber("quotient", p0)
				/ toNumber("quotient", p1)); // TODO: assuming toNumber
												// returns an integer value
	}

	public static SchemeObject remainder(SchemeObject p0, SchemeObject p1)
			throws SchemeException {
		return new SchemeNumber(toNumber("remainder", p0)
				% toNumber("remainder", p1)); // TODO: assuming toNumber
												// returns an integer value
	}

	public static SchemeObject charToInt(SchemeObject o) throws SchemeException {
		if (o instanceof SchemeCharacter)
			return new SchemeNumber(((SchemeCharacter) o).getValue());
		throw new SchemeException(
				"char->integer: Invalid parameter type; expected character, got "
						+ o.getClass());
	}

	public static SchemeObject intToChar(SchemeObject o) throws SchemeException {
		if (o instanceof SchemeNumber)
			return new SchemeCharacter((char) ((SchemeNumber) o).getValue());
		throw new SchemeException(
				"integer->char: Invalid parameter type; expected integer, got "
						+ o.getClass());
	}

	public static SchemeObject write(SchemeObject o) throws SchemeException {
		System.out.print(o);
		return Symbol.fromString("undefined");
	}

	public static SchemeObject makeString(List<SchemeObject> parameters)
			throws SchemeException {
		switch (parameters.size()) {
		case 1:
			return new SchemeString(toNumber("make-string", parameters.get(0)));
		case 2:
			int length = toNumber("make-string", parameters.get(0));
			if (!(parameters.get(1) instanceof SchemeCharacter))
				throw new SchemeException(
						"make-string: Invalid parameter type; expected character as second parameter, got "
								+ parameters.get(1).getClass());
			char c = ((SchemeCharacter) parameters.get(1)).getValue();
			SchemeString ret = new SchemeString(length);
			for (int i = 0; i < length; ++i)
				ret.setAt(i, c);
			return ret;

		default:
			throw new SchemeException(
					"make-string: Expected 1 or 2 parameters, got "
							+ parameters.size());
		}
	}

	public static SchemeObject stringLength(SchemeObject o)
			throws SchemeException {
		if (o instanceof SchemeString)
			return new SchemeNumber(((SchemeString) o).getLength());
		throw new SchemeException(
				"string-length: Invalid parameter type; expected string, got "
						+ o.getClass());
	}

	public static SchemeObject stringRef(SchemeObject str, SchemeObject indexObj)
			throws SchemeException {
		if (!(str instanceof SchemeString))
			throw new SchemeException(
					"string-ref: Invalid parameter type; expected string, got "
							+ str.getClass());
		return new SchemeCharacter(((SchemeString) str).getAt(toNumber(
				"string-ref", indexObj)));
	}

	public static SchemeObject stringSet(SchemeObject str,
			SchemeObject indexObj, SchemeObject charObj) throws SchemeException {
		if (!(str instanceof SchemeString))
			throw new SchemeException(
					"string-set!: Invalid parameter type; expected string, got "
							+ str.getClass());
		if (!(charObj instanceof SchemeCharacter))
			throw new SchemeException(
					"string-set!: Invalid parameter type; expected character, got "
							+ charObj.getClass());
		((SchemeString) str).setAt(toNumber("string-set!", indexObj),
				((SchemeCharacter) charObj).getValue());
		return charObj;
	}

	public static SchemeObject stringToSymbol(SchemeObject str)
			throws SchemeException {
		if (!(str instanceof SchemeString))
			throw new SchemeException(
					"string->symbol: Invalid parameter type; expected string, got "
							+ str.getClass());
		return Symbol.fromString(((SchemeString) str).getValue());
	}

	public static SchemeObject symbolToString(SchemeObject sym)
			throws SchemeException {
		if (!(sym instanceof Symbol))
			throw new SchemeException(
					"symbol->string: Invalid parameter type; expected symbol, got "
							+ sym.getClass());
		return new SchemeString(((Symbol) sym).toString());
	}

	public static SchemeObject makeVector(List<SchemeObject> parameters)
			throws SchemeException {
		switch (parameters.size()) {
		case 1:
			return new Vector(toNumber("make-vector", parameters.get(0)));
		case 2:
			int length = toNumber("make-vector", parameters.get(0));
			SchemeObject o = parameters.get(1);
			Vector ret = new Vector(length);
			for (int i = 0; i < length; ++i)
				ret.setAt(i, o);
			return ret;

		default:
			throw new SchemeException(
					"make-vector: Expected 1 or 2 parameters, got "
							+ parameters.size());
		}
	}

	public static SchemeObject vectorLength(SchemeObject o)
			throws SchemeException {
		if (o instanceof Vector)
			return new SchemeNumber(((Vector) o).getLength());
		throw new SchemeException(
				"vector-length: Invalid parameter type; expected vector, got "
						+ o.getClass());
	}

	public static SchemeObject vectorRef(SchemeObject vec, SchemeObject indexObj)
			throws SchemeException {
		if (!(vec instanceof Vector))
			throw new SchemeException(
					"vector-ref: Invalid parameter type; expected vector, got "
							+ vec.getClass());
		return ((Vector) vec).getAt(toNumber("vector-ref", indexObj));
	}

	public static SchemeObject vectorSet(SchemeObject vec,
			SchemeObject indexObj, SchemeObject obj) throws SchemeException {
		if (!(vec instanceof Vector))
			throw new SchemeException(
					"vector-set!: Invalid parameter type; expected vector, got "
							+ vec.getClass());
		((Vector) vec).setAt(toNumber("vector-set!", indexObj), obj);
		return obj;
	}

}

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

	private static SchemeNumber asNumber(String procedure, SchemeObject o)
			throws SchemeException {
		if (o instanceof SchemeNumber)
			return ((SchemeNumber) o);
		else
			throw new SchemeException(procedure
					+ ": Invalid type conversion; expected Number, got "
					+ o.getClass());
	}

	public static SchemeObject add(List<SchemeObject> parameters)
			throws SchemeException {
		SchemeNumber ret = new Fixnum(0);
		for (SchemeObject o : parameters)
			ret = ret.add(asNumber("+", o));
		return ret;
	}

	public static SchemeObject sub(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() == 0)
			throw new SchemeException("-: Expected at least one parameter");

		SchemeNumber ret = asNumber("-", parameters.get(0));
		if (parameters.size() == 1)
			return new Fixnum(0).sub(ret);
		for (SchemeObject o : parameters.subList(1, parameters.size()))
			ret = ret.sub(asNumber("-", o));
		return ret;
	}

	public static SchemeObject mul(List<SchemeObject> parameters)
			throws SchemeException {
		SchemeNumber ret = new Fixnum(1);
		for (SchemeObject o : parameters)
			ret = ret.mul(asNumber("*", o));
		return ret;
	}

	public static SchemeObject div(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() == 0)
			throw new SchemeException("/: Expected at least one parameter");

		SchemeNumber ret = asNumber("/", parameters.get(0));
		if (parameters.size() == 1)
			return new Fixnum(1).div(ret);
		for (SchemeObject o : parameters.subList(1, parameters.size()))
			ret = ret.div(asNumber("/", o));
		return ret;
	}

	public static SchemeObject lt(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException("<: Expected at least two parameters");

		SchemeNumber last = asNumber("<", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			SchemeNumber now = asNumber("<", o);
			if (last.ge(now))
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject le(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException("<=: Expected at least two parameters");

		SchemeNumber last = asNumber("<=", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			SchemeNumber now = asNumber("<=", o);
			if (last.gt(now))
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject gt(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException(">: Expected at least two parameters");

		SchemeNumber last = asNumber(">", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			SchemeNumber now = asNumber(">", o);
			if (last.le(now))
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject ge(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException(">=: Expected at least two parameters");

		SchemeNumber last = asNumber(">=", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			SchemeNumber now = asNumber(">=", o);
			if (last.lt(now))
				return False.getInstance();
			last = now;
		}
		return True.getInstance();
	}

	public static SchemeObject numeq(List<SchemeObject> parameters)
			throws SchemeException {
		if (parameters.size() < 2)
			throw new SchemeException("=: Expected at least two parameters");

		final SchemeNumber last = asNumber("=", parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			SchemeNumber now = asNumber("=", o);
			if (!last.eq(now))
				return False.getInstance();
		}
		return True.getInstance();
	}

	public static SchemeObject quotient(SchemeObject p0, SchemeObject p1)
			throws SchemeException {
		return asNumber("quotient", p0).idiv(asNumber("quotient", p1));
	}

	public static SchemeObject remainder(SchemeObject p0, SchemeObject p1)
			throws SchemeException {
		return asNumber("remainder", p0).mod(asNumber("remainder", p1));
	}

	private static int toInteger(String procedure, SchemeObject o)
			throws SchemeException {
		if (o instanceof Fixnum)
			return ((Fixnum) o).getValue();
		else
			throw new SchemeException(procedure
					+ ": Invalid type conversion; expected Fixnum, got "
					+ o.getClass());
	}

	public static SchemeObject charToInt(SchemeObject o) throws SchemeException {
		if (o instanceof SchemeCharacter)
			return new Fixnum(((SchemeCharacter) o).getValue());
		throw new SchemeException(
				"char->integer: Invalid parameter type; expected character, got "
						+ o.getClass());
	}

	public static SchemeObject intToChar(SchemeObject o) throws SchemeException {
		return new SchemeCharacter((char) toInteger("integer->char", o));
	}

	public static SchemeObject write(SchemeObject o) throws SchemeException {
		System.out.print(o);
		return Symbol.fromString("undefined");
	}

	public static SchemeObject makeString(List<SchemeObject> parameters)
			throws SchemeException {
		switch (parameters.size()) {
		case 1:
			return new SchemeString(toInteger("make-string", parameters.get(0)));
		case 2:
			int length = toInteger("make-string", parameters.get(0));
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
			return new Fixnum(((SchemeString) o).getLength());
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
		return new SchemeCharacter(((SchemeString) str).getAt(toInteger(
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
		((SchemeString) str).setAt(toInteger("string-set!", indexObj),
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
			return new Vector(toInteger("make-vector", parameters.get(0)));
		case 2:
			int length = toInteger("make-vector", parameters.get(0));
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
			return new Fixnum(((Vector) o).getLength());
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
		return ((Vector) vec).getAt(toInteger("vector-ref", indexObj));
	}

	public static SchemeObject vectorSet(SchemeObject vec,
			SchemeObject indexObj, SchemeObject obj) throws SchemeException {
		if (!(vec instanceof Vector))
			throw new SchemeException(
					"vector-set!: Invalid parameter type; expected vector, got "
							+ vec.getClass());
		((Vector) vec).setAt(toInteger("vector-set!", indexObj), obj);
		return obj;
	}

	public static SchemeObject numberToString(List<SchemeObject> parameters)
			throws SchemeException {
		switch (parameters.size()) {
		case 1:
			return new SchemeString(asNumber("number->string",
					parameters.get(0)).toString(false));
		case 2:
			SchemeNumber num = asNumber("number->string", parameters.get(0));
			int base = toInteger("number->string", parameters.get(1));
			return new SchemeString(num.toString(false, base));

		default:
			throw new SchemeException(
					"number->string: Expected 1 or 2 parameters, got "
							+ parameters.size());
		}
	}

	public static SchemeObject stringToNumber(List<SchemeObject> parameters)
			throws SchemeException {
		try {
			switch (parameters.size()) {
			case 1:
				SchemeObject value = parameters.get(0);
				if (!(value instanceof SchemeString))
					throw new SchemeException(
							"string->number: Invalid parameter type; expected string, got "
									+ value.getClass());
				return SchemeNumber.fromString(
						((SchemeString) value).getValue(), 10);
			case 2:
				SchemeObject value_twoParams = parameters.get(0);
				if (!(value_twoParams instanceof SchemeString))
					throw new SchemeException(
							"string->number: Invalid parameter type; expected string, got "
									+ value_twoParams.getClass());
				int base = toInteger("string->number", parameters.get(1));
				return SchemeNumber.fromString(
						((SchemeString) value_twoParams).getValue(), base);

			default:
				throw new SchemeException(
						"string->number: Expected 1 or 2 parameters, got "
								+ parameters.size());
			}
		} catch (NumberFormatException ex) {
			throw new SchemeException(
					"string->number: Value can not be converted");
		}
	}
}

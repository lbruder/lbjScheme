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
		System.out.println(o);
		return Symbol.fromString("undefined");
	}
}

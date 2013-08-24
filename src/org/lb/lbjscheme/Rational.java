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

import java.math.BigInteger;
import java.util.regex.*;

public final class Rational extends SchemeNumber {
	private final static BigInteger _two = BigInteger.valueOf(2);

	private final BigInteger _n;
	private final BigInteger _d;

	private static final Pattern _rationalRegex = Pattern
			.compile("^([+-]?\\d+)/(\\d+)$");

	public Rational(int value) {
		_n = BigInteger.valueOf(value);
		_d = BigInteger.ONE;
	}

	public Rational(BigInteger value) {
		_n = value;
		_d = BigInteger.ONE;
	}

	private Rational(BigInteger n, BigInteger d) {
		_n = n;
		_d = d;
	}

	@Override
	public int getLevel() {
		return 3; // 1 = Fixnum, 2 = Bignum, 3 = Rational, 4 = Real, 5 = Complex
	}

	@Override
	public String toString(boolean forDisplay, int base) throws SchemeException {
		assertBaseTen(base);
		return _n.toString() + "/" + _d.toString();
	}

	private static void assertBaseTen(int base) throws SchemeException {
		if (base != 10)
			throw new SchemeException(
					"Rationals may only be converted from or to string in base 10");
	}

	@Override
	public SchemeNumber promote() {
		return new Real(_n.doubleValue() / _d.doubleValue());
	}

	public static SchemeNumber valueOf(String value, int base)
			throws SchemeException {
		assertBaseTen(base);
		Matcher m = _rationalRegex.matcher(value);
		if (!m.matches())
			throw new SchemeException(
					"Value can not be converted to a rational");
		BigInteger n = new BigInteger(m.group(1), 10);
		BigInteger d = new BigInteger(m.group(2), 10);
		return valueOf(n, d);
	}

	public static SchemeNumber valueOf(BigInteger n, BigInteger d) {
		BigInteger gcd = n.gcd(d);
		n = n.divide(gcd);
		d = d.divide(gcd);

		if (d.equals(BigInteger.ONE))
			return Bignum.valueOf(n);
		else if (d.signum() == -1)
			return new Rational(n.negate(), d.negate());
		else
			return new Rational(n, d);
	}

	@Override
	public SchemeNumber getNumerator() {
		return Bignum.valueOf(_n);
	}

	@Override
	public SchemeNumber getDenominator() {
		return Bignum.valueOf(_d);
	}

	@Override
	protected SchemeNumber doAdd(SchemeNumber other) {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._d).add(_d.multiply(o._n)),
				_d.multiply(o._d));
	}

	@Override
	public SchemeNumber doSub(SchemeNumber other) {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._d).subtract(_d.multiply(o._n)),
				_d.multiply(o._d));
	}

	@Override
	public SchemeNumber doMul(SchemeNumber other) {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._n), _d.multiply(o._d));
	}

	@Override
	public SchemeNumber doDiv(SchemeNumber other) {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._d), _d.multiply(o._n));
	}

	@Override
	public SchemeNumber doIdiv(SchemeNumber other) throws SchemeException {
		throw new SchemeException("quotient: Integer expected");
	}

	@Override
	public SchemeNumber doMod(SchemeNumber other) throws SchemeException {
		throw new SchemeException("remainder: Integer expected");
	}

	@Override
	public boolean isZero() {
		// No Rational can ever be zero, as it would be converted to a Fixnum
		// on the fly. Using this implementation for reference purposes.
		return _n.compareTo(BigInteger.ZERO) == 0;
	}

	@Override
	protected int doCompareTo(SchemeNumber other) {
		BigInteger diff = _n.multiply(((Rational) other)._d).subtract(
				_d.multiply(((Rational) other)._n));
		return diff.signum();
	}

	@Override
	public SchemeNumber roundToInteger() {
		BigInteger n = _n;
		BigInteger d = _d;
		if (_d.testBit(0)) {
			// make sure denominator is divisible by 2
			n = n.multiply(_two);
			d = d.multiply(_two);
		}

		return Bignum.valueOf(n.add(d.divide(_two)).divide(d));
	}
}

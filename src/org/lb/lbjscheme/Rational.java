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

import java.math.*;
import java.util.regex.*;

public final class Rational extends SchemeNumber {
	private final static BigInteger _two = BigInteger.valueOf(2);

	private final BigInteger _n;
	private final BigInteger _d;
	private final boolean _isExact;

	private static final Pattern _rationalRegex = Pattern
			.compile("^([+-]?\\d+)/(\\d+)$");

	public Rational(int value) {
		_n = BigInteger.valueOf(value);
		_d = BigInteger.ONE;
		_isExact = true;
	}

	public Rational(BigInteger value) {
		_n = value;
		_d = BigInteger.ONE;
		_isExact = true;
	}

	private Rational(BigInteger n, BigInteger d, boolean isExact)
			throws SchemeException {
		if (d.equals(BigInteger.ZERO))
			throw new SchemeException("Division by zero");
		_n = n;
		_d = d;
		_isExact = isExact;
	}

	@Override
	public int getLevel() {
		return 3; // 1 = Fixnum, 2 = Bignum, 3 = Rational, 4 = Real, 5 = Complex
	}

	@Override
	public String toString(boolean forDisplay, int base) throws SchemeException {
		assertBaseTen(base);
		if (_isExact) return _n.toString() + "/" + _d.toString();
		return promoteToLevel(4).toString(forDisplay, base);
	}

	private static void assertBaseTen(int base) throws SchemeException {
		if (base != 10)
			throw new SchemeException(
					"Rationals may only be converted from or to string in base 10");
	}

	@Override
	public SchemeNumber promoteToLevel(int targetLevel) {
		// TODO: Infinity => Exception!
		if (targetLevel == 5) // promote to complex
			return new Complex(this);
		return new Real(new BigDecimal(_n).divide(new BigDecimal(_d),
				MathContext.DECIMAL64).doubleValue());
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
		return valueOf(n, d, true);
	}

	public static SchemeNumber valueOf(BigInteger n, BigInteger d,
			boolean isExact) throws SchemeException {
		BigInteger gcd = n.gcd(d);
		n = n.divide(gcd);
		d = d.divide(gcd);

		if (d.equals(BigInteger.ONE))
			return isExact ? Bignum.valueOf(n) : new Real(n);
		else if (d.signum() == -1)
			return new Rational(n.negate(), d.negate(), isExact);
		else
			return new Rational(n, d, isExact);
	}

	@Override
	public SchemeNumber getNumerator() {
		return _isExact ? Bignum.valueOf(_n) : Bignum.valueOf(_n).makeInexact();
	}

	@Override
	public SchemeNumber getDenominator() {
		return _isExact ? Bignum.valueOf(_d) : Bignum.valueOf(_d).makeInexact();
	}

	@Override
	public boolean isExact() {
		return _isExact;
	}

	@Override
	protected SchemeNumber doAdd(SchemeNumber other) throws SchemeException {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._d).add(_d.multiply(o._n)),
				_d.multiply(o._d), _isExact && o._isExact);
	}

	@Override
	public SchemeNumber doSub(SchemeNumber other) throws SchemeException {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._d).subtract(_d.multiply(o._n)),
				_d.multiply(o._d), _isExact && o._isExact);
	}

	@Override
	public SchemeNumber doMul(SchemeNumber other) throws SchemeException {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._n), _d.multiply(o._d), _isExact
				&& o._isExact);
	}

	@Override
	public SchemeNumber doDiv(SchemeNumber other) throws SchemeException {
		Rational o = (Rational) other;
		return valueOf(_n.multiply(o._d), _d.multiply(o._n), _isExact
				&& o._isExact);
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
	public boolean isInteger() {
		// Integral rationals are converted to Fixnum or Bignum on the fly and
		// can therefore never be integral
		return false;
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
	public SchemeNumber roundToNearestInteger() {
		BigInteger n = _n;
		BigInteger d = _d;
		if (_d.testBit(0)) {
			// make sure denominator is divisible by 2
			n = n.multiply(_two);
			d = d.multiply(_two);
		}

		if (_n.signum() == 1) n = n.add(d.divide(_two));
		if (_n.signum() == -1) n = n.subtract(d.divide(_two));

		return Bignum.valueOf(n.divide(d));
	}

	@Override
	public SchemeNumber makeInexact() {
		try {
			return new Rational(_n, _d, false);
		} catch (SchemeException e) {
			throw new RuntimeException("Impossible exception");
		}
	}

	@Override
	public SchemeNumber makeExact() {
		if (_isExact) return this;
		try {
			return new Rational(_n, _d, true);
		} catch (SchemeException e) {
			throw new RuntimeException("Impossible exception");
		}
	}

	@Override
	public SchemeNumber floor() {
		final SchemeNumber ret = Bignum.valueOf(_n.subtract(_n.mod(_d)).divide(
				_d));
		return _isExact ? ret : ret.makeInexact();
	}

	@Override
	public SchemeNumber ceiling() {
		final SchemeNumber ret = Bignum.valueOf(_n.subtract(_n.mod(_d))
				.divide(_d).add(BigInteger.ONE));
		return _isExact ? ret : ret.makeInexact();
	}

	@Override
	public SchemeNumber truncate() {
		final SchemeNumber ret = Bignum.valueOf(_n.divide(_d));
		return _isExact ? ret : ret.makeInexact();
	}

	@Override
	public SchemeNumber round() {
		final SchemeNumber ret = Bignum.valueOf(new BigDecimal(_n)
				.divide(new BigDecimal(_d))
				.setScale(0, BigDecimal.ROUND_HALF_EVEN).toBigInteger());
		return _isExact ? ret : ret.makeInexact();
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		return new BigDecimal(_n).divide(new BigDecimal(_d),
				MathContext.DECIMAL64).doubleValue();
	}
}

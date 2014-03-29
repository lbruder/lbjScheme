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

public final class Real extends SchemeNumber {
	private final static BigDecimal _oneHalf = BigDecimal.valueOf(5, 1);
	private final double _value;

	public Real(int value) {
		_value = value;
	}

	public Real(BigInteger value) {
		_value = value.doubleValue();
	}

	public Real(double value) {
		_value = value;
	}

	@Override
	public int getLevel() {
		return 4; // 1 = Fixnum, 2 = Bignum, 3 = Rational, 4 = Real, 5 = Complex
	}

	@Override
	public String toString(boolean forDisplay, int base) throws SchemeException {
		assertBaseTen(base);
		return Double.toString(_value);
	}

	private static void assertBaseTen(int base) throws SchemeException {
		if (base != 10)
			throw new SchemeException(
					"Real numbers may only be converted from or to string in base 10");
	}

	@Override
	public SchemeNumber promoteToLevel(int targetLevel) {
		return new Complex(this);
	}

	public static SchemeNumber valueOf(String value, int base)
			throws SchemeException {
		assertBaseTen(base);

		value = value.replaceAll("[sfdl]", "e");

		try {
			return new Real(Double.parseDouble(value));
		} catch (NumberFormatException ex) {
			throw new SchemeException("Value '" + value
					+ "' can not be parsed as a real number");
		}
	}

	@Override
	public SchemeNumber getNumerator() {
		return this;
	}

	@Override
	public SchemeNumber getDenominator() {
		return new Fixnum(1);
	}

	@Override
	protected SchemeNumber doAdd(SchemeNumber other) {
		Real o = (Real) other;
		return new Real(_value + o._value);
	}

	@Override
	public SchemeNumber doSub(SchemeNumber other) {
		Real o = (Real) other;
		return new Real(_value - o._value);
	}

	@Override
	public SchemeNumber doMul(SchemeNumber other) {
		Real o = (Real) other;
		return new Real(_value * o._value);
	}

	@Override
	public SchemeNumber doDiv(SchemeNumber other) {
		Real o = (Real) other;
		return new Real(_value / o._value);
	}

	@Override
	public SchemeNumber doIdiv(SchemeNumber other) throws SchemeException {
		throw new SchemeException("quotient: Integer expected");
	}

	@Override
	public SchemeNumber doMod(SchemeNumber other) throws SchemeException {
		final Real r = (Real) other;
		if (isInteger() && r.isInteger()) return new Real(_value % r._value);
		throw new SchemeException("remainder: Integer expected");
	}

	@Override
	protected int doCompareTo(SchemeNumber other) {
		if (_value > ((Real) other)._value) return 1;
		if (_value < ((Real) other)._value) return -1;
		return 0;
	}

	@Override
	public boolean isExact() {
		return false;
	}

	@Override
	public boolean isReal() {
		return true;
	}

	@Override
	public boolean isInteger() {
		return Double.toString(_value).endsWith(".0");
	}

	@Override
	public boolean isZero() {
		return _value == 0.0;
	}

	@Override
	public SchemeNumber makeExact() {
		final BigDecimal number = new BigDecimal(Double.toString(_value));
		int scale = number.scale();
		BigInteger numerator = number.movePointRight(scale).toBigInteger();
		while (scale < 0) {
			numerator = numerator.multiply(BigInteger.TEN);
			scale++;
		}
		final BigInteger denominator = BigInteger.TEN.pow(scale);

		try {
			return Rational.valueOf(numerator, denominator, true);
		} catch (SchemeException e) {
			throw new RuntimeException("Impossible exception");
		}
	}

	public double getValue() {
		return _value;
	}

	@Override
	public SchemeNumber roundToNearestInteger() {
		BigDecimal n = new BigDecimal(Double.toString(_value));
		if (n.signum() == 1) n = n.add(_oneHalf);
		if (n.signum() == -1) n = n.subtract(_oneHalf);

		return Bignum.valueOf(n.toBigInteger());
	}

	@Override
	public SchemeNumber makeInexact() {
		return this; // Reals are inexact already
	}

	@Override
	public SchemeNumber floor() throws SchemeException {
		return Bignum.valueOf(
				BigDecimal.valueOf(_value).setScale(0, BigDecimal.ROUND_FLOOR)
						.toBigInteger()).makeInexact();
	}

	@Override
	public SchemeNumber ceiling() throws SchemeException {
		return Bignum.valueOf(
				BigDecimal.valueOf(_value)
						.setScale(0, BigDecimal.ROUND_CEILING).toBigInteger())
				.makeInexact();
	}

	@Override
	public SchemeNumber truncate() throws SchemeException {
		return Bignum.valueOf(
				BigDecimal.valueOf(_value).setScale(0, BigDecimal.ROUND_DOWN)
						.toBigInteger()).makeInexact();

	}

	@Override
	public SchemeNumber round() throws SchemeException {
		return Bignum
				.valueOf(
						BigDecimal.valueOf(_value)
								.setScale(0, BigDecimal.ROUND_HALF_EVEN)
								.toBigInteger()).makeInexact();
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		return _value;
	}
}

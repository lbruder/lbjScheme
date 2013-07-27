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

public final class Bignum extends SchemeNumber {
	private final BigInteger _value;

	public Bignum(String value, int base) {
		_value = new BigInteger(value, base);
	}

	public Bignum(long value) {
		_value = BigInteger.valueOf(value);
	}

	public Bignum(BigInteger value) {
		_value = value;
	}

	@Override
	public int getLevel() {
		return 2; // 1 = Fixnum, 2 = Bignum, 3 = Rational, 4 = Real, 5 = Complex
	}

	@Override
	public String toString(boolean forDisplay, int base) {
		return _value.toString(base);
	}

	@Override
	public SchemeNumber promote() {
		return null; // TODO
	}

	@Override
	protected SchemeNumber doAdd(SchemeNumber other) {
		return new Bignum(_value.add(((Bignum) other)._value));
	}

	@Override
	public SchemeNumber doSub(SchemeNumber other) {
		return new Bignum(_value.subtract(((Bignum) other)._value));
	}

	@Override
	public SchemeNumber doMul(SchemeNumber other) {
		return new Bignum(_value.multiply(((Bignum) other)._value));
	}

	@Override
	public SchemeNumber doDiv(SchemeNumber other) {
		return new Bignum(_value.divide(((Bignum) other)._value));
	}

	@Override
	public SchemeNumber doIdiv(SchemeNumber other) {
		return new Bignum(_value.divide(((Bignum) other)._value));
	}

	@Override
	public SchemeNumber doMod(SchemeNumber other) {
		return new Bignum(_value.mod(((Bignum) other)._value));
	}

	@Override
	public boolean doEq(SchemeNumber other) {
		return _value.compareTo(((Bignum) other)._value) == 0;
	}

	@Override
	public boolean doLt(SchemeNumber other) {
		return _value.compareTo(((Bignum) other)._value) < 0;
	}

	@Override
	public boolean doLe(SchemeNumber other) {
		return _value.compareTo(((Bignum) other)._value) <= 0;
	}

	@Override
	public boolean doGt(SchemeNumber other) {
		return _value.compareTo(((Bignum) other)._value) > 0;
	}

	@Override
	public boolean doGe(SchemeNumber other) {
		return _value.compareTo(((Bignum) other)._value) >= 0;
	}
}

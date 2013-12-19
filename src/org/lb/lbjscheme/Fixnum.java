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

public final class Fixnum extends SchemeNumber {
	private final int _value;

	public Fixnum(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	@Override
	public int getLevel() {
		return 1; // 1 = Fixnum, 2 = Bignum, 3 = Rational, 4 = Real, 5 = Complex
	}

	@Override
	public String toString(boolean forDisplay, int base) {
		return Integer.toString(_value, base);
	}

	@Override
	public SchemeNumber promoteToLevel(int targetLevel) {
		return new Bignum(_value);
	}

	public static Fixnum valueOf(String value, int base) {
		return new Fixnum(Integer.parseInt(value, base));
	}

	public static SchemeNumber valueOf(long value) {
		if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
			return new Bignum(value);
		return new Fixnum((int) value);
	}

	@Override
	protected SchemeNumber doAdd(SchemeNumber other) {
		return valueOf((long) _value + (long) ((Fixnum) other)._value);
	}

	@Override
	public SchemeNumber doSub(SchemeNumber other) {
		return valueOf((long) _value - (long) ((Fixnum) other)._value);
	}

	@Override
	public SchemeNumber doMul(SchemeNumber other) {
		return valueOf((long) _value * (long) ((Fixnum) other)._value);
	}

	@Override
	public SchemeNumber doDiv(SchemeNumber other) throws SchemeException {
		return new Rational(_value).div(other);
	}

	@Override
	public SchemeNumber doIdiv(SchemeNumber other) {
		return valueOf((long) _value / (long) ((Fixnum) other)._value);
	}

	@Override
	public SchemeNumber doMod(SchemeNumber other) {
		return valueOf((long) _value % (long) ((Fixnum) other)._value);
	}

	@Override
	public boolean isZero() {
		return _value == 0;
	}

	@Override
	protected int doCompareTo(SchemeNumber other) {
		if (_value > ((Fixnum) other)._value) return 1;
		if (_value < ((Fixnum) other)._value) return -1;
		return 0;
	}

	@Override
	public SchemeNumber roundToNearestInteger() {
		return this;
	}

	@Override
	public SchemeNumber floor() {
		return this;
	}

	@Override
	public SchemeNumber ceiling() {
		return this;
	}

	@Override
	public SchemeNumber truncate() {
		return this;
	}

	@Override
	public SchemeNumber round() {
		return this;
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		return _value;
	}
}

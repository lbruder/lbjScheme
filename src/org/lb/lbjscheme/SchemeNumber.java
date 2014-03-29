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

public abstract class SchemeNumber extends SchemeObject {
	@Override
	public String toString(boolean forDisplay) {
		try {
			return toString(forDisplay, 10);
		} catch (Exception ex) {
			return "<internal error>";
		}
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	public SchemeNumber add(SchemeNumber other) throws SchemeException {
		if (other.getLevel() == getLevel()) return doAdd(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).add(other);
		return add(other.promoteToLevel(getLevel()));
	}

	public SchemeNumber sub(SchemeNumber other) throws SchemeException {
		if (other.getLevel() == getLevel()) return doSub(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).sub(other);
		return sub(other.promoteToLevel(getLevel()));
	}

	public SchemeNumber mul(SchemeNumber other) throws SchemeException {
		if (other.getLevel() == getLevel()) return doMul(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).mul(other);
		return mul(other.promoteToLevel(getLevel()));
	}

	public SchemeNumber div(SchemeNumber other) throws SchemeException {
		if (other.getLevel() == getLevel()) return doDiv(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).div(other);
		return div(other.promoteToLevel(getLevel()));
	}

	public SchemeNumber idiv(SchemeNumber other) throws SchemeException {
		if (other.getLevel() == getLevel()) return doIdiv(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).idiv(other);
		return idiv(other.promoteToLevel(getLevel()));
	}

	public SchemeNumber mod(SchemeNumber other) throws SchemeException {
		if (other.getLevel() == getLevel()) return doMod(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).mod(other);
		return mod(other.promoteToLevel(getLevel()));
	}

	public boolean eq(SchemeNumber other) throws SchemeException {
		return compareTo(other) == 0;
	}

	public boolean lt(SchemeNumber other) throws SchemeException {
		return compareTo(other) < 0;
	}

	public boolean le(SchemeNumber other) throws SchemeException {
		return compareTo(other) <= 0;
	}

	public boolean gt(SchemeNumber other) throws SchemeException {
		return compareTo(other) > 0;
	}

	public boolean ge(SchemeNumber other) throws SchemeException {
		return compareTo(other) >= 0;
	}

	public SchemeNumber getNumerator() throws SchemeException {
		return this;
	}

	public SchemeNumber getDenominator() throws SchemeException {
		return Fixnum.valueOf(1);
	}

	public boolean isExact() {
		return true;
	}

	public int compareTo(SchemeNumber other) {
		if (other.getLevel() == getLevel()) return doCompareTo(other);
		if (other.getLevel() > getLevel())
			return promoteToLevel(other.getLevel()).compareTo(other);
		return compareTo(other.promoteToLevel(getLevel()));
	}

	public abstract boolean isZero();

	public abstract int getLevel();

	public abstract SchemeNumber promoteToLevel(int targetLevel);

	public abstract String toString(boolean forDisplay, int base)
			throws SchemeException;

	public abstract SchemeNumber roundToNearestInteger() throws SchemeException;

	protected abstract int doCompareTo(SchemeNumber other);

	protected abstract SchemeNumber doAdd(SchemeNumber other)
			throws SchemeException;

	protected abstract SchemeNumber doSub(SchemeNumber other)
			throws SchemeException;

	protected abstract SchemeNumber doMul(SchemeNumber other)
			throws SchemeException;

	protected abstract SchemeNumber doDiv(SchemeNumber other)
			throws SchemeException;

	protected abstract SchemeNumber doIdiv(SchemeNumber other)
			throws SchemeException;

	protected abstract SchemeNumber doMod(SchemeNumber other)
			throws SchemeException;

	public abstract SchemeNumber makeInexact();

	public abstract SchemeNumber makeExact();

	public abstract SchemeNumber floor() throws SchemeException;

	public abstract SchemeNumber ceiling() throws SchemeException;

	public abstract SchemeNumber truncate() throws SchemeException;

	public abstract SchemeNumber round() throws SchemeException;

	public static SchemeNumber fromString(String value, int base)
			throws SchemeException {
		try {
			return Fixnum.valueOf(value, base);
		} catch (Exception ex) {
			try {
				return Bignum.valueOf(value, base);
			} catch (Exception ex2) {
				try {
					return Rational.valueOf(value, base);
				} catch (Exception ex3) {
					try {
						return Real.valueOf(value.charAt(0)
								+ value.substring(1).replace("#", "0"), base);
					} catch (Exception ex4) {
						try {
							return Complex.valueOf(value, base);
						} catch (Exception ex5) {
							throw new SchemeException(
									"The string '"
											+ value
											+ "' can not be converted to a number in base "
											+ base);
						}
					}
				}
			}
		}
	}
}

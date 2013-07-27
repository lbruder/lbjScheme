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

public abstract class SchemeNumber implements SchemeObject {
	@Override
	public String toString() {
		return toString(false, 10);
	}

	@Override
	public String toString(boolean forDisplay) {
		return toString(forDisplay, 10);
	}

	public SchemeNumber add(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doAdd(other);
		if (other.getLevel() > getLevel())
			return promote().add(other);
		else
			return add(other.promote());
	}

	public SchemeNumber sub(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doSub(other);
		if (other.getLevel() > getLevel())
			return promote().sub(other);
		else
			return sub(other.promote());
	}

	public SchemeNumber mul(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doMul(other);
		if (other.getLevel() > getLevel())
			return promote().mul(other);
		else
			return mul(other.promote());
	}

	public SchemeNumber div(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doDiv(other);
		if (other.getLevel() > getLevel())
			return promote().div(other);
		else
			return div(other.promote());
	}

	public SchemeNumber idiv(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doIdiv(other);
		if (other.getLevel() > getLevel())
			return promote().idiv(other);
		else
			return idiv(other.promote());
	}

	public SchemeNumber mod(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doMod(other);
		if (other.getLevel() > getLevel())
			return promote().mod(other);
		else
			return mod(other.promote());
	}

	public boolean eq(SchemeNumber other) {
		if (other.getLevel() > getLevel())
			return promote().eq(other);
		if (other.getLevel() < getLevel())
			return eq(other.promote());
		return doEq(other);
	}

	public boolean lt(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doLt(other);
		if (other.getLevel() > getLevel())
			return promote().lt(other);
		else
			return lt(other.promote());
	}

	public boolean le(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doLe(other);
		if (other.getLevel() > getLevel())
			return promote().le(other);
		else
			return le(other.promote());
	}

	public boolean gt(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doGt(other);
		if (other.getLevel() > getLevel())
			return promote().gt(other);
		else
			return gt(other.promote());
	}

	public boolean ge(SchemeNumber other) {
		if (other.getLevel() == getLevel())
			return doGe(other);
		if (other.getLevel() > getLevel())
			return promote().ge(other);
		else
			return ge(other.promote());
	}

	public abstract int getLevel();

	public abstract SchemeNumber promote();

	public abstract String toString(boolean forDisplay, int base);

	protected abstract SchemeNumber doAdd(SchemeNumber other);

	protected abstract SchemeNumber doSub(SchemeNumber other);

	protected abstract SchemeNumber doMul(SchemeNumber other);

	protected abstract SchemeNumber doDiv(SchemeNumber other);

	protected abstract SchemeNumber doIdiv(SchemeNumber other);

	protected abstract SchemeNumber doMod(SchemeNumber other);

	protected abstract boolean doEq(SchemeNumber other);

	protected abstract boolean doLt(SchemeNumber other);

	protected abstract boolean doLe(SchemeNumber other);

	protected abstract boolean doGt(SchemeNumber other);

	protected abstract boolean doGe(SchemeNumber other);

	public static SchemeNumber fromString(String value, int base) {
		return new Fixnum(Integer.parseInt(value, base)); // TODO: Others
	}
}

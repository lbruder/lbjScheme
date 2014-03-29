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

import java.util.*;

public final class Nil extends SchemeList {
	private Nil() {
	}

	private static final Nil _instance = new Nil();

	public static Nil getInstance() {
		return _instance;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public boolean isDottedList() {
		return false;
	}

	@Override
	public List<SchemeObject> toJavaList() {
		return new ArrayList<>();
	}

	@Override
	public String toString(boolean forDisplay) {
		return "()";
	}

	@Override
	public Iterator<SchemeObject> iterator() {
		return new PairIterator(this);
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		return null;
	}
}

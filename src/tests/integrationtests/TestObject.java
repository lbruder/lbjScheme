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

package tests.integrationtests;

import java.util.*;

public final class TestObject {
	private int _value;

	public int getValue() {
		return _value;
	}

	public void setValue(int value) {
		_value = value;
	}

	public String reverseString(String param) {
		StringBuilder sb = new StringBuilder(param.length());
		for (int i = param.length() - 1; i >= 0; --i)
			sb.append(param.charAt(i));
		return sb.toString();
	}

	public List<Integer> primesUpTo(int max) {
		boolean[] sieve = new boolean[max];
		List<Integer> ret = new ArrayList<>();
		for (int i = 2; i < max; ++i) {
			if (!sieve[i]) {
				ret.add(i);
				for (int j = i; j < max; j += i)
					sieve[j] = true;
			}
		}
		return ret;
	}
}

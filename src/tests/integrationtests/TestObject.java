package tests.integrationtests;

import java.util.*;

public class TestObject {
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

package org.lb.lbjscheme;

import java.util.*;

public abstract class Builtin implements SchemeObject {
	private final String _name;

	public Builtin(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	@Override
	public String toString() {
		return "Builtin procedure " + _name;
	}

	protected void assertParameterCount(int expected, List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected != got) throw new SchemeException(_name + ": Invalid parameter count; expected: " + expected + ", got: " + got);
	}

	public abstract SchemeObject apply(List<SchemeObject> parameters) throws SchemeException;
}

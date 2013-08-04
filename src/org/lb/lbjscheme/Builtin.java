package org.lb.lbjscheme;

import java.util.List;

public abstract class Builtin implements SchemeObject {

	protected static final True _true = True.getInstance();
	protected static final False _false = False.getInstance();

	public abstract String getName();

	public abstract SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException;

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(boolean forDisplay) {
		return "Builtin procedure " + getName();
	}

	protected void assertParameterCount(int expected,
			List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected != got)
			throw new SchemeException(getName()
					+ ": Invalid parameter count; expected: " + expected
					+ ", got: " + got);
	}

	protected void assertParameterCountMin(int expected,
			List<SchemeObject> parameters) throws SchemeException {
		final int got = parameters.size();
		if (expected > got)
			throw new SchemeException(getName()
					+ ": Invalid parameter count; expected at least: "
					+ expected + ", got: " + got);
	}

	protected void assertParameterType(SchemeObject o,
			Class<? extends SchemeObject> expected) throws SchemeException {
		final Class<? extends SchemeObject> got = o.getClass();
		if (got.equals(expected))
			return;
		throw new SchemeException(getName()
				+ ": Invalid parameter type; expected: " + expected + ", got: "
				+ got);
	}

	protected SchemeNumber asNumber(SchemeObject o) throws SchemeException {
		if (o instanceof SchemeNumber)
			return ((SchemeNumber) o);
		else
			throw new SchemeException(getName()
					+ ": Invalid type conversion; expected Number, got "
					+ o.getClass());
	}

	protected int getFixnum(SchemeObject o) throws SchemeException {
		if (o instanceof Fixnum)
			return ((Fixnum) o).getValue();
		else
			throw new SchemeException(getName()
					+ ": Invalid type conversion; expected Fixnum, got "
					+ o.getClass());
	}
}
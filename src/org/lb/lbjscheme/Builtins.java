package org.lb.lbjscheme;

public final class Builtins {
	public static SchemeObject cons(SchemeObject o1, SchemeObject o2) {
		return new Pair(o1, o2);
	}

	public static SchemeObject car(SchemeObject o) throws SchemeException {
		if (o instanceof Pair) return ((Pair) o).getCar();
		throw new SchemeException("car: Invalid parameter type; expected Pair, got " + o.getClass());
	}

	public static SchemeObject cdr(SchemeObject o) throws SchemeException {
		if (o instanceof Pair) return ((Pair) o).getCdr();
		throw new SchemeException("car: Invalid parameter type; expected Pair, got " + o.getClass());
	}

	public static SchemeObject setCar(SchemeObject o, SchemeObject newCar) throws SchemeException {
		if (o instanceof Pair) {
			((Pair) o).setCar(newCar);
			return newCar;
		}
		throw new SchemeException("set-car!: Invalid parameter type; expected Pair, got " + o.getClass());
	}

	public static SchemeObject setCdr(SchemeObject o, SchemeObject newCdr) throws SchemeException {
		if (o instanceof Pair) {
			((Pair) o).setCdr(newCdr);
			return newCdr;
		}
		throw new SchemeException("set-cdr!: Invalid parameter type; expected Pair, got " + o.getClass());
	}
}

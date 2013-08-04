package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Car extends Builtin {
	@Override
	public String getName() {
		return "car";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(1, parameters);
		SchemeObject o = parameters.get(0);
		assertParameterType(o, Pair.class);
		return ((Pair) o).getCar();
	}
}

package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class SetCar extends Builtin {
	@Override
	public String getName() {
		return "set-car!";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(2, parameters);
		SchemeObject o = parameters.get(0);
		SchemeObject newCar = parameters.get(1);
		assertParameterType(o, Pair.class);
		((Pair) o).setCar(newCar);
		return newCar;
	}
}

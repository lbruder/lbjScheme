package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class VectorSet extends Builtin {
	@Override
	public String getName() {
		return "vector-set!";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(3, parameters);
		SchemeObject vec = parameters.get(0);
		SchemeObject valueToSet = parameters.get(2);
		assertParameterType(vec, Vector.class);
		((Vector) vec).setAt(getFixnum(parameters.get(1)), valueToSet);
		return valueToSet;
	}
}

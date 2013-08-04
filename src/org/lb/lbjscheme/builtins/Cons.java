package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Cons extends Builtin {
	@Override
	public String getName() {
		return "cons";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(2, parameters);
		return new Pair(parameters.get(0), parameters.get(1));
	}
}

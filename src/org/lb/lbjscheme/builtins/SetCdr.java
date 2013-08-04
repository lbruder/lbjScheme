package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class SetCdr extends Builtin {
	@Override
	public String getName() {
		return "set-cdr!";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(2, parameters);
		SchemeObject o = parameters.get(0);
		SchemeObject newCdr = parameters.get(1);
		assertParameterType(o, Pair.class);
		((Pair) o).setCdr(newCdr);
		return newCdr;
	}
}

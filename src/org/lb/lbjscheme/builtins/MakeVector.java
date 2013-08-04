package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class MakeVector extends Builtin {
	@Override
	public String getName() {
		return "make-vector";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCountMin(1, parameters);
		assertParameterCountMax(2, parameters);
		final int length = getFixnum(parameters.get(0));
		final Vector ret = new Vector(length);

		if (parameters.size() == 1)
			return ret;

		final SchemeObject valueToSet = parameters.get(1);
		for (int i = 0; i < length; ++i)
			ret.setAt(i, valueToSet);
		return ret;
	}
}

package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Sub extends Builtin {
	@Override
	public String getName() {
		return "-";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCountMin(1, parameters);
		SchemeNumber ret = asNumber(parameters.get(0));
		if (parameters.size() == 1)
			return new Fixnum(0).sub(ret);
		for (SchemeObject o : parameters.subList(1, parameters.size()))
			ret = ret.sub(asNumber(o));
		return ret;
	}
}

package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Gt extends Builtin {
	@Override
	public String getName() {
		return ">";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCountMin(2, parameters);
		SchemeNumber last = asNumber(parameters.get(0));
		for (SchemeObject o : parameters.subList(1, parameters.size())) {
			SchemeNumber now = asNumber(o);
			if (last.le(now))
				return _false;
			last = now;
		}
		return _true;
	}
}

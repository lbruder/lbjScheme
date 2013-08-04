package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Add extends Builtin {
	@Override
	public String getName() {
		return "+";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		SchemeNumber ret = new Fixnum(0);
		for (SchemeObject o : parameters)
			ret = ret.add(asNumber(o));
		return ret;
	}
}

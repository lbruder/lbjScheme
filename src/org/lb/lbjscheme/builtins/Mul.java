package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class Mul extends Builtin {
	@Override
	public String getName() {
		return "*";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		SchemeNumber ret = new Fixnum(1);
		for (SchemeObject o : parameters)
			ret = ret.mul(asNumber(o));
		return ret;
	}
}

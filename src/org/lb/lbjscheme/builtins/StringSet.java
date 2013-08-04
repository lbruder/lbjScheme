package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class StringSet extends Builtin {
	@Override
	public String getName() {
		return "string-set!";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCount(3, parameters);
		SchemeObject str = parameters.get(0);
		SchemeObject charObj = parameters.get(2);
		assertParameterType(str, SchemeString.class);
		assertParameterType(charObj, SchemeCharacter.class);
		((SchemeString) str).setAt(getFixnum(parameters.get(1)),
				((SchemeCharacter) charObj).getValue());
		return charObj;
	}
}

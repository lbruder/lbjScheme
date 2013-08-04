package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class MakeString extends Builtin {
	@Override
	public String getName() {
		return "make-string";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		assertParameterCountMin(1, parameters);
		assertParameterCountMax(2, parameters);
		final int length = getFixnum(parameters.get(0));
		final SchemeString ret = new SchemeString(length);

		if (parameters.size() == 1)
			return ret;

		final SchemeObject asChar = parameters.get(1);
		assertParameterType(asChar, SchemeCharacter.class);
		final char c = ((SchemeCharacter) asChar).getValue();
		for (int i = 0; i < length; ++i)
			ret.setAt(i, c);
		return ret;
	}
}

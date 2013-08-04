package org.lb.lbjscheme.builtins;

import java.util.List;
import org.lb.lbjscheme.*;

public final class StringToNumber extends Builtin {
	@Override
	public String getName() {
		return "string->number";
	}

	@Override
	public SchemeObject apply(List<SchemeObject> parameters)
			throws SchemeException {
		try {
			assertParameterCountMin(1, parameters);
			assertParameterCountMax(2, parameters);
			SchemeObject value = parameters.get(0);
			assertParameterType(value, SchemeString.class);
			return SchemeNumber.fromString(((SchemeString) value).getValue(),
					parameters.size() == 1 ? 10 : getFixnum(parameters.get(1)));
		} catch (NumberFormatException ex) {
			throw new SchemeException(
					"string->number: Value can not be converted");
		}
	}
}

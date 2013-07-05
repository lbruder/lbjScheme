package org.lb.lbjscheme;

import java.util.*;

public final class Symbol implements SchemeObject {
	private final String _name;

	private Symbol(String name) {
		_name = name;
	}

	@Override
	public String toString() {
		return _name;
	}

	private static final HashMap<String, Symbol> _cache = new HashMap<String, Symbol>();

	public static Symbol fromString(String name) {
		Symbol ret = _cache.get(name);
		if (ret != null)
			return ret;
		ret = new Symbol(name);
		_cache.put(name, ret);
		return ret;
	}
}

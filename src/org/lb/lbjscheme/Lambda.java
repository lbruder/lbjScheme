package org.lb.lbjscheme;

import java.util.*;

public final class Lambda implements SchemeObject {
	private final String _name;
	private final List<Symbol> _parameterNames;
	private final boolean _hasRestParameter;
	private final Pair _forms;
	private final Environment _captured;

	public Lambda(String name, List<Symbol> parameterNames,
			boolean hasRestParameter, Pair forms, Environment captured) {
		_name = name;
		_parameterNames = parameterNames;
		_hasRestParameter = hasRestParameter;
		_forms = forms;
		_captured = captured;
	}

	public String getName() {
		return _name;
	}

	public List<Symbol> getParameterNames() {
		return _parameterNames;
	}

	public boolean hasRestParameter() {
		return _hasRestParameter;
	}

	public Pair getForms() {
		return _forms;
	}

	public Environment getCaptured() {
		return _captured;
	}

	@Override
	public String toString() {
		return "<procedure " + _name + ">";
	}
}

package org.lb.lbjscheme;

import java.util.*;

public final class Vector implements SchemeObject {
	private final SchemeObject[] _values;

	public Vector(int length) {
		_values = new SchemeObject[length];
	}

	public Vector(SchemeList items) {
		List<SchemeObject> asList = new ArrayList<SchemeObject>();
		for (SchemeObject o : items)
			asList.add(o);
		_values = asList.toArray(new SchemeObject[0]);
	}

	public int getLength() {
		return _values.length;
	}

	public SchemeObject getAt(int position) {
		return _values[position];
	}

	public void setAt(int position, SchemeObject value) {
		_values[position] = value;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("#(");
		for (SchemeObject c : _values) {
			ret.append(c.toString());
			ret.append(" ");
		}

		if (ret.length() > 2)
			ret.setCharAt(ret.length() - 1, ')');
		else
			ret.append(')');

		return ret.toString();
	}
}

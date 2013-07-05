package org.lb.lbjscheme;

import java.util.*;

public class Nil implements SchemeObject, SchemeList {
	private Nil() {
	}

	private static final Nil _instance = new Nil();

	public static Nil getInstance() {
		return _instance;
	}

	@Override
	public boolean isDottedList() {
		return false;
	}

	@Override
	public List<SchemeObject> toJavaList() {
		return new ArrayList<SchemeObject>();
	}

	@Override
	public String toString() {
		return "()";
	}

	@Override
	public Iterator<SchemeObject> iterator() {
		return new PairIterator(this);
	}
}

package org.lb.lbjscheme;

import java.util.*;

public interface SchemeList extends Iterable<SchemeObject>, SchemeObject {
	public abstract boolean isDottedList();

	public abstract Iterator<SchemeObject> iterator();

	public abstract List<SchemeObject> toJavaList();
}

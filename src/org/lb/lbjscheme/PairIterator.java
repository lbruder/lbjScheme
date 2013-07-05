package org.lb.lbjscheme;

import java.util.Iterator;

final class PairIterator implements Iterator<SchemeObject> {
	private SchemeObject _current;

	public PairIterator(SchemeObject current) {
		_current = current;
	}

	@Override
	public boolean hasNext() {
		return !(_current instanceof Nil);
	}

	@Override
	public SchemeObject next() {
		if (_current instanceof Nil) throw new UnsupportedOperationException();
		if (_current instanceof Pair) {
			SchemeObject ret = ((Pair) _current).getCar();
			_current = ((Pair) _current).getCdr();
			return ret;
		} else {
			SchemeObject ret = _current;
			_current = Nil.getInstance();
			return ret;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}

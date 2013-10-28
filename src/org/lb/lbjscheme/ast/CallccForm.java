package org.lb.lbjscheme.ast;

public class CallccForm extends SyntaxTreeObject {
	private final SyntaxTreeObject _target;

	public CallccForm(SyntaxTreeObject target) {
		_target = target;
	}

	public SyntaxTreeObject getTarget() {
		return _target;
	}
}
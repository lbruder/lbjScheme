// lbjScheme
// An experimental Scheme subset interpreter in Java, based on SchemeNet.cs
// Copyright (c) 2013, Leif Bruder <leifbruder@gmail.com>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

package org.lb.lbjscheme.vm;

import java.util.List;
import org.lb.lbjscheme.*;

public final class MakeClosure extends LabeledOpcode {
	private final String _name;
	private final String _label;
	private final boolean _hasRestParameter;
	private final List<Symbol> _parameterNames;
	private int _position;

	public MakeClosure(String name, String closureLabel,
			boolean hasRestParameter, List<Symbol> parameterNames) {
		_name = name;
		_label = closureLabel;
		_hasRestParameter = hasRestParameter;
		_parameterNames = parameterNames;
		_position = -1;
	}

	@Override
	public boolean isLabel(String label) {
		return _label.equals(label);
	}

	@Override
	public void setPosition(int value) {
		_position = value;
	}

	@Override
	public void execute() {
		_vm.executeMakeClosure(_name, _position, _hasRestParameter,
				_parameterNames);
	}

	@Override
	public String toString() {
		return "MAKECLOSURE " + _name + " " + _position + "...";
	}
}

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

package org.lb.lbjscheme;

import java.util.*;
import org.lb.lbjscheme.vm.*;

public final class CompiledProgram implements Emitter {
	private final ArrayList<Opcode> _opcodes = new ArrayList<Opcode>();

	public int getNumberOfStatements() {
		return _opcodes.size();
	}

	@Override
	public void emitCall() {
		_opcodes.add(new Call());
	}

	@Override
	public void emitContinue() {
		_opcodes.add(new Continue());
	}

	@Override
	public void emitDefineVariable(Symbol variable) {
		_opcodes.add(new DefineVariable(variable));
	}

	@Override
	public void emitGetVariable(Symbol variable) {
		_opcodes.add(new GetVariable(variable));
	}

	@Override
	public void emitInitArgs() {
		_opcodes.add(new InitArgs());
	}

	@Override
	public void emitJump(String label) {
		LabeledOpcode op = new Jump(label);
		_opcodes.add(op);
		addLabeledOpcode(op);
	}

	@Override
	public void emitJumpIfFalse(String label) {
		LabeledOpcode op = new JumpIfFalse(label);
		_opcodes.add(op);
		addLabeledOpcode(op);
	}

	@Override
	public void emitLiteral(SchemeObject value) {
		_opcodes.add(new Literal(value));
	}

	@Override
	public void emitMakeClosure(String name, String closureLabel,
			boolean hasRestParameter, List<Symbol> parameterNames) {
		LabeledOpcode op = new MakeClosure(name, closureLabel,
				hasRestParameter, parameterNames);
		_opcodes.add(op);
		addLabeledOpcode(op);
	}

	@Override
	public void emitPopAll() {
		_opcodes.add(new PopAll());
	}

	@Override
	public void emitPushAll() {
		_opcodes.add(new PushAll());
	}

	@Override
	public void emitPushArg() {
		_opcodes.add(new PushArg());
	}

	@Override
	public void emitSetContinuationRegisterToLabel(String label) {
		LabeledOpcode op = new SetContinuationRegisterToLabel(label);
		_opcodes.add(op);
		addLabeledOpcode(op);
	}

	@Override
	public void emitSetArgsToValueRegister() {
		_opcodes.add(new SetArgsToValueRegister());
	}

	@Override
	public void emitSetVariable(Symbol variable) {
		_opcodes.add(new SetVariable(variable));
	}

	private final List<LabeledOpcode> _labeledOpcodes = new ArrayList<LabeledOpcode>();

	@Override
	public void setLabelPositionToHere(String label) {
		for (int i = 0; i < _labeledOpcodes.size(); ++i) {
			final LabeledOpcode op = _labeledOpcodes.get(i);
			if (op.isLabel(label)) {
				op.setPosition(_opcodes.size());
				_labeledOpcodes.remove(i);
				i--;
			}
		}
	}

	private void addLabeledOpcode(LabeledOpcode op) {
		_labeledOpcodes.add(op);
	}

	public boolean isRunnable() {
		return _labeledOpcodes.isEmpty();
	}

	public void setVm(VirtualMachine vm) {
		for (Opcode i : _opcodes)
			i.setVm(vm);
	}

	public void executeOpcode(int ip) throws SchemeException {
		// System.out.println(ip + ": " + _opcodes.get(ip).toString());
		_opcodes.get(ip).execute();
	}
}

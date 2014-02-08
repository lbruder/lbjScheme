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

import java.util.*;
import org.lb.lbjscheme.*;

public final class VirtualMachine {
	private static final False _false = False.getInstance();
	private final Environment globalEnvironment;
	private final Stack<Environment> _environmentStack = new Stack<Environment>();
	private final Stack<Integer> _continueStack = new Stack<Integer>();
	private final Stack<LinkedList<SchemeObject>> _argumentsStack = new Stack<LinkedList<SchemeObject>>();

	private int ip;
	private Environment environmentRegister;
	private int continueRegister;
	private SchemeObject valueRegister;
	private LinkedList<SchemeObject> argumentsRegister;

	public VirtualMachine(final Environment globalEnv) {
		globalEnvironment = globalEnv;
	}

	void executeCall() throws SchemeException {
		final List<SchemeObject> parameters = argumentsRegister;

		if (valueRegister instanceof Builtin) {
			valueRegister = ((Builtin) valueRegister).apply(parameters);
			ip = continueRegister;
			return;
		}

		if (valueRegister instanceof CompiledLambda) {
			final CompiledLambda closure = (CompiledLambda) valueRegister;
			environmentRegister = new Environment(closure.captured);
			environmentRegister.expand(closure.parameterNames,
					closure.hasRestParameter, parameters);
			ip = closure.pc;
			return;
		}

		// TODO: Lambdas from (eval)?

		throw new SchemeException("Internal error: Invalid CALL target: "
				+ valueRegister.getClass().getSimpleName());
	}

	void executeContinue() {
		ip = continueRegister;
	}

	void executeDefineVariable(Symbol variable) throws SchemeException {
		environmentRegister.define(variable, valueRegister);
		ip++;
	}

	void executeGetVariable(Symbol variable) throws SchemeException {
		valueRegister = environmentRegister.get(variable);
		ip++;
	}

	void executeInitArgs() {
		argumentsRegister = new LinkedList<SchemeObject>();
		ip++;
	}

	void executeJump(int position) {
		ip = position;
	}

	void executeJumpIfFalse(int position) {
		ip = valueRegister == _false ? position : ip + 1;
	}

	void executeLiteral(SchemeObject value) {
		valueRegister = value;
		ip++;
	}

	void executeMakeClosure(String name, int position,
			boolean hasRestParameter, List<Symbol> parameterNames) {
		valueRegister = new CompiledLambda(name, environmentRegister, position,
				parameterNames, hasRestParameter);
		ip++;
	}

	void executePopAll() {
		environmentRegister = _environmentStack.pop();
		continueRegister = _continueStack.pop();
		argumentsRegister = _argumentsStack.pop();
		ip++;
	}

	void executePushAll() {
		_argumentsStack.push(argumentsRegister);
		_continueStack.push(continueRegister);
		_environmentStack.push(environmentRegister);
		ip++;
	}

	void executePushArg() {
		argumentsRegister.addFirst(valueRegister);
		ip++;
	}

	void executeSetArgumentRegisterToValue() {
		// TODO: Check
		argumentsRegister = new LinkedList<SchemeObject>();
		for (SchemeObject i : (SchemeList) valueRegister)
			argumentsRegister.add(i);
		ip++;
	}

	void executeSetContinuationRegisterToPosition(int position) {
		continueRegister = position;
		ip++;
	}

	void executeSetVariable(Symbol variable) throws SchemeException {
		environmentRegister.set(variable, valueRegister);
		ip++;
	}

	public SchemeObject run(final CompiledProgram prog) throws SchemeException {
		return run(prog, 0);
	}

	public SchemeObject run(final CompiledProgram prog, final int initialIp)
			throws SchemeException {
		if (!prog.isRunnable())
			throw new SchemeException(
					"Internal error: Program is not runnable yet");
		ip = initialIp;
		environmentRegister = globalEnvironment;
		continueRegister = -1;
		valueRegister = Nil.getInstance();
		argumentsRegister = new LinkedList<SchemeObject>();
		_argumentsStack.clear();
		_continueStack.clear();
		_environmentStack.clear();
		final int numStatements = prog.getNumberOfStatements();

		prog.setVm(this);
		while (ip < numStatements && ip >= 0)
			prog.executeOpcode(ip);

		if (!_argumentsStack.isEmpty() || !_continueStack.isEmpty()
				|| !_environmentStack.isEmpty())
			throw new SchemeException(
					"Bad program: Stack not empty after last instruction");
		if (!argumentsRegister.isEmpty())
			throw new SchemeException(
					"Bad program: Arguments register not empty after last instruction");
		return valueRegister;
	}
}

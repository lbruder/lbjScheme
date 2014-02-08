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

import java.io.*;
import org.lb.lbjscheme.vm.VirtualMachine;

public final class CompilingEvaluator extends Evaluator {
	private final Analyzer _analyzer;
	private final CompiledProgram _program;
	private final Compiler _compiler;
	private final VirtualMachine _vm;

	public CompilingEvaluator(final InputPort in, final OutputPort out)
			throws SchemeException {
		super(new Environment());

		_analyzer = new Analyzer();
		_program = new CompiledProgram();
		_compiler = new Compiler(_program);

		final Reader r = new Reader(new InputPort(new StringReader(
				Environment.getInteractionInitScript())));
		while (true) {
			try {
				_compiler.compile(_analyzer.analyze(r.read()), false);
			} catch (EOFException ex) {
				break;
			}
		}

		final Environment global = getGlobalEnvironment();
		global.addBuiltins();
		global.setInputPort(in);
		global.setOutputPort(out);
		_vm = new VirtualMachine(global);
		_vm.run(_program);
		global.addRedefinableBuiltins();
		global.lock();
	}

	@Override
	public SchemeObject eval(String commands) throws SchemeException {
		final Reader r = new Reader(new InputPort(new StringReader(commands)));
		SchemeObject ret = Symbol.fromString("undefined");
		while (true) {
			try {
				ret = eval(r.read());
			} catch (EOFException ex) {
				return ret;
			}
		}
	}

	@Override
	public SchemeObject eval(SchemeObject o, Environment env)
			throws SchemeException {
		final int startIp = _program.getNumberOfStatements();
		_compiler.compile(_analyzer.analyze(o), false);
		return _vm.run(_program, startIp);
	}
}

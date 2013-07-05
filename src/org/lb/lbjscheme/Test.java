package org.lb.lbjscheme;

import java.io.*;

public final class Test {
	public static void main(String[] args) throws IOException, SchemeException {
		Reader r = new Reader(new InputStreamReader(System.in));
		Evaluator e = new InterpretingEvaluator();

		// Our basic read-eval-print-loop:
		while (true) {
			try {
				System.out.println(e.eval(r.read()));
			} catch (SchemeException ex) {
				System.out.println(ex.getMessage());
				System.in.skip(System.in.available());
			}
		}

		// TODO:
		// - Builtins
		// - Base library
		// - Ports
		// - Unit tests!
		// - Gradually add full numeric tower to number data type
		// - Some kind of interface to native Java data types
		// - Interface to javax.script
		// - AnalyzingEvaluator
		// - CompilingEvaluator
	}
}

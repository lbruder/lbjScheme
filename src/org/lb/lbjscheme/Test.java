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
import java.util.*;

public final class Test {
	public static void main(String[] args) throws IOException, SchemeException {

		if (hasArgument(args, "-h") || hasArgument(args, "-?")) {
			System.out.println("Command line switches:");
			System.out.println("-a      Use analyzing evaluator (faster)");
			System.out.println("-h, -?  Show this text");
			System.out.println("-i      Use interpreting evaluator (default)");
			System.out.println("-r      Enter REPL after executing files");
			System.exit(0);
		}

		final boolean interactiveRepl = hasArgument(args, "-r")
				|| getFileNames(args).size() == 0;
		final boolean useAnalyzingEvaluator = hasArgument(args, "-a");
		final boolean useInterpretingEvaluator = hasArgument(args, "-i")
				|| !useAnalyzingEvaluator;

		if (useAnalyzingEvaluator && useInterpretingEvaluator) {
			System.out.println("Arguments -a and -i can not be used together");
			System.exit(1);
		}

		final Environment globalEnv = Environment.newInteractionEnvironment();
		final Evaluator e = useInterpretingEvaluator ? new InterpretingEvaluator(
				globalEnv) : new AnalyzingEvaluator(globalEnv);

		for (String fileName : getFileNames(args)) {
			final FileReader r = new FileReader(fileName);
			repl(new Reader(r), e, false);
			r.close();
		}

		if (interactiveRepl)
			repl(new Reader(new InputStreamReader(System.in)), e, true);

		// TODO:
		// - Unit tests!
		// - apply, error
		// - Add real and complex numbers
		// - Complete builtins and base library
		// - Some kind of interface to native Java data types
		// - Interface to javax.script
		// - Line numbers in error messages (can of worms)
		// - CompilingEvaluator
		// - Continuations
	}

	private static boolean hasArgument(String[] args, String arg) {
		for (String i : args)
			if (i.equals(arg))
				return true;
		return false;
	}

	private static List<String> getFileNames(String[] args) {
		final List<String> ret = new ArrayList<String>();
		for (String i : args)
			if (!i.startsWith("-"))
				ret.add(i);
		return ret;
	}

	private static void repl(final Reader r, final Evaluator e,
			final boolean printPromptAndResults) throws IOException {
		while (true) {
			try {
				if (printPromptAndResults) {
					System.out.print("> ");
					System.out.flush();
				}
				final SchemeObject result = e.eval(r.read());
				if (printPromptAndResults)
					System.out.println(result);
			} catch (EOFException ex) {
				break;
			} catch (SchemeException ex) {
				System.out.println(ex.getMessage());
				System.in.skip(System.in.available());
			} catch (Exception ex) {
				System.out.println("Internal error:" + System.lineSeparator()
						+ ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
	}
}

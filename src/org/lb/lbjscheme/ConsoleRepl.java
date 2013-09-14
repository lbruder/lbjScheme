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

public final class ConsoleRepl {
	public static void main(String[] args) throws IOException, SchemeException {

		if (hasArgument(args, "-h") || hasArgument(args, "-?")) {
			System.out.println("Command line switches:");
			System.out.println("-a      Use analyzing evaluator (faster)");
			System.out.println("-h, -?  Show this text");
			System.out.println("-i      Use interpreting evaluator (default)");
			System.out.println("-r      Enter REPL after executing files");
			System.out.println("-v      Print version info, then quit");
			System.exit(0);
		}

		if (hasArgument(args, "-v")) {
			System.out.println("lbjScheme V0.3");
			System.out
					.println("A Scheme subset interpreter in Java, based on SchemeNet.cs");
			System.out
					.println("Copyright (c) 2013, Leif Bruder <leifbruder@gmail.com>");
			System.out
					.println("Permission to use, copy, modify, and/or distribute this software for any");
			System.out
					.println("purpose with or without fee is hereby granted, provided that the above");
			System.out
					.println("copyright notice and this permission notice appear in all copies.");
			System.out.println();
			System.out
					.println("THE SOFTWARE IS PROVIDED \"AS IS\" AND THE AUTHOR DISCLAIMS ALL");
			System.out
					.println("WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED");
			System.out
					.println("WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE");
			System.out
					.println("AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR");
			System.out
					.println("CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM");
			System.out
					.println("LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,");
			System.out
					.println("NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN");
			System.out
					.println("CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.");
			System.out.println();
			System.out
					.println("BEWARE: This is experimental code. I'm striving for full R5RS");
			System.out
					.println("compliance when done, but at the moment there are several parts");
			System.out
					.println("missing, such as continuations and multiple value returns. Visit");
			System.out
					.println("github.com/lbruder/lbjScheme for more information and updates.");
			System.out.println("Bug reports are welcome ;)");
			System.out.println();

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

		final InputPort inputPort = new InputPort(new InputStreamReader(
				System.in));
		final OutputPort outputPort = new OutputPort(new OutputStreamWriter(
				System.out));
		final Evaluator e = useInterpretingEvaluator ? new InterpretingEvaluator(
				inputPort, outputPort) : new AnalyzingEvaluator(inputPort,
				outputPort);

		for (String fileName : getFileNames(args)) {
			final FileReader r = new FileReader(fileName);
			repl(r, e, false);
			r.close();
		}

		if (interactiveRepl)
			repl(new InputStreamReader(System.in), e, true);
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

	private static void repl(final java.io.Reader input, final Evaluator e,
			final boolean printPromptAndResults) throws IOException {
		final Reader r = new Reader(new InputPort(input));
		while (true) {
			try {
				if (printPromptAndResults) {
					System.out.print("> ");
					System.out.flush();
				}
				final SchemeObject result = e.eval(r.read());
				if (!printPromptAndResults)
					continue;
				if ((result instanceof Symbol)
						&& result.toString(true).equals("undefined"))
					continue;
				System.out.println(result);
			} catch (EOFException ex) {
				break;
			} catch (SchemeException ex) {
				System.out.println(ex.getMessage());
				System.in.skip(System.in.available());
			} catch (Exception ex) {
				System.out.println("Internal error:");
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
	}
}

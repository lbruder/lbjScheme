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

package tests.integrationtests;

import java.io.*;
import junit.framework.TestCase;
import org.lb.lbjscheme.*;

public abstract class EvaluatorTest extends TestCase {
	protected Evaluator interp;

	private void literalTest(String expression) throws SchemeException {
		evalTest(expression, expression);
	}

	private void evalTest(String expression, String expected)
			throws SchemeException {
		assertEquals(expected, interp.eval(expression).toString(false));
	}

	public void testBooleanLiterals() throws SchemeException {
		literalTest("#t");
		literalTest("#f");
	}

	public void testFixnumLiterals() throws SchemeException {
		literalTest("42");
		literalTest("-42");
		literalTest("12345678901234567890123456789012345678901234567890");
	}

	public void testRationalLiterals() throws SchemeException {
		literalTest("3/4");
		literalTest("12/7");
		literalTest("-12/7");
		evalTest("12/9", "4/3");
		evalTest(
				"1000000000000000000000000000000/1000000000000000000000000000",
				"1000");
	}

	public void testFlonumLiterals() throws SchemeException {
		literalTest("42.0");
		evalTest("1E21", "1.0E21");
		literalTest("1.0E21");
		literalTest("-42.0");
		literalTest("-1.0E21");
		evalTest("-1E21", "-1.0E21");
	}

	public void testCharacterLiterals() throws SchemeException {
		literalTest("#\\a");
		literalTest("#\\A");
		literalTest("#\\(");
		literalTest("#\\space");
		literalTest("#\\newline");
	}

	// char, complex
	public void testStringLiterals() throws SchemeException {
		literalTest("\"asd\"");
		literalTest("\"   asd  test  \"");
	}

	public void testQuotedLists() throws SchemeException {
		evalTest("'()", "()");
		evalTest("'(a)", "(a)");
		evalTest("'(b)", "(b)");
		evalTest("'(1 2 3 4 5)", "(1 2 3 4 5)");
		evalTest("'(a . b)", "(a . b)");
		evalTest("'(a b . c)", "(a b . c)");
	}

	public void testQuotedSymbols() throws SchemeException {
		evalTest("'asd", "asd");
		evalTest("'asd-test?!", "asd-test?!");
	}

	public void testUndefinedVariable() throws SchemeException {
		try {
			literalTest("asd");
			fail("Evaluating an undefined symbol should throw an error");
		} catch (SchemeException ex) {
			assertTrue(true);
		}
	}

	public void testGlobalVariable() throws SchemeException {
		interp.eval("(define asd 42)");
		evalTest("asd", "42");
	}

	public void testSetGlobalVariable() throws SchemeException {
		interp.eval("(define asd 42)");
		evalTest("asd", "42");
		interp.eval("(set! asd 3.1415)");
		evalTest("asd", "3.1415");
	}

	public void testApplyBuiltin() throws SchemeException {
		evalTest("(apply + '())", "0");
		evalTest("(apply + '(1))", "1");
		evalTest("(apply + '(1 2 3))", "6");
	}

	public void testApplyLambda() throws SchemeException {
		interp.eval("(define (foo a b) (list a b))");
		evalTest("(apply foo '(1 2))", "(1 2)");
		evalTest("(apply foo '(bar baz))", "(bar baz)");
		try {
			interp.eval("(apply foo '(1 2 3 4 5))");
			fail("Apply with an invalid number of parameters should throw an error");
		} catch (SchemeException ex) {
			assertTrue(true);
		}
	}

	public void testValues() throws SchemeException {
		evalTest("(call-with-values (lambda () (values 1 2)) +)", "3");
		evalTest("(call-with-values * -)", "-1");
	}

	public void testR5rsTests() throws Exception {
		final BufferedReader in = new BufferedReader(new FileReader(
				"r5rs_tests.scm"));
		String script = "";
		for (;;) {
			String read = in.readLine();
			if (read == null)
				break;
			script += read + "\n";
		}
		in.close();
		if (script.equals(""))
			fail("r5rs_tests.txt not found");
		interp.eval(script);
		assertTrue(true);
	}
}

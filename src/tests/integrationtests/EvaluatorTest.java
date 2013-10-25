package tests.integrationtests;

import junit.framework.TestCase;
import org.lb.lbjscheme.*;

public abstract class EvaluatorTest extends TestCase {
	protected Evaluator interp;

	private void literalTest(String expression) throws SchemeException {
		literalTest(expression, expression);
	}

	private void literalTest(String expression, String expected)
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
		literalTest("12/9", "4/3");
		literalTest(
				"1000000000000000000000000000000/1000000000000000000000000000",
				"1000");
	}

	public void testFlonumLiterals() throws SchemeException {
		literalTest("42.0");
		literalTest("1E21", "1.0E21");
		literalTest("1.0E21");
		literalTest("-42.0");
		literalTest("-1.0E21");
		literalTest("-1E21", "-1.0E21");
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
		literalTest("'()", "()");
		literalTest("'(a)", "(a)");
		literalTest("'(b)", "(b)");
		literalTest("'(1 2 3 4 5)", "(1 2 3 4 5)");
		literalTest("'(a . b)", "(a . b)");
		literalTest("'(a b . c)", "(a b . c)");
	}

	public void testQuotedSymbols() throws SchemeException {
		literalTest("'asd", "asd");
		literalTest("'asd-test?!", "asd-test?!");
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
		literalTest("asd", "42");
	}

	public void testSetGlobalVariable() throws SchemeException {
		interp.eval("(define asd 42)");
		literalTest("asd", "42");
		interp.eval("(set! asd 3.1415)");
		literalTest("asd", "3.1415");
	}
}

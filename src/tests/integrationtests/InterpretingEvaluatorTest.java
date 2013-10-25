package tests.integrationtests;

import org.lb.lbjscheme.*;

public class InterpretingEvaluatorTest extends EvaluatorTest {
	public void setUp() throws SchemeException {
		interp = new InterpretingEvaluator(null, null);
	}
}

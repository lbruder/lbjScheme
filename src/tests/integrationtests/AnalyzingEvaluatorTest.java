package tests.integrationtests;

import org.lb.lbjscheme.*;

public class AnalyzingEvaluatorTest extends EvaluatorTest {
	public void setUp() throws SchemeException {
		interp = new AnalyzingEvaluator(null, null);
	}
}

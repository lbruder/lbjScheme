package tests.unittests;

import junit.framework.TestCase;
import org.lb.lbjscheme.*;

public class FalseTest extends TestCase {
	public void testDisplay() {
		assertEquals("#f", False.getInstance().toString(true));
	}

	public void testWrite() {
		assertEquals("#f", False.getInstance().toString(false));
	}
}

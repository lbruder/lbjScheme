package tests.unittests;

import junit.framework.TestCase;
import org.lb.lbjscheme.*;

public class TrueTest extends TestCase {
	public void testDisplay() {
		assertEquals("#t", True.getInstance().toString(true));
	}

	public void testWrite() {
		assertEquals("#t", True.getInstance().toString(false));
	}
}

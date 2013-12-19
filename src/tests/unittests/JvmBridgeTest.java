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

package tests.unittests;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.lb.lbjscheme.*;

public class JvmBridgeTest extends TestCase {
	public void testNullValueFromJavaObject() throws SchemeException {
		final Object o = null;
		assertEquals(Nil.getInstance(), JvmBridge.fromJavaObject(o));
	}

	public void testBooleanFromJavaObject() {
		assertEquals(True.getInstance(), JvmBridge.fromJavaObject(true));
		assertEquals(False.getInstance(), JvmBridge.fromJavaObject(false));
	}

	public void testNumberFromJavaObject() {
		assertTrue(JvmBridge.fromJavaObject(15) instanceof SchemeNumber);
		assertTrue(((SchemeNumber) JvmBridge.fromJavaObject(15)).isExact());

		assertTrue(JvmBridge.fromJavaObject(15.678) instanceof SchemeNumber);
		assertFalse(((SchemeNumber) JvmBridge.fromJavaObject(15.678)).isExact());
	}

	public void testCharacterFromJavaObject() {
		assertTrue(JvmBridge.fromJavaObject('A') instanceof SchemeCharacter);
		assertEquals("#\\A", JvmBridge.fromJavaObject('A').toString(false));
	}

	public void testStringFromJavaObject() {
		assertTrue(JvmBridge.fromJavaObject("Test") instanceof SchemeString);
		assertEquals("Test", JvmBridge.fromJavaObject("Test").toString(true));
	}

	public void testListFromJavaObject() throws SchemeException {
		final ArrayList<Integer> testList = new ArrayList<>();
		for (int i = 0; i < 10; ++i)
			testList.add(i);
		final SchemeObject o = JvmBridge.fromJavaObject(testList);
		assertTrue(o instanceof SchemeList);
		final SchemeList sl = (SchemeList) o;
		assertFalse(sl.isDottedList());
		assertEquals("(0 1 2 3 4 5 6 7 8 9)", sl.toString());
	}

	public void testArrayFromJavaObject() throws SchemeException {
		final SchemeObject o = JvmBridge
				.fromJavaObject(new int[] { 1, 2, 3, 4 });
		assertTrue(o instanceof Vector);
		final Vector v = (Vector) o;
		assertEquals("#(1 2 3 4)", v.toString());
	}

	public void testNilToJavaObject() throws SchemeException {
		assertEquals(null, JvmBridge.toJavaObject(Nil.getInstance()));
	}

	public void testTrueToJavaObject() throws SchemeException {
		assertEquals(true, JvmBridge.toJavaObject(True.getInstance()));
	}

	public void testFalseToJavaObject() throws SchemeException {
		assertEquals(false, JvmBridge.toJavaObject(False.getInstance()));
	}

	public void testFixnumToJavaObject() throws SchemeException {
		assertEquals(42, JvmBridge.toJavaObject(Fixnum.valueOf(42)));
	}

	public void testRealToJavaObject() throws SchemeException {
		assertEquals(42.123, JvmBridge.toJavaObject(new Real(42.123)));
	}

	public void testSymbolToJavaObject() throws SchemeException {
		assertEquals("Test", JvmBridge.toJavaObject(Symbol.fromString("Test")));
	}

	public void testStringToJavaObject() throws SchemeException {
		assertEquals("Test", JvmBridge.toJavaObject(new SchemeString("Test")));
	}

	public void testListToJavaObject() throws SchemeException {
		final ArrayList<SchemeObject> alist = new ArrayList<>();
		for (int i = 1; i < 5; ++i)
			alist.add(Fixnum.valueOf(i));
		final SchemeList list = Pair.fromIterable(alist);

		final Object o = JvmBridge.toJavaObject(list);

		assertTrue(o instanceof ArrayList<?>);
		final ArrayList<?> asList = (ArrayList<?>) o;
		assertEquals(alist.size(), asList.size());
		for (int i = 0; i < asList.size(); ++i)
			assertEquals(alist.get(i).toString(), asList.get(i).toString());
	}
}

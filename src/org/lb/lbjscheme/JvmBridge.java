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

import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;

public final class JvmBridge implements SchemeObject {
	private final Object _obj;

	public JvmBridge(Object obj) {
		_obj = obj;
	}

	public SchemeList getMethodNames() {
		final Method[] methods = _obj.getClass().getMethods();

		final String[] names = new String[methods.length];
		int index = 0;
		for (Method m : methods)
			names[index++] = m.getName();
		Arrays.sort(names);

		final List<String> uniqueSortedNames = new ArrayList<String>();
		for (String i : names)
			if (!uniqueSortedNames.contains(i))
				uniqueSortedNames.add(i);

		final List<SchemeObject> ret = new ArrayList<SchemeObject>();
		for (String i : uniqueSortedNames)
			ret.add(new SchemeString(i));

		return Pair.fromIterable(ret);
	}

	public SchemeObject call(String methodName, List<SchemeObject> parameters)
			throws SchemeException {
		Object[] parameterArray = new Object[parameters.size()];
		for (int i = 0; i < parameterArray.length; ++i)
			parameterArray[i] = toJavaObject(parameters.get(i));
		Class<?>[] parameterTypes = new Class<?>[parameterArray.length];
		for (int i = 0; i < parameterArray.length; ++i)
			parameterTypes[i] = parameterArray[i].getClass();

		Method m = null;
		try {
			m = _obj.getClass().getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new SchemeException("No suitable method found");
		} catch (SecurityException e) {
			throw new SchemeException("Unable to call methods on object");
		}

		if (m == null)
			throw new SchemeException("No suitable method found");

		try {
			return fromJavaObject(m.invoke(_obj, parameterArray));
		} catch (IllegalAccessException e) {
			throw new SchemeException("Method not accessible");
		} catch (IllegalArgumentException e) {
			throw new SchemeException("Invalid parameter type");
		} catch (InvocationTargetException e) {
			throw new SchemeException("Error in foreign method: "
					+ e.getMessage());
		}
	}// (sys:call test "equals" "asd")

	public static SchemeObject fromJavaObject(boolean o) {
		return o ? True.getInstance() : False.getInstance();
	}

	public static SchemeObject fromJavaObject(char o) {
		return new SchemeCharacter(o);
	}

	public static SchemeObject fromJavaObject(byte o) {
		return new Fixnum(o);
	}

	public static SchemeObject fromJavaObject(short o) {
		return new Fixnum(o);
	}

	public static SchemeObject fromJavaObject(int o) {
		return new Fixnum(o);
	}

	public static SchemeObject fromJavaObject(long o) {
		return Fixnum.valueOf(o);
	}

	public static SchemeObject fromJavaObject(float o) {
		return new Real(o);
	}

	public static SchemeObject fromJavaObject(double o) {
		return new Real(o);
	}

	public static SchemeObject fromJavaObject(BigInteger o) {
		return new Bignum(o);
	}

	public static SchemeObject fromJavaObject(String o) {
		return new SchemeString(o);
	}

	public static SchemeObject fromJavaObject(Object o) throws SchemeException {
		if (o == null)
			return Nil.getInstance();
		if (o instanceof Boolean)
			return fromJavaObject(((Boolean) o).booleanValue());
		if (o instanceof Character)
			return fromJavaObject(((Character) o).charValue());
		if (o instanceof Byte)
			return fromJavaObject(((Byte) o).byteValue());
		if (o instanceof Short)
			return fromJavaObject(((Short) o).shortValue());
		if (o instanceof Integer)
			return fromJavaObject(((Integer) o).intValue());
		if (o instanceof Long)
			return fromJavaObject(((Long) o).longValue());
		if (o instanceof Float)
			return fromJavaObject(((Float) o).floatValue());
		if (o instanceof Double)
			return fromJavaObject(((Double) o).doubleValue());
		if (o instanceof BigInteger)
			return fromJavaObject((BigInteger) o);
		if (o instanceof String)
			return fromJavaObject((String) o);
		if (o.getClass().isArray())
			return fromJavaArray(o);
		if (o instanceof Collection<?>) {
			final List<SchemeObject> values = new ArrayList<SchemeObject>();
			for (Object i : (Collection<?>) o)
				values.add(fromJavaObject(i));
			return Pair.fromIterable(values);
		}

		throw new SchemeException("Unable to convert object of type "
				+ o.getClass() + " into Scheme object (yet)");
	}

	private static SchemeObject fromJavaArray(Object o) throws SchemeException {
		final List<SchemeObject> asList = new ArrayList<SchemeObject>();

		if (o instanceof boolean[]) {
			for (boolean i : (boolean[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof char[]) {
			for (char i : (char[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof byte[]) {
			for (byte i : (byte[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof short[]) {
			for (short i : (short[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof int[]) {
			for (int i : (int[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof long[]) {
			for (long i : (long[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof float[]) {
			for (float i : (float[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof double[]) {
			for (double i : (double[]) o)
				asList.add(fromJavaObject(i));
		} else if (o instanceof Object[]) {
			for (Object i : (Object[]) o)
				asList.add(fromJavaObject(i));
		} else
			throw new SchemeException("Unable to convert array of type "
					+ o.getClass().getComponentType()
					+ " into Scheme object (yet)");

		return new Vector(Pair.fromIterable(asList));
	}

	public static Object toJavaObject(SchemeObject o) throws SchemeException {
		return o.toJavaObject();
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean forDisplay) {
		if (forDisplay)
			return _obj.toString();
		else
			return "<JVM object: " + _obj.toString() + ">";
	}

	@Override
	public Object toJavaObject() throws SchemeException {
		return _obj;
	}
}

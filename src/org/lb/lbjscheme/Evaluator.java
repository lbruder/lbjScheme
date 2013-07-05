package org.lb.lbjscheme;

public interface Evaluator {

	public abstract Environment getGlobalEnvironment();

	public abstract SchemeObject eval(String commands) throws SchemeException;

	public abstract SchemeObject eval(SchemeObject o) throws SchemeException;

	public abstract SchemeObject eval(SchemeObject o, Environment env) throws SchemeException;

}
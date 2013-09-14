package org.lb.lbjscheme;

import java.awt.GraphicsEnvironment;

public class Main {
	public static void main(String[] args) throws Exception {
		if (GraphicsEnvironment.isHeadless() || System.console() != null) {
			new ConsoleRepl().run(args);
		} else {
			new Gui().show();
		}
	}
}

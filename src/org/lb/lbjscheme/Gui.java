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

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.BevelBorder;

// TODO: Evaluate in separate thread to keep the GUI running and allow user to cancel a running script
// TODO: Put REPL input into a queue to read from in the _defaultInputPort
// TODO: Report runtime after evaluation of script or REPL input
// TODO: REPL history via up/down keys
// TODO: Load/Save script file
// TODO: Allow changing the evaluator

public final class Gui {
	private final Font _courierFont;

	private final JFrame _mainForm;
	private final JPanel _mainPanel;
	private final JPanel _replPanel;
	private final JPanel _scriptPanel;
	private final JTextArea _scriptEditor;
	private final JTextArea _replOutput;
	private final JTextField _replInput;
	private final JScrollPane _scriptScrollPane;
	private final JScrollPane _outputScrollPane;

	private final Symbol _undefinedSymbol = Symbol.fromString("undefined");

	private final InputPort _defaultInputPort;
	private final OutputPort _defaultOutputPort;
	private final Evaluator _eval;
	private String _output = "";

	public Gui() throws SchemeException {
		_defaultInputPort = new InputPort(new java.io.Reader() {
			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				// TODO Read lines from REPL
				throw new IOException("No REPL input yet!");
			}

			@Override
			public void close() throws IOException {
				// Nothing to close
			}
		});

		_defaultOutputPort = new OutputPort(new java.io.Writer() {
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				_output += String.valueOf(cbuf, off, len);
			}

			@Override
			public void flush() throws IOException {
				flushOutput();
			}

			@Override
			public void close() throws IOException {
				// Nothing to close
			}
		});
		_eval = new AnalyzingEvaluator(_defaultInputPort, _defaultOutputPort);

		_courierFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

		_mainForm = new JFrame("Scheme REPL");
		_mainPanel = new JPanel(new GridLayout(1, 2));
		_replPanel = new JPanel(new BorderLayout());
		_scriptPanel = new JPanel(new BorderLayout());
		_scriptEditor = new JTextArea();
		_replInput = new JTextField();
		_replOutput = new JTextArea();
		_scriptScrollPane = new JScrollPane(_scriptEditor);
		_outputScrollPane = new JScrollPane(_replOutput);

		final KeyListener f5Listener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// Uninteresting
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					println("Execute script");
					try {
						_eval.eval(_scriptEditor.getText());
					} catch (Exception e1) {
						println(e1.getMessage());
					}
					print("> ");
					_replInput.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// Uninteresting
			}
		};

		_scriptEditor.setFont(_courierFont);
		_scriptEditor.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		_scriptEditor.addKeyListener(f5Listener);

		_scriptPanel.add(_scriptScrollPane);

		_replOutput.setFont(_courierFont);
		_replOutput.setEditable(false);
		_replOutput.setBackground(SystemColor.control);

		_outputScrollPane.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));

		_replOutput.setLineWrap(true);

		_replInput.setFont(_courierFont);
		_replInput.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		_replInput.addKeyListener(f5Listener);
		_replInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String input = _replInput.getText();
				println(input);
				try {
					SchemeObject result = _eval.eval(input);
					if (result != _undefinedSymbol)
						println(result.toString(false));
					_replInput.setText("");
				} catch (Exception e1) {
					println(e1.getMessage());
				}
				print("> ");
			}
		});

		_replPanel.add(_outputScrollPane, BorderLayout.CENTER);
		_replPanel.add(_replInput, BorderLayout.SOUTH);

		_mainPanel.add(_replPanel);
		_mainPanel.add(_scriptPanel);

		_mainForm.getContentPane().add(_mainPanel);
		_mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_mainForm.setSize(new Dimension(1000, 720));

		print("> ");

		String crlf = System.getProperty("line.separator");
		_scriptEditor
				.setText("; lbjScheme V0.6"
						+ crlf
						+ "; A Scheme subset interpreter in Java, based on SchemeNet.cs"
						+ crlf
						+ "; Copyright (c) 2013, Leif Bruder <leifbruder@gmail.com>"
						+ crlf
						+ ";"
						+ crlf
						+ "; Permission to use, copy, modify, and/or distribute this software"
						+ crlf
						+ "; for any purpose with or without fee is hereby granted, provided"
						+ crlf
						+ "; that the above copyright notice and this permission notice"
						+ crlf
						+ "; appear in all copies."
						+ crlf
						+ ";"
						+ crlf
						+ "; THE SOFTWARE IS PROVIDED \"AS IS\" AND THE AUTHOR DISCLAIMS ALL"
						+ crlf
						+ "; WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED"
						+ crlf
						+ "; WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE"
						+ crlf
						+ "; AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR"
						+ crlf
						+ "; CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM"
						+ crlf
						+ "; LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,"
						+ crlf
						+ "; NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN"
						+ crlf
						+ "; CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE."
						+ crlf
						+ ";"
						+ crlf
						+ "; Usage: Enter a Scheme script in this window, then press F5 to run."
						+ crlf
						+ "; On the left you see an output window and, below it, a REPL input."
						+ crlf
						+ "; Press ENTER to execute any Scheme statements entered there."
						+ crlf
						+ ";"
						+ crlf
						+ "; BEWARE: This is experimental code. I'm striving for full R5RS"
						+ crlf
						+ "; compliance when done, but at the moment there are several parts"
						+ crlf
						+ "; missing, such as continuations and hygienic macros. Visit"
						+ crlf
						+ "; github.com/lbruder/lbjScheme for more information and updates."
						+ crlf + "; Bug reports are welcome ;)" + crlf + crlf
						+ "(define (factors n)" + crlf
						+ "  (filter (lambda (i) (zero? (remainder n i)))"
						+ crlf + "          (range 1 n)))" + crlf + crlf
						+ "(define (prime? n)" + crlf
						+ "  (= 2 (length (factors n))))" + crlf + crlf
						+ "(define (primes upto)" + crlf
						+ "  (filter prime? (range 1 upto)))" + crlf + crlf
						+ "(display (primes 1000))");

		_eval.getGlobalEnvironment().define(Symbol.fromString("test"),
				new JvmBridge("asd"));
	}

	public void show() {
		_mainForm.setVisible(true);
		_replInput.requestFocusInWindow();
	}

	private void println(String str) {
		print(str + System.getProperty("line.separator"));
	}

	private void print(String str) {
		_output += str;
		flushOutput();
	}

	private void flushOutput() {
		_replOutput.setText(_output);
		final int height = _replOutput.getSize().height;
		final Rectangle lastLineRect = new Rectangle(0, height, 0, height);
		_outputScrollPane.scrollRectToVisible(lastLineRect);
	}
}

package org.lb.lbjscheme;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.BevelBorder;

// TODO: Evaluate in separate thread to keep the GUI running and allow user to cancel a running script
// TODO: Put REPL input into a queue to read from in the _defaultInputPort
// TODO: Fonts
// TODO: Report runtime after evaluation of script or REPL input
// TODO: REPL history via up/down keys
// TODO: Load/Save script file
// TODO: Allow changing the evaluator

public final class Gui {
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
				return 0;
			}

			@Override
			public void close() throws IOException {
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
			}
		});
		_eval = new AnalyzingEvaluator(_defaultInputPort, _defaultOutputPort);

		_mainForm = new JFrame("Scheme REPL");
		_mainPanel = new JPanel(new GridLayout(1, 2));
		_replPanel = new JPanel(new BorderLayout());
		_scriptPanel = new JPanel(new BorderLayout());
		_scriptEditor = new JTextArea();
		_replInput = new JTextField();
		_replOutput = new JTextArea();
		_scriptScrollPane = new JScrollPane(_scriptEditor);
		_outputScrollPane = new JScrollPane(_replOutput);

		_scriptEditor.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		_scriptEditor.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					println("Execute script");
					try {
						_eval.eval(_scriptEditor.getText());
					} catch (SchemeException e1) {
						println(e1.getMessage());
					}
					print("> ");
					_replInput.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		_scriptPanel.add(_scriptScrollPane);

		_replOutput.setEditable(false);
		_replOutput.setBackground(SystemColor.control);

		_outputScrollPane.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));

		_replOutput.setLineWrap(true);

		_replInput.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
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
				} catch (SchemeException e1) {
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
		_mainForm.setSize(new Dimension(800, 600));

		print("> ");
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

	public static void main(String[] args) throws SchemeException {
		new Gui().show();
	}
}

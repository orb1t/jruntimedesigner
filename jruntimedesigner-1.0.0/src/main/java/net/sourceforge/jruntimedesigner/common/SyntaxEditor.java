/**
 * 
 */
package net.sourceforge.jruntimedesigner.common;

import java.awt.BorderLayout;
import java.io.StringReader;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SyntaxEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;

	public SyntaxEditor() {
		setLayout(new BorderLayout());

		textArea = new RSyntaxTextArea(80, 70);
		textArea.setTabSize(3);
		textArea.setCaretPosition(0);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setClearWhitespaceLinesEnabled(false);
		// textArea.setWhitespaceVisible(true);
		textArea.setPaintMatchedBracketPair(true);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);

		scrollPane = new RTextScrollPane(textArea, true);

		add(scrollPane, BorderLayout.CENTER);

	}

	public RSyntaxTextArea getTextArea() {
		return textArea;
	}

	public void setSyntaxStyle(String style) {
		textArea.setSyntaxEditingStyle(style);
	}

	public void setEditable(boolean enabled) {
		textArea.setEditable(enabled);
	}

	public String getText() {
		return textArea.getText();
	}

	public void setText(String txt) {
		try {
			StringReader r = new StringReader(txt);
			textArea.read(r, null);
			r.close();
			textArea.setCaretPosition(0);
			textArea.discardAllEdits();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

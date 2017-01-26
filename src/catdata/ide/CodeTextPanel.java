package catdata.ide;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
/*
  @author ryan
 * A text editor.
 */
public class CodeTextPanel extends JPanel {

	public final JTextArea area = new JTextArea();

	
	public void setText(String s) {
		area.setText(s);
		area.setCaretPosition(0);
	}

	public String getText() {
		return area.getText();
	}

	public CodeTextPanel(String title, String text) {
		this(BorderFactory.createEtchedBorder(), title, text);
	}
	
	public CodeTextPanel(Border bb, String title, String text) {
		super(new GridLayout(1, 1));
		if (bb != null) {
		Border b = BorderFactory.createTitledBorder(
				bb, title);
		setBorder(b);
		}

		JScrollPane p = new JScrollPane(area);
		add(p);
		p.setBorder(BorderFactory.createEmptyBorder());
		setText(text);

		area.setFont(new Font("Courier", Font.PLAIN, 13));
		//area.setEditable(false);

		UndoManager m = new UndoManager();
		// area.setundoManager = new UndoManager();
		Document doc = area.getDocument();
		doc.addUndoableEditListener((UndoableEditEvent e) -> m.addEdit(e.getEdit()));

		InputMap im = area.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = area.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");

		am.put("Undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (m.canUndo()) {
						m.undo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});
		am.put("Redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (m.canRedo()) {
						m.redo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});

		// setWordWrap(true);
		
		IdeOptions.theCurrentOptions.apply(this.area);
	}

	public void setWordWrap(boolean b) {
		area.setLineWrap(b);
		area.setWrapStyleWord(b);
	}

}

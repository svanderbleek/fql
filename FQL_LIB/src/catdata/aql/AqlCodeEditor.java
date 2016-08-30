package catdata.aql;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.codehaus.jparsec.error.ParserException;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;

import catdata.ide.CodeEditor;
import catdata.ide.Language;
import catdata.ide.Program;

@SuppressWarnings("serial")
public final class AqlCodeEditor extends
		CodeEditor<Program<Exp<? extends Object>>, Env, Display> {

	public AqlCodeEditor(int untitled_count, String content) {
		super(untitled_count, content);
		SyntaxScheme scheme = topArea.getSyntaxScheme();
		scheme.getStyle(Token.RESERVED_WORD).foreground = Color.RED;
		scheme.getStyle(Token.RESERVED_WORD_2).foreground = Color.BLUE;
	}

	@Override
	public Language lang() {
		return Language.AQL;
	}

	@Override
	protected String getATMFlhs() {
		return "text/" + Language.AQL.name();
	}

	@Override
	protected String getATMFrhs() {
		return "catdata.aql.AqlTokenMaker"; //TODO
	}

	protected void doTemplates() {
		CompletionProvider provider = createCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
				java.awt.event.InputEvent.META_DOWN_MASK
						| java.awt.event.InputEvent.SHIFT_DOWN_MASK);
		ac.setTriggerKey(key);
		ac.install(this.topArea);
	}

	private CompletionProvider createCompletionProvider() {
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		
		return provider;

	}

	@Override
	protected Program<Exp<? extends Object>> parse(String program) throws ParserException {
		return AqlParser.parseProgram(program);
	}

	@Override
	protected Display makeDisplay(String foo, Program<Exp<? extends Object>> init,
			Env env, long start, long middle) {
			Display ret = new Display(foo, init, env, start, middle);
			return ret;
	}

	String last_str;
	Program<Exp<? extends Object>> last_prog;
	Env last_env;

	@Override
	protected Env makeEnv(String str, Program<Exp<? extends Object>> init) {
			last_env = Driver.makeEnv(str, init, toUpdate, last_str,
					last_prog, last_env);
			last_prog = init;
			last_str = str;
			return last_env;
	}



	@Override
	protected String textFor(Env env) {
		return "Done.";
	}

}
package catdata.aql.exp;

import java.util.List;
import java.util.Map;

import catdata.Pair;
import catdata.Program;
import catdata.Util;
import catdata.aql.Instance;
import catdata.aql.Kind;
import catdata.aql.examples.AqlExamples;
import catdata.aql.gui.AqlViewer;
import catdata.ide.Example;
import catdata.ide.Examples;

//TODO: have this execute pragmas?
class AqlInACan {

	private static String quote(String s) {
		s = s.replace("\\", "\\" + "\\"); //  \ --> \\
		s = s.replace("\"", "\\\""); // " --> \"
		s = s.replace("\n", "\\n"); // <LF> --> \n
		return s;
	}
	
	private static List<Example> skip() {
		return Util.list(AqlExamples.aqlKb, AqlExamples.aqlJdbc, AqlExamples.aqlMatch);
	}
	
	//
	
	@SuppressWarnings("unused")
	private static String makeHtml() {
		String s = "";
		String t = "";
		for (Example ex : Util.alphabetical(Examples.getExamples(AqlExamples.class))) {
			if (skip().contains(ex)) {
				continue;
			}
			s += "\nif (v == \"" + ex.getName() + "\") { document.getElementById('code').innerHTML = \"" + quote(ex.getText()) + "\" }"; 
			t += "\n<option value = \"" + ex.getName() + "\">" + ex.getName() + "</option>";
		}
		String html = 
				  "<html>"
				+ "\n<head>"
				+ "\n<title>Try AQL</title>"
				+ "\n</head>"
				+ "\n<script>"
		        + "\nfunction setExample(t) {"
		        + "\n    var v = t.value;"
		        + "\n" + s
		        + "\n}; "
		        + "\n</script>"
				+ "\n<body>"
				+ "Choose example:"
				+ "\n<select name=\"example\" onChange = \"setExample(this);\">"
				+ "\n<option disabled selected value> -- select an option -- </option>"
				+ "\n" + t
				+ "\n</select>"
				+ "\n<br>"
				+ "\nEnter AQL code here:"
				+ "\n<form action=\"cgi-bin/try.cgi\""
				+ "\n      method=\"POST\">"
				+ "\n<textarea name=\"code\" id=\"code\" cols=80 rows=40>"
				+ "\n</textarea>"
				+ "\n<br>"
				+ "\n<input type=\"submit\" value=\"Run\">"
				+ "\n</form>"
				+ "\n</body>"
				+ "\n</html>"
				+ "\n";
		return html;
	}

	public static void main(String... args) {
//		System.out.println(makeHtml());
		System.out.println(openCan(args[0]));
	}


	
	private static String openCan(String can) {
		try {
			Program<Exp<?>> program = AqlParser.parseProgram(can);
			String html = "<html><head><title>Result</title></head><body>\n\n";
			AqlEnv env = new AqlEnv();
			env.typing = new AqlTyping(program);	
			for (String n : program.order) {
				Exp<?> exp = program.exps.get(n);
				Object o = Util.timeout(() -> exp.eval(env), 10 * 1000); //hardcode timeout, do not exec pragmas
				env.defs.put(n, exp.kind(), o);
				if (exp.kind().equals(Kind.INSTANCE)) {
					html += "<p><h2>" + n + " =\n</h2>" + toHtml((Instance<?,?,?,?,?,?,?,?,?>) o) 
						+ "\n</p><br><hr>\n";
				}
			}
			return html + "\n\n</body></html>";
		} catch (Throwable ex) {
			ex.printStackTrace();
			return "ERROR " + ex.getMessage();
		}
	}
	
	public static <Ty,En,Sym,Fk,Att,Gen,Sk,X,Y> String toHtml(Instance<Ty,En,Sym,Fk,Att,Gen,Sk,X,Y> I) {
		String ret = "<div>";
		
		Map<En, Pair<List<String>,Object[][]>> tables = AqlViewer.makeEnTables(I.algebra());
		
		for (En t : tables.keySet()) {
				ret += "<table style=\"float: left\" border=\"1\" cellpadding=\"3\" cellspacing=\"1\">";
	
			List<String> cols = tables.get(t).first;
			cols.remove(0);
			cols.add(0, t.toString());
			Object[][] rows = tables.get(t).second;
			ret += "<tr>";
			for (String col : cols) {
				ret += "<th>" + col + "</th>";
			}
			ret += "</tr>";
			for (Object[] row : rows) {
				ret += "<tr>";
				for (Object col : row) {
					ret += "<td>" + strip(col.toString()) + "</td>";
				}
				ret += "</tr>";
			}
			ret += "</table>";
		} 
		return ret + "</div><br style=\"clear:both;\">"; 
	}

	private static String strip(String s) {
		s = s.replace("<", "");
		s = s.replace(">", "");
		s = s.replace("[", "");
		s = s.replace("]", "");
		s = s.replace("|", "");
		return s;
	}
	

}

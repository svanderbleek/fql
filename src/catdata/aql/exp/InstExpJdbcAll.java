package catdata.aql.exp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import catdata.Chc;
import catdata.Ctx;
import catdata.Null;
import catdata.Pair;
import catdata.Triple;
import catdata.Util;
import catdata.aql.AqlJs;
import catdata.aql.AqlOptions;
import catdata.aql.AqlOptions.AqlOption;
import catdata.aql.AqlProver;
import catdata.aql.AqlProver.ProverName;
import catdata.aql.Collage;
import catdata.aql.DP;
import catdata.aql.Eq;
import catdata.aql.ImportAlgebra;
import catdata.aql.Instance;
import catdata.aql.Kind;
import catdata.aql.Schema;
import catdata.aql.Term;
import catdata.aql.TypeSide;
import catdata.aql.Var;
import catdata.aql.fdm.SaturatedInstance;
import catdata.sql.SqlColumn;
import catdata.sql.SqlForeignKey;
import catdata.sql.SqlInstance;
import catdata.sql.SqlSchema;
import catdata.sql.SqlTable;

public class InstExpJdbcAll extends InstExp<String, String, Void, String, String, String, Null<String>, String, Null<String>> {

	private final Map<String, String> options;

	private final String clazz;
	private final String jdbcString;

	@Override
	public long timeout() {
		return (Long) AqlOptions.getOrDefault(options, AqlOption.timeout);
	}

	public InstExpJdbcAll(String clazz, String jdbcString, List<Pair<String, String>> options) {
		this.clazz = clazz;
		this.jdbcString = jdbcString;
		Util.load(clazz);
		this.options = Util.toMapSafely(options);
	}

	private Instance<String, String, Void, String, String, String, Null<String>, String, Null<String>> toInstance(SqlInstance inst, SqlSchema info) {
		boolean checkJava = !(Boolean) AqlOptions.getOrDefault(options, AqlOption.allow_java_eqs_unsafe);

		Collage<String, Void, Void, Void, Void, Void, Void> col = new Collage<>();
		col.tys.add("dom");
		col.java_tys.put("dom", "java.lang.Object");
		col.java_parsers.put("dom", "return input[0]");
		AqlJs<String, Void> js = new AqlJs<>(new Ctx<>(), col.java_tys, col.java_parsers, new Ctx<>());

		DP<String, Void, Void, Void, Void, Void, Void> dpT = AqlProver.create(new AqlOptions(ProverName.free), col, js);
		TypeSide<String, Void> typeSide = new TypeSide<>(col.tys, new HashMap<>(), new HashSet<>(), js, dpT, checkJava);

		Collage<String, String, Void, String, String, Void, Void> col0 = new Collage<>(typeSide.collage());
		Set<Triple<Pair<Var, String>, Term<String, String, Void, String, String, Void, Void>, Term<String, String, Void, String, String, Void, Void>>> eqs = new HashSet<>();

		for (SqlTable table : info.tables) {
			col0.ens.add(table.name);
			for (SqlColumn c : table.columns) {
				col0.atts.put(c.toString(), new Pair<>(table.name, "dom"));
			}
		}

		for (SqlForeignKey fk : info.fks) {
			col0.fks.put(fk.toString(), new Pair<>(fk.source.name, fk.target.name));

			Var v = new Var("x");

			for (SqlColumn tcol : fk.map.keySet()) {
				SqlColumn scol = fk.map.get(tcol);
				String l = scol.toString();
				String r = tcol.toString();
				Term<String, String, Void, String, String, Void, Void> lhs = Term.Att(l, Term.Var(v));
				Term<String, String, Void, String, String, Void, Void> rhs = Term.Att(r, Term.Fk(fk.toString(), Term.Var(v)));
				eqs.add(new Triple<>(new Pair<>(v, fk.source.name), lhs, rhs));
				col0.eqs.add(new Eq<>(new Ctx<>(new Pair<>(v, Chc.inRight(fk.source.name))), lhs, rhs));
			}
		}

		DP<String, String, Void, String, String, Void, Void> dp = AqlProver.create(new AqlOptions(options, col0), col0, js);

		Schema<String, String, Void, String, String> sch = new Schema<>(typeSide, col0.ens, col0.atts.map, col0.fks.map, eqs, dp, checkJava);

		Ctx<String, Collection<String>> ens0 = new Ctx<>(Util.newSetsFor0(sch.ens));
		Ctx<String, Collection<Null<String>>> tys0 = new Ctx<>();
		Ctx<String, Ctx<String, String>> fks0 = new Ctx<>();
		Ctx<String, Ctx<String, Term<String, Void, Void, Void, Void, Void, Null<String>>>> atts0 = new Ctx<>();
		AqlOptions op = new AqlOptions(options, null);

		for (String ty : sch.typeSide.tys) {
			tys0.put(ty, Util.singList(new Null<>(ty)));
		}

		int fr = 0;
		Map<SqlTable, Map<Map<SqlColumn, Optional<Object>>, String>> iso1 = new HashMap<>();
		// Map<SqlTable, Map<String, Map<SqlColumn, Optional<Object>>>> iso2 =
		// new HashMap<>();

		for (SqlTable table : info.tables) {
			Set<Map<SqlColumn, Optional<Object>>> tuples = inst.get(table);

			Map<Map<SqlColumn, Optional<Object>>, String> i1 = new HashMap<>();
			// Map<String, Map<SqlColumn, Optional<Object>>> i2 = new
			// HashMap<>();
			for (Map<SqlColumn, Optional<Object>> tuple : tuples) {
				String i = "v" + (fr++);
				i1.put(tuple, i);
				// i2.put(i, tuple);
				ens0.get(table.name).add(i);
				for (SqlColumn c : table.columns) {
					// SqlType ty = c.type;
					if (!atts0.containsKey(i)) {
						atts0.put(i, new Ctx<>());
					}
					Optional<Object> val = tuple.get(c);
					atts0.get(i).put(c.toString(), conv("dom", val));
				}
			}
			iso1.put(table, i1);
			// iso2.put(table, i2);
		}

		for (SqlForeignKey fk : info.fks) {
			for (Map<SqlColumn, Optional<Object>> in : inst.get(fk.source)) {
				Map<SqlColumn, Optional<Object>> out = inst.follow(in, fk);
				String tgen = iso1.get(fk.target).get(out);
				String sgen = iso1.get(fk.source).get(in);
				if (!fks0.containsKey(sgen)) {
					fks0.put(sgen, new Ctx<>());
				}
				fks0.get(sgen).put(fk.toString(), tgen);
			}
		}

		ImportAlgebra<String, String, Void, String, String, String, Null<String>> alg = new ImportAlgebra<>(sch, ens0, tys0, fks0, atts0, Object::toString, Object::toString);

		return new SaturatedInstance<>(alg, alg, (Boolean) op.getOrDefault(AqlOption.require_consistency), (Boolean) op.getOrDefault(AqlOption.allow_java_eqs_unsafe));
	}

	private static Term<String, Void, Void, Void, Void, Void, Null<String>> conv(String t, Optional<Object> val) {
		if (!val.isPresent()) {
			return Term.Sk(new Null<>(t));
		}
		return Term.Obj(val.get(), t);
	}

	@Override
	public Instance<String, String, Void, String, String, String, Null<String>, String, Null<String>> eval(AqlEnv env) {

		try (Connection conn = DriverManager.getConnection(jdbcString)) {
			SqlSchema sch = new SqlSchema(conn.getMetaData());
			SqlInstance inst = new SqlInstance(sch, conn);
			return toInstance(inst, sch);
		} catch (SQLException exn) {
			exn.printStackTrace();
			throw new RuntimeException("JDBC exception: " + exn.getMessage());
		}

	}

	@Override
	public String toString() {
		String s = "";
		if (!options.isEmpty()) {
			s = "options" + Util.sep(options, "\n\t\t", " = ");
		}
		return "import_jdbc_all " + Util.quote(clazz) + " " + Util.quote(jdbcString) + " {\n\t" + s + "\n}";
	}

	@Override
	public Collection<Pair<String, Kind>> deps() {
		Set<Pair<String, Kind>> ret = new HashSet<>();
		return ret;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((jdbcString == null) ? 0 : jdbcString.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstExpJdbcAll other = (InstExpJdbcAll) obj;
		AqlOptions op = new AqlOptions(options, null);
		Boolean reload = (Boolean) op.getOrDefault(AqlOption.always_reload);
		if (reload) {
			return false;
		}
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (jdbcString == null) {
			if (other.jdbcString != null)
				return false;
		} else if (!jdbcString.equals(other.jdbcString))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		return true;
	}

	@Override
	public SchExp<String, String, Void, String, String> type(AqlTyping G) {
		return new SchExp.SchExpInst<>(this);
	}

}

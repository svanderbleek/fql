package fql_lib.examples;

public class Patrick5Examples extends Example {

	@Override
	public boolean isPatrick() {
		return true;
	}
	
	@Override
	public String getName() {
		return "P Delta";
	}

	@Override
	public String getText() {
		return s;
	}
	
	String s = "string = type \"java.lang.String\""
			+ "\nint    = type \"java.lang.Integer\""
			+ "\n"
			+ "\nBob   = constant string \"Bob\""
			+ "\nSue   = constant string \"Sue\""
			+ "\nAlice = constant string \"Alice\""
			+ "\nSmith = constant string \"Smith\""
			+ "\nJones = constant string \"Jones\""
			+ "\nxxx   = constant string \"xxx\""
			+ "\n"
			+ "\nc115234 = constant string \"115-234\""
			+ "\nc112988 = constant string \"112-988\""
			+ "\nc198887 = constant string \"198-887\""
			+ "\nc100 = constant int \"100\""
			+ "\nc250 = constant int \"250\""
			+ "\nc300 = constant int \"300\""
			+ "\nc0   = constant int \"0\""
			+ "\n"
			+ "\nC = schema {"
			+ "\n nodes"
			+ "\n	T1, T2;"
			+ "\n edges"
			+ "\n	t1_ssn    : T1 -> string,"
			+ "\n	t1_first  : T1 -> string,"
			+ "\n	t1_last   : T1 -> string,"
			+ "\n	t2_first  : T2 -> string,"
			+ "\n	t2_last   : T2 -> string,"
			+ "\n	t2_salary : T2 -> int;"
			+ "\n equations; "
			+ "\n}"
			+ "\n"
			+ "\nD = schema {"
			+ "\n nodes "
			+ "\n	T;"
			+ "\n edges"
			+ "\n	ssn0    : T -> string,"
			+ "\n	first0  : T -> string,"
			+ "\n	last0   : T -> string,"
			+ "\n	salary0 : T -> int;"
			+ "\n equations;"
			+ "\n}"
			+ "\n"
			+ "\nF = mapping {"
			+ "\n nodes "
			+ "\n	T1 -> T,"
			+ "\n	T2 -> T;"
			+ "\n edges"
			+ "\n	t1_ssn    -> T.ssn0,"
			+ "\n	t1_first  -> T.first0,"
			+ "\n	t2_first  -> T.first0,"
			+ "\n	t1_last   -> T.last0,"
			+ "\n	t2_last   -> T.last0,"
			+ "\n	t2_salary -> T.salary0;"
			+ "\n} : C -> D"
			+ "\n"
			+ "\nJ = instance {"
			+ "\n variables "
			+ "\n	XF667 : T, XF891 : T, XF221 : T;"
			+ "\n equations"
			+ "\n	XF667.ssn0 = c115234, XF891.ssn0 = c112988, XF221.ssn0 = c198887,"
			+ "\n	XF667.first0 = Bob, XF891.first0 = Sue, XF221.first0 = Alice,"
			+ "\n	XF667.last0 = Smith, XF891.last0 = Smith, XF221.last0 = Jones,"
			+ "\n	XF667.salary0 = c250, XF891.salary0 = c300, XF221.salary0 = c100;"
			+ "\n} : D "
			+ "\n"
			+ "\ndf = delta F J"
			+ "\n"
			+ "\nJ0 = instance {"
			+ "\n variables "
			+ "\n	zXF667 : T, zXF891 : T, zXF221 : T, v : T;"
			+ "\n equations"
			+ "\n	zXF667.ssn0 = c115234, zXF891.ssn0 = c112988, zXF221.ssn0 = c198887, v.ssn0 = xxx,"
			+ "\n	zXF667.first0 = Bob, zXF891.first0 = Sue, zXF221.first0 = Alice, v.first0 = xxx,"
			+ "\n	zXF667.last0 = Smith, zXF891.last0 = Smith, zXF221.last0 = Jones, v.last0 = xxx,"
			+ "\n	zXF667.salary0 = c250, zXF891.salary0 = c300, zXF221.salary0 = c100, v.salary0 = c0;"
			+ "\n} : D "
			+ "\n"
			+ "\ntrans = transform {"
			+ "\n variables"
			+ "\n 	XF667 -> zXF667, XF891 -> zXF891, XF221 -> zXF221;"
			+ "\n} : J -> J0"
			+ "\n"
			+ "\ntrans0 = delta F trans"
			+ "\n";



;

}
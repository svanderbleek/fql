package fql_lib.examples;

import fql_lib.ide.Example;
import fql_lib.ide.Language;

public class IntegrationExample extends Example {

	@Override
	public Language lang() {
		return Language.FQLPP;
	}

	@Override
	public String getName() {
		return "Integration";
	}

	@Override
	public String getText() {
		return s;
	}
	
	String s = "//Example via Peter Gates"
			+ "\n/* p12 ****************************************************/"
			+ "\ncategory A = {"
			+ "\n	objects "
			+ "\n		Observation,"
			+ "\n		Person,"
			+ "\n		Gender,"
			+ "\n		ObsType;"
			+ "\n	arrows "
			+ "\n		f:Observation->Person, "
			+ "\n		g:Person->Gender,"
			+ "\n		h:Observation->ObsType;"
			+ "\n	equations;"
			+ "\n}"
			+ "\n"
			+ "\ncategory B = {"
			+ "\n	objects "
			+ "\n		Observation,"
			+ "\n		Patient,"
			+ "\n		Method,"
			+ "\n		Type;"
			+ "\n	arrows "
			+ "\n		f:Observation->Patient, "
			+ "\n		g:Observation->Method, "
			+ "\n		h:Method->Type;"
			+ "\n	equations;"
			+ "\n}"
			+ "\n"
			+ "\n/* p16 *****************************************************/"
			+ "\ncategory C = {"
			+ "\n	objects O,P,T;"
			+ "\n	arrows f:O->P, g:O->T;"
			+ "\n	equations;"
			+ "\n}"
			+ "\n"
			+ "\nfunctor J = {"
			+ "\n	objects O->Observation,P->Person, T->ObsType;"
			+ "\n	arrows f->Observation.f, g->Observation.h;"
			+ "\n} : C -> A"
			+ "\n"
			+ "\nfunctor K = {"
			+ "\n	objects O->Observation,P->Patient,T->Type;"
			+ "\n	arrows f->Observation.f, g->Observation.g.h;"
			+ "\n} :  C -> B"
			+ "\n"
			+ "\n/* p17 *************************************************/"
			+ "\ncategory PushoutSchema = { //This should be automatic"
			+ "\n	objects "
			+ "\n		Gender,"
			+ "\n		Type,"
			+ "\n		Patient,"
			+ "\n		Method,"
			+ "\n		Observation;"
			+ "\n	arrows"
			+ "\n		f:Observation->Patient,"
			+ "\n		g:Observation->Method,"
			+ "\n		h:Method->Type,"
			+ "\n		i:Patient->Gender;"
			+ "\n	equations;"
			+ "\n}"
			+ "\n"
			+ "\nfunctor iA = {//This is the natural inclusion of A into colimit"
			+ "\n	objects "
			+ "\n		Gender->Gender, "
			+ "\n		Person->Patient,"
			+ "\n		Observation->Observation,"
			+ "\n		ObsType->Type;"
			+ "\n	arrows"
			+ "\n		f->Observation.f,"
			+ "\n		g->Patient.i,"
			+ "\n		h->Observation.g.h;"
			+ "\n} : A -> PushoutSchema"
			+ "\n"
			+ "\nfunctor iB = {//This is the natural inclusion of B into colimit"
			+ "\n	objects "
			+ "\n		Patient->Patient,"
			+ "\n		Observation->Observation,"
			+ "\n		Method->Method,"
			+ "\n		Type->Type;"
			+ "\n	arrows"
			+ "\n		f->Observation.f,"
			+ "\n		g->Observation.g,"
			+ "\n		h->Method.h;"
			+ "\n} : B -> PushoutSchema"
			+ "\n"
			+ "\n//We can make a commutative square mapping to Cat to show that J;iA=K;iB."
			+ "\n//C1 = "
			+ "\n// O f-> P"
			+ "\n// |g    | x1"
			+ "\n// \\/    \\/"
			+ "\n// T ->x2 X"
			+ "\ncategory C1 = {"
			+ "\n	objects O,P,T,X;"
			+ "\n	arrows f:O->P, g:O->T, x1:P->X, x2:T->X;"
			+ "\n	equations O.g.x2 = O.f.x1;"
			+ "\n}"
			+ "\n"
			+ "\nfunctor check1 = {"
			+ "\n	objects P -> A, T -> B, O -> C, X -> PushoutSchema;"
			+ "\n	arrows f -> J, x1 -> iA, g -> K, x2 -> iB;"
			+ "\n} : C1 -> Cat "
			+ "\n"
			+ "\n"
			+ "\n/* p19 *********************************************************/"
			+ "\nfunctor sig = {"
			+ "\n	objects"
			+ "\n		O->{},"
			+ "\n		P->{Peter},"
			+ "\n		T->{BloodPressure,BodyWeight};"
			+ "\n	arrows f->{},g->{};"
			+ "\n} : C -> Set"
			+ "\n"
			+ "\nfunctor InstA = {"
			+ "\n	objects "
			+ "\n		Observation->{1,2,3},"
			+ "\n		Person->{Peter,Paul},"
			+ "\n		Gender->{M,F},"
			+ "\n		ObsType->{BloodPressure,BodyWeight,HeartRate};"
			+ "\n	arrows"
			+ "\n		f->{(1,Peter),(2,Peter),(3,Paul)},"
			+ "\n		g->{(Peter,M),(Paul,M)},"
			+ "\n		h->{(1,BloodPressure),(2,BodyWeight),(3,HeartRate)};"
			+ "\n} : A -> Set"
			+ "\n"
			+ "\nfunctor AC = (J ; InstA) //delta is composition"
			+ "\n"
			+ "\n/* p21 ***************************************************/"
			+ "\nfunctor InstB = {"
			+ "\n	objects"
			+ "\n		Observation->{1,2,3,4},"
			+ "\n		Patient->{Pete,Jane},"
			+ "\n		Method->{1,2,3,4},"
			+ "\n		Type->{BP,Wt};"
			+ "\n	arrows"
			+ "\n		f->{(1,Pete),(2,Pete),(3,Jane),(4,Jane)},"
			+ "\n		g->{(1,1),(2,2),(3,3),(4,1)},"
			+ "\n		h->{(1,BP),(2,BP),(3,Wt),(4,Wt)};"
			+ "\n} : B -> Set"
			+ "\n"
			+ "\nfunctor BC= (K ; InstB)"
			+ "\n"
			+ "\n/* p26 *******************************************/"
			+ "\n"
			+ "\ntransform phi = {"
			+ "\n	objects"
			+ "\n		O->{},"
			+ "\n		P->{(Peter,Peter)},"
			+ "\n		T->{(BloodPressure,BloodPressure),(BodyWeight,BodyWeight)};"
			+ "\n} : (sig:C->Set) -> (AC:C->Set)"
			+ "\n"
			+ "\ntransform del = {"
			+ "\n	objects"
			+ "\n		O->{},"
			+ "\n		P->{(Peter,Pete)},"
			+ "\n		T->{(BloodPressure,BP),(BodyWeight,Wt)};"
			+ "\n} : (sig:C->Set) -> (BC:C->Set)"
			+ "\n"
			+ "\nfunctor X = {"
			+ "\n	objects O -> sig, P -> BC, T -> AC;"
			+ "\n	arrows g -> phi, f -> del;"
			+ "\n} : C -> (Set^C)"
			+ "\n"
			+ "\nfunctor colimX = apply sigma snd C C on object uncurry X"
			+ "\ntransform eta  = apply return sigma delta snd C C on uncurry X "
			+ "\n"
			+ "\n// Now we have to tweak colimX and eta to build our commutative square"
			+ "\ncategory D = {"
			+ "\n 	objects uniq;"
			+ "\n 	arrows;"
			+ "\n 	equations;"
			+ "\n}"
			+ "\n"
			+ "\nfunctor colimX2 = curry (snd D C; colimX)"
			+ "\ntransform eta2 = CURRY eta"
			+ "\n"
			+ "\n//C1 ="
			+ "\n//"
			+ "\n// O f-> P"
			+ "\n// |g    | x1"
			+ "\n// \\/    \\/"
			+ "\n// T ->x2 X"
			+ "\n"
			+ "\n//since check2 is a functor that depends on transforms,"
			+ "\n//we have to explicitly name the transforms here"
			+ "\ntransform ft = apply X on path O.f in C"
			+ "\ntransform gt = apply X on path O.g in C"
			+ "\ntransform x1t = APPLY eta2 on P"
			+ "\ntransform x2t = APPLY eta2 on T"
			+ "\n"
			+ "\nfunctor check2 = {"
			+ "\n	objects "
			+ "\n		O -> sig, "
			+ "\n		P -> BC, "
			+ "\n		T -> AC,"
			+ "\n		X -> apply colimX2 on object uniq;"
			+ "\n	arrows "
			+ "\n		f -> ft, "
			+ "\n		g -> gt, "
			+ "\n		x1 -> x1t, "
			+ "\n		x2 -> x2t;"
			+ "\n} : C1 -> (Set^C) "
			+ "\n"
			+ "\n/* ************************************************************************************"
			+ "\n *  In the rest of the presentation, we construct a colim and commutative diagram on "
			+ "\n * PushoutSchema->Set, rather than C->Set."
			+ "\n */"
			+ "\ntransform t1a = apply sigma K on arrow del"
			+ "\ntransform t1b = apply coreturn sigma delta K on InstB"
			+ "\ntransform t1c = (t1a; t1b)  "
			+ "\ntransform t1  = apply sigma iB on arrow t1c"
			+ "\n"
			+ "\ntransform t2a = apply sigma J on arrow phi"
			+ "\ntransform t2b = apply coreturn sigma delta J on InstA"
			+ "\ntransform t2c = (t2a; t2b)  "
			+ "\ntransform t2  = apply sigma iA on arrow t2c"
			+ "\n"
			+ "\n/////////////////////////////////////////////////////////////////////////////////////"
			+ "\n//now, we want to construct a commutative square, along t1 and t2, but "
			+ "\n//the domains of t1 and t2 are isomorphic, not equal.  "
			+ "\n//we can use 'iso' to construct the isomorphism automatically."
			+ "\nfunctor xxx1 = dom t1"
			+ "\nfunctor xxx2 = dom t2"
			+ "\n"
			+ "\ntransform xxx= iso1 xxx1 xxx2"
			+ "\ntransform yyy= iso2 xxx1 xxx2"
			+ "\ntransform t2_hack = (xxx; t2)"
			+ "\ntransform t1_hack = (yyy; t1)"
			+ "\n"
			+ "\n//now we repeat the colim construction but on PushoutSchema instead of C"
			+ "\n//C = "
			+ "\n// O f-> P"
			+ "\n// |g    "
			+ "\n// \\/    "
			+ "\n// T "
			+ "\nfunctor Y = {"
			+ "\n	objects O -> dom t1, P -> cod t1, T -> cod t2;"
			+ "\n	arrows g -> t2_hack, f -> t1;"
			+ "\n} : C -> (Set^PushoutSchema) "
			+ "\n"
			+ "\nfunctor colimY = apply sigma snd C PushoutSchema on object uncurry Y"
			+ "\ntransform etta  = apply return sigma delta snd C PushoutSchema on uncurry Y"
			+ "\nfunctor colimY2 = curry (snd D PushoutSchema; colimY)"
			+ "\ntransform etta2 = CURRY etta"
			+ "\n"
			+ "\ntransform ft2 = apply Y on path O.f in C"
			+ "\ntransform gt2 = apply Y on path O.g in C"
			+ "\ntransform x1t2 = APPLY etta2 on P"
			+ "\ntransform x2t2 = APPLY etta2 on T"
			+ "\n"
			+ "\n//C1 ="
			+ "\n//"
			+ "\n// O f-> P"
			+ "\n// |g    | x1"
			+ "\n// \\/    \\/"
			+ "\n// T ->x2 X"
			+ "\nfunctor check3 = {"
			+ "\n	objects "
			+ "\n		O -> dom ft2, "
			+ "\n		P -> cod ft2, "
			+ "\n		T -> cod gt2,"
			+ "\n		X -> apply colimY2 on object uniq;"
			+ "\n	arrows "
			+ "\n		f -> ft2, "
			+ "\n		g -> gt2, "
			+ "\n		x1 -> x1t2, "
			+ "\n		x2 -> x2t2;"
			+ "\n} : C1 -> (Set^PushoutSchema) "
			+ "\n"
			+ "\n//////////////////////////////////////////////////////////////////////////"
			+ "\n//or, rather than use 'iso', to connect dom(t1) and dom(t2), we can "
			+ "\n//construct transforms t1j : Q -> dom(t1) and t2j : Q -> dom(t2) "
			+ "\n"
			+ "\ntransform t1d = apply return sigma delta K on sig"
			+ "\nfunctor t1x = delta K"
			+ "\ntransform t1y = return sigma delta iB "
			+ "\nfunctor dsiB = (sigma iB; delta iB)"
			+ "\ncategory bset = (Set^B)"
			+ "\nfunctor idBset = id bset"
			+ "\ntransform t1e = right whisker t1x t1y"
			+ "\ntransform t1e2 = apply t1e on apply sigma K on object sig"
			+ "\ntransform t1f = (t1d ; t1e2) "
			+ "\ntransform t1g = apply sigma (K ; iB) on arrow t1f"
			+ "\ntransform t1h = coreturn sigma delta (K ; iB) "
			+ "\ntransform t1i = apply t1h on apply sigma iB on object apply sigma K on object sig"
			+ "\ntransform t1j = (t1g ; t1i) "
			+ "\n"
			+ "\nfunctor Q = dom t1j"
			+ "\n"
			+ "\ntransform t2d = apply return sigma delta J on sig"
			+ "\nfunctor t2x = delta J"
			+ "\ntransform t2y = return sigma delta iA "
			+ "\nfunctor dsiA = (sigma iA; delta iA)"
			+ "\ncategory aset = (Set^A)"
			+ "\nfunctor idAset = id aset"
			+ "\ntransform t2e = right whisker t2x t2y"
			+ "\ntransform t2e2 = apply t2e on apply sigma J on object sig"
			+ "\ntransform t2f = (t2d ; t2e2) "
			+ "\ntransform t2g = apply sigma (J ; iA) on arrow t2f"
			+ "\ntransform t2h = coreturn sigma delta (J ; iA) "
			+ "\ntransform t2i = apply t2h on apply sigma iA on object apply sigma J on object sig"
			+ "\ntransform t2j = (t2g ; t2i) "
			+ "\n"
			+ "\ntransform t1_real = (t1j ; t1)"
			+ "\ntransform t2_real = (t2j ; t2)"
			+ "\n"
			+ "\n//then we repeat the colim construction as before"
			+ "\nfunctor Z = {"
			+ "\n	objects O -> dom t1_real, P -> cod t1_real, T -> cod t2_real;"
			+ "\n	arrows g -> t2_real, f -> t1_real;"
			+ "\n} : C -> (Set^PushoutSchema) "
			+ "\n"
			+ "\nfunctor colimZ = apply sigma snd C PushoutSchema on object uncurry Z"
			+ "\ntransform ettta  = apply return sigma delta snd C PushoutSchema on uncurry Z"
			+ "\nfunctor colimZ2 = curry (snd D PushoutSchema; colimZ)"
			+ "\ntransform ettta2 = CURRY ettta"
			+ "\n"
			+ "\ntransform ft2_r = apply Z on path O.f in C"
			+ "\ntransform gt2_r = apply Z on path O.g in C"
			+ "\ntransform x1t2_r = APPLY ettta2 on P"
			+ "\ntransform x2t2_r = APPLY ettta2 on T"
			+ "\n"
			+ "\nfunctor ANSWER = apply colimZ2 on object uniq"
			+ "\n"
			+ "\n//and this is our commutative square"
			+ "\nfunctor check4 = {"
			+ "\n	objects "
			+ "\n		O -> dom ft2_r, "
			+ "\n		P -> cod ft2_r, "
			+ "\n		T -> cod gt2_r,"
			+ "\n		X -> ANSWER;"
			+ "\n	arrows "
			+ "\n		f -> ft2_r, "
			+ "\n		g -> gt2_r, "
			+ "\n		x1 -> x1t2_r, "
			+ "\n		x2 -> x2t2_r;"
			+ "\n} : C1 -> (Set^PushoutSchema)"
			+ "\n";



	
}

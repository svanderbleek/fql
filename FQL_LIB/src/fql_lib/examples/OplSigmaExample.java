package fql_lib.examples;

import fql_lib.ide.Example;
import fql_lib.ide.Language;

public class OplSigmaExample extends Example {
	
	@Override
	public Language lang() {
		return Language.OPL;
	} 

	@Override
	public String getName() {
		return "Sigma";
	}

	@Override
	public String getText() {
		return s;
	}
	
	String s = 
			"C = theory {"
					+ "\n	sorts "
					+ "\n		Amphibian,"
					+ "\n		LandAnimal,"
					+ "\n		WaterAnimal,"
					+ "\n		String;"
					+ "\n	symbols"
					+ "\n		attA: Amphibian -> String, "
					+ "\n		attL: LandAnimal -> String, "
					+ "\n		attW: WaterAnimal -> String,"
					+ "\n		IsAL: Amphibian -> LandAnimal,"
					+ "\n		IsAW: Amphibian -> WaterAnimal;"
					+ "\n	equations;"
					+ "\n}"
					+ "\n"
					+ "\nI0= presentation {"
					+ "\n	generators "
					+ "\n		a1, a2 : Amphibian,"
					+ "\n		l1, l2, l3, l4, l5 : LandAnimal,"
					+ "\n		w1, w2, w3, w4 : WaterAnimal,"
					+ "\n		gecko, frog, human, cow, horse, dolphin, fish : String;"
					+ "\n	equations"
					+ "\n		 attA(a1) = gecko,  attA(a2) = frog,"
					+ "\n		 attL(l1) = gecko,  attL(l2) = frog, "
					+ "\n		 attL(l3) = human,  attL(l4) = cow, "
					+ "\n		 attL(l5) = horse,  attW(w1) = fish, "
					+ "\n		 attW(w2) = gecko,  attW(w3) = frog, "
					+ "\n		 attW(w4) = dolphin,  IsAL(a1) = l1, "
					+ "\n		 IsAL(a2) = l2,  IsAW(a1) = w2,  IsAW(a2) = w3; "
					+ "\n} : C"
					+ "\n"
					+ "\nD = theory {"
					+ "\n	sorts "
					+ "\n		yAmphibian,"
					+ "\n		yLandAnimal,"
					+ "\n		yWaterAnimal,"
					+ "\n		yAnimal,"
					+ "\n		String;"
					+ "\n	symbols"
					+ "\n		yattA:yAmphibian->String, "
					+ "\n		yattL:yLandAnimal->String, "
					+ "\n		yattW:yWaterAnimal->String,"
					+ "\n		yIsAL:yAmphibian->yLandAnimal,"
					+ "\n		yIsAW:yAmphibian->yWaterAnimal,"
					+ "\n		yIsALL:yLandAnimal->yAnimal,"
					+ "\n		yIsAWW:yWaterAnimal->yAnimal;"
					+ "\n	equations"
					+ "\n		forall x. yIsALL(yIsAL(x)) = yIsAWW(yIsAW(x));"
					+ "\n}"
					+ "\n"
					+ "\nF = mapping {"
					+ "\n	sorts "
					+ "\n		Amphibian->yAmphibian,"
					+ "\n		LandAnimal->yLandAnimal,"
					+ "\n		WaterAnimal->yWaterAnimal,"
					+ "\n		String -> String;"
					+ "\n	symbols"
					+ "\n		attA -> forall x. yattA(x), "
					+ "\n		attL -> forall x. yattL(x), "
					+ "\n		attW -> forall x. yattW(x),"
					+ "\n		IsAL -> forall x. yIsAL(x),"
					+ "\n		IsAW -> forall x. yIsAW(x);"
					+ "\n} : C -> D"
					+ "\n"
					+ "\nI = saturate I0"
					+ "\nJ = sigma F I0"
					+ "\nK = saturate J //requires KB on sorts without constants"
					+ "\n"
					+ "\nI1= presentation {"
					+ "\n	generators "
					+ "\n		xa1 : Amphibian,"
					+ "\n		xl1, xl2, xl3, xl4 : LandAnimal,"
					+ "\n		xw1, xw2, xw3: WaterAnimal,"
					+ "\n		gecko, frog, human, cow, horse, dolphin, fish : String;"
					+ "\n	equations"
					+ "\n		attL(xl1) = gecko, attL(xl2) = frog, "
					+ "\n		attL(xl3) = human, attL(xl4) = cow, "
					+ "\n		attW(xw1) = fish, attW(xw2) = gecko, "
					+ "\n		attW(xw3) = frog, IsAL(xa1) = xl1, "
					+ "\n		IsAW(xa1) = xw2, attA(xa1) = gecko; "
					+ "\n} : C"
					+ "\n"
					+ "\nt = transpres {"
					+ "\n	sorts "
					+ "\n		String -> {(gecko,gecko),(frog,frog),(human,human),(cow,cow),"
					+ "\n				 (horse,horse),(dolphin,dolphin),(fish,fish)},"
					+ "\n		Amphibian -> {(xa1,a1)},"
					+ "\n		LandAnimal -> {(xl1,l1),(xl2,l2),(xl3,l3),(xl4,l4)},"
					+ "\n		WaterAnimal -> {(xw1,w1),(xw2,w2),(xw3,w3)};"
					+ "\n} : I1 -> I0"
					+ "\n"
					+ "\nt0 = sigma F t"
					+ "\n"
					+ "\n////////////////////////////////////"
					+ "\n"
					+ "\nC0 = schema {"
					+ "\n	entities Amphibian, LandAnimal, WaterAnimal;"
					+ "\n} : C"
					+ "\n"
					+ "\nD0 = schema {"
					+ "\n	entities yAmphibian, yLandAnimal, yWaterAnimal, yAnimal;"
					+ "\n} : D"
					+ "\n"
					+ "\nIX = instance C0 I0 none"
					+ "\n"
					+ "\nFX = mapping {"
					+ "\n	sorts "
					+ "\n		Amphibian->yAmphibian,"
					+ "\n		LandAnimal->yLandAnimal,"
					+ "\n		WaterAnimal->yWaterAnimal;"
					+ "\n	symbols"
					+ "\n		attA -> forall x. yattA(x), "
					+ "\n		attL -> forall x. yattL(x), "
					+ "\n		attW -> forall x. yattW(x),"
					+ "\n		IsAL -> forall x. yIsAL(x),"
					+ "\n		IsAW -> forall x. yIsAW(x);"
					+ "\n} : C0 -> D0"
					+ "\n"
					+ "\nJX = sigma FX IX"
					+ "\n";

}

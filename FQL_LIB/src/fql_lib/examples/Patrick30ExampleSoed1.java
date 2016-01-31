package fql_lib.examples;

import fql_lib.ide.Example;
import fql_lib.ide.Language;

public class Patrick30ExampleSoed1 extends Example {

	@Override
	public Language lang() {
		return Language.FPQL;
	}

	
	@Override
	public String getName() {
		return "SOED";
	}

	@Override
	public String getText() {
		return s;
	}

	String s = "String : type"
			+ "\n"
			+ "\ngecko frog human cow horse dolphin fish : String"
			+ "\n"
			+ "\nC = schema {"
			+ "\n	nodes "
			+ "\n		Amphibian,"
			+ "\n		LandAnimal,"
			+ "\n		WaterAnimal;"
			+ "\n	edges"
			+ "\n		attA: Amphibian -> String, "
			+ "\n		attL: LandAnimal -> String, "
			+ "\n		attW: WaterAnimal -> String,"
			+ "\n		IsAL: Amphibian -> LandAnimal,"
			+ "\n		IsAW: Amphibian -> WaterAnimal;"
			+ "\n	equations;"
			+ "\n}"
			+ "\n"
			+ "\nI = instance {"
			+ "\n	variables "
			+ "\n		a1 a2: Amphibian,"
			+ "\n		l1 l2 l3 l4 l5: LandAnimal,"
			+ "\n		w1 w2 w3 w4: WaterAnimal;"
			+ "\n	equations"
			+ "\n		a1.attA = gecko, a2.attA = frog,"
			+ "\n		l1.attL = gecko, l2.attL = frog, l3.attL = human, l4.attL = cow, l5.attL = horse,"
			+ "\n		w1.attW = fish, w2.attW = gecko, w3.attW = frog, w4.attW = dolphin,"
			+ "\n		a1.IsAL = l1, a2.IsAL = l2,"
			+ "\n		a1.IsAW = w2, a2.IsAW = w3; "
			+ "\n} : C"
			+ "\n"
			+ "\nD = schema {"
			+ "\n	nodes "
			+ "\n		yAmphibian,"
			+ "\n		yLandAnimal,"
			+ "\n		yWaterAnimal,"
			+ "\n		yAnimal;"
			+ "\n	edges"
			+ "\n		yattA:yAmphibian->String, "
			+ "\n		yattL:yLandAnimal->String, "
			+ "\n		yattW:yWaterAnimal->String,"
			+ "\n		yIsAL:yAmphibian->yLandAnimal,"
			+ "\n		yIsAW:yAmphibian->yWaterAnimal,"
			+ "\n		yIsALL:yLandAnimal->yAnimal,"
			+ "\n		yIsAWW:yWaterAnimal->yAnimal;"
			+ "\n	equations"
			+ "\n		yAmphibian.yIsAL.yIsALL=yAmphibian.yIsAW.yIsAWW;"
			+ "\n}"
			+ "\n"
			+ "\nF = mapping {"
			+ "\n	nodes "
			+ "\n		Amphibian->yAmphibian,"
			+ "\n		LandAnimal->yLandAnimal,"
			+ "\n		WaterAnimal->yWaterAnimal;"
			+ "\n	edges"
			+ "\n		attA -> yAmphibian.yattA, "
			+ "\n		attL -> yLandAnimal.yattL, "
			+ "\n		attW -> yWaterAnimal.yattW,"
			+ "\n		IsAL -> yAmphibian.yIsAL,"
			+ "\n		IsAW -> yAmphibian.yIsAW;"
			+ "\n} : C -> D"
			+ "\n"
			+ "\nJ = sigma F I"
			+ "\n	"
			+ "\nJ0 = soed {"
			+ "\n	exists am : Amphibian->yAmphibian,"
			+ "\n		  la : LandAnimal->yLandAnimal,"
			+ "\n		  wa : WaterAnimal->yWaterAnimal;"
			+ "\n	forall a:Amphibian, a.am.yattA = a.attA, "
			+ "\n				     a.IsAL.la = a.am.yIsAL, "
			+ "\n				     a.IsAW.wa = a.am.yIsAW;"
			+ "\n	forall l:LandAnimal, l.la.yattL = l.attL ;"
			+ "\n	forall w:WaterAnimal, w.wa.yattW = w.attW ;		  "
			+ "\n} : C -> D on I"
			+ "\n"
			+ "\nJ1 = supersoed {"
			+ "\n	exists am : Amphibian -> yAmphibian,"
			+ "\n	  la : LandAnimal -> yLandAnimal,"
			+ "\n	  wa : WaterAnimal -> yWaterAnimal;"
			+ "\nforall a:Amphibian, -> @am(a).yattA = a.attA, "
			+ "\n			     @la(a.IsAL) = @am(a).yIsAL, "
			+ "\n			     @wa(a.IsAW) = @am(a).yIsAW;"
			+ "\nforall l:LandAnimal, -> @la(l).yattL = l.attL ;"
			+ "\nforall w:WaterAnimal, -> @wa(w).yattW = w.attW ;"		  
			+ "\n} : C -> D on I"
			+ "\n"
			+ "\n/*"	
			+ "\nJ0 = soed {"
			+ "\nexists am : Amphibian->yAmphibian,"
			+ "\n	  la : LandAnimal->yLandAnimal,"
			+ "\n	  wa : WaterAnimal->yWaterAnimal;"
			+ "\nattA = am.yattA, "
			+ "\nattL = la.yattL,"
			+ "\nattW = wa.yattW,"		  
			+ "\nIsAL.la = am.yIsAL," 
			+ "\nIsAW.wa = am.yIsAW;"
			+ "\n		} : C -> D on I"
			+ "\n*/"
			+ "\n";
}

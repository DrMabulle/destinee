package destinee.main;

import java.math.BigDecimal;

import destinee.probas.Proba;
import destinee.probas.ResolutionAttaque;
import destinee.utils.ConversionUtil;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		long globaStartTime = System.currentTimeMillis();

		long startTime = System.currentTimeMillis();
		System.out.println("D�but du traitement : proba(170, 19)");
		BigDecimal resultat1 = Proba.calculerProba(170, 19);
		long stopTime = System.currentTimeMillis();
		long time1 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out.println("D�but du traitement : proba(168, 18)");
		BigDecimal resultat2 = Proba.calculerProba(168, 18);
		stopTime = System.currentTimeMillis();
		long time2 = stopTime - startTime;

		int nbDAtt = 30;
		int nbDDef = 27;

		int bonusAtt = 11;
		int bonusDef = 3;

		startTime = System.currentTimeMillis();
		System.out.println("D�but du traitement : r�solution d'une attaque (" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef
				+ ", reussite critique ");
		BigDecimal resultat3 = ResolutionAttaque.resoudreAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		stopTime = System.currentTimeMillis();
		long time3 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out.println("D�but du traitement : r�solution d'une attaque (" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef
				+ ", reussite ");
		BigDecimal resultat4 = ResolutionAttaque.resoudreAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		stopTime = System.currentTimeMillis();
		long time4 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out
				.println("D�but du traitement : r�solution d'une attaque (" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef + ", esquive ");
		BigDecimal resultat5 = ResolutionAttaque.resoudreAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		stopTime = System.currentTimeMillis();
		long time5 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out.println("D�but du traitement : r�solution d'une attaque (" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef
				+ ", esquive critique ");
		BigDecimal resultat6 = ResolutionAttaque.resoudreAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		stopTime = System.currentTimeMillis();
		long time6 = stopTime - startTime;

		long globalStopTime = System.currentTimeMillis();
		System.out.println("R�cap des temps : ");
		System.out.println("Traitement 1 ex�cut� en " + ConversionUtil.longVersStringFormat(time1) + " millisecondes");
		System.out.println("proba(170,19) = " + ConversionUtil.bigDecimalVersString(resultat1, 12));
		System.out.println("Traitement 2 ex�cut� en " + ConversionUtil.longVersStringFormat(time2) + " millisecondes");
		System.out.println("proba(168, 18) = " + ConversionUtil.bigDecimalVersString(resultat2, 12));
		System.out.println("Traitement 3 ex�cut� en " + ConversionUtil.longVersStringFormat(time3) + " millisecondes");
		System.out.println("chances de toucher avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + " (reussite critique ) : " 
				+ ConversionUtil.bigDecimalVersString(resultat3, 12));
		System.out.println("Traitement 4 ex�cut� en " + ConversionUtil.longVersStringFormat(time4) + " millisecondes");
		System.out.println("chances de toucher avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + " (reussite normale ) : " 
				+ ConversionUtil.bigDecimalVersString(resultat4, 12));
		System.out.println("Traitement 5 ex�cut� en " + ConversionUtil.longVersStringFormat(time5) + " millisecondes");
		System.out.println("chances de toucher avec avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + " (esquive ) : " 
				+ ConversionUtil.bigDecimalVersString(resultat5, 12));
		System.out.println("Traitement 6 ex�cut� en " + ConversionUtil.longVersStringFormat(time6) + " millisecondes");
		System.out.println("chances de toucher avec avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + " (esquive critique ) : "
				+ ConversionUtil.bigDecimalVersString(resultat6, 12));
		System.out.println("Traitement total ex�cut� en " + ConversionUtil.longVersStringFormat(globalStopTime - globaStartTime) + " millisecondes");
		System.out.println(ConversionUtil.bigDecimalVersString(resultat3.add(resultat4.add(resultat5.add(resultat6))), 12));
	}

}

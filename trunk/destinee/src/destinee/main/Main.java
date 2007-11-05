package destinee.main;

import java.math.BigDecimal;

import destinee.probas.Proba;
import destinee.probas.ProbaPlus;
import destinee.probas.ProbaMoins;
import destinee.probas.ResolAttaque;
import destinee.utils.ConversionUtil;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		long globaStartTime = System.currentTimeMillis();
		
		long startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : proba(170, 19)");
		BigDecimal resultat1 = Proba.calculerProba(170, 19);
		long stopTime = System.currentTimeMillis();
		long time1 = stopTime - startTime;
		
		
		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : proba(168, 18)");
		BigDecimal resultat2 = Proba.calculerProba(168, 18);
		stopTime = System.currentTimeMillis();
		long time2 = stopTime - startTime;
		
		int nbDAtt = 30;
		int nbDDef = 27;
		
		int bonusAtt = 11;
		int bonusDef = 3;
		
		
		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : résolution d'une attaque ("+ nbDAtt + "D+" + bonusAtt + " contre " + nbDDef+"D+"+ bonusDef +", reussite critique " );
		BigDecimal resultat3 = ResolAttaque.resolAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, 0);
		stopTime = System.currentTimeMillis();
		long time3 = stopTime - startTime;
		
		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : résolution d'une attaque ("+ nbDAtt + "D+" + bonusAtt + " contre " + nbDDef+"D+"+ bonusDef +", reussite " );
		BigDecimal resultat4 = ResolAttaque.resolAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, 1);
		stopTime = System.currentTimeMillis();
		long time4 = stopTime - startTime;
		
		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : résolution d'une attaque ("+ nbDAtt + "D+" + bonusAtt + " contre " + nbDDef+"D+"+ bonusDef +", esquive " );
		BigDecimal resultat5 = ResolAttaque.resolAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, 2);
		stopTime = System.currentTimeMillis();
		long time5 = stopTime - startTime;
		
		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : résolution d'une attaque ("+ nbDAtt + "D+" + bonusAtt + " contre " + nbDDef+"D+"+ bonusDef +", esquive critique " );
		BigDecimal resultat6 = ResolAttaque.resolAttaque(nbDAtt, bonusAtt, nbDDef, bonusDef, 3);
		stopTime = System.currentTimeMillis();
		long time6 = stopTime - startTime;
		
		
		long globalStopTime = System.currentTimeMillis();
		System.out.println("Récap des temps : ");
		System.out.println("Traitement 1 exécuté en " + ConversionUtil.longVersStringFormat(time1) + " millisecondes");
		System.out.println("proba(170,19) = " + resultat1) ;
		System.out.println("Traitement 2 exécuté en " + ConversionUtil.longVersStringFormat(time2) + " millisecondes");
		System.out.println("proba(168, 18) = " + resultat2) ;
		System.out.println("Traitement 3 exécuté en " + ConversionUtil.longVersStringFormat(time3) + " millisecondes");
		System.out.println("chances de toucher avec "+ nbDAtt + "D+"+ bonusAtt + " sur " + nbDDef + "D+"+bonusDef + " (reussite critique ) :"  +resultat3) ;
		System.out.println("Traitement 4 exécuté en " + ConversionUtil.longVersStringFormat(time4) + " millisecondes");
		System.out.println("chances de toucher avec "+ nbDAtt + "D+"+ bonusAtt + " sur " + nbDDef + "D+"+bonusDef + " (reussite normale ) :" + resultat4) ;
		System.out.println("Traitement 5 exécuté en " + ConversionUtil.longVersStringFormat(time5) + " millisecondes");
		System.out.println("chances de toucher avec avec "+ nbDAtt + "D+"+ bonusAtt + " sur " + nbDDef + "D+"+bonusDef + " (esquive ) :" + resultat5) ;
		System.out.println("Traitement 6 exécuté en " + ConversionUtil.longVersStringFormat(time6) + " millisecondes");
		System.out.println("chances de toucher avec avec "+ nbDAtt + "D+"+ bonusAtt + " sur " + nbDDef + "D+"+bonusDef + " (esquive critique ) : " + resultat6) ;
		System.out.println("Traitement total exécuté en " + ConversionUtil.longVersStringFormat(globalStopTime - globaStartTime) + " millisecondes");
		System.out.println( resultat3.add(resultat4.add(resultat5.add(resultat6))));
	}

}

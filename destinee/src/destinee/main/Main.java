package destinee.main;

import java.math.BigDecimal;

import destinee.core.utils.ConversionUtil;
import destinee.data.Attaque;
import destinee.data.AttaqueBerserk;
import destinee.data.AttaqueBrutale;
import destinee.data.AttaqueImparable;
import destinee.data.AttaqueKamikaze;
import destinee.data.AttaqueMagique;
import destinee.data.AttaqueNormale;
import destinee.data.AttaquePrecise;
import destinee.data.AttaqueRapide;
import destinee.data.Cible;
import destinee.data.Perso;
import destinee.probas.Proba;
import destinee.probas.ResolutionAttaque;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

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

		Perso noone = new Perso(12,10,8,5,5,-3);
		
		Cible cible = new Cible(5,3,9);
		
		int nbDAtt = noone.getNombreDeDesAttaque();
		int bonusAtt = noone.getBonusAttaque();
		
		int nbDDef = cible.getNombreDeDesDefense();
		int bonusDef = cible.getBonusDefense();

		Attaque attaqueUn = new AttaqueNormale(noone);
		Attaque attaqueDeux = new AttaqueImparable(noone);
//		Attaque attaqueTrois = new AttaqueImparable(noone);
		
		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : résolution d'une attaque précise (" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef
				+ ", reussite critique ");
		BigDecimal resultat3 = ResolutionAttaque.ResoudreAttaque(attaqueDeux, cible,0);
		stopTime = System.currentTimeMillis();
		long time3 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : dégats moyens d'une attaque normale (" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef);
		double resultat4 = ResolutionAttaque.esperanceDeDegats(attaqueUn, cible);
		stopTime = System.currentTimeMillis();
		long time4 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out
				.println("Début du traitement : dégats moyens attaque brutale(" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef);
		double resultat5 = ResolutionAttaque.esperanceDeDegats(attaqueDeux, cible,0);
		stopTime = System.currentTimeMillis();
		long time5 = stopTime - startTime;

		startTime = System.currentTimeMillis();
		System.out.println("Début du traitement : dégats moyens d'une attaque imparable(" + nbDAtt + "D+" + bonusAtt + " contre " + nbDDef + "D+" + bonusDef);
		double resultat6 = ResolutionAttaque.esperanceDeDegats(attaqueDeux, cible,1);
		stopTime = System.currentTimeMillis();
		long time6 = stopTime - startTime;

		long globalStopTime = System.currentTimeMillis();
		System.out.println("Récap des temps : ");
		System.out.println("Traitement 1 exécuté en " + ConversionUtil.longVersStringFormat(time1) + " millisecondes");
		System.out.println("proba(170,19) = " + resultat1);
		System.out.println("Traitement 2 exécuté en " + ConversionUtil.longVersStringFormat(time2) + " millisecondes");
		System.out.println("proba(168, 18) = " + resultat2);
		System.out.println("Traitement 3 exécuté en " + ConversionUtil.longVersStringFormat(time3) + " millisecondes");
		System.out.println("chances de toucher avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + " (reussite critique )(attaque précise) : " 
				+ resultat3);
		System.out.println("Traitement 4 exécuté en " + ConversionUtil.longVersStringFormat(time4) + " millisecondes");
		System.out.println("esperance de dégats avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef +" : " 
				+ resultat4);
		System.out.println("Traitement 5 exécuté en " + ConversionUtil.longVersStringFormat(time5) + " millisecondes");
		System.out.println("esperance de dégats en attaque precise avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + "  : " 
				+resultat5);
		System.out.println("Traitement 6 exécuté en " + ConversionUtil.longVersStringFormat(time6) + " millisecondes");
		System.out.println("esperance de dégats en attaque precise avec " + nbDAtt + "D+" + bonusAtt + " sur " + nbDDef + "D+" + bonusDef + " (esquive critique ) : "
				+ resultat6);
		System.out.println("Traitement total exécuté en " + ConversionUtil.longVersStringFormat(globalStopTime - globaStartTime) + " millisecondes");
		System.out.println(attaqueUn.getNbDesDeg());
		System.out.println(attaqueDeux.getNbDesDeg());
		System.out.println(attaqueDeux.getNbDesDegatsCritique());
	}

}

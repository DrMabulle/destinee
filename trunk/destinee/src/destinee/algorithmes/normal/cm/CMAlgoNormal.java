/**
 * 
 */
package destinee.algorithmes.normal.cm;

import java.util.List;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.normal.cm.threads.DestineeQueryProcessor;
import destinee.algorithmes.normal.data.ChaineAttaques;
import destinee.algorithmes.normal.utils.GestionnaireChainesAttaques;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.utils.CachePersos;
import destinee.core.exception.DestineeException;
import destinee.core.utils.ConversionUtil;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public class CMAlgoNormal
{

	/**
	 * @param args
	 * @throws DestineeException
	 */
	public static void main(String[] args) throws DestineeException
	{
		long startTime = System.currentTimeMillis();

		Perso noone = new Perso(16, 0, 10, 0, 5, 0, 0, "No-one");
		Perso koumi = new Perso(14, 0, 10, 0, 5, 0, 0, "Koumi");
		Perso laporte = new Perso(12, 0, 10, 0, 5, 0, 0, "LaPorte");

		CachePersos.getInstance().addPerso(noone.getIdentifiant(), koumi);
		CachePersos.getInstance().addPerso(koumi.getIdentifiant(), noone);
		CachePersos.getInstance().addPerso(laporte.getIdentifiant(), laporte);

		DestineeToLogicGateway prolog = DestineeToLogicGatewayImpl.getDefaultInstance();

		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		// prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		// prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_PRECISE);
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		// prolog.ajouterAttaquePerso(laporte.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(laporte.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		Cible cible = new Cible(10, 0, 15);

		prolog.ajouterPerso(noone.getIdentifiant(), noone.getNombreDeDesAttaque(), noone.getBonusAttaque(), noone.getNombreDeDesDegats(), noone
				.getBonusDegats(), 10, 0);
		prolog.ajouterPerso(koumi.getIdentifiant(), koumi.getNombreDeDesAttaque(), koumi.getBonusAttaque(), koumi.getNombreDeDesDegats(), koumi
				.getBonusDegats(), 10, 0);
		prolog.ajouterPerso(laporte.getIdentifiant(), laporte.getNombreDeDesAttaque(), laporte.getBonusAttaque(), laporte.getNombreDeDesDegats(), laporte
				.getBonusDegats(), 10, 0);

		long tempsIntermediaire = System.currentTimeMillis();

		DestineeQueryProcessor.processQuery(prolog, cible);

		List<ChaineAttaques> chainesAtt = GestionnaireChainesAttaques.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaques theChaineAttaques : chainesAtt)
		{
			System.out.println("-----------------------------");
			System.out.println("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			System.out.println("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
			System.out.println("Probabilité de réalisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
		}

		System.out.println("-----------------------------");
		long stopTime = System.currentTimeMillis();
		System.out.println("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
		System.out.println("Temps Prolog : " + ConversionUtil.longVersStringFormat(tempsIntermediaire - startTime) + " ms");
		System.out.println("Temps Process : " + ConversionUtil.longVersStringFormat(stopTime - tempsIntermediaire) + " ms");
	}

}

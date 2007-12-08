/**
 * 
 */
package destinee.algorithmes.degrade.cm;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.degrade.cm.threads.DestineeQueryProcessorDegrade;
import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.algorithmes.degrade.utils.GestionnaireChainesAttaquesD;
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
public class CMAlgoDegrade
{

	/**
	 * @param args
	 * @throws DestineeException
	 */
	public static void main(String[] args) throws DestineeException
	{
		long startTime = System.currentTimeMillis();

		Perso koumi = new Perso(16, 10, 12, 7, 5, 0, 0, "Koumi");
		Perso noone = new Perso(12, 0, 8, 0, 5, 0, 0, "No-one");

		CachePersos.getInstance().addPerso(koumi.getIdentifiant(), koumi);
		CachePersos.getInstance().addPerso(noone.getIdentifiant(), noone);

		DestineeToLogicGateway prolog = DestineeToLogicGatewayImpl.getDefaultInstance();

		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		// prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		Cible cible = new Cible(10, -5, 15);

		prolog.ajouterPerso(koumi.getIdentifiant(), koumi.getNombreDeDesAttaque(), koumi.getBonusAttaque(), koumi.getNombreDeDesDegats(), koumi
				.getBonusDegats(), 10, 0);

		prolog.ajouterPerso(noone.getIdentifiant(), noone.getNombreDeDesAttaque(), noone.getBonusAttaque(), noone.getNombreDeDesDegats(), noone
				.getBonusDegats(), 10, 0);

		String queryString = "generationChainesAttaques(Result).";
		List<Map<String, Vector<String>>> resultQuery = prolog.query(queryString);

		long tempsIntermediaire = System.currentTimeMillis();

		DestineeQueryProcessorDegrade.processQuery(resultQuery, "Result", cible);

		List<ChaineAttaquesD> chainesAtt = GestionnaireChainesAttaquesD.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaquesD theChaineAttaques : chainesAtt)
		{
			System.out.println("-----------------------------");
			System.out.println("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			System.out.println("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
		}

		System.out.println("-----------------------------");
		long stopTime = System.currentTimeMillis();
		System.out.println("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
		System.out.println("Temps Prolog : " + ConversionUtil.longVersStringFormat(tempsIntermediaire - startTime) + " ms");
		System.out.println("Temps Process : " + ConversionUtil.longVersStringFormat(stopTime - tempsIntermediaire) + " ms");
	}

}

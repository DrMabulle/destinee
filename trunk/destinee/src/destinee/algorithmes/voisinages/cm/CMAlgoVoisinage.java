/**
 * 
 */
package destinee.algorithmes.voisinages.cm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.voisinages.cm.threads.DestineeQueryProcessorVoisinage;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
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
public class CMAlgoVoisinage
{

	/**
	 * @param args
	 * @throws DestineeException
	 */
	public static void main(String[] args) throws DestineeException
	{
		long startTime = System.currentTimeMillis();

		Map<String, Double> maitrisesNoOne = new HashMap<String, Double>();
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);

		Map<String, Double> maitrisesKoumi = new HashMap<String, Double>();
		maitrisesKoumi.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrisesKoumi.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrisesKoumi.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrisesKoumi.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);

		Map<String, Double> maitrisesLaPorte = new HashMap<String, Double>();
		maitrisesLaPorte.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrisesLaPorte.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrisesLaPorte.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrisesLaPorte.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);

		Perso noone = new Perso(16, 0, 10, 0, 5, 0, 0, "NoOne", maitrisesNoOne);
		Perso koumi = new Perso(14, 0, 10, 0, 5, 0, 0, "Koumi", maitrisesKoumi);
		Perso laporte = new Perso(12, 0, 10, 0, 5, 0, 0, "LaPorte", maitrisesLaPorte);

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

		prolog.ajouterAttaquePerso(laporte.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
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

		DestineeQueryProcessorVoisinage.processQuery(prolog, cible);

		List<ChaineAttaquesV> chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaquesV theChaineAttaques : chainesAtt)
		{
			System.out.println("-----------------------------");
			System.out.println("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			System.out.println("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
			System.out.println("Probabilité de réalisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
		}

		System.out.println("-----------------------------");
		long stopTime = System.currentTimeMillis();
		System.out.println("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}

}

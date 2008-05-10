/**
 * 
 */
package destinee.algorithmes.degrade.cm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.degrade.cm.threads.DestineeQueryProcessorDegrade;
import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.algorithmes.degrade.utils.GestionnaireChainesAttaquesD;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.utils.CachePersos;
import destinee.core.exception.DestineeException;
import destinee.core.log.LogFactory;
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

		Map<String, Double> maitrisesNoOne = new HashMap<String, Double>();
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);

		Map<String, Double> maitrisesKoumi = new HashMap<String, Double>();
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrisesNoOne.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);

		Perso koumi = new Perso(16, 10, 12, 7, 5, 0, 0, "Koumi", maitrisesKoumi);
		Perso noone = new Perso(12, 0, 8, 0, 5, 0, 0, "No-one", maitrisesNoOne);

		CachePersos.addPerso(koumi.getIdentifiant(), koumi);
		CachePersos.addPerso(noone.getIdentifiant(), noone);

		DestineeToLogicGateway prolog = DestineeToLogicGatewayImpl.getDefaultInstance();

		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		// prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		Cible cible = new Cible(10, -5, 15);

		prolog.ajouterPerso(koumi.getIdentifiant(), koumi.getNombreDeDesAttaque(), koumi.getBonusAttaque(), koumi.getNombreDeDesDegats(), koumi
				.getBonusDegats(), 10, 0);

		prolog.ajouterPerso(noone.getIdentifiant(), noone.getNombreDeDesAttaque(), noone.getBonusAttaque(), noone.getNombreDeDesDegats(), noone
				.getBonusDegats(), 10, 0);

		String queryString = "generationChainesAttaques(Result).";
		List<Map<String, List<String>>> resultQuery = prolog.query(queryString);

		long tempsIntermediaire = System.currentTimeMillis();

		DestineeQueryProcessorDegrade.processQuery(resultQuery, "Result", cible);

		List<ChaineAttaquesD> chainesAtt = GestionnaireChainesAttaquesD.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaquesD theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			LogFactory.logInfo("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
		}

		LogFactory.logInfo("-----------------------------");
		long stopTime = System.currentTimeMillis();
		LogFactory.logInfo("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
		LogFactory.logInfo("Temps Prolog : " + ConversionUtil.longVersStringFormat(tempsIntermediaire - startTime) + " ms");
		LogFactory.logInfo("Temps Process : " + ConversionUtil.longVersStringFormat(stopTime - tempsIntermediaire) + " ms");
	}

}

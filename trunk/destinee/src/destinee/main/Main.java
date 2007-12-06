package destinee.main;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import logic.gateways.DestineeToLogicGateway;
import destinee.constantes.ConstantesAttaques;
import destinee.core.exception.DestineeException;
import destinee.core.utils.ConversionUtil;
import destinee.data.ChaineAttaques;
import destinee.data.Cible;
import destinee.data.Perso;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;
import destinee.utils.CachePersos;
import destinee.utils.DestineeQueryProcessor;
import destinee.utils.GestionnaireChainesAttaques;

public class Main
{

	/**
	 * @param args
	 * @throws DestineeException Exceptions lors de l'exécution
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
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		prolog.ajouterAttaquePerso(koumi.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_NORMALE);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_BERSERK);
		prolog.ajouterAttaquePerso(noone.getIdentifiant(), ConstantesAttaques.ID_ATTAQUE_RAPIDE);

		Cible cible = new Cible(10, -5, 15);

		String queryString = "genererScenarios([att('Koumi','Normale'),att('Koumi','Normale'),att('Koumi','Rapide')], Result).";
		List<Map<String, Vector<String>>> resultQuery = prolog.query(queryString);
		DestineeQueryProcessor.processQuery(resultQuery, "Result", cible);

		List<ChaineAttaques> chainesAtt = GestionnaireChainesAttaques.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaques theChaineAttaques : chainesAtt)
		{
			System.out.println("-----------------------------");
			System.out.println("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			System.out.println("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
			System.out.println("Probabilité de réalisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
		}
		
		System.out.println("-----------------------------");
		System.out.println("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(System.currentTimeMillis() - startTime) + "ms");
	}

}

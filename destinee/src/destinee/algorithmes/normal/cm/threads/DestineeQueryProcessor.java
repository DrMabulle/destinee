/**
 * 
 */
package destinee.algorithmes.normal.cm.threads;

import java.util.List;
import java.util.Map;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.normal.data.Scenario;
import destinee.algorithmes.normal.data.ScenarioElement;
import destinee.algorithmes.normal.utils.GestionnaireChainesAttaques;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueBerserk;
import destinee.commun.data.AttaqueBrutale;
import destinee.commun.data.AttaqueImparable;
import destinee.commun.data.AttaqueMagique;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.AttaquePrecise;
import destinee.commun.data.AttaqueRapide;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.probas.ResolutionAttaque;
import destinee.commun.utils.CachePersos;
import destinee.core.exception.DestineeException;
import destinee.core.exception.TechnicalException;

/**
 * @author Bubulle et No-one
 * 
 * Class permettant d'analyser les résultats de la Query Prolog effectuée afin de créer les scénarios retournés par Prolog
 * 
 * Producer
 */
public class DestineeQueryProcessor extends Thread
{

	private Cible cible;
	private DestineeToLogicGateway prologGateway;

	private static final String QUERY = "generationScenarios(Result).";

	/**
	 * Constructeur par defaut
	 * 
	 * @param aGateway une DestineeToLogicGateway
	 * @param aScenarioVariableName nom de la variable Prolog utilisée
	 * @param aCible Cible des attaques
	 */
	public DestineeQueryProcessor(DestineeToLogicGateway aGateway, Cible aCible)
	{
		prologGateway = aGateway;
		cible = aCible;

		start();
	}

	public static void processQuery(DestineeToLogicGateway aGateway, Cible aCible) throws DestineeException
	{

		Thread processor = new DestineeQueryProcessor(aGateway, aCible);

		// Démarrer 4 Threads de traitement des scénarios
		Thread t1 = new TraitementScenarios();
		Thread t2 = new TraitementScenarios();
		Thread t3 = new TraitementScenarios();
		Thread t4 = new TraitementScenarios();
		Thread t5 = new TraitementScenarios();
		Thread t6 = new TraitementScenarios();
		Thread t7 = new TraitementScenarios();
		Thread t8 = new TraitementScenarios();

		try
		{
			processor.join();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			// Dire aux Threads de traitement de scénario qu'ils peuvent s'arrêter une fois les traitements effectués (pas de nouveaux scénarios)
			TraitementScenarios.arreterTraitements();
		}

		try
		{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
		}
		catch (InterruptedException e)
		{
			throw new TechnicalException("erreur lors du traitements des scénarios", e);
		}
	}

	public void run()
	{
		Map<String, List<String>> result = prologGateway.queryOnce(QUERY);

		// Variables temporaires
		List<String> vector;
		String[] details;
		String persoId, typeAttaque, typeResolution;
		Perso attaquant;
		Attaque attaque;

		while (result != null && !result.isEmpty())
		{
			vector = result.get("Result");
			/*
			 * On a une String de la forme [att(Perso1,Type1,Resolution1), ..., att(PersoN,TypeN,ResolutionN)]
			 */

			CachePersos.getNouvellesInstances();
			Scenario scenar = new Scenario(cible.clone());

			try
			{
				/*
				 * Parser la String représentant le scénario
				 */
				// Découper les attaques une à une
				for (String attaqueS : vector)
				{
					// attaqueS est du type att(Perso,Type,Resolution)
					attaqueS = attaqueS.substring("att(".length(), attaqueS.length() - 1);

					// Il ne reste plus que Perso,Type,Resolution
					details = attaqueS.split(",");
					persoId = details[0];
					typeAttaque = details[1];
					typeResolution = details[2];

					attaquant = CachePersos.getPerso(persoId);
					attaque = getNouvelleAttaque(typeAttaque, attaquant);

					// Construire un ScenarioElement avec ces données et l'ajouté au scénario
					ScenarioElement scenarElt = getScenarioElement(attaque, typeResolution);
					scenar.ajouterElement(scenarElt);
				}

			}
			catch (Exception e)
			{
				// throw new TechnicalException("erreur lors du parsing des résultats de la query Prolog", e);
				e.printStackTrace();
			}

			// Le scénario étant complet, l'ajouté au gestionnaire de chaines d'attaques
			GestionnaireChainesAttaques.getInstance().ajouterScenarioATraiter(scenar);

			// préparer la prochaine itération
			result = prologGateway.next();
		}

		// Une fois tous les résultats récupérés, fermer la Query Prolog
		prologGateway.stop();
	}

	/**
	 * @param aAttaque une attaque
	 * @param aTypeResolution un type de résolution
	 * @return le ScenarioElement correspondant
	 * @throws TechnicalException si le type de résolution est erroné
	 */
	private static ScenarioElement getScenarioElement(Attaque aAttaque, String aTypeResolution) throws TechnicalException
	{
		int typeResol = getTypeResolution(aTypeResolution);
		return new ScenarioElement(aAttaque, typeResol);
	}

	/**
	 * @param aTypeResolution un type de résolution
	 * @return l'entier correspondant au type de résolution
	 * @throws TechnicalException si le type de résolution est erroné
	 */
	private static int getTypeResolution(String aTypeResolution) throws TechnicalException
	{
		if ("Coup critique".equals(aTypeResolution))
		{
			return ResolutionAttaque.RESOLUTION_COUP_CRITIQUE;
		}
		else if ("Coup simple".equals(aTypeResolution))
		{
			return ResolutionAttaque.RESOLUTION_COUP_SIMPLE;
		}
		else if ("Esquive simple".equals(aTypeResolution))
		{
			return ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE;
		}
		else if ("Esquive parfaite".equals(aTypeResolution))
		{
			return ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE;
		}
		else if ("Echec competence".equals(aTypeResolution))
		{
			return ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE;
		}
		else
		{
			throw new TechnicalException("Type de résolution incorrect : " + aTypeResolution);
		}
	}

	/**
	 * @param aTypeAttaque un type d'attaque
	 * @param aAttaquant un attaquant
	 * @return l'instance d'attaque correspondante
	 * @throws TechnicalException si le type d'attaque est incorrect
	 */
	private static Attaque getNouvelleAttaque(String aTypeAttaque, Perso aAttaquant) throws TechnicalException
	{
		Attaque attaque = null;
		if (ConstantesAttaques.ID_ATTAQUE_BERSERK.equals(aTypeAttaque))
		{
			attaque = new AttaqueBerserk(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_BRUTALE.equals(aTypeAttaque))
		{
			attaque = new AttaqueBrutale(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_IMPARABLE.equals(aTypeAttaque))
		{
			attaque = new AttaqueImparable(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_MAGIQUE.equals(aTypeAttaque))
		{
			attaque = new AttaqueMagique(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_NORMALE.equals(aTypeAttaque))
		{
			attaque = new AttaqueNormale(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_PRECISE.equals(aTypeAttaque))
		{
			attaque = new AttaquePrecise(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_RAPIDE.equals(aTypeAttaque))
		{
			attaque = new AttaqueRapide(aAttaquant);
		}
		else
		{
			throw new TechnicalException("Type d'attaque incorrect : " + aTypeAttaque);
		}

		return attaque;
	}
}

/**
 * 
 */
package destinee.algorithmes.normal.cm.threads;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import destinee.algorithmes.normal.data.Scenario;
import destinee.algorithmes.normal.data.ScenarioElement;
import destinee.algorithmes.normal.utils.CacheScenarioElements;
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
import destinee.commun.utils.CacheAttaques;
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

	private List<Map<String, Vector<String>>> queryResults;
	private String scenarioVariableName;
	private Cible cible;

	/**
	 * Constructeur par defaut
	 * 
	 * @param results Résultats de la query Prolog à traiter
	 * @param aScenarioVariableName nom de la variable Prolog utilisée
	 * @param aCible Cible des attaques
	 */
	public DestineeQueryProcessor(List<Map<String, Vector<String>>> results, String aScenarioVariableName, Cible aCible)
	{
		queryResults = results;
		scenarioVariableName = aScenarioVariableName;
		cible = aCible;

		start();
	}

	public static void processQuery(List<Map<String, Vector<String>>> results, String scenarioVariableName, Cible aCible) throws DestineeException
	{

		Thread processor = new DestineeQueryProcessor(results, scenarioVariableName, aCible);

		// Démarrer 4 Threads de traitement des scénarios
		Thread t1 = new TraitementScenarios();
		Thread t2 = new TraitementScenarios();

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
		}
		catch (InterruptedException e)
		{
			throw new TechnicalException("erreur lors du traitements des scénarios", e);
		}
	}

	public void run()
	{
		if (queryResults != null)
		{
			for (Map<String, Vector<String>> theMap : queryResults)
			{
				Vector<String> vector = theMap.get(scenarioVariableName);
				/*
				 * On a une String de la forme [att(Perso1,Type1,Resolution1), ..., att(PersoN,TypeN,ResolutionN)]
				 */

				// if (vector == null)
				// {
				// throw new FonctionnalException("Mauvais identifiant de variable Prolog.");
				// }
				CachePersos.getInstance().getNouvellesInstances();
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
						String[] details = attaqueS.split(",");
						String persoId = details[0];
						String typeAttaque = details[1];
						String typeResolution = details[2];

						Perso attaquant = CachePersos.getInstance().getPerso(persoId);
						Attaque attaque = getNouvelleAttaque(typeAttaque, attaquant);

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
			}

		}
		else
		{
			// throw new FonctionnalException("Résultats de la query Prolog vides.");
		}
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
		String idScenarElt = CacheScenarioElements.getIdentifiantScenarioElement(aAttaque, typeResol);
		ScenarioElement scenarElt = CacheScenarioElements.getInstance().getScenarioElement(idScenarElt);

		if (scenarElt == null)
		{
			scenarElt = new ScenarioElement(aAttaque, typeResol);
			// CacheScenarioElements.getInstance().addScenarioElement(idScenarElt, scenarElt);
			// FIXME pour le moment on n'utilise pas le cache
		}

		return scenarElt;
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
		String idAttaque = CacheAttaques.getIdentifiantAttaque(aAttaquant, aTypeAttaque);
		Attaque attaque = CacheAttaques.getInstance().getAttaque(idAttaque);
		if (attaque == null)
		{
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

			// Ajouter l'attaque au cache pour une réutilisation plus rapide (gros problèmes de mémoire !)
			// CacheAttaques.getInstance().addAttaque(idAttaque, attaque); // FIXME pour le moment, on n'utilise pas le cache
			// TODO Attaque kamikaze et charge
		}
		return attaque;
	}
}

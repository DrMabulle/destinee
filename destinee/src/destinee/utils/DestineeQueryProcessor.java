/**
 * 
 */
package destinee.utils;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import destinee.constantes.ConstantesAttaques;
import destinee.core.exception.DestineeException;
import destinee.core.exception.FonctionnalException;
import destinee.core.exception.TechnicalException;
import destinee.data.Attaque;
import destinee.data.AttaqueBerserk;
import destinee.data.AttaqueBrutale;
import destinee.data.AttaqueImparable;
import destinee.data.AttaqueMagique;
import destinee.data.AttaqueNormale;
import destinee.data.AttaquePrecise;
import destinee.data.AttaqueRapide;
import destinee.data.Cible;
import destinee.data.Perso;
import destinee.data.Scenario;
import destinee.data.ScenarioElement;
import destinee.probas.ResolutionAttaque;

/**
 * @author Bubulle et No-one
 * 
 * Class permettant d'analyser les résultats de la Query Prolog effectuée afin de créer les scénarios retournés par Prolog
 */
public class DestineeQueryProcessor
{
	public static void processQuery(List<Map<String, Vector<String>>> results, String scenarioVariableName, Cible aCible) throws DestineeException
	{
		if (results != null)
		{
			for (Map<String, Vector<String>> theMap : results)
			{
				Vector<String> vector = theMap.get(scenarioVariableName);
				/*
				 * On a une String de la forme [att(Perso1,Type1,Resolution1), ..., att(PersoN,TypeN,ResolutionN)]
				 */

				if (vector == null)
				{
					throw new FonctionnalException("Mauvais identifiant de variable Prolog.");
				}

				Scenario scenar = new Scenario(aCible);

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
					throw new TechnicalException("erreur lors du parsing des résultats de la query Prolog", e);
				}

				// Le scénario étant complet, l'ajouté au gestionnaire de chaines d'attaques
				GestionnaireChainesAttaques.getInstance().ajouterScenario(scenar);
			}
		}
		else
		{
			throw new FonctionnalException("Résultats de la query Prolog vides.");
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
			CacheScenarioElements.getInstance().addScenarioElement(idScenarElt, scenarElt);
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
			CacheAttaques.getInstance().addAttaque(idAttaque, attaque);
			// TODO Attaque kamikaze et charge
		}
		return attaque;
	}
}

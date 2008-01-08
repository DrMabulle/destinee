/**
 * 
 */
package destinee.algorithmes.normal.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.properties.PropertiesFactory;
import destinee.core.utils.ConversionUtil;

/**
 * @author Bubulle et No-one
 * 
 */
public class Scenario
{
	/**
	 * Espérance de dégâts
	 */
	private double esperanceDegats;
	/**
	 * Probabilité de réalisation
	 */
	private BigDecimal probaRealisation = null;

	/**
	 * Liste des éléments scénaristiques
	 */
	private List<ScenarioElement> listeElements;

	/**
	 * La cible des attaques
	 */
	private Cible cible;

	private static final String CLE_ARRET_TRAITEMENT = "destinee.scenario.evaluation.testerProbas";
	private static final String CLE_VALEUR_MIN = "destinee.scenario.evaluation.valeurMin";

	public Scenario(Cible aCible)
	{

		super();
		listeElements = new ArrayList<ScenarioElement>();
		cible = aCible;
	}

	/**
	 * Méthode permettant de connaitre l'espérance de dégâts de ce scénario
	 * 
	 * @return l'espérance de dégâts de ce scénario
	 */
	public double getEsperanceDegats()
	{

		// Si le scénario n'a pas encore été évaluer, le faire
		if (probaRealisation == null)
		{
			evalerEvenement();
		}

		return esperanceDegats;
	}

	/**
	 * Méthode permettant de connaitre la probabilité de réalisation de ce scénario
	 * 
	 * @return probabilité de réalisation de ce scénario
	 */
	public BigDecimal getProbaRealisation()
	{

		// Si le scénario n'a pas encore été évaluer, le faire
		if (probaRealisation == null)
		{
			evalerEvenement();
		}

		return probaRealisation;
	}

	/**
	 * Méthode permettant d'évaluer le scénario, en termes d'espérance de dégâts et de probabilité de réalisation
	 */
	private void evalerEvenement()
	{
		long startTime = System.currentTimeMillis();

		probaRealisation = new BigDecimal(1);
		esperanceDegats = 0;

		// Réinitialiser la fatigue et les malus de la cible et des persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefense();
		for (ScenarioElement scenarioElemt : listeElements)
		{
			scenarioElemt.getAttaque().getPerso().reinitialiserFatigue();
		}

		BigDecimal probaTmp = null;
		double esperanceTmp = 0;

		String valeurMinTemp = PropertiesFactory.getOptionalString(CLE_VALEUR_MIN);
		BigDecimal valeurMin = ConversionUtil.stringVersBigDecimal(valeurMinTemp, new BigDecimal(0.00001));
		// valeurMin.pow(listeElements.size() + 1);
		Boolean arretPossible = PropertiesFactory.getOptionalBoolean(CLE_ARRET_TRAITEMENT);

		for (ScenarioElement scenarioElemt : listeElements)
		{
			probaTmp = ResolutionAttaque.resoudreAttaque(scenarioElemt.getAttaque(), cible, scenarioElemt.getTypeResolution());
			esperanceTmp = ResolutionAttaque.esperanceDeDegats(scenarioElemt.getAttaque(), cible, scenarioElemt.getTypeResolution());

			// Multiplier la proba de réalisation globale par la proba de
			// réalisation de l'élément scénaristique
			probaRealisation = probaRealisation.multiply(probaTmp);

			// Arrêter l'evaluation du scenario si la probailité passe sous le seuil défini
			if (Boolean.TRUE.equals(arretPossible))
			{
				if (probaRealisation.compareTo(valeurMin) < 0)
				{
					probaRealisation = BigDecimal.ZERO;
					esperanceDegats = 0;
					// System.out.println("scenario " + toString() + ": abandon. " + (System.currentTimeMillis() - startTime) + " ms");
					return;
				}
			}

			// Additionner l'espérance de dégâts de l'élément scénaristique à
			// l'espérance de dégâts cumulée
			esperanceDegats += esperanceTmp;

			// Incrémenter les malus de la cible
			cible.incrementerFatigue();
			cible.incrementerMalusDefense(scenarioElemt.getAttaque(), scenarioElemt.getTypeResolution());

			// Incrémenter la fatigue du perso
			scenarioElemt.getAttaque().getPerso().incrementerFatigue(scenarioElemt.getAttaque());
		}
		// TODO une methode pour récuperer la fatigue si on gere les cumuls, formule sur le wiki
		// TODO une gestion de la charge : attaque identique a l'attaque normale mais générant un point de fatigue en plus

		System.out.println("scenario " + toString() + ": " + ConversionUtil.bigDecimalVersString(probaRealisation, 15) + ". "
				+ (System.currentTimeMillis() - startTime) + " ms");
	}

	/**
	 * Méthode permettant de récupérer l'identifiant de la chaine d'attaques utilisée dans ce scénario
	 * 
	 * @return l'identifiant de la chaine d'attaques utilisée dans ce scénario
	 */
	public String getIdentifiantChaineAttaques()
	{
		StringBuffer sb = new StringBuffer("");

		for (ScenarioElement scenarioElemt : listeElements)
		{
			sb.append(scenarioElemt.getAttaque().getPerso().getIdentifiant());
			sb.append(scenarioElemt.getAttaque().getTypeAttaque());
			sb.append("-");
		}

		return sb.toString();
	}

	/**
	 * Méthode permettant d'ajouter un élément scénaristique au scénario
	 * 
	 * @param aScenarioElmt le ScenarioElement à ajouter
	 */
	public void ajouterElement(ScenarioElement aScenarioElmt)
	{
		listeElements.add(aScenarioElmt);
		// réinitialiser les probas et espérances
		probaRealisation = null;
		esperanceDegats = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aArg0)
	{
		/*
		 * Deux Scénarios sont considérés égaux s'ils ont la même chaine de ScenarioElement et la même cible
		 */
		if (aArg0 != null && aArg0 instanceof Scenario)
		{
			Scenario scenar = (Scenario) aArg0;
			// Même cible, même nombre d'éléments
			if (cible.equals(scenar.cible) && listeElements.size() == scenar.listeElements.size())
			{
				boolean chainesEgales = true;
				// Comparer les éléments de chaque chaine
				for (int i = 0; i < listeElements.size() && chainesEgales; i++)
				{
					chainesEgales &= listeElements.get(i).equals(scenar.listeElements.get(i));
				}

				if (chainesEgales)
				{
					return true;
				}
			}
		}

		return super.equals(aArg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hashcode = 0;
		for (ScenarioElement elt : listeElements)
		{
			hashcode += elt.hashCode();
		}
		return cible.hashCode() + hashcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String result = "";
		for (ScenarioElement scenarElem : listeElements)
		{
			result += scenarElem.toString() + "-";
		}
		return result;
	}

}

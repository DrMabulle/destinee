/**
 * 
 */
package destinee.algorithmes.normal.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;
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
	 * @throws TechnicalException e
	 */
	public double getEsperanceDegats() throws TechnicalException
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
	 * @throws TechnicalException e
	 */
	public BigDecimal getProbaRealisation() throws TechnicalException
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
	 * 
	 * @throws TechnicalException e
	 */
	private void evalerEvenement() throws TechnicalException
	{
		long startTime = System.currentTimeMillis();

		probaRealisation = BigDecimal.ONE;
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
					if (LogFactory.isLogDebugEnabled())
						LogFactory.logDebug("scenario " + toString() + ": abandon. " + (System.currentTimeMillis() - startTime) + " ms");
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

		if (LogFactory.isLogDebugEnabled())
			LogFactory.logDebug("scenario " + toString() + ": " + ConversionUtil.bigDecimalVersString(probaRealisation, 15) + ". "
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
		// Vérification de l'égalité des références
		if (this == aArg0)
		{
			return true;
		}

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
				int size = listeElements.size();
				for (int i = 0; i < size && chainesEgales; i++)
				{
					chainesEgales &= listeElements.get(i).equals(scenar.listeElements.get(i));
				}

				if (chainesEgales)
				{
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hashcode = 7;
		for (ScenarioElement elt : listeElements)
		{
			hashcode = 13 * hashcode + elt.hashCode();
		}
		return 13 * hashcode + cible.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer(150);
		for (ScenarioElement scenarElem : listeElements)
		{
			result.append(scenarElem.toString()).append(" - ");
		}
		return result.toString();
	}

}

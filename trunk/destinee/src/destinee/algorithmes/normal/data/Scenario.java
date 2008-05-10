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
	 * Esp�rance de d�g�ts
	 */
	private double esperanceDegats;
	/**
	 * Probabilit� de r�alisation
	 */
	private BigDecimal probaRealisation = null;

	/**
	 * Liste des �l�ments sc�naristiques
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
	 * M�thode permettant de connaitre l'esp�rance de d�g�ts de ce sc�nario
	 * 
	 * @return l'esp�rance de d�g�ts de ce sc�nario
	 * @throws TechnicalException e
	 */
	public double getEsperanceDegats() throws TechnicalException
	{

		// Si le sc�nario n'a pas encore �t� �valuer, le faire
		if (probaRealisation == null)
		{
			evalerEvenement();
		}

		return esperanceDegats;
	}

	/**
	 * M�thode permettant de connaitre la probabilit� de r�alisation de ce sc�nario
	 * 
	 * @return probabilit� de r�alisation de ce sc�nario
	 * @throws TechnicalException e
	 */
	public BigDecimal getProbaRealisation() throws TechnicalException
	{

		// Si le sc�nario n'a pas encore �t� �valuer, le faire
		if (probaRealisation == null)
		{
			evalerEvenement();
		}

		return probaRealisation;
	}

	/**
	 * M�thode permettant d'�valuer le sc�nario, en termes d'esp�rance de d�g�ts et de probabilit� de r�alisation
	 * 
	 * @throws TechnicalException e
	 */
	private void evalerEvenement() throws TechnicalException
	{
		long startTime = System.currentTimeMillis();

		probaRealisation = BigDecimal.ONE;
		esperanceDegats = 0;

		// R�initialiser la fatigue et les malus de la cible et des persos
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

			// Multiplier la proba de r�alisation globale par la proba de
			// r�alisation de l'�l�ment sc�naristique
			probaRealisation = probaRealisation.multiply(probaTmp);

			// Arr�ter l'evaluation du scenario si la probailit� passe sous le seuil d�fini
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

			// Additionner l'esp�rance de d�g�ts de l'�l�ment sc�naristique �
			// l'esp�rance de d�g�ts cumul�e
			esperanceDegats += esperanceTmp;

			// Incr�menter les malus de la cible
			cible.incrementerFatigue();
			cible.incrementerMalusDefense(scenarioElemt.getAttaque(), scenarioElemt.getTypeResolution());

			// Incr�menter la fatigue du perso
			scenarioElemt.getAttaque().getPerso().incrementerFatigue(scenarioElemt.getAttaque());
		}
		// TODO une methode pour r�cuperer la fatigue si on gere les cumuls, formule sur le wiki
		// TODO une gestion de la charge : attaque identique a l'attaque normale mais g�n�rant un point de fatigue en plus

		if (LogFactory.isLogDebugEnabled())
			LogFactory.logDebug("scenario " + toString() + ": " + ConversionUtil.bigDecimalVersString(probaRealisation, 15) + ". "
					+ (System.currentTimeMillis() - startTime) + " ms");
	}

	/**
	 * M�thode permettant de r�cup�rer l'identifiant de la chaine d'attaques utilis�e dans ce sc�nario
	 * 
	 * @return l'identifiant de la chaine d'attaques utilis�e dans ce sc�nario
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
	 * M�thode permettant d'ajouter un �l�ment sc�naristique au sc�nario
	 * 
	 * @param aScenarioElmt le ScenarioElement � ajouter
	 */
	public void ajouterElement(ScenarioElement aScenarioElmt)
	{
		listeElements.add(aScenarioElmt);
		// r�initialiser les probas et esp�rances
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
		// V�rification de l'�galit� des r�f�rences
		if (this == aArg0)
		{
			return true;
		}

		/*
		 * Deux Sc�narios sont consid�r�s �gaux s'ils ont la m�me chaine de ScenarioElement et la m�me cible
		 */
		if (aArg0 != null && aArg0 instanceof Scenario)
		{
			Scenario scenar = (Scenario) aArg0;
			// M�me cible, m�me nombre d'�l�ments
			if (cible.equals(scenar.cible) && listeElements.size() == scenar.listeElements.size())
			{
				boolean chainesEgales = true;
				// Comparer les �l�ments de chaque chaine
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

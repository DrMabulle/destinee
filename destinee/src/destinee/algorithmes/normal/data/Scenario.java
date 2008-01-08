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
	 */
	public double getEsperanceDegats()
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
	 */
	public BigDecimal getProbaRealisation()
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
	 */
	private void evalerEvenement()
	{
		long startTime = System.currentTimeMillis();

		probaRealisation = new BigDecimal(1);
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
					// System.out.println("scenario " + toString() + ": abandon. " + (System.currentTimeMillis() - startTime) + " ms");
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

		System.out.println("scenario " + toString() + ": " + ConversionUtil.bigDecimalVersString(probaRealisation, 15) + ". "
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

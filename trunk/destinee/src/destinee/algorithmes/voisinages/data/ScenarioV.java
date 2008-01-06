/**
 * 
 */
package destinee.algorithmes.voisinages.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import destinee.commun.data.Attaque;
import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.properties.PropertiesFactory;
import destinee.core.utils.ConversionUtil;


/**
 * @author Bubulle et No-one
 *
 */
public class ScenarioV
{
	private double esperanceDegats;
	private BigDecimal probaRealisation = null;
	private List<Integer> listeResultats;
	private ChaineAttaquesV chaine;
	
	private static final String CLE_ARRET_TRAITEMENT = "destinee.scenario.evaluation.testerProbas";
	private static final String CLE_VALEUR_MIN = "destinee.scenario.evaluation.valeurMin";
	
	public ScenarioV(ChaineAttaquesV aChaineAtt)
	{
		super();
		chaine = aChaineAtt;
		listeResultats = new ArrayList<Integer>(aChaineAtt.size());
	}
	
	
	public ScenarioV(ChaineAttaquesV aChaineAtt, List<Integer> resolutions)
	{
		super();
		chaine = aChaineAtt;
		listeResultats = new ArrayList<Integer>(resolutions);
	}
	
	/**
	 * Méthode permettant d'ajouter un type de résolution au scénario
	 * 
	 * @param aTypeResolution un type de résolution
	 */
	public void ajouterElementResolution(int aTypeResolution)
	{
		listeResultats.add(aTypeResolution);
		// réinitialiser les probas et espérances
		probaRealisation = null;
		esperanceDegats = 0;
	}
	
	/**
	 * Méthode permettant de modifier un type de résolution du scénario
	 * 
	 * @param aPosition position du type de résolution à modifier
	 * @param aTypeResolution un type de résolution
	 */
	public void changerTypeResolution(int aPosition, int aTypeResolution)
	{
		listeResultats.set(aPosition, aTypeResolution);
		// réinitialiser les probas et espérances
		probaRealisation = null;
		esperanceDegats = 0;
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
		probaRealisation = new BigDecimal(1);
		esperanceDegats = 0;

		Cible cible = chaine.getCible();
		// Réinitialiser la fatigue et les malus de la cible et des persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefense();
		for (Attaque att : chaine.getListeAttaques())
		{
			att.getPerso().reinitialiserFatigue();
		}

		BigDecimal probaTmp = null;
		double esperanceTmp = 0;

		String valeurMinTemp = PropertiesFactory.getOptionalString(CLE_VALEUR_MIN);
		BigDecimal valeurMin = ConversionUtil.stringVersBigDecimal(valeurMinTemp, new BigDecimal(0.00001));
		// valeurMin.pow(listeElements.size() + 1);

		/*
		 * Lors de la recherche du scenario initial, la liste des types de résolution n'est pas entièrement remplie.
		 * On itère uniquement sur les résolutions afin de trouver la meilleure combinaison.
		 */
		List<Attaque> attaques = chaine.getListeAttaques();
		for (int i = 0; i < listeResultats.size(); i++)
		{
			Attaque att = attaques.get(i);
			int typeResolution = listeResultats.get(i);
			
			probaTmp = ResolutionAttaque.resoudreAttaque(att, cible, typeResolution);
			esperanceTmp = ResolutionAttaque.esperanceDeDegats(att, cible, typeResolution);

			// Multiplier la proba de réalisation globale par la proba de
			// réalisation de l'élément scénaristique
			probaRealisation = probaRealisation.multiply(probaTmp);

			// Arrêter l'evaluation du scenario si la probailité passe sous le seuil défini
			if (Boolean.TRUE.equals(PropertiesFactory.getOptionalBoolean(CLE_ARRET_TRAITEMENT)))
			{
				if (probaRealisation.compareTo(valeurMin) < 0)
				{
					probaRealisation = BigDecimal.ZERO;
					esperanceDegats = 0;
					return;
				}
			}

			// Additionner l'espérance de dégâts de l'élément scénaristique à
			// l'espérance de dégâts cumulée
			esperanceDegats += esperanceTmp;

			// Incrémenter les malus de la cible
			cible.incrementerFatigue();
			cible.incrementerMalusDefense(att, typeResolution);

			// Incrémenter la fatigue du perso
			att.getPerso().incrementerFatigue(att);
		}
	}
	
	public List<Integer> getListeTypesResolution()
	{
		return listeResultats;
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
		 * Deux Scénarios sont considérés égaux s'ils ont la même chaine d'attaque, les même résolutions
		 */
		if (aArg0 != null && aArg0 instanceof ScenarioV)
		{
			ScenarioV scenar = (ScenarioV) aArg0;
			// Même cible, même nombre d'éléments
			if (listeResultats.size() == scenar.listeResultats.size() && this.chaine.equals(scenar.chaine))
			{
				boolean chainesEgales = true;
				// Comparer les éléments de chaque chaine
				for (int i = 0; i < listeResultats.size() && chainesEgales; i++)
				{
					chainesEgales &= listeResultats.get(i).equals(scenar.listeResultats.get(i));
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
		for (Integer elt : listeResultats)
		{
			hashcode += elt.hashCode();
		}
		return chaine.hashCode() + hashcode;
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
		for (Integer elt : listeResultats)
		{
			result += elt.toString() + " - ";
		}
		return result;
	}
}

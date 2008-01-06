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
	 * M�thode permettant d'ajouter un type de r�solution au sc�nario
	 * 
	 * @param aTypeResolution un type de r�solution
	 */
	public void ajouterElementResolution(int aTypeResolution)
	{
		listeResultats.add(aTypeResolution);
		// r�initialiser les probas et esp�rances
		probaRealisation = null;
		esperanceDegats = 0;
	}
	
	/**
	 * M�thode permettant de modifier un type de r�solution du sc�nario
	 * 
	 * @param aPosition position du type de r�solution � modifier
	 * @param aTypeResolution un type de r�solution
	 */
	public void changerTypeResolution(int aPosition, int aTypeResolution)
	{
		listeResultats.set(aPosition, aTypeResolution);
		// r�initialiser les probas et esp�rances
		probaRealisation = null;
		esperanceDegats = 0;
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
		probaRealisation = new BigDecimal(1);
		esperanceDegats = 0;

		Cible cible = chaine.getCible();
		// R�initialiser la fatigue et les malus de la cible et des persos
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
		 * Lors de la recherche du scenario initial, la liste des types de r�solution n'est pas enti�rement remplie.
		 * On it�re uniquement sur les r�solutions afin de trouver la meilleure combinaison.
		 */
		List<Attaque> attaques = chaine.getListeAttaques();
		for (int i = 0; i < listeResultats.size(); i++)
		{
			Attaque att = attaques.get(i);
			int typeResolution = listeResultats.get(i);
			
			probaTmp = ResolutionAttaque.resoudreAttaque(att, cible, typeResolution);
			esperanceTmp = ResolutionAttaque.esperanceDeDegats(att, cible, typeResolution);

			// Multiplier la proba de r�alisation globale par la proba de
			// r�alisation de l'�l�ment sc�naristique
			probaRealisation = probaRealisation.multiply(probaTmp);

			// Arr�ter l'evaluation du scenario si la probailit� passe sous le seuil d�fini
			if (Boolean.TRUE.equals(PropertiesFactory.getOptionalBoolean(CLE_ARRET_TRAITEMENT)))
			{
				if (probaRealisation.compareTo(valeurMin) < 0)
				{
					probaRealisation = BigDecimal.ZERO;
					esperanceDegats = 0;
					return;
				}
			}

			// Additionner l'esp�rance de d�g�ts de l'�l�ment sc�naristique �
			// l'esp�rance de d�g�ts cumul�e
			esperanceDegats += esperanceTmp;

			// Incr�menter les malus de la cible
			cible.incrementerFatigue();
			cible.incrementerMalusDefense(att, typeResolution);

			// Incr�menter la fatigue du perso
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
		 * Deux Sc�narios sont consid�r�s �gaux s'ils ont la m�me chaine d'attaque, les m�me r�solutions
		 */
		if (aArg0 != null && aArg0 instanceof ScenarioV)
		{
			ScenarioV scenar = (ScenarioV) aArg0;
			// M�me cible, m�me nombre d'�l�ments
			if (listeResultats.size() == scenar.listeResultats.size() && this.chaine.equals(scenar.chaine))
			{
				boolean chainesEgales = true;
				// Comparer les �l�ments de chaque chaine
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

/**
 * 
 */
package destinee.algorithmes.voisinages.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import destinee.commun.data.Attaque;
import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.exception.TechnicalException;
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
	private final List<Integer> listeResultats;
	private final ChaineAttaquesV chaine;
	private double indiceBourrinisme = -1;

	private String toString;

	private static final String CLE_ARRET_TRAITEMENT = "destinee.scenario.evaluation.testerProbas";
	private static final String CLE_VALEUR_MIN = "destinee.scenario.evaluation.valeurMin";

	public ScenarioV(final ChaineAttaquesV aChaineAtt)
	{
		super();
		chaine = aChaineAtt;
		listeResultats = new ArrayList<Integer>(aChaineAtt.size());
	}

	public ScenarioV(final ChaineAttaquesV aChaineAtt, final List<Integer> resolutions)
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
	public void ajouterElementResolution(final int aTypeResolution)
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
	public void changerTypeResolution(final int aPosition, final int aTypeResolution)
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
	 * @throws TechnicalException e
	 */
	public double getEsperanceDegats() throws TechnicalException
	{

		// Si le sc�nario n'a pas encore �t� �valuer, le faire
		if (probaRealisation == null)
		{
			evaluerEvenement();
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
			evaluerEvenement();
		}

		return probaRealisation;
	}

	/**
	 * M�thode permettant d'�valuer le sc�nario, en termes d'esp�rance de d�g�ts et de probabilit� de r�alisation
	 * 
	 * @throws TechnicalException e
	 */
	private void evaluerEvenement() throws TechnicalException
	{
		// long startTime = System.currentTimeMillis();

		probaRealisation = BigDecimal.ONE;
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
		BigDecimal valeurMin = ConversionUtil.stringVersBigDecimal(valeurMinTemp, new BigDecimal("0.0005"));
		// valeurMin.pow(listeElements.size() + 1);
		Boolean arretPossible = PropertiesFactory.getOptionalBoolean(CLE_ARRET_TRAITEMENT);

		/*
		 * Lors de la recherche du scenario initial, la liste des types de r�solution n'est pas enti�rement remplie. On it�re uniquement sur les r�solutions
		 * afin de trouver la meilleure combinaison.
		 */
		List<Attaque> attaques = chaine.getListeAttaques();

		// Variables temporaires
		Attaque att;
		int typeResolution;
		int tailleListeResultats = listeResultats.size();

		for (int i = 0; i < tailleListeResultats; i++)
		{
			att = attaques.get(i);
			typeResolution = listeResultats.get(i);

			probaTmp = ResolutionAttaque.resoudreAttaque(att, cible, typeResolution);
			esperanceTmp = ResolutionAttaque.esperanceDeDegats(att, cible, typeResolution);

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

	/**
	 * @return la liste des types de r�solution du sc�nario
	 */
	public List<Integer> getListeTypesResolution()
	{
		return listeResultats;
	}

	/**
	 * @return l'indice de bourrinisme du sc�nario
	 */
	public double getIndiceBourrinisme()
	{
		if (indiceBourrinisme == -1)
		{
			// L'indice de bourrinisme n'a pas �t� calcul�. Le faire
			indiceBourrinisme = 0;
			Integer theResolution;

			for (Iterator<Integer> iter = listeResultats.iterator(); iter.hasNext();)
			{
				theResolution = iter.next();
				switch (theResolution)
				{
					case ResolutionAttaque.RESOLUTION_COUP_SIMPLE:
						indiceBourrinisme += 2;
						break;
					case ResolutionAttaque.RESOLUTION_COUP_CRITIQUE:
						indiceBourrinisme += 3;
						break;
					default:
						// Dans les autres cas, l'indice de bourrinisme est augment� de 0
						break;
				}
			}

			indiceBourrinisme /= listeResultats.size();
		}

		return indiceBourrinisme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object aArg0)
	{
		// V�rification de l'�galit� des r�f�rences
		if (this == aArg0)
		{
			return true;
		}

		// /*
		// * Deux Sc�narios sont consid�r�s �gaux s'ils ont la m�me chaine d'attaque, les m�me r�solutions
		// */
		// if (aArg0 != null && aArg0 instanceof ScenarioV)
		// {
		// ScenarioV scenar = (ScenarioV) aArg0;
		// // M�me cible, m�me nombre d'�l�ments
		// if (listeResultats.size() == scenar.listeResultats.size() && this.chaine.equals(scenar.chaine))
		// {
		// boolean chainesEgales = true;
		// // Comparer les �l�ments de chaque chaine
		// int tailleListeResultats = listeResultats.size();
		// for (int i = 0; i < tailleListeResultats && chainesEgales; i++)
		// {
		// chainesEgales &= listeResultats.get(i).equals(scenar.listeResultats.get(i));
		// }
		//
		// if (chainesEgales)
		// {
		// return true;
		// }
		// }
		// }
		//
		// return false;

		return this.toString().equals(aArg0.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hashcode = 11;
		for (Integer elt : listeResultats)
		{
			hashcode = hashcode * 31 + elt.hashCode();
		}
		return hashcode * 31 + chaine.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if (toString == null)
		{
			StringBuilder result = new StringBuilder(256);
			for (Integer elt : listeResultats)
			{
				result.append(elt.toString()).append(" - ");
			}
			toString = result.toString();
		}
		return toString;
	}
}

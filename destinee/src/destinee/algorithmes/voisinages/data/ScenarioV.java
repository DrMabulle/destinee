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
	 * Méthode permettant d'ajouter un type de résolution au scénario
	 * 
	 * @param aTypeResolution un type de résolution
	 */
	public void ajouterElementResolution(final int aTypeResolution)
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
	public void changerTypeResolution(final int aPosition, final int aTypeResolution)
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
		// long startTime = System.currentTimeMillis();

		probaRealisation = BigDecimal.ONE;
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
		BigDecimal valeurMin = ConversionUtil.stringVersBigDecimal(valeurMinTemp, new BigDecimal("0.0005"));
		// valeurMin.pow(listeElements.size() + 1);
		Boolean arretPossible = PropertiesFactory.getOptionalBoolean(CLE_ARRET_TRAITEMENT);

		/*
		 * Lors de la recherche du scenario initial, la liste des types de résolution n'est pas entièrement remplie. On itère uniquement sur les résolutions
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

	/**
	 * @return la liste des types de résolution du scénario
	 */
	public List<Integer> getListeTypesResolution()
	{
		return listeResultats;
	}

	/**
	 * @return l'indice de bourrinisme du scénario
	 */
	public double getIndiceBourrinisme()
	{
		if (indiceBourrinisme == -1)
		{
			// L'indice de bourrinisme n'a pas été calculé. Le faire
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
						// Dans les autres cas, l'indice de bourrinisme est augmenté de 0
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
		// Vérification de l'égalité des références
		if (this == aArg0)
		{
			return true;
		}

		// /*
		// * Deux Scénarios sont considérés égaux s'ils ont la même chaine d'attaque, les même résolutions
		// */
		// if (aArg0 != null && aArg0 instanceof ScenarioV)
		// {
		// ScenarioV scenar = (ScenarioV) aArg0;
		// // Même cible, même nombre d'éléments
		// if (listeResultats.size() == scenar.listeResultats.size() && this.chaine.equals(scenar.chaine))
		// {
		// boolean chainesEgales = true;
		// // Comparer les éléments de chaque chaine
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
			StringBuffer result = new StringBuffer(150);
			for (Integer elt : listeResultats)
			{
				result.append(elt.toString()).append(" - ");
			}
			toString = result.toString();
		}
		return toString;
	}
}

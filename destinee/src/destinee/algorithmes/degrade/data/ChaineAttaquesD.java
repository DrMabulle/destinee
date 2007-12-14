/**
 * 
 */
package destinee.algorithmes.degrade.data;

import java.util.ArrayList;
import java.util.List;

import destinee.commun.data.Attaque;
import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;

/**
 * @author Bubulle et No-one
 * 
 */
public class ChaineAttaquesD
{
	private List<Attaque> chaineAttaque = new ArrayList<Attaque>();
	private Cible cible;
	private boolean hasBeenEvaluated = false;
	private double esperanceDegatCumulee = 0;

	/**
	 * Constructeur par défaut
	 */
	public ChaineAttaquesD(Cible aCible)
	{
		super();
		cible = aCible;
	}

	/**
	 * @return the esperanceDegatCumulee
	 */
	public double getEsperanceDegatCumulee()
	{
		if (!hasBeenEvaluated)
		{
			evaluer();
			hasBeenEvaluated = true;
		}
		return esperanceDegatCumulee;
	}

	/**
	 * Méthode permettant d'évaluer l'espérance de dégâts cumulés de la chaine d'attaque Algorithme dégradé
	 */
	private void evaluer()
	{
		// Réinitialiser la fatigue et les malus de la cible et des persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefense();
		for (Attaque attaque : chaineAttaque)
		{
			attaque.getPerso().reinitialiserFatigue();
		}

		// Evaluer l'espérance de dégâts
		for (Attaque attaque : chaineAttaque)
		{
			esperanceDegatCumulee += ResolutionAttaque.esperanceDeDegats(attaque, cible);
			// les malus de défense de la cible sont augmentés de l'espérance de malus de défense de l'attaque
			cible.incrementerMalusDefence(ResolutionAttaque.esperanceMalusDefense(attaque, cible));
		}
	}

	/**
	 * @return the identifiant
	 */
	public String getIdentifiant()
	{
		StringBuffer sb = new StringBuffer("");

		for (Attaque attaque : chaineAttaque)
		{
			sb.append(attaque.getPerso().getIdentifiant());
			sb.append(attaque.getTypeAttaque());
			sb.append("-");
		}

		return sb.toString();
	}

	/**
	 * Ajouter une attaque à la chaine d'attaque
	 * 
	 * @param aAttaque une attaque à ajouter
	 */
	public void ajouterAttaque(Attaque aAttaque)
	{
		chaineAttaque.add(aAttaque);
		hasBeenEvaluated = false;
	}

}

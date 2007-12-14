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
	 * Constructeur par d�faut
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
	 * M�thode permettant d'�valuer l'esp�rance de d�g�ts cumul�s de la chaine d'attaque Algorithme d�grad�
	 */
	private void evaluer()
	{
		// R�initialiser la fatigue et les malus de la cible et des persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefense();
		for (Attaque attaque : chaineAttaque)
		{
			attaque.getPerso().reinitialiserFatigue();
		}

		// Evaluer l'esp�rance de d�g�ts
		for (Attaque attaque : chaineAttaque)
		{
			esperanceDegatCumulee += ResolutionAttaque.esperanceDeDegats(attaque, cible);
			// les malus de d�fense de la cible sont augment�s de l'esp�rance de malus de d�fense de l'attaque
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
	 * Ajouter une attaque � la chaine d'attaque
	 * 
	 * @param aAttaque une attaque � ajouter
	 */
	public void ajouterAttaque(Attaque aAttaque)
	{
		chaineAttaque.add(aAttaque);
		hasBeenEvaluated = false;
	}

}

/**
 * 
 */
package destinee.commun.data;

import destinee.commun.probas.ResolutionAttaque;

/**
 * @author Bubulle et No-one Class contenant les informations relatives à la cible du combat
 */
public class Cible implements Cloneable
{
	private int nombreDeDesDefense;
	private int bonusDefense;
	private int armure;
	private double malusDesDefense;
	private int fatigue;

	/**
	 * Constructeur par défaut
	 * 
	 * @param aNombreDeDesDefense nombre de dés de défense
	 * @param aBonusDefense bonus fixe en défense
	 * @param aArmure quantité d'armure
	 */
	public Cible(int aNombreDeDesDefense, int aBonusDefense, int aArmure)
	{
		super();
		nombreDeDesDefense = aNombreDeDesDefense;
		bonusDefense = aBonusDefense;
		armure = aArmure;
		malusDesDefense = 0.5;
		fatigue = 0;
	}

	/**
	 * @return Le nombre de dés de défense effectif : défense initiale - malus
	 */
	public int getNbDesDefenseEffectif()
	{
		return nombreDeDesDefense - ((int) getMalusDesDefense());
	}

	/**
	 * @return the nombreDeDesDefense
	 */
	public int getNbDesDefense()
	{
		return nombreDeDesDefense;
	}

	/**
	 * @param aNombreDeDesDefense the nombreDeDesDefense to set
	 */
	public void setNombreDeDesDefense(int aNombreDeDesDefense)
	{
		nombreDeDesDefense = aNombreDeDesDefense;
	}

	/**
	 * @return Bonus de défense fixe - fatigue
	 */
	public int getBonusDefenseEffectif()
	{
		return bonusDefense - getFatigue();
	}

	/**
	 * @return bonus de défense fixe
	 */
	public int getBonusDefense()
	{
		return bonusDefense;
	}

	/**
	 * @return the armure
	 */
	public int getArmure()
	{
		return armure;
	}

	/**
	 * @return la fatigue de la cible
	 */
	public int getFatigue()
	{
		return fatigue;
	}

	/**
	 * @param aFatigue the fatigue to set
	 */
	public void setFatigue(int aFatigue)
	{
		fatigue = aFatigue;
	}

	/**
	 * @return the malusDesDefense
	 */
	public double getMalusDesDefense()
	{
		return malusDesDefense;
	}

	/**
	 * @param aMalusDesDefense the malusDesDefense to set
	 */
	public void setMalusDesDefense(int aMalusDesDefense)
	{
		malusDesDefense = aMalusDesDefense;
	}

	/**
	 * Méthode permettant d'incrémenter la fatigue de la cible lors d'une attaque subie
	 */
	public void incrementerFatigue()
	{
		fatigue++;
	}

	/**
	 * Réinitialiser la fatigue de la cible
	 */
	public void reinitialiserFatigue()
	{
		fatigue = 0;
	}

	/**
	 * Incrémente le malus de défence en fonction de l'attaque subie et du type de résolution
	 * 
	 * @param aAttaque une attaque
	 * @param typeResolution un type de résolution
	 */
	public void incrementerMalusDefence(Attaque aAttaque, int typeResolution)
	{
		// Il n'y a pas de malus d'esquive sur une attaque imparable
		// Pour toutes les autres, on a :
		// -1D en cas de coup critique
		// -0,5D en cas de coup simple ou esquive simple
		// rien en cas d'esquive parfaite
		if (!(aAttaque instanceof AttaqueImparable))
		{
			switch (typeResolution)
			{
				case ResolutionAttaque.RESOLUTION_COUP_CRITIQUE: // attaque critique
					malusDesDefense += 1;
					break;
				case ResolutionAttaque.RESOLUTION_COUP_SIMPLE: // attaque reussie
					malusDesDefense += 0.5;
					break;
				case ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE: // esquive reussie
					malusDesDefense += 0.5;
					break;
				case ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE: // esquive parfaite
					// malusDesDefense += 0;
					break;
			}
		}
	}

	/**
	 * Incrémente le malus de défence d'une espérance de malus de défense. Algorithme dégradé.
	 * 
	 * @param aEsperanceMalusDefense une espérance de malus de défense
	 */
	public void incrementerMalusDefence(double aEsperanceMalusDefense)
	{
		malusDesDefense += aEsperanceMalusDefense;
	}

	/**
	 * Réinitialiser les malus de défence de la cible
	 */
	public void reinitialiserMalusDefence()
	{
		malusDesDefense = 0.5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Cible clone()
	{
		return new Cible(nombreDeDesDefense, bonusDefense, armure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aObj)
	{
		if (aObj != null && aObj instanceof Cible)
		{
			Cible cible = (Cible) aObj;
			if (getArmure() == cible.getArmure() && getBonusDefense() == cible.getBonusDefense() && getNbDesDefense() == cible.getNbDesDefense())
			{
				return true;
			}
		}
		return super.equals(aObj);
	}

}

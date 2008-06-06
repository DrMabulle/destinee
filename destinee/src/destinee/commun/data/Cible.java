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
	private int nombreDeDesDefenseMagique;
	private int bonusDefenseMagique;
	private int armure;
	private double malusDesDefense;
	private int fatigue;

	/**
	 * Constructeur par défaut
	 * 
	 * @param aNombreDeDesDefense nombre de dés de défense
	 * @param aBonusDefense bonus fixe en défense
	 * @param aNombreDeDesDM nombre de dés de DM
	 * @param aBonusDM bonus fixe de DM
	 * @param aArmure quantité d'armure
	 */
	public Cible(final int aNombreDeDesDefense, final int aBonusDefense, final int aNombreDeDesDM, final int aBonusDM, final int aArmure)
	{
		this(aNombreDeDesDefense, aBonusDefense, aNombreDeDesDM, aBonusDM, aArmure, 0);
	}

	/**
	 * Constructeur par défaut
	 * 
	 * @param aNombreDeDesDefense nombre de dés de défense
	 * @param aBonusDefense bonus fixe en défense
	 * @param aNombreDeDesDM nombre de dés de DM
	 * @param aBonusDM bonus fixe de DM
	 * @param aArmure quantité d'armure
	 * @param aFatigue la fatigue
	 */
	public Cible(final int aNombreDeDesDefense, final int aBonusDefense, final int aNombreDeDesDM, final int aBonusDM, final int aArmure,
			final int aFatigue)
	{
		super();
		nombreDeDesDefense = aNombreDeDesDefense;
		bonusDefense = aBonusDefense;
		nombreDeDesDefenseMagique = aNombreDeDesDM;
		bonusDefenseMagique = aBonusDM;
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
	public void setNombreDeDesDefense(final int aNombreDeDesDefense)
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
	 * @return the nombreDeDesDefenseMagique
	 */
	public int getNombreDeDesDefenseMagique()
	{
		return nombreDeDesDefenseMagique;
	}

	/**
	 * @param aNombreDeDesDefenseMagique the nombreDeDesDefenseMagique to set
	 */
	public void setNombreDeDesDefenseMagique(final int aNombreDeDesDefenseMagique)
	{
		nombreDeDesDefenseMagique = aNombreDeDesDefenseMagique;
	}

	/**
	 * @return the bonusDefenseMagique
	 */
	public int getBonusDefenseMagique()
	{
		return bonusDefenseMagique;
	}

	/**
	 * @param aBonusDefenseMagique the bonusDefenseMagique to set
	 */
	public void setBonusDefenseMagique(final int aBonusDefenseMagique)
	{
		bonusDefenseMagique = aBonusDefenseMagique;
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
	public void setFatigue(final int aFatigue)
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
	public void setMalusDesDefense(final double aMalusDesDefense)
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
	public void incrementerMalusDefense(final Attaque aAttaque, final int typeResolution)
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
	public void incrementerMalusDefence(final double aEsperanceMalusDefense)
	{
		malusDesDefense += aEsperanceMalusDefense;
	}

	/**
	 * Réinitialiser les malus de défence de la cible
	 */
	public void reinitialiserMalusDefense()
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
		return new Cible(nombreDeDesDefense, bonusDefense, nombreDeDesDefenseMagique, bonusDefenseMagique, armure, fatigue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object aObj)
	{
		// Vérification de l'égalité des références
		if (this == aObj)
		{
			return true;
		}

		if (aObj != null && aObj instanceof Cible)
		{
			Cible cible = (Cible) aObj;
			if (getArmure() == cible.getArmure() && getBonusDefense() == cible.getBonusDefense()
					&& getNbDesDefense() == cible.getNbDesDefense())
			{
				return true;
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
		int hashcode = 211;
		hashcode = 5 * hashcode + getArmure();
		hashcode = 5 * hashcode + getBonusDefense();
		hashcode = 5 * hashcode + getNbDesDefense();
		return hashcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(50);
		sb.append("Cible : DEF=").append(getNbDesDefense()).append("D+").append(getBonusDefense()).append(", ARM=").append(getArmure());
		return sb.toString();
	}

}

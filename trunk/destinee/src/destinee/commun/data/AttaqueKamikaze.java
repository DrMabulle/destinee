/**
 * 
 */
package destinee.commun.data;

import destinee.commun.constantes.ConstantesAttaques;

/**
 * @author Bubulle et No-one
 * 
 */
public class AttaqueKamikaze extends AttaqueAbstract
{
	/**
	 * @param aPerso un Perso
	 */
	public AttaqueKamikaze(final Perso aPerso)
	{
		super(aPerso);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getBonusAtt()
	 */
	public int getBonusAtt()
	{
		return getPerso().getBonusAttaqueEffectif();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesAtt()
	 */
	public int getNbDesAtt()
	{
		return (getPerso().getNombreDeDesAttaque() + ((getPerso().getSacrificePourAttaque() / 20)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (getPerso().getNombreDeDesDegats() + ((getPerso().getSacrificePourDegat() / 20)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getBonusDeg(int)
	 */
	public int getBonusDeg(final int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getBonusDegatsCritique(int)
	 */
	public int getBonusDegatsCritique(final int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDegatsCritique()
	 */
	public int getNbDesDegatsCritique()
	{
		return (int) (1.5 * (getPerso().getNombreDeDesDegats() + ((getPerso().getSacrificePourDegat() / 20))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getTypeAttaque()
	 */
	public String getTypeAttaque()
	{
		return ConstantesAttaques.ID_ATTAQUE_KAMIKAZE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getAugmentationFatigue()
	 */
	public int getAugmentationFatigue()
	{
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.commun.data.Attaque#getCoutEnPA()
	 */
	@Override
	public int getCoutEnPA()
	{
		return 6;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder(64);
		result.append(super.toString());
		result.append('(').append(getPerso().getSacrificePourAttaque()).append('/');
		result.append(getPerso().getSacrificePourDegat()).append(')');
		return result.toString();
	}
}

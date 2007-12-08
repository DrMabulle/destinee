/**
 * 
 */
package destinee.commun.data;

import destinee.commun.constantes.ConstantesAttaques;

/**
 * @author Bubulle et No-one
 * 
 */
public class AttaqueBrutale extends AttaqueAbstract
{

	/**
	 * @param aPerso un Perso
	 */
	public AttaqueBrutale(Perso aPerso)
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
		return (int) (0.6667 * getPerso().getNombreDeDesAttaque());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (int) (1.5 * getPerso().getNombreDeDesDegats());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getBonusDeg(int)
	 */
	public int getBonusDeg(int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getBonusDegatsCritique(int)
	 */
	public int getBonusDegatsCritique(int aArmureCible)
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
		return (int) (2.25 * getPerso().getNombreDeDesDegats());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getTypeAttaque()
	 */
	public String getTypeAttaque()
	{
		return ConstantesAttaques.ID_ATTAQUE_BRUTALE;
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

}

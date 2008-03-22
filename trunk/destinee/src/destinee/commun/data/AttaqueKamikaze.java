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

	private int bonusDesAttaque;
	private int bonusDesDegats;

	/**
	 * @param aPerso un Perso
	 */
	public AttaqueKamikaze(Perso aPerso)
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
		return (getPerso().getNombreDeDesAttaque() + bonusDesAttaque);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (getPerso().getNombreDeDesDegats() + bonusDesDegats);
	}

	/**
	 * @return the bonusDesAttaque
	 */
	public int getBonusDesAttaque()
	{
		return bonusDesAttaque;
	}

	/**
	 * @param aBonusDesAttaque the bonusDesAttaque to set
	 */
	public void setBonusDesAttaque(int aBonusDesAttaque)
	{
		bonusDesAttaque = aBonusDesAttaque;
	}

	/**
	 * @return the bonusDesDegats
	 */
	public int getBonusDesDegats()
	{
		return bonusDesDegats;
	}

	/**
	 * @param aBonusDesDegats the bonusDesDegats to set
	 */
	public void setBonusDesDegats(int aBonusDesDegats)
	{
		bonusDesDegats = aBonusDesDegats;
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
		return (int) (1.5 * (getPerso().getNombreDeDesDegats() + getBonusDesDegats()));
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

}
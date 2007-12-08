/**
 * 
 */
package destinee.commun.data;

import destinee.commun.constantes.ConstantesAttaques;

/**
 * @author Bubulle et No-one
 * 
 */
public class AttaqueRapide extends AttaqueAbstract
{

	/**
	 * @param aPerso un Perso
	 */
	public AttaqueRapide(Perso aPerso)
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
		return (int) (0.5 * getPerso().getNombreDeDesAttaque());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (int) (0.5 * getPerso().getNombreDeDesDegats());
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
		return (int) (1.5 * (int) 0.5 * getPerso().getNombreDeDesDegats());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getTypeAttaque()
	 */
	public String getTypeAttaque()
	{
		return ConstantesAttaques.ID_ATTAQUE_RAPIDE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getAugmentationFatigue()
	 */
	public int getAugmentationFatigue()
	{
		return 1;
	}

}

/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 */
public class AttaquePrecise extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso un Perso
	 */
	public AttaquePrecise(Perso aPerso)
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
		return getPerso().getBonusAttaque();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesAtt()
	 */
	public int getNbDesAtt()
	{
		return (int) (1.5 * getPerso().getNombreDeDesAttaque());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (int) (0.667 * getPerso().getNombreDeDesDegats());
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDeg(int)
	 */
	public int getBonusDeg(int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDegatsCritique(int)
	 */
	public int getBonusDegatsCritique(int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesDegatsCritique()
	 */
	public int getNbDesDegatsCritique()
	{
		return (int) (((int) (0.667 * getPerso().getNombreDeDesDegats()) * 1.5 ));
	}

}

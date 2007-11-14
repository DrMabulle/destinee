/**
 * 
 */
package destinee.data;


/**
 * @author AMOROS
 * 
 */
public class AttaqueRapide extends AttaqueAbstract implements Attaque
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
		return getPerso().getBonusAttaque();
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

	public int getBonusDeg(int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	public int getBonusDegatsCritique(int aArmureCible)
	{
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	public int getNbDesDegatsCritique()
	{
		return (int)(1.5 * (int) 0.5 * getPerso().getNombreDeDesDegats());
	}

}

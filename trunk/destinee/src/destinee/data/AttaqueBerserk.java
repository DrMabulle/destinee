/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 */
/**
 * @author AMOROS
 *
 */
public class AttaqueBerserk extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso
	 *            un Perso
	 */
	public AttaqueBerserk(Perso aPerso)
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
		return (2 * getPerso().getNombreDeDesAttaque());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (2 * getPerso().getNombreDeDesDegats());
	}

	public int getBonusDeg(int aArmureCible)
	{
		return getPerso().getBonusDegats() - aArmureCible;
	}

	public int getBonusDegatsCritique(int aArmureCible)
	{
		return getPerso().getBonusDegats();
	}

	public int getNbDesDegatsCritique()
	{
		return (getPerso().getNombreDeDesDegats() * 3);
	}

}

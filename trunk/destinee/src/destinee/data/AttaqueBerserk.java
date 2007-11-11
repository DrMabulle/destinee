/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 */
public class AttaqueBerserk extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso un Perso
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
	 * @see destinee.data.Attaque#getBonusDeg()
	 */
	public int getBonusDeg()
	{
		return getPerso().getBonusDegats();
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

}

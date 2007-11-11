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
		return (int) (3 / 2 * getPerso().getNombreDeDesAttaque());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (int) (2 / 3 * getPerso().getNombreDeDesDegats());
	}

}

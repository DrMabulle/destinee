/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 *
 */
public class attaqueImparable extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso
	 */
	public attaqueImparable(Perso aPerso)
	{
		super(aPerso);
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusAtt()
	 */
	public int getBonusAtt()
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDeg()
	 */
	public int getBonusDeg()
	{
		return getPerso().getBonusDegats();
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesAtt()
	 */
	public int getNbDesAtt()
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return getPerso().getNombreDeDesAttaque();
	}

}

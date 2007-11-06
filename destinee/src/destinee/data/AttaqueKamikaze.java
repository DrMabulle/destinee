/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 *
 */
public class AttaqueKamikaze extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso
	 */
	public AttaqueKamikaze(Perso aPerso)
	{
		super(aPerso);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusAtt()
	 */
	public int getBonusAtt()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDeg()
	 */
	public int getBonusDeg()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesAtt()
	 */
	public int getNbDesAtt()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}

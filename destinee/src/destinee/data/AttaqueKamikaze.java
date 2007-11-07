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

	private int bonusDesAttaque;
	private int bonusDesDegats;

	/**
	 * @param aPerso
	 */
	public AttaqueKamikaze(Perso aPerso)
	{
		super(aPerso);
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusAtt()
	 */
	public int getBonusAtt()
	{
		return getPerso().getBonusAttaque();
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
		return (getPerso().getNombreDeDesAttaque() + bonusDesAttaque);
	}

	/* (non-Javadoc)
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

}

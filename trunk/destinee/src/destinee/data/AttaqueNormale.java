/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 */
public class AttaqueNormale extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso un Perso
	 */
	public AttaqueNormale(Perso aPerso)
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
		return getPerso().getNombreDeDesAttaque();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return getPerso().getNombreDeDesDegats();
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDeg(int)
	 */
	public int getBonusDeg(int aArmureCible)
	{
		// TODO Auto-generated method stub
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDegatsCritique(int)
	 */
	public int getBonusDegatsCritique(int aArmureCible)
	{
		// TODO Auto-generated method stub
		return (getPerso().getBonusDegats() - aArmureCible);
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesDegatsCritique()
	 */
	public int getNbDesDegatsCritique()
	{
		// TODO Auto-generated method stub
		return (int) (1.5 * getPerso().getNombreDeDesDegats());
	}

}

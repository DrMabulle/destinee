/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 */
public class AttaqueMagique extends AttaqueAbstract implements Attaque
{

	/**
	 * @param aPerso un Perso
	 */
	public AttaqueMagique(Perso aPerso)
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
		return (int) (0.5 * getPerso().getBonusPM());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesAtt()
	 */
	public int getNbDesAtt()
	{
		return getPerso().getNombreDeDesPM();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.data.Attaque#getNbDesDeg()
	 */
	public int getNbDesDeg()
	{
		return (int) (0.5 * getPerso().getNombreDeDesPM());
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDeg(int)
	 */
	public int getBonusDeg(int aArmureCible)
	{
		return (int) ( 0.5 * getPerso().getBonusPM() );
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getBonusDegatsCritique(int)
	 */
	public int getBonusDegatsCritique(int aArmureCible)
	{
		return (int) ( 0.5 * getPerso().getBonusPM() );
	}

	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getNbDesDegatsCritique()
	 */
	public int getNbDesDegatsCritique()
	{
		return (int) (0.5 * getPerso().getNombreDeDesPM());
	}

}

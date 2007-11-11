/**
 * 
 */
package destinee.data;

/**
 * classe abstraite des attaques
 * 
 * @author AMOROS
 * 
 */
public abstract class AttaqueAbstract
{

	private Perso perso;

	/**
	 * @return the perso
	 */
	protected Perso getPerso()
	{
		return perso;
	}

	/**
	 * @param aPerso the perso to set
	 */
	protected void setPerso(Perso aPerso)
	{
		perso = aPerso;
	}

	/**
	 * @param aPerso
	 */
	public AttaqueAbstract(Perso aPerso)
	{
		super();
		perso = aPerso;
	}
}

/**
 * 
 */
package destinee.data;

/**
 * classe abstraite des attaques
 * 
 * @author Bubulle et No-one
 * 
 */
public abstract class AttaqueAbstract implements Attaque
{

	/**
	 * Le personnage responsable de l'attaque
	 */
	private Perso perso;


	/* (non-Javadoc)
	 * @see destinee.data.Attaque#getPerso()
	 */
	public Perso getPerso()
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

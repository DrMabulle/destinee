/**
 * 
 */
package destinee.data;

/**
 * @author bkessler
 *
 */
public class ScenarioElement
{

	/**
	 * L'attaque effectuée
	 */
	private Attaque attaque;
	/**
	 * La résolution de l'attaque
	 */
	private int typeResolution;
	/**
	 * @param aAttaque
	 * @param aTypeResolution
	 */
	public ScenarioElement(Attaque aAttaque, int aTypeResolution)
	{
		super();
		attaque = aAttaque;
		typeResolution = aTypeResolution;
	}
	/**
	 * @return the attaque
	 */
	public Attaque getAttaque()
	{
		return attaque;
	}

	/**
	 * @return the typeResolution
	 */
	public int getTypeResolution()
	{
		return typeResolution;
	}
}

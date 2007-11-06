/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 */
public interface Attaque
{
	/**
	 * rend le nombre de dés d'attaque
	 * 
	 * @return NbDesAtt
	 */
	public int getNbDesAtt();

	/**
	 * rend le nombre de dés de dégats
	 * 
	 * @return nbDesDeg
	 */
	public int getNbDesDeg();

	/**
	 * rend le bonus d'attaque
	 * 
	 * @return bonusAtt
	 */
	public int getBonusAtt();

	/**
	 * rend le bonus de dégats
	 * 
	 * @return bonusDeg
	 */
	public int getBonusDeg();

}

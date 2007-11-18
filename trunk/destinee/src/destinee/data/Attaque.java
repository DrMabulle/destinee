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
	 * rend le nombre de d�s d'attaque
	 * 
	 * @return NbDesAtt
	 */
	public int getNbDesAtt();

	/**
	 * rend le nombre de d�s de d�gats
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
	 * rend le bonus de d�gats (en fonction de l'armure de la cible)
	 * 
	 * @return bonusDeg
	 */
	public int getBonusDeg(int armureCible);
	
	/**
	 * rend le nombre de d�s de d�gats lors d'une attaque critique
	 * 
	 * @return bonusDeg
	 */
	public int getNbDesDegatsCritique();
	
	/**
	 * rend le bonus de d�gats lors d'une attaque critique (en fonction de l'armure de la cible)
	 * 
	 * @return bonusDeg
	 */
	public int getBonusDegatsCritique(int armureCible);
	
	/**
	 * @return le personnage faisant l'attaque
	 */
	public Perso getPerso();

	/**
	 * @return le type d'attaque
	 */
	public String getTypeAttaque();
	
	/**
	 * @return le nombre de points de fatigue subi en faisant l'attaque
	 */
	public int getAugmentationFatigue();
}

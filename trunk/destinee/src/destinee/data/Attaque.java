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
	 * rend le bonus de dégats (en fonction de l'armure de la cible)
	 * 
	 * @return bonusDeg
	 */
	public int getBonusDeg(int armureCible);
	
	/**
	 * rend le nombre de dés de dégats lors d'une attaque critique
	 * 
	 * @return bonusDeg
	 */
	public int getNbDesDegatsCritique();
	
	/**
	 * rend le bonus de dégats lors d'une attaque critique (en fonction de l'armure de la cible)
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

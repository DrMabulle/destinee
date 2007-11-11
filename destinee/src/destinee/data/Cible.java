/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS contient les informations relatives a la cible du combat
 */
public class Cible
{
	private int nombreDeDesDefense;
	private int bonusDefense;
	private int armure;
	private int compteurAttaques;
	private int compteurAttaquesCritiques;

	/**
	 * Constructeur par défaut
	 * 
	 * @param aNombreDeDesDefense
	 *            nombre de dés de défense
	 * @param aBonusDefense
	 *            bonus fixe en défense
	 * @param aArmure
	 *            quantité d'armure
	 */
	public Cible(int aNombreDeDesDefense, int aBonusDefense, int aArmure)
	{
		super();
		nombreDeDesDefense = aNombreDeDesDefense;
		bonusDefense = aBonusDefense;
		armure = aArmure;
		compteurAttaques = 0;
		compteurAttaquesCritiques = 0;
	}

	/**
	 * @return the nombreDeDesDefense
	 */
	public int getNombreDeDesDefense()
	{
		return nombreDeDesDefense;
	}

	/**
	 * @param aNombreDeDesDefense the nombreDeDesDefense to set
	 */
	public void setNombreDeDesDefense(int aNombreDeDesDefense)
	{
		nombreDeDesDefense = aNombreDeDesDefense;
	}

	/**
	 * @return the bonusDefense
	 */
	public int getBonusDefense()
	{
		return bonusDefense;
	}

	/**
	 * @param aBonusDefense the bonusDefense to set
	 */
	public void setBonusDefense(int aBonusDefense)
	{
		bonusDefense = aBonusDefense;
	}

	/**
	 * @return the armure
	 */
	public int getArmure()
	{
		return armure;
	}

	/**
	 * @param aArmure the armure to set
	 */
	public void setArmure(int aArmure)
	{
		armure = aArmure;
	}

	/**
	 * @return the compteurAttaques
	 */
	public int getCompteurAttaques()
	{
		return compteurAttaques;
	}

	/**
	 * @param aCompteurAttaques the compteurAttaques to set
	 */
	public void setCompteurAttaques(int aCompteurAttaques)
	{
		compteurAttaques = aCompteurAttaques;
	}

	/**
	 * @return the compteurAttaquesCritiques
	 */
	public int getCompteurAttaquesCritiques()
	{
		return compteurAttaquesCritiques;
	}

	/**
	 * @param aCompteurAttaquesCritiques the compteurAttaquesCritiques to set
	 */
	public void setCompteurAttaquesCritiques(int aCompteurAttaquesCritiques)
	{
		compteurAttaquesCritiques = aCompteurAttaquesCritiques;
	}

}

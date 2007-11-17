/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS contient les informations relatives a la cible du combat
 */
public class Cible
{
	private int nombreDeDesDefenseInitial;
	private int bonusDefenseInitial;
	private int armure;
	private int malusDesDefense;
	private int fatigue;
	
	
	

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
		nombreDeDesDefenseInitial = aNombreDeDesDefense;
		bonusDefenseInitial = aBonusDefense;
		armure = aArmure;
		malusDesDefense = 0;
		fatigue = 0;
	}

	/**
	 * @return the nombreDeDesDefense
	 */
	public int getNombreDeDesDefense()
	{
		return nombreDeDesDefenseInitial - getMalusDesDefense();
	}

	/**
	 * @return the nombreDeDesDefenseInitial
	 */
	public int getNombreDeDesDefenseInitial()
	{
		return nombreDeDesDefenseInitial;
	}
	
	/**
	 * @param aNombreDeDesDefenseInitial the nombreDeDesDefense to set
	 */
	public void setNombreDeDesDefense(int aNombreDeDesDefenseInitial)
	{
		nombreDeDesDefenseInitial = aNombreDeDesDefenseInitial;
	}

	/**
	 * @return the bonusDefense
	 */
	public int getBonusDefense()
	{
		return bonusDefenseInitial - getFatigue();
	}

	/**
	 * @return the bonusDefenseInitial
	 */
	public int getBonusDefenseInitial()
	{
		return bonusDefenseInitial;
	}

	
	/**
	 * @param aBonusDefense the bonusDefenseInitial to set
	 */
	public void setBonusDefenseInitial(int aBonusDefenseInitial)
	{
		bonusDefenseInitial = aBonusDefenseInitial;
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
	 * @return the fatigue
	 */
	public int getFatigue()
	{
		return fatigue;
	}

	/**
	 * @param aFatigue the fatigue to set
	 */
	public void setFatigue(int aFatigue)
	{
		fatigue = aFatigue;
	}

	/**
	 * @return the malusDesDefense
	 */
	public int getMalusDesDefense()
	{
		return malusDesDefense;
	}

	/**
	 * @param aMalusDesDefense the malusDesDefense to set
	 */
	public void setMalusDesDefense(int aMalusDesDefense)
	{
		malusDesDefense = aMalusDesDefense;
	}

	/**
	 * @param aNombreDeDesDefenseInitial the nombreDeDesDefenseInitial to set
	 */
	public void setNombreDeDesDefenseInitial(int aNombreDeDesDefenseInitial)
	{
		nombreDeDesDefenseInitial = aNombreDeDesDefenseInitial;
	}

}

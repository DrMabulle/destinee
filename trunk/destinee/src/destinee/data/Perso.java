/**
 * 
 */
package destinee.data;

/**
 * @author AMOROS
 * 
 * Perso servira a stocker les caracteristiques de chaque personnage Un
 * personnage est d�fini par son nolbre de d�s d'attaque, son bonus d'attaque
 * son nombre de d�s de d�gats, son bonus de d�gats, ainsi que le nombre de d�s
 * et le bonus de PM.
 * 
 * Il sera peut-etre n�cessaire de rajouter a ces caracteristique la liste des
 * attaques qu'il peut effectuer. Pour l'instant le scenario est g�n�r� par
 * prolog et on le suppose l�gal.
 */
public class Perso
{
	// on suppose que les persos n'auront pas a changer leurs caracteristiques
	// pendant une chaine d'attaques, les caracs n'ont pas a etre bidouillables.

	private int nombreDeDesAttaque;
	private int bonusAttaque;
	private int nombreDeDesDegats;
	private int bonusDegats;
	private int nombreDeDesPM;
	private int bonusPM;

	/**
	 * @param aNombreDeDesAttaque
	 * @param aBonusAttaque
	 * @param aNombreDeDesDegats
	 * @param aBonusDegats
	 * @param aNombreDeDesPM
	 * @param aBonusPM
	 */
	public Perso(int aNombreDeDesAttaque, int aBonusAttaque, int aNombreDeDesDegats, int aBonusDegats, int aNombreDeDesPM, int aBonusPM)
	{
		super();
		nombreDeDesAttaque = aNombreDeDesAttaque;
		bonusAttaque = aBonusAttaque;
		nombreDeDesDegats = aNombreDeDesDegats;
		bonusDegats = aBonusDegats;
		nombreDeDesPM = aNombreDeDesPM;
		bonusPM = aBonusPM;
	}

	/**
	 * @return the nombreDeDesAttaque
	 */
	public int getNombreDeDesAttaque()
	{
		return nombreDeDesAttaque;
	}

	/**
	 * @param aNombreDeDesAttaque
	 *            the nombreDeDesAttaque to set
	 */
	public void setNombreDeDesAttaque(int aNombreDeDesAttaque)
	{
		nombreDeDesAttaque = aNombreDeDesAttaque;
	}

	/**
	 * @return the bonusAttaque
	 */
	public int getBonusAttaque()
	{
		return bonusAttaque;
	}

	/**
	 * @param aBonusAttaque
	 *            the bonusAttaque to set
	 */
	public void setBonusAttaque(int aBonusAttaque)
	{
		bonusAttaque = aBonusAttaque;
	}

	/**
	 * @return the nombreDeDesDegats
	 */
	public int getNombreDeDesDegats()
	{
		return nombreDeDesDegats;
	}

	/**
	 * @param aNombreDeDesDegats
	 *            the nombreDeDesDegats to set
	 */
	public void setNombreDeDesDegats(int aNombreDeDesDegats)
	{
		nombreDeDesDegats = aNombreDeDesDegats;
	}

	/**
	 * @return the bonusDegats
	 */
	public int getBonusDegats()
	{
		return bonusDegats;
	}

	/**
	 * @param aBonusDegats
	 *            the bonusDegats to set
	 */
	public void setBonusDegats(int aBonusDegats)
	{
		bonusDegats = aBonusDegats;
	}

	/**
	 * @return the nombreDeDesPM
	 */
	public int getNombreDeDesPM()
	{
		return nombreDeDesPM;
	}

	/**
	 * @param aNombreDeDesPM
	 *            the nombreDeDesPM to set
	 */
	public void setNombreDeDesPM(int aNombreDeDesPM)
	{
		nombreDeDesPM = aNombreDeDesPM;
	}

	/**
	 * @return the bonusPM
	 */
	public int getBonusPM()
	{
		return bonusPM;
	}

	/**
	 * @param aBonusPM
	 *            the bonusPM to set
	 */
	public void setBonusPM(int aBonusPM)
	{
		bonusPM = aBonusPM;
	}

}

/**
 * 
 */
package destinee.commun.data;

import java.util.Map;

import destinee.commun.constantes.ConstantesAttaques;

/**
 * @author Bubulle et No-one
 * 
 * Perso servira a stocker les caracteristiques de chaque personnage Un personnage est d�fini par son nolbre de d�s d'attaque, son bonus d'attaque son nombre de
 * d�s de d�gats, son bonus de d�gats, ainsi que le nombre de d�s et le bonus de PM.
 * 
 * Il sera peut-etre n�cessaire de rajouter a ces caracteristique la liste des attaques qu'il peut effectuer. Pour l'instant le scenario est g�n�r� par prolog
 * et on le suppose l�gal.
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
	private String identifiant;
	private int fatigue;
	private int fatigueIntiale;
	private Map<String, Double> maitrisesAtt;

	/**
	 * @param aNombreDeDesAttaque
	 * @param aBonusAttaque
	 * @param aNombreDeDesDegats
	 * @param aBonusDegats
	 * @param aNombreDeDesPM
	 * @param aBonusPM
	 * @param aFatigueInitiale fatigue initiale du perso
	 * @param aIdentifiant identifiant du personnage
	 */
	public Perso(int aNombreDeDesAttaque, int aBonusAttaque, int aNombreDeDesDegats, int aBonusDegats, int aNombreDeDesPM, int aBonusPM, int aFatigueInitiale,
			String aIdentifiant, Map<String, Double> aMaitrisesAtt)
	{
		super();
		nombreDeDesAttaque = aNombreDeDesAttaque;
		bonusAttaque = aBonusAttaque;
		nombreDeDesDegats = aNombreDeDesDegats;
		bonusDegats = aBonusDegats;
		nombreDeDesPM = aNombreDeDesPM;
		bonusPM = aBonusPM;
		identifiant = aIdentifiant;
		fatigue = aFatigueInitiale;
		fatigueIntiale = aFatigueInitiale;
		maitrisesAtt = aMaitrisesAtt;
		maitrisesAtt.put(ConstantesAttaques.ID_ATTAQUE_NORMALE, 1.00);
	}

	// /**
	// * Constructeur par copie
	// *
	// * @param aPerso un perso � copier
	// */
	// public Perso(Perso aPerso)
	// {
	// new Perso(aPerso.nombreDeDesAttaque, aPerso.bonusAttaque, aPerso.nombreDeDesDegats, aPerso.bonusDegats, aPerso.nombreDeDesPM, aPerso.bonusPM,
	// aPerso.fatigueIntiale, aPerso.identifiant);
	// }

	/**
	 * @return the nombreDeDesAttaque
	 */
	public int getNombreDeDesAttaque()
	{
		return nombreDeDesAttaque;
	}

	/**
	 * @param aNombreDeDesAttaque the nombreDeDesAttaque to set
	 */
	public void setNombreDeDesAttaque(int aNombreDeDesAttaque)
	{
		nombreDeDesAttaque = aNombreDeDesAttaque;
	}

	/**
	 * @param aBonusAttaque the bonusAttaque to set
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
	 * @param aNombreDeDesDegats the nombreDeDesDegats to set
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
	 * @param aBonusDegats the bonusDegats to set
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
	 * @param aNombreDeDesPM the nombreDeDesPM to set
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
	 * @param aBonusPM the bonusPM to set
	 */
	public void setBonusPM(int aBonusPM)
	{
		bonusPM = aBonusPM;
	}

	/**
	 * @return the identifiant
	 */
	public String getIdentifiant()
	{
		return identifiant;
	}

	/**
	 * @param aIdentifiant the identifiant to set
	 */
	public void setIdentifiant(String aIdentifiant)
	{
		identifiant = aIdentifiant;
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
	 * @return the bonusAttaqueInitial
	 */
	public int getBonusAttaque()
	{
		return bonusAttaque;
	}

	/**
	 * @return Le bonus d'attaque effectif : bonus fixe - fatigue
	 */
	public int getBonusAttaqueEffectif()
	{
		return bonusAttaque - fatigue;
	}

	/**
	 * Retourne le pourcentage de maitrise de la comp�tence d'attaque du perso
	 * 
	 * @param aNomAttaque un identifiant d'attaque
	 * @return le pourcentage de maitrise de la comp�tence d'attaque du perso
	 */
	public double getMaitriseAttaque(String aNomAttaque)
	{
		Double result = maitrisesAtt.get(aNomAttaque);
		if (result == null)
		{
			result = new Double(0.0);
		}
		return result.doubleValue();
	}

	/**
	 * Incr�mente la fatigue du joueur en fonction du type d'attaque. R�gle : incr�ment de fatigue = PA / 2
	 * 
	 * @param aAttaque une attaque
	 */
	public void incrementerFatigue(Attaque aAttaque)
	{
		fatigue += aAttaque.getAugmentationFatigue();
	}

	public void reinitialiserFatigue()
	{
		fatigue = fatigueIntiale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getIdentifiant();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Perso clone()
	{
		return new Perso(nombreDeDesAttaque, bonusAttaque, nombreDeDesDegats, bonusDegats, nombreDeDesPM, bonusPM, fatigueIntiale, identifiant, maitrisesAtt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object aObj)
	{
		// V�rification de l'�galit� des r�f�rences
		if (this == aObj)
		{
			return true;
		}

		if (aObj != null && aObj instanceof Perso)
		{
			Perso perso = (Perso) aObj;
			/*
			 * if (getBonusAttaque() == perso.getBonusAttaque() && getBonusDegats() == perso.getBonusDegats() && getBonusPM() == perso.getBonusPM() &&
			 * getIdentifiant().equals(perso.getIdentifiant()) && getNombreDeDesAttaque() == perso.getNombreDeDesAttaque() && getNombreDeDesDegats() ==
			 * perso.getNombreDeDesDegats() && getNombreDeDesPM() == perso.getNombreDeDesPM())
			 */
			if (getIdentifiant().equals(perso.getIdentifiant()))
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return getIdentifiant().hashCode();
	}
}

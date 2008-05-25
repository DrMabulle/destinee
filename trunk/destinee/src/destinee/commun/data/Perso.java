/**
 * 
 */
package destinee.commun.data;

import java.util.Map;

import destinee.commun.constantes.ConstantesAttaques;

/**
 * @author Bubulle et No-one
 * 
 * Perso servira a stocker les caracteristiques de chaque personnage Un personnage est défini par son nolbre de dés d'attaque, son bonus d'attaque son nombre de
 * dés de dégats, son bonus de dégats, ainsi que le nombre de dés et le bonus de PM.
 * 
 * Il sera peut-etre nécessaire de rajouter a ces caracteristique la liste des attaques qu'il peut effectuer. Pour l'instant le scenario est généré par prolog
 * et on le suppose légal.
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
	private final String identifiant;
	private int fatigue;
	private int fatigueIntiale;
	private int sacrificePourAttaque, sacrificePourDegat, sacrificeMax;
	private int paCycle1, paCycle2;
	private Map<String, Double> maitrisesAtt;

	/**
	 * @param aNombreDeDesAttaque nombre de dés d'attaque
	 * @param aBonusAttaque bonus d'attaque
	 * @param aNombreDeDesDegats nombre de dés de dégâts
	 * @param aBonusDegats bonus de dégâts
	 * @param aNombreDeDesPM nombre de dés de puissance magique
	 * @param aBonusPM bonus de puissance magique
	 * @param aFatigueInitiale fatigue initiale du perso
	 * @param aIdentifiant identifiant du personnage
	 */
	public Perso(final int aNombreDeDesAttaque, final int aBonusAttaque, final int aNombreDeDesDegats, final int aBonusDegats, final int aNombreDeDesPM,
			final int aBonusPM, final int aFatigueInitiale, final String aIdentifiant, final Map<String, Double> aMaitrisesAtt)
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

		sacrificePourAttaque = 0;
		sacrificePourDegat = 0;
		sacrificeMax = 0;
		paCycle1 = 10;
		paCycle2 = 0;
	}

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
	public void setNombreDeDesAttaque(final int aNombreDeDesAttaque)
	{
		nombreDeDesAttaque = aNombreDeDesAttaque;
	}

	/**
	 * @param aBonusAttaque the bonusAttaque to set
	 */
	public void setBonusAttaque(final int aBonusAttaque)
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
	public void setNombreDeDesDegats(final int aNombreDeDesDegats)
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
	public void setBonusDegats(final int aBonusDegats)
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
	public void setNombreDeDesPM(final int aNombreDeDesPM)
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
	public void setBonusPM(final int aBonusPM)
	{
		bonusPM = aBonusPM;
	}

	/**
	 * @return the identifiant
	 */
	public final String getIdentifiant()
	{
		return identifiant;
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
	public void setFatigue(final int aFatigue)
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
	 * @return the sacrificePourAttaque
	 */
	public int getSacrificePourAttaque()
	{
		return sacrificePourAttaque;
	}

	/**
	 * @param aSacrificePourAttaque the sacrificePourAttaque to set
	 */
	public void setSacrificePourAttaque(final int aSacrificePourAttaque)
	{
		sacrificePourAttaque = aSacrificePourAttaque;
	}

	/**
	 * @return the sacrificePourDegat
	 */
	public int getSacrificePourDegat()
	{
		return sacrificePourDegat;
	}

	/**
	 * @param aSacrificePourDegat the sacrificePourDegat to set
	 */
	public void setSacrificePourDegat(final int aSacrificePourDegat)
	{
		sacrificePourDegat = aSacrificePourDegat;
	}

	/**
	 * @return the sacrificeMax
	 */
	public int getSacrificeMax()
	{
		return sacrificeMax;
	}

	/**
	 * @param aSacrificeMax the sacrificeMax to set
	 */
	public void setSacrificeMax(final int aSacrificeMax)
	{
		sacrificeMax = aSacrificeMax;
	}

	/**
	 * @return le nombre de PA au cycle 1
	 */
	public int getPaCycle1()
	{
		return paCycle1;
	}

	/**
	 * @param aPaCycle1 un nombre de PA pour le cycle 1
	 */
	public void setPaCycle1(final int aPaCycle1)
	{
		paCycle1 = aPaCycle1;
	}

	/**
	 * @return le nombre de PA au cycle 2
	 */
	public int getPaCycle2()
	{
		return paCycle2;
	}

	/**
	 * @param aPaCycle2 un nombre de PA pour le cycle 2
	 */
	public void setPaCycle2(final int aPaCycle2)
	{
		paCycle2 = aPaCycle2;
	}

	/**
	 * Retourne le pourcentage de maitrise de la compétence d'attaque du perso
	 * 
	 * @param aNomAttaque un identifiant d'attaque
	 * @return le pourcentage de maitrise de la compétence d'attaque du perso
	 */
	public double getMaitriseAttaque(final String aNomAttaque)
	{
		Double result = maitrisesAtt.get(aNomAttaque);
		if (result == null)
		{
			result = new Double(0.0);
		}
		return result.doubleValue();
	}

	/**
	 * Incrémente la fatigue du joueur en fonction du type d'attaque. Règle : incrément de fatigue = PA / 2
	 * 
	 * @param aAttaque une attaque
	 */
	public void incrementerFatigue(final Attaque aAttaque)
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
		Perso clone = new Perso(nombreDeDesAttaque, bonusAttaque, nombreDeDesDegats, bonusDegats, nombreDeDesPM, bonusPM, fatigueIntiale, identifiant,
			maitrisesAtt);
		clone.setSacrificePourAttaque(getSacrificePourAttaque());
		clone.setSacrificePourDegat(getSacrificePourDegat());
		clone.setSacrificeMax(getSacrificeMax());
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object aObj)
	{
		// Vérification de l'égalité des références
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

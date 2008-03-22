/**
 * 
 */
package destinee.commun.data;

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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aObj)
	{
		// V�rification de l'�galit� des r�f�rences
		if (this == aObj)
		{
			return true;
		}

		/*
		 * Deux attaques sont consid�r�es comme �gales si elles sont du m�me type et son effectu�es par le m�me perso
		 */
		if (aObj != null && aObj instanceof Attaque)
		{
			Attaque att = (Attaque) aObj;
			if (getTypeAttaque().equals(att.getTypeAttaque()) && getPerso().equals(att.getPerso()))
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
		int hashcode = 31;
		hashcode = hashcode * 211 + getTypeAttaque().hashCode();
		hashcode = hashcode * 211 + getPerso().hashCode();
		return hashcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPerso().toString() + "_" + getTypeAttaque();
	}
}

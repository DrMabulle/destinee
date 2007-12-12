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


	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aObj)
	{
		/*
		 * Deux attaques sont consid�r�es comme �gales si elles sont du m�me type et son effectu�es par le m�me perso
		 */
		if (aObj != null && aObj instanceof Attaque)
		{
			Attaque att = (Attaque) aObj;
			if (getTypeAttaque().equals(att.getTypeAttaque())
					&& getPerso().equals(att.getPerso()))
			{
				return true;
			}
		}
		return super.equals(aObj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return (getTypeAttaque().hashCode() * getPerso().hashCode()) + 1 ;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPerso().toString() + "_" + getTypeAttaque();
	}
}

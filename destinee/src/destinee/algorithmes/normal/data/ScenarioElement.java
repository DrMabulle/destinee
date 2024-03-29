/**
 * 
 */
package destinee.algorithmes.normal.data;

import destinee.commun.data.Attaque;
import destinee.commun.probas.ResolutionAttaque;

/**
 * @author Bubulle et No-one
 * 
 */
public class ScenarioElement
{

	/**
	 * L'attaque effectu�e
	 */
	private Attaque attaque;
	/**
	 * La r�solution de l'attaque
	 */
	private int typeResolution;

	/**
	 * @param aAttaque
	 * @param aTypeResolution
	 */
	public ScenarioElement(final Attaque aAttaque, final int aTypeResolution)
	{
		super();
		attaque = aAttaque;
		typeResolution = aTypeResolution;
	}

	/**
	 * @return the attaque
	 */
	public Attaque getAttaque()
	{
		return attaque;
	}

	/**
	 * @return the typeResolution
	 */
	public int getTypeResolution()
	{
		return typeResolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object aObj)
	{
		// V�rification de l'�galit� des r�f�rences
		if (this == aObj)
		{
			return true;
		}

		/*
		 * Deux ScenarioElements sont consid�r�s �gaux s'ils ont la m�me attaque et la m�me r�solution
		 */
		if (aObj != null && aObj instanceof ScenarioElement)
		{
			ScenarioElement scenarElt = (ScenarioElement) aObj;
			if (getAttaque() != null && getAttaque().equals(scenarElt.getAttaque()) && getTypeResolution() == scenarElt.getTypeResolution())
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
		int hashcode = 13;
		hashcode = 17 * hashcode + getTypeResolution();
		hashcode = 17 * hashcode + getAttaque().hashCode();
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
		StringBuilder result = new StringBuilder(32);
		result.append(getAttaque().toString()).append('_');

		if (getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_CRITIQUE)
		{
			result.append("CC");
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_SIMPLE)
		{
			result.append("CS");
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE)
		{
			result.append("ES");
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE)
		{
			result.append("EP");
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE)
		{
			result.append("Echec");
		}
		return result.toString();
	}
}

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
	public ScenarioElement(Attaque aAttaque, int aTypeResolution)
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
	public boolean equals(Object aObj)
	{
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
		return super.equals(aObj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return getTypeResolution() * getAttaque().hashCode() + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String result = getAttaque().toString() + "_";

		if (getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_CRITIQUE)
		{
			result += "CpCrit";
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_SIMPLE)
		{
			result += "CpSimp";
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE)
		{
			result += "EsqSimp";
		}
		else if (getTypeResolution() == ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE)
		{
			result += "EsqPft";
		}
		return result;
	}
}

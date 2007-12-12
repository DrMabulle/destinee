/**
 * 
 */
package destinee.algorithmes.normal.data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import destinee.core.utils.ConversionUtil;

/**
 * @author Bubulle et No-one
 * 
 */
public class ChaineAttaques
{
	private Set<String> scenarios = new HashSet<String>();
	private String identifiant;
	private double esperanceDegatCumulee = 0;
	private BigDecimal probaRealisationCumulee = new BigDecimal(0);

	/**
	 * Constructeur par d�faut
	 */
	public ChaineAttaques(String aIdentifiant)
	{
		super();
		identifiant = aIdentifiant;
	}

	/**
	 * M�thode permettant d'ajouter un sc�nario � la chaine d'attaque, et ainsi cumuler les esp�rances de d�g�ts
	 * 
	 * @param aScenario le sc�nario � ajouter
	 */
	public synchronized void ajouterScenario(Scenario aScenario)
	{
		// On n'ajoute qu'une seule fois chaque sc�nario
		if (!scenarios.contains(aScenario.toString()))
		{
			// Ajouter le sc�nario
			scenarios.add(aScenario.toString());

			// Cumuler l'esp�rance de d�g�ts pond�r�e par la proba de r�alisation
			esperanceDegatCumulee = esperanceDegatCumulee + 
				aScenario.getEsperanceDegats() * ConversionUtil.bigdecimalVersDouble(aScenario.getProbaRealisation(), 20);

			// Cumuler les probas de r�alisation de l'ensemble des sc�narios
			probaRealisationCumulee = probaRealisationCumulee.add(aScenario.getProbaRealisation());
		}
	}

	/**
	 * @return the esperanceDegatCumulee
	 */
	public double getEsperanceDegatCumulee()
	{
		return esperanceDegatCumulee;
	}

	/**
	 * @return the probaRealisationCumulee
	 */
	public BigDecimal getProbaRealisationCumulee()
	{
		return probaRealisationCumulee;
	}

	/**
	 * @return the identifiant
	 */
	public String getIdentifiant()
	{
		return identifiant;
	}

}

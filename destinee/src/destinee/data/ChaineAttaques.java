/**
 * 
 */
package destinee.data;

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
	private Set<Scenario> scenarios = new HashSet<Scenario>();
	private String identifiant;
	private double esperanceDegatCumulee = 0;
	private BigDecimal probaRealisationCumulee = new BigDecimal(0);
	
	/**
	 * Constructeur par défaut
	 */
	public ChaineAttaques(String aIdentifiant)
	{
		super();
		identifiant = aIdentifiant;
	}

	/**
	 * Méthode permettant d'ajouter un scénario à la chaine d'attaque, et ainsi cumuler les espérances de dégâts
	 * @param aScenario le scénario à ajouter
	 */
	public void ajouterScenario(Scenario aScenario) 
	{
		// On n'ajoute qu'une seule fois chaque scénario
		if (!scenarios.contains(aScenario)) 
		{
			// Ajouter le scénario
			scenarios.add(aScenario);
			
			// Cumuler l'espérance de dégâts pondérée par la proba de réalisation
			esperanceDegatCumulee = esperanceDegatCumulee * ConversionUtil.bigdecimalVersDouble(probaRealisationCumulee, 10)
				+ aScenario.getEsperanceDegats() * ConversionUtil.bigdecimalVersDouble(aScenario.getProbaRealisation(), 10);
			
			// Cumuler les probas de réalisation de l'ensemble des scénarios
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

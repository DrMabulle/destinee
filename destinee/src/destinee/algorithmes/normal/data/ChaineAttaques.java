/**
 * 
 */
package destinee.algorithmes.normal.data;

import java.math.BigDecimal;

import destinee.core.exception.TechnicalException;
import destinee.core.utils.ConversionUtil;

/**
 * @author Bubulle et No-one
 * 
 */
public class ChaineAttaques
{
	// private Set<String> scenarios = new HashSet<String>();
	private String identifiant;
	private double esperanceDegatCumulee = 0;
	private BigDecimal probaRealisationCumulee = BigDecimal.ZERO;

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
	 * 
	 * @param aScenario le scénario à ajouter
	 * @throws TechnicalException e
	 */
	public synchronized void ajouterScenario(Scenario aScenario) throws TechnicalException
	{
		// On n'ajoute qu'une seule fois chaque scénario
		// if (!scenarios.contains(aScenario.toString()))
		// {
		// Ajouter le scénario
		// scenarios.add(aScenario.toString());

		// Cumuler l'espérance de dégâts pondérée par la proba de réalisation
		esperanceDegatCumulee += aScenario.getEsperanceDegats() * ConversionUtil.bigdecimalVersDouble(aScenario.getProbaRealisation(), 20);

		// Cumuler les probas de réalisation de l'ensemble des scénarios
		probaRealisationCumulee = probaRealisationCumulee.add(aScenario.getProbaRealisation());
		// }
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

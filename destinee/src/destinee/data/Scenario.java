/**
 * 
 */
package destinee.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import destinee.probas.ResolutionAttaque;

/**
 * @author bkessler
 *
 */
public class Scenario
{

	/**
	 * Esp�rance de d�g�ts
	 */
	private double esperanceDegats;
	/**
	 * Probabilit� de r�alisation
	 */
	private BigDecimal probaRealisation = null;
	
	/**
	 * Liste des �l�ments sc�naristiques
	 */
	private List<ScenarioElement> listeElements;
	
	/**
	 * La cible des attaques
	 */
	private Cible cible;
	
	public Scenario(Cible aCible) {
		
		super();
		listeElements = new ArrayList<ScenarioElement>();
		cible = aCible;
	}
	
	/**
	 * M�thode permettant de connaitre l'esp�rance de d�g�ts de ce sc�nario
	 * @return l'esp�rance de d�g�ts de ce sc�nario
	 */
	public double getEsperanceDegats() {
		
		// Si le sc�nario n'a pas encore �t� �valuer, le faire
		if (probaRealisation == null) {
			evalerEvenement();
		}
		
		return esperanceDegats;
	}
	
	/**
	 * M�thode permettant de connaitre la probabilit� de r�alisation de ce sc�nario
	 * @return probabilit� de r�alisation de ce sc�nario
	 */
	public BigDecimal getProbaRealisation() {

		// Si le sc�nario n'a pas encore �t� �valuer, le faire
		if (probaRealisation == null) {
			evalerEvenement();
		}
		
		return probaRealisation;
	}
	
	/**
	 * M�thode permettant d'�valuer le sc�nario, en termes d'esp�rance de d�g�ts
	 * et de probabilit� de r�alisation
	 */
	private void evalerEvenement() {
		
		probaRealisation = new BigDecimal(1);
		esperanceDegats = 0;
		
		BigDecimal probaTmp = null;
		double esperanceTmp = 0;
		
		double malusDesDef = 0.5;
		
		for (ScenarioElement scenarioElemt : listeElements)
		{
			// TODO
			probaTmp = ResolutionAttaque.resoudreAttaque(scenarioElemt.getAttaque(), cible, scenarioElemt.getTypeResolution());
			esperanceTmp = ResolutionAttaque.esperanceDeDegats(scenarioElemt.getAttaque(), cible, scenarioElemt.getTypeResolution());
			
			// Multiplier la proba de r�alisation globale par la proba de r�alisation de l'�l�ment sc�naristique
			probaRealisation = probaRealisation.multiply(probaTmp);
			
			// Additionner l'esp�rance de d�g�ts de l'�l�ment sc�naristique � l'esp�rance de d�g�ts cumul�e
			esperanceDegats += esperanceTmp;
			
			// Incr�menter le compteur des malus de d�fense
			if (scenarioElemt.getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_CRITIQUE) {
				
				malusDesDef += 1;
				
			} else if (scenarioElemt.getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_SIMPLE
					|| scenarioElemt.getTypeResolution() == ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE) {
				
				malusDesDef += 0.5;
			}
		}
	}
}

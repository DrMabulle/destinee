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
	 * Espérance de dégâts
	 */
	private double esperanceDegats;
	/**
	 * Probabilité de réalisation
	 */
	private BigDecimal probaRealisation = null;
	
	/**
	 * Liste des éléments scénaristiques
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
	 * Méthode permettant de connaitre l'espérance de dégâts de ce scénario
	 * @return l'espérance de dégâts de ce scénario
	 */
	public double getEsperanceDegats() {
		
		// Si le scénario n'a pas encore été évaluer, le faire
		if (probaRealisation == null) {
			evalerEvenement();
		}
		
		return esperanceDegats;
	}
	
	/**
	 * Méthode permettant de connaitre la probabilité de réalisation de ce scénario
	 * @return probabilité de réalisation de ce scénario
	 */
	public BigDecimal getProbaRealisation() {

		// Si le scénario n'a pas encore été évaluer, le faire
		if (probaRealisation == null) {
			evalerEvenement();
		}
		
		return probaRealisation;
	}
	
	/**
	 * Méthode permettant d'évaluer le scénario, en termes d'espérance de dégâts
	 * et de probabilité de réalisation
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
			
			// Multiplier la proba de réalisation globale par la proba de réalisation de l'élément scénaristique
			probaRealisation = probaRealisation.multiply(probaTmp);
			
			// Additionner l'espérance de dégâts de l'élément scénaristique à l'espérance de dégâts cumulée
			esperanceDegats += esperanceTmp;
			
			// Incrémenter le compteur des malus de défense
			if (scenarioElemt.getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_CRITIQUE) {
				
				malusDesDef += 1;
				
			} else if (scenarioElemt.getTypeResolution() == ResolutionAttaque.RESOLUTION_COUP_SIMPLE
					|| scenarioElemt.getTypeResolution() == ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE) {
				
				malusDesDef += 0.5;
			}
		}
	}
}

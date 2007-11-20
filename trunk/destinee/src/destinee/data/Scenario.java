/**
 * 
 */
package destinee.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import destinee.probas.ResolutionAttaque;
import destinee.utils.CachePersos;

/**
 * @author Bubulle et No-one
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
	@SuppressWarnings("unchecked")
	private void evalerEvenement() {
		
		probaRealisation = new BigDecimal(1);
		esperanceDegats = 0;
		
		// Réinitialiser la fatigue et les malus de la cible et des persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefence();
		for (Iterator iterator = CachePersos.getInstance().getEnsemblePersos().iterator(); iterator.hasNext();)
		{
			Perso perso = (Perso) iterator.next();
			perso.reinitialiserFatigue();
		}
		
		BigDecimal probaTmp = null;
		double esperanceTmp = 0;
		
		for (ScenarioElement scenarioElemt : listeElements)
		{
			probaTmp = ResolutionAttaque.resoudreAttaque(scenarioElemt.getAttaque(), cible, scenarioElemt.getTypeResolution());
			esperanceTmp = ResolutionAttaque.esperanceDeDegats(scenarioElemt.getAttaque(), cible, scenarioElemt.getTypeResolution());
			
			// Multiplier la proba de réalisation globale par la proba de réalisation de l'élément scénaristique
			probaRealisation = probaRealisation.multiply(probaTmp);
			
			// Additionner l'espérance de dégâts de l'élément scénaristique à l'espérance de dégâts cumulée
			esperanceDegats += esperanceTmp;
			
			// Incrémenter les malus de la cible
			cible.incrementerFatigue();
			cible.incrementerMalusDefence(scenarioElemt.getAttaque(), scenarioElemt.getTypeResolution());
			
			// Incrémenter la fatigue du perso
			scenarioElemt.getAttaque().getPerso().incrementerFatigue(scenarioElemt.getAttaque());
		}	
		//TODO une methode pour récuperer la fatigue si on gere les cumuls, formule sur le wiki
		//TODO une gestion de la charge : attaque identique a l'attaque normale mais générant un point de fatigue en plus
	}
	
	/**
	 * Méthode permettant de récupérer l'identifiant de la chaine d'attaques utilisée dans ce scénario
	 * @return l'identifiant de la chaine d'attaques utilisée dans ce scénario
	 */
	public String getIdentifiantChaineAttaques() 
	{
		StringBuffer sb = new StringBuffer("");
		
		for (ScenarioElement scenarioElemt : listeElements)
		{
			sb.append(scenarioElemt.getAttaque().getPerso().getIdentifiant());
			sb.append(scenarioElemt.getAttaque().getTypeAttaque());
			sb.append("-");
		}
		
		return sb.toString();
	}
	
	/**
	 * Méthode permettant d'ajouter un élément scénaristique au scénario
	 * @param aScenarioElmt le ScenarioElement à ajouter
	 */
	public void ajouterElement(ScenarioElement aScenarioElmt)
	{
		listeElements.add(aScenarioElmt);
		// réinitialiser les probas et espérances
		probaRealisation = null;
		esperanceDegats = 0;
	}
}

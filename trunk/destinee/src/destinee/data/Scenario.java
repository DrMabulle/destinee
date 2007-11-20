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
	@SuppressWarnings("unchecked")
	private void evalerEvenement() {
		
		probaRealisation = new BigDecimal(1);
		esperanceDegats = 0;
		
		// R�initialiser la fatigue et les malus de la cible et des persos
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
			
			// Multiplier la proba de r�alisation globale par la proba de r�alisation de l'�l�ment sc�naristique
			probaRealisation = probaRealisation.multiply(probaTmp);
			
			// Additionner l'esp�rance de d�g�ts de l'�l�ment sc�naristique � l'esp�rance de d�g�ts cumul�e
			esperanceDegats += esperanceTmp;
			
			// Incr�menter les malus de la cible
			cible.incrementerFatigue();
			cible.incrementerMalusDefence(scenarioElemt.getAttaque(), scenarioElemt.getTypeResolution());
			
			// Incr�menter la fatigue du perso
			scenarioElemt.getAttaque().getPerso().incrementerFatigue(scenarioElemt.getAttaque());
		}	
		//TODO une methode pour r�cuperer la fatigue si on gere les cumuls, formule sur le wiki
		//TODO une gestion de la charge : attaque identique a l'attaque normale mais g�n�rant un point de fatigue en plus
	}
	
	/**
	 * M�thode permettant de r�cup�rer l'identifiant de la chaine d'attaques utilis�e dans ce sc�nario
	 * @return l'identifiant de la chaine d'attaques utilis�e dans ce sc�nario
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
	 * M�thode permettant d'ajouter un �l�ment sc�naristique au sc�nario
	 * @param aScenarioElmt le ScenarioElement � ajouter
	 */
	public void ajouterElement(ScenarioElement aScenarioElmt)
	{
		listeElements.add(aScenarioElmt);
		// r�initialiser les probas et esp�rances
		probaRealisation = null;
		esperanceDegats = 0;
	}
}

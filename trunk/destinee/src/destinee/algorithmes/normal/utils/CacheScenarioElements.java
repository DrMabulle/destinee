/**
 * 
 */
package destinee.algorithmes.normal.utils;

import java.util.Hashtable;
import java.util.Map;

import destinee.algorithmes.normal.data.ScenarioElement;
import destinee.commun.data.Attaque;

/**
 * @author Bubulle et No-one Cache de gestion des Persos utilisés
 */
public class CacheScenarioElements
{
	private static CacheScenarioElements instance = new CacheScenarioElements();

	private Map<String, ScenarioElement> scenarioElements;

	/**
	 * Constructeur par défaut
	 */
	private CacheScenarioElements()
	{
		super();
		scenarioElements = new Hashtable<String, ScenarioElement>(1000, 0.8f);
	}

	/**
	 * Méthode permettant de récupérer l'instance par défaut du Cache
	 * 
	 * @return l'instance par défaut
	 */
	public static CacheScenarioElements getInstance()
	{
		return instance;
	}

	/**
	 * Méthode permettant de récupérer le ScenarioElement répondant à l'identifiant passé en paramètre
	 * 
	 * @param aIdentifiant une chaine de caractère identifiant de manière unique un ScenarioElement
	 * @return le ScenarioElement répondant à l'identifiant passé en paramètre
	 */
	public ScenarioElement getScenarioElement(String aIdentifiant)
	{
		return scenarioElements.get(aIdentifiant);
	}

	/**
	 * Méthode permettant d'ajouter un ScenarioElement au cache
	 * 
	 * @param aIdentifiant un identifiant de perso
	 * @param aScenarioElement une attaque
	 */
	public void addScenarioElement(String aIdentifiant, ScenarioElement aScenarioElement)
	{
		scenarioElements.put(aIdentifiant, aScenarioElement);
	}

	/**
	 * Méthode permettant de générer un identifiant unique pour un ScenarioElement, en fonction de son attaque et du type de résolution
	 * 
	 * @param aAttaquant un attaquant
	 * @param aTypeAttaque un type d'attaque
	 * @return l'identifiant unique de cette attaque
	 */
	public static String getIdentifiantScenarioElement(Attaque aAttaque, int aTypeResolution)
	{
		return "ScenarioElement(" + aAttaque.toString() + "," + aTypeResolution + ")";
	}
}

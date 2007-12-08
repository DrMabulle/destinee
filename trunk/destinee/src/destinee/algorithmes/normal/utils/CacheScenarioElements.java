/**
 * 
 */
package destinee.algorithmes.normal.utils;

import java.util.Hashtable;
import java.util.Map;

import destinee.algorithmes.normal.data.ScenarioElement;
import destinee.commun.data.Attaque;

/**
 * @author Bubulle et No-one Cache de gestion des Persos utilis�s
 */
public class CacheScenarioElements
{
	private static CacheScenarioElements instance = new CacheScenarioElements();

	private Map<String, ScenarioElement> scenarioElements;

	/**
	 * Constructeur par d�faut
	 */
	private CacheScenarioElements()
	{
		super();
		scenarioElements = new Hashtable<String, ScenarioElement>(1000, 0.8f);
	}

	/**
	 * M�thode permettant de r�cup�rer l'instance par d�faut du Cache
	 * 
	 * @return l'instance par d�faut
	 */
	public static CacheScenarioElements getInstance()
	{
		return instance;
	}

	/**
	 * M�thode permettant de r�cup�rer le ScenarioElement r�pondant � l'identifiant pass� en param�tre
	 * 
	 * @param aIdentifiant une chaine de caract�re identifiant de mani�re unique un ScenarioElement
	 * @return le ScenarioElement r�pondant � l'identifiant pass� en param�tre
	 */
	public ScenarioElement getScenarioElement(String aIdentifiant)
	{
		return scenarioElements.get(aIdentifiant);
	}

	/**
	 * M�thode permettant d'ajouter un ScenarioElement au cache
	 * 
	 * @param aIdentifiant un identifiant de perso
	 * @param aScenarioElement une attaque
	 */
	public void addScenarioElement(String aIdentifiant, ScenarioElement aScenarioElement)
	{
		scenarioElements.put(aIdentifiant, aScenarioElement);
	}

	/**
	 * M�thode permettant de g�n�rer un identifiant unique pour un ScenarioElement, en fonction de son attaque et du type de r�solution
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

/**
 * 
 */
package destinee.commun.utils;

import java.util.Hashtable;
import java.util.Map;

import destinee.commun.data.Attaque;
import destinee.commun.data.Perso;

/**
 * @author Bubulle et No-one Cache de gestion des Persos utilisés
 */
public class CacheAttaques
{
	private static CacheAttaques instance = new CacheAttaques();

	private Map<String, Attaque> attaques;

	/**
	 * Constructeur par défaut
	 */
	private CacheAttaques()
	{
		super();
		attaques = new Hashtable<String, Attaque>(200, 0.8f);
	}

	/**
	 * Méthode permettant de récupérer l'instance par défaut du Cache
	 * 
	 * @return l'instance par défaut
	 */
	public static CacheAttaques getInstance()
	{
		return instance;
	}

	/**
	 * Méthode permettant de récupérer l'attaque répondant à l'identifiant passé en paramètre
	 * 
	 * @param aIdentifiant une chaine de caractère identifiant de manière unique une attaque
	 * @return l'attaque répondant à l'identifiant passé en paramètre
	 */
	public Attaque getAttaque(String aIdentifiant)
	{
		return attaques.get(aIdentifiant);
	}

	/**
	 * Méthode permettant d'ajouter une attaque au cache
	 * 
	 * @param aIdentifiant un identifiant de perso
	 * @param aAttaque une attaque
	 */
	public void addAttaque(String aIdentifiant, Attaque aAttaque)
	{
		attaques.put(aIdentifiant, aAttaque);
	}

	/**
	 * Méthode permettant de générer un identifiant unique pour une attaque, en fonction de son type et de l'attaquant
	 * 
	 * @param aAttaquant un attaquant
	 * @param aTypeAttaque un type d'attaque
	 * @return l'identifiant unique de cette attaque
	 */
	public static String getIdentifiantAttaque(Perso aAttaquant, String aTypeAttaque)
	{
		return "Attaque(" + aAttaquant.getIdentifiant() + "," + aTypeAttaque + ")";
	}
}

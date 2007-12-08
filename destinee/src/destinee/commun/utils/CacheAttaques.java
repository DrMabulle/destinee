/**
 * 
 */
package destinee.commun.utils;

import java.util.Hashtable;
import java.util.Map;

import destinee.commun.data.Attaque;
import destinee.commun.data.Perso;

/**
 * @author Bubulle et No-one Cache de gestion des Persos utilis�s
 */
public class CacheAttaques
{
	private static CacheAttaques instance = new CacheAttaques();

	private Map<String, Attaque> attaques;

	/**
	 * Constructeur par d�faut
	 */
	private CacheAttaques()
	{
		super();
		attaques = new Hashtable<String, Attaque>(200, 0.8f);
	}

	/**
	 * M�thode permettant de r�cup�rer l'instance par d�faut du Cache
	 * 
	 * @return l'instance par d�faut
	 */
	public static CacheAttaques getInstance()
	{
		return instance;
	}

	/**
	 * M�thode permettant de r�cup�rer l'attaque r�pondant � l'identifiant pass� en param�tre
	 * 
	 * @param aIdentifiant une chaine de caract�re identifiant de mani�re unique une attaque
	 * @return l'attaque r�pondant � l'identifiant pass� en param�tre
	 */
	public Attaque getAttaque(String aIdentifiant)
	{
		return attaques.get(aIdentifiant);
	}

	/**
	 * M�thode permettant d'ajouter une attaque au cache
	 * 
	 * @param aIdentifiant un identifiant de perso
	 * @param aAttaque une attaque
	 */
	public void addAttaque(String aIdentifiant, Attaque aAttaque)
	{
		attaques.put(aIdentifiant, aAttaque);
	}

	/**
	 * M�thode permettant de g�n�rer un identifiant unique pour une attaque, en fonction de son type et de l'attaquant
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

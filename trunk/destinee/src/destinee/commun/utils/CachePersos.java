/**
 * 
 */
package destinee.commun.utils;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import destinee.commun.data.Perso;

/**
 * @author Bubulle et No-one Cache de gestion des Persos utilis�s
 */
public class CachePersos
{
	private static CachePersos instance = new CachePersos();

	private Map<String, Perso> persosInitiaux;
	private Map<String, Perso> persos;

	/**
	 * Constructeur par d�faut
	 */
	private CachePersos()
	{
		super();
		persosInitiaux = new Hashtable<String, Perso>();
		persos = new Hashtable<String, Perso>();
	}

	/**
	 * M�thode permettant de r�cup�rer l'instance par d�faut du Cache
	 * 
	 * @return l'instance par d�faut
	 */
	public static CachePersos getInstance()
	{
		return instance;
	}

	/**
	 * M�thode permettant de r�cup�rer le Perso r�pondant � l'identifiant pass� en param�tre
	 * 
	 * @param aIdentifiant un identifiant de Perso
	 * @return le Perso r�pondant � l'identifiant pass� en param�tre
	 */
	public Perso getPerso(String aIdentifiant)
	{
		return persos.get(aIdentifiant);
	}

	/**
	 * M�thode permettant d'ajouter un perso au cache
	 * 
	 * @param aIdentifiant un identifiant de perso
	 * @param aPerso un perso
	 */
	public void addPerso(String aIdentifiant, Perso aPerso)
	{
		persosInitiaux.put(aIdentifiant, aPerso);
		persos.put(aIdentifiant, aPerso.clone());
	}

	/**
	 * M�thode permettant de r�cuperer l'ensemble des persos
	 * 
	 * @return l'ensemble des Perso
	 */
	public Set<Perso> getEnsemblePersos()
	{
		return new HashSet<Perso>(persos.values());
	}

	/**
	 * Permet d'obtenir de nouvelles instances des objets Perso, afin de permettre une utilisation concurrente des objets
	 */
	public void getNouvellesInstances()
	{
		persos = new Hashtable<String, Perso>();

		for (Map.Entry<String, Perso> entry : persosInitiaux.entrySet())
		{
			persos.put(entry.getKey(), entry.getValue().clone());
		}
	}
}

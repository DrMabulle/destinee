/**
 * 
 */
package destinee.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import destinee.data.Perso;

/**
 * @author Bubulle et No-one
 * Cache de gestion des Persos utilis�s
 */
public class CachePersos
{
	private static CachePersos instance = new CachePersos();
	
	private Map<String, Perso> persos;

	/**
	 * Constructeur par d�faut
	 */
	private CachePersos()
	{
		super();
		persos = new HashMap<String, Perso>();
	}
	
	/**
	 * M�thode permettant de r�cup�rer l'instance par d�faut du Cache
	 * @return l'instance par d�faut
	 */
	public static CachePersos getInstance() 
	{
		return instance;
	}
	
	/**
	 * M�thode permettant de r�cup�rer le Perso r�pondant � l'identifiant pass� en param�tre
	 * @param aIdentifiant un identifiant de Perso
	 * @return le Perso r�pondant � l'identifiant pass� en param�tre
	 */
	public Perso getPerso(String aIdentifiant)
	{
		return persos.get(aIdentifiant);
	}
	
	/**
	 * M�thode permettant d'ajouter un perso au cache
	 * @param aIdentifiant un identifiant de perso
	 * @param aPerso un perso
	 */
	public void addPerso(String aIdentifiant, Perso aPerso)
	{
		persos.put(aIdentifiant, aPerso);
	}
	
	/**
	 * M�thode permettant de r�cuperer l'ensemble des persos 
	 * @return
	 */
	public Set<Perso> getEnsemblePersos()
	{
		return new HashSet<Perso>(persos.values());
	}
}

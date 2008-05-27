/**
 * 
 */
package destinee.commun.utils;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import destinee.commun.data.Cible;
import destinee.commun.data.Perso;

/**
 * @author Bubulle et No-one Cache de gestion des Persos utilis�s
 */
public final class CachePersos
{
	private static final Map<String, Perso> persosInitiaux = new Hashtable<String, Perso>();
	private static final Map<String, Perso> persos = new Hashtable<String, Perso>();
	private static Cible cible;

	/**
	 * M�thode permettant de r�cup�rer le Perso r�pondant � l'identifiant pass� en param�tre
	 * 
	 * @param aIdentifiant un identifiant de Perso
	 * @return le Perso r�pondant � l'identifiant pass� en param�tre
	 */
	public static final Perso getPerso(final String aIdentifiant)
	{
		return persos.get(aIdentifiant);
	}

	/**
	 * M�thode permettant d'ajouter un perso au cache
	 * 
	 * @param aIdentifiant un identifiant de perso
	 * @param aPerso un perso
	 */
	public static final void addPerso(final String aIdentifiant, final Perso aPerso)
	{
		persosInitiaux.put(aIdentifiant, aPerso);
		persos.put(aIdentifiant, aPerso.clone());
	}

	/**
	 * M�thode permettant de r�cuperer l'ensemble des persos
	 * 
	 * @return l'ensemble des Perso
	 */
	public static final Set<Perso> getEnsemblePersos()
	{
		return new HashSet<Perso>(persos.values());
	}

	/**
	 * Permet d'obtenir de nouvelles instances des objets Perso, afin de permettre une utilisation concurrente des objets
	 */
	public static final void getNouvellesInstances()
	{
		persos.clear();

		for (Map.Entry<String, Perso> entry : persosInitiaux.entrySet())
		{
			persos.put(entry.getKey(), entry.getValue().clone());
		}
	}

	/**
	 * M�thode permettant de connaitre le nombre de persos en cache
	 * 
	 * @return le nombre de persos en cache
	 */
	public static final int getNombrePersos()
	{
		return persos.size();
	}

	/**
	 * Permet de r�cup�rer la cible
	 * 
	 * @return the cible
	 */
	public static Cible getCible()
	{
		return cible;
	}

	/**
	 * Renseigne la cible
	 * 
	 * @param aCible the cible to set
	 */
	public static void setCible(final Cible aCible)
	{
		cible = aCible;
	}
}

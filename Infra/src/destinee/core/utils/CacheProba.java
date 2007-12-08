/**
 * 
 */
package destinee.core.utils;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Bubulle et No-one Cache utilis� pour les calculs de Proba, ProbaPlus et ProbaMoins
 */
public class CacheProba
{

	/**
	 * Contenu du cache
	 */
	private Map<String, BigDecimal> cache;
	/**
	 * Instance par d�faut
	 */
	private static CacheProba defaultInstance = null;

	/**
	 * constructeur par defaut
	 */
	private CacheProba()
	{
		cache = new Hashtable<String, BigDecimal>(20000, 0.8F);
	}

	/**
	 * M�thode de r�cup�ration de l'instance par d�faut
	 * 
	 * @return l'instance par d�faut
	 */
	public static CacheProba getDefaultInstance()
	{
		if (defaultInstance == null)
		{
			defaultInstance = new CacheProba();
		}
		return defaultInstance;
	}

	/**
	 * Permet de stocker une donn�e dans le cache
	 * 
	 * @param cle cl� unique permettant de placer la donn�e
	 * @param valeur valeur � stocker
	 */
	public void stockerDonnees(String cle, BigDecimal valeur)
	{
		cache.put(cle, valeur);
		// System.out.println("" + cache.size() + " donn�es dans le cache.");
	}

	/**
	 * Permet d'acc�der � une donn�e stock�e dans le cache
	 * 
	 * @param cle la cl� d'entr�e du cache
	 * @return la valeur associ�e � la cl� (eventuellement null)
	 */
	public BigDecimal recupererDonnees(Object cle)
	{
		return cache.get(cle);
	}

	/**
	 * M�thode permettant de connaitre le nombre de donn�es stock�es en cache actuellement
	 * 
	 * @return la taille actuelle du cache
	 */
	public int size()
	{
		return cache.size();
	}

	/**
	 * M�thode permettant de vider toutes les donn�es stock�es dans le cache
	 */
	public void viderCache()
	{
		cache.clear();
	}
}

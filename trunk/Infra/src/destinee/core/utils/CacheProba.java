/**
 * 
 */
package destinee.core.utils;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Bubulle et No-one Cache utilisé pour les calculs de Proba, ProbaPlus et ProbaMoins
 */
public class CacheProba
{

	/**
	 * Contenu du cache
	 */
	private Map<String, BigDecimal> cache;
	/**
	 * Instance par défaut
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
	 * Méthode de récupération de l'instance par défaut
	 * 
	 * @return l'instance par défaut
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
	 * Permet de stocker une donnée dans le cache
	 * 
	 * @param cle clé unique permettant de placer la donnée
	 * @param valeur valeur à stocker
	 */
	public void stockerDonnees(String cle, BigDecimal valeur)
	{
		cache.put(cle, valeur);
		// System.out.println("" + cache.size() + " données dans le cache.");
	}

	/**
	 * Permet d'accéder à une donnée stockée dans le cache
	 * 
	 * @param cle la clé d'entrée du cache
	 * @return la valeur associée à la clé (eventuellement null)
	 */
	public BigDecimal recupererDonnees(Object cle)
	{
		return cache.get(cle);
	}

	/**
	 * Méthode permettant de connaitre le nombre de données stockées en cache actuellement
	 * 
	 * @return la taille actuelle du cache
	 */
	public int size()
	{
		return cache.size();
	}

	/**
	 * Méthode permettant de vider toutes les données stockées dans le cache
	 */
	public void viderCache()
	{
		cache.clear();
	}
}

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
public final class CacheProba
{

	/**
	 * Contenu du cache
	 */
	private static final Map<String, BigDecimal> cache = new Hashtable<String, BigDecimal>(20000, 0.8F);

	/**
	 * Crée une nouvelle instance de CacheProba
	 */
	private CacheProba()
	{
		super();
	}

	/**
	 * Permet de stocker une donnée dans le cache
	 * 
	 * @param cle clé unique permettant de placer la donnée
	 * @param valeur valeur à stocker
	 */
	public static final void stockerDonnees(final String cle, final BigDecimal valeur)
	{
		cache.put(cle, valeur);
	}

	/**
	 * Permet d'accéder à une donnée stockée dans le cache
	 * 
	 * @param cle la clé d'entrée du cache
	 * @return la valeur associée à la clé (eventuellement null)
	 */
	public static final BigDecimal recupererDonnees(final Object cle)
	{
		return cache.get(cle);
	}

	/**
	 * Méthode permettant de connaitre le nombre de données stockées en cache actuellement
	 * 
	 * @return la taille actuelle du cache
	 */
	public static final int size()
	{
		return cache.size();
	}

	/**
	 * Méthode permettant de vider toutes les données stockées dans le cache
	 */
	public static final void viderCache()
	{
		cache.clear();
	}
}

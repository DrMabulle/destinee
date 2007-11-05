/**
 * 
 */
package destinee.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AMOROS cache
 */
public class CacheProba
{

	/**
	 * Contenu du cache
	 */
	private Map<String, BigDecimal> maMap;
	/**
	 * Instance par défaut
	 */
	private static CacheProba defaultInstance = null;

	/**
	 * constructeur par defaut
	 */
	private CacheProba()
	{
		maMap = new HashMap<String, BigDecimal>(20000, 0.8F);
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
		maMap.put(cle, valeur);
		System.out.println("stockage de " + cle);
	}

	/**
	 * Permet d'accéder à une donnée stockée dans le cache
	 * 
	 * @param cle la clé d'entrée du cache
	 * @return la valeur associée à la clé (eventuellement null)
	 */
	public BigDecimal recupererDonnees(Object cle)
	{
		BigDecimal result = maMap.get(cle);
		if (result == null)
		{
			System.out.println("pas de valeur stockée pour la clé " + cle);
		}
		else
		{
			System.out.println("la valeur stockée pour la clé " + cle + " est " + result);
		}

		return result;
	}
}

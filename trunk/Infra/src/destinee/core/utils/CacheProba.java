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
	private static Map<String, BigDecimal> cache = new Hashtable<String, BigDecimal>(20000, 0.8F);

	/**
	 * Permet de stocker une donn�e dans le cache
	 * 
	 * @param cle cl� unique permettant de placer la donn�e
	 * @param valeur valeur � stocker
	 */
	public static void stockerDonnees(String cle, BigDecimal valeur)
	{
		cache.put(cle, valeur);
	}

	/**
	 * Permet d'acc�der � une donn�e stock�e dans le cache
	 * 
	 * @param cle la cl� d'entr�e du cache
	 * @return la valeur associ�e � la cl� (eventuellement null)
	 */
	public static BigDecimal recupererDonnees(Object cle)
	{
		return cache.get(cle);
	}

	/**
	 * M�thode permettant de connaitre le nombre de donn�es stock�es en cache actuellement
	 * 
	 * @return la taille actuelle du cache
	 */
	public static int size()
	{
		return cache.size();
	}

	/**
	 * M�thode permettant de vider toutes les donn�es stock�es dans le cache
	 */
	public static void viderCache()
	{
		cache.clear();
	}
}

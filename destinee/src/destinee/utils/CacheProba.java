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
	 * Instance par d�faut
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
		maMap.put(cle, valeur);
		System.out.println("stockage de " + cle);
	}

	/**
	 * Permet d'acc�der � une donn�e stock�e dans le cache
	 * 
	 * @param cle la cl� d'entr�e du cache
	 * @return la valeur associ�e � la cl� (eventuellement null)
	 */
	public BigDecimal recupererDonnees(Object cle)
	{
		BigDecimal result = maMap.get(cle);
		if (result == null)
		{
			System.out.println("pas de valeur stock�e pour la cl� " + cle);
		}
		else
		{
			System.out.println("la valeur stock�e pour la cl� " + cle + " est " + result);
		}

		return result;
	}
}

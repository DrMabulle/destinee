/**
 * 
 */
package destinee.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AMOROS
 * cache
 */
public class Cache {

	private Map maMap;
	private static Cache defaultInstance = null;
	/**
	 * constructeur par defaut
	 */
	private Cache() {
		maMap = new HashMap(20000,0.8F);
	}

		
	/**
	 * @return récuperation de l'instance par défaut
	 */
	public static Cache getDefaultInstance() {
		if (defaultInstance == null) {
			defaultInstance = new Cache();
		}
		return defaultInstance;
	}
	/**permet de stocker une donnée dans le cache
	 * @param cle clé unique permettant de placer la donnée
	 * @param valeur valeur a stocker
	 */
	public void rangerDonnee(Object cle, Object valeur) {
		maMap.put(cle, valeur);
		System.out.println("stockage de " + cle);
	}
	
	/**
	 * permet d'aceder a une donnée stockée dans le cache
	 * @param cle : la cle d'entrée du cache
	 * @return la valeur associée a la clé (eventuellement null)
	 */
	public Object sortirDonnee(Object cle)
	{
		Object result = maMap.get(cle);
		if(result == null) 
		{
			System.out.println("pas de valeur stockée pour la clé " + cle);
		}
		else
		{
			System.out.println("la valeur stockée pour la clé " + cle + " est "+ result);
		}
		
		
		return result;
	}
}

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
	 * @return r�cuperation de l'instance par d�faut
	 */
	public static Cache getDefaultInstance() {
		if (defaultInstance == null) {
			defaultInstance = new Cache();
		}
		return defaultInstance;
	}
	/**permet de stocker une donn�e dans le cache
	 * @param cle cl� unique permettant de placer la donn�e
	 * @param valeur valeur a stocker
	 */
	public void rangerDonnee(Object cle, Object valeur) {
		maMap.put(cle, valeur);
		System.out.println("stockage de " + cle);
	}
	
	/**
	 * permet d'aceder a une donn�e stock�e dans le cache
	 * @param cle : la cle d'entr�e du cache
	 * @return la valeur associ�e a la cl� (eventuellement null)
	 */
	public Object sortirDonnee(Object cle)
	{
		Object result = maMap.get(cle);
		if(result == null) 
		{
			System.out.println("pas de valeur stock�e pour la cl� " + cle);
		}
		else
		{
			System.out.println("la valeur stock�e pour la cl� " + cle + " est "+ result);
		}
		
		
		return result;
	}
}

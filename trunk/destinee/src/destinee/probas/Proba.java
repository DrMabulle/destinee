/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;

import destinee.utils.Cache;

/**
 * @author AMOROS
 *
 *permet de déterminer la probabilité de tomber sur une nombre A en jetant B dés a dix faces
 */
public class Proba {
	
	
	public static BigDecimal calculerProba(int resultatCible, int nbDes) 
	{
		BigDecimal result = new BigDecimal (0);
		if(resultatCible < nbDes 					//résultat inférieur au nombre de dés
				|| nbDes < 1 						// pas de dés lancés
				|| resultatCible > 10* nbDes) 		// résultat supérieur au score maximum
		{
			result = new BigDecimal (0)	;
		}
		else if(nbDes == 1) 
		{
			result = new BigDecimal (0.1);			//pour un seul dé on a une équiprobabilité à 10%
		}
		else
		{
			// on calcule la probabilité de maniere récursive. pour calculer une probabilité A avec N dés,
			// on somme les probabilités d'obtenir entre A-1 et A-10 avec N-1 dés, et on multiplie le résultat pas 0,1
			// on met le resultat en cache pour future utilisation.
			// Les cas précédents permettent d'eviter de calculer les impossibilités
			String cle = Proba.genererCle(resultatCible, nbDes);
			
			Object resultatCache = Cache.getDefaultInstance().sortirDonnee(cle);
			
			if (resultatCache == null) 
			{
				System.out.println("cache non trouvé, calcul de " + cle);
				for (int i = resultatCible - 1; i >= resultatCible - 10; i--) 
				{
					BigDecimal temp = Proba.calculerProba(i, nbDes - 1);
					temp = temp.multiply(new BigDecimal(0.1));
					result = result.add(temp);
				}
				Cache.getDefaultInstance().rangerDonnee(cle, result);

			}
			else 
			{
				result = (BigDecimal) resultatCache;
				System.out.println("cache trouvé pour " + cle);
			}
		}
		return result;
	}
	/** sert a generer la clé unique de chaque objet proba
	 * @param resultatCible : nombre a atteindre avec les dés
	 * @param nbDes : nombre de dés
	 * @return proba( resultatCible , nbdés)
	 */
	private static String genererCle(int resultatCible, int nbDes) 
	{
		StringBuffer cle = new StringBuffer("proba(");
		cle.append(resultatCible).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

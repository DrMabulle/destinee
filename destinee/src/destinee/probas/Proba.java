/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;

import destinee.utils.Cache;

/**
 * @author AMOROS
 *
 *permet de d�terminer la probabilit� de tomber sur une nombre A en jetant B d�s a dix faces
 */
public class Proba {
	
	
	public static BigDecimal calculerProba(int resultatCible, int nbDes) 
	{
		BigDecimal result = new BigDecimal (0);
		if(resultatCible < nbDes 					//r�sultat inf�rieur au nombre de d�s
				|| nbDes < 1 						// pas de d�s lanc�s
				|| resultatCible > 10* nbDes) 		// r�sultat sup�rieur au score maximum
		{
			result = new BigDecimal (0)	;
		}
		else if(nbDes == 1) 
		{
			result = new BigDecimal (0.1);			//pour un seul d� on a une �quiprobabilit� � 10%
		}
		else
		{
			// on calcule la probabilit� de maniere r�cursive. pour calculer une probabilit� A avec N d�s,
			// on somme les probabilit�s d'obtenir entre A-1 et A-10 avec N-1 d�s, et on multiplie le r�sultat pas 0,1
			// on met le resultat en cache pour future utilisation.
			// Les cas pr�c�dents permettent d'eviter de calculer les impossibilit�s
			String cle = Proba.genererCle(resultatCible, nbDes);
			
			Object resultatCache = Cache.getDefaultInstance().sortirDonnee(cle);
			
			if (resultatCache == null) 
			{
				System.out.println("cache non trouv�, calcul de " + cle);
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
				System.out.println("cache trouv� pour " + cle);
			}
		}
		return result;
	}
	/** sert a generer la cl� unique de chaque objet proba
	 * @param resultatCible : nombre a atteindre avec les d�s
	 * @param nbDes : nombre de d�s
	 * @return proba( resultatCible , nbd�s)
	 */
	private static String genererCle(int resultatCible, int nbDes) 
	{
		StringBuffer cle = new StringBuffer("proba(");
		cle.append(resultatCible).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

package destinee.probas;

import java.math.BigDecimal;
import destinee.utils.Cache;
/**
 * @author AMOROS
 *
 *permet de d�terminer la probabilit� de tomber sur une nombre sup�rieur a A en jetant B d�s a dix faces
 */
public class ProbaPlus 
{
	
	public static BigDecimal calculerProba(int seuilMin, int nbDes) 
	{
		BigDecimal result = new BigDecimal (0);
		if(seuilMin < nbDes) 						//r�sultat inf�rieur au nombre de d�s
		{
			result = new BigDecimal (1);
		}
		else if( nbDes < 1) 							// pas de d�s lanc�s
		{
			result = new BigDecimal (0)	;
		}
		else if (seuilMin > 10* nbDes)				// seuil sup�rieur au score maximum
		{
			result = new BigDecimal (0)	;
		}
		else if (seuilMin < 5.5 * nbDes)			// seuil inf�rieur a la moyenne, on va se simplifier la vie 
		{											//en passant par le calcul de probaMoins qui est plus rapide
			BigDecimal temp1 = ProbaMoins.calculerProba(seuilMin, nbDes);
			temp1 = temp1.add(Proba.calculerProba(seuilMin, nbDes));
			BigDecimal temp2 = new BigDecimal (1);
			result = temp2.subtract(temp1);
		}
		else
		{
			// on calcule la probabilit� de faire plus qu'un certain r�sultat en lan�ant un certain 
			//nombre de d�s. Pour cela on calcule la probabilit� d'atteindre chacun des nombres 
			//sup�rieurs au seuil avec le nombre de d�s lanc�s.
			String cle = ProbaPlus.genererCle(seuilMin, nbDes);
			
			Object resultatCache = Cache.getDefaultInstance().sortirDonnee(cle);
			
			if (resultatCache == null) 
			{
				System.out.println("cache non trouv�, calcul de " + cle);
				for (int i = seuilMin + 1; i <= (nbDes * 10); i++) 
				{
					BigDecimal temp = Proba.calculerProba(i, nbDes);
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
		System.out.println("probaPlus(" + seuilMin + "," + nbDes +  ") : " + result);
		return result;
	}
	/** sert a generer la cl� unique de chaque objet probaPlus
	 * @param seuilMin : nombre a d�passer avec les d�s
	 * @param nbDes : nombre de d�s
	 * @return probaPlus(seuilMin , nbDes)
	 */
	private static String genererCle(int seuilMin, int nbDes) 
	{
		StringBuffer cle = new StringBuffer("probaPlus(");
		cle.append(seuilMin).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

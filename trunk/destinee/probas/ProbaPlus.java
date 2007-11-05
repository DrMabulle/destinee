package destinee.probas;

import java.math.BigDecimal;
import destinee.utils.Cache;
/**
 * @author AMOROS
 *
 *permet de déterminer la probabilité de tomber sur une nombre supérieur a A en jetant B dés a dix faces
 */
public class ProbaPlus 
{
	
	public static BigDecimal calculerProba(int seuilMin, int nbDes) 
	{
		BigDecimal result = new BigDecimal (0);
		if(seuilMin < nbDes) 						//résultat inférieur au nombre de dés
		{
			result = new BigDecimal (1);
		}
		else if( nbDes < 1) 							// pas de dés lancés
		{
			result = new BigDecimal (0)	;
		}
		else if (seuilMin > 10* nbDes)				// seuil supérieur au score maximum
		{
			result = new BigDecimal (0)	;
		}
		else if (seuilMin < 5.5 * nbDes)			// seuil inférieur a la moyenne, on va se simplifier la vie 
		{											//en passant par le calcul de probaMoins qui est plus rapide
			BigDecimal temp1 = ProbaMoins.calculerProba(seuilMin, nbDes);
			temp1 = temp1.add(Proba.calculerProba(seuilMin, nbDes));
			BigDecimal temp2 = new BigDecimal (1);
			result = temp2.subtract(temp1);
		}
		else
		{
			// on calcule la probabilité de faire plus qu'un certain résultat en lançant un certain 
			//nombre de dés. Pour cela on calcule la probabilité d'atteindre chacun des nombres 
			//supérieurs au seuil avec le nombre de dés lancés.
			String cle = ProbaPlus.genererCle(seuilMin, nbDes);
			
			Object resultatCache = Cache.getDefaultInstance().sortirDonnee(cle);
			
			if (resultatCache == null) 
			{
				System.out.println("cache non trouvé, calcul de " + cle);
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
				System.out.println("cache trouvé pour " + cle);
			}
		}
		System.out.println("probaPlus(" + seuilMin + "," + nbDes +  ") : " + result);
		return result;
	}
	/** sert a generer la clé unique de chaque objet probaPlus
	 * @param seuilMin : nombre a dépasser avec les dés
	 * @param nbDes : nombre de dés
	 * @return probaPlus(seuilMin , nbDes)
	 */
	private static String genererCle(int seuilMin, int nbDes) 
	{
		StringBuffer cle = new StringBuffer("probaPlus(");
		cle.append(seuilMin).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

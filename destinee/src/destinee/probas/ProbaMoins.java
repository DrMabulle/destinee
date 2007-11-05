package destinee.probas;

import java.math.BigDecimal;
import destinee.utils.Cache;
/**
 * @author AMOROS
 *
 *permet de déterminer la probabilité de tomber sur une nombre inférieur a A en jetant B dés a dix faces
 */
public class ProbaMoins 
{
	
	public static BigDecimal calculerProba(int seuilMax, int nbDes) 
	{
		BigDecimal result = new BigDecimal (0);
		if(seuilMax < nbDes) 						//résultat inférieur au nombre de dés
		{
			result = new BigDecimal (0);
		}
		else if( nbDes < 1) 							// pas de dés lancés
		{
			result = new BigDecimal (1)	;
		}
		else if (seuilMax > 10* nbDes)				// seuil supérieur au score maximum
		{
			result = new BigDecimal (1)	;
		}
		else if (seuilMax > 5.5 * nbDes)			//seuil supérieur a la moyenne, on se simplifie la vie en passant
		{											// par le calcul de probaPlus qui est plus rapide dans ce cas
			BigDecimal temp1 = ProbaPlus.calculerProba(seuilMax, nbDes);
			temp1 = temp1.add(Proba.calculerProba(seuilMax, nbDes));
			BigDecimal temp2 = new BigDecimal (1);
			result = temp2.subtract(temp1);
		}
		else
		{
			// on calcule la probabilité de faire moins qu'un certain résultat en lançant un certain 
			//nombre de dés. Pour cela on calcule la probabilité d'atteindre chacun des nombres 
			//inférieurs au seuil avec le nombre de dés lancés.
			String cle = ProbaMoins.genererCle(seuilMax, nbDes);
			
			Object resultatCache = Cache.getDefaultInstance().sortirDonnee(cle);
			
			if (resultatCache == null) 
			{
				System.out.println("cache non trouvé, calcul de " + cle);
				for (int i = seuilMax - 1; i >= nbDes; i--) 
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
		System.out.println("probaMoins(" + seuilMax + "," + nbDes +  ") : " + result);
		return result;
	}
	/** sert a generer la clé unique de chaque objet probaMoins
	 * @param seuilMax : nombre a dépasser avec les dés
	 * @param nbDes : nombre de dés
	 * @return probaPlus(seuilMax , nbDes)
	 */
	private static String genererCle(int seuilMin, int nbDes) 
	{
		StringBuffer cle = new StringBuffer("probaMoins(");
		cle.append(seuilMin).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

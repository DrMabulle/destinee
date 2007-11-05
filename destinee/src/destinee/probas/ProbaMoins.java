package destinee.probas;

import java.math.BigDecimal;
import destinee.utils.Cache;
/**
 * @author AMOROS
 *
 *permet de d�terminer la probabilit� de tomber sur une nombre inf�rieur a A en jetant B d�s a dix faces
 */
public class ProbaMoins 
{
	
	public static BigDecimal calculerProba(int seuilMax, int nbDes) 
	{
		BigDecimal result = new BigDecimal (0);
		if(seuilMax < nbDes) 						//r�sultat inf�rieur au nombre de d�s
		{
			result = new BigDecimal (0);
		}
		else if( nbDes < 1) 							// pas de d�s lanc�s
		{
			result = new BigDecimal (1)	;
		}
		else if (seuilMax > 10* nbDes)				// seuil sup�rieur au score maximum
		{
			result = new BigDecimal (1)	;
		}
		else if (seuilMax > 5.5 * nbDes)			//seuil sup�rieur a la moyenne, on se simplifie la vie en passant
		{											// par le calcul de probaPlus qui est plus rapide dans ce cas
			BigDecimal temp1 = ProbaPlus.calculerProba(seuilMax, nbDes);
			temp1 = temp1.add(Proba.calculerProba(seuilMax, nbDes));
			BigDecimal temp2 = new BigDecimal (1);
			result = temp2.subtract(temp1);
		}
		else
		{
			// on calcule la probabilit� de faire moins qu'un certain r�sultat en lan�ant un certain 
			//nombre de d�s. Pour cela on calcule la probabilit� d'atteindre chacun des nombres 
			//inf�rieurs au seuil avec le nombre de d�s lanc�s.
			String cle = ProbaMoins.genererCle(seuilMax, nbDes);
			
			Object resultatCache = Cache.getDefaultInstance().sortirDonnee(cle);
			
			if (resultatCache == null) 
			{
				System.out.println("cache non trouv�, calcul de " + cle);
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
				System.out.println("cache trouv� pour " + cle);
			}
		}
		System.out.println("probaMoins(" + seuilMax + "," + nbDes +  ") : " + result);
		return result;
	}
	/** sert a generer la cl� unique de chaque objet probaMoins
	 * @param seuilMax : nombre a d�passer avec les d�s
	 * @param nbDes : nombre de d�s
	 * @return probaPlus(seuilMax , nbDes)
	 */
	private static String genererCle(int seuilMin, int nbDes) 
	{
		StringBuffer cle = new StringBuffer("probaMoins(");
		cle.append(seuilMin).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

package destinee.commun.probas;

import java.math.BigDecimal;

import destinee.core.utils.CacheProba;

/**
 * @author Bubulle et No-one
 * 
 * Class utilitaire pour les calculs de probasMoins
 */
public class ProbaMoins
{
	/**
	 * M�thode permettant de d�terminer la probabilit� de tomber sur une nombre inf�rieur � A en jetant B d�s � 10 faces
	 * 
	 * @param seuilMax seuil maximum (A)
	 * @param nbDes nombre de d�s (B)
	 * @return la probabilit� de faire moins de A en lan�ant B d�s � 10 faces
	 */
	public static BigDecimal calculerProba(int seuilMax, int nbDes)
	{
		BigDecimal result = null;
		if (seuilMax < nbDes)
		{
			// r�sultat inf�rieur au nombre de d�s
			result = BigDecimal.ZERO;
		}
		else if (nbDes < 1)
		{
			// pas de d�s lanc�s
			result = BigDecimal.ONE;
		}
		else if (seuilMax > 10 * nbDes)
		{
			// seuil sup�rieur au score maximum
			result = BigDecimal.ONE;
		}
		else if (seuilMax > 5.5 * nbDes)
		{
			// Seuil sup�rieur � la moyenne : on se simplifie la vie en passant
			// par le calcul de probaPlus qui est plus rapide dans ce cas
			BigDecimal probaPlus = ProbaPlus.calculerProba(seuilMax, nbDes);
			probaPlus = probaPlus.add(Proba.calculerProba(seuilMax, nbDes));
			result = BigDecimal.ONE.subtract(probaPlus);
		}
		else
		{
			// On calcule la probabilit� de faire moins qu'un certain r�sultat en lan�ant un certain
			// nombre de d�s. Pour cela on calcule la probabilit� d'atteindre chacun des nombres
			// inf�rieurs au seuil avec le nombre de d�s lanc�s.
			String cle = ProbaMoins.genererCle(seuilMax, nbDes);

			BigDecimal resultatCache = CacheProba.getDefaultInstance().recupererDonnees(cle);

			if (resultatCache == null)
			{
				// Le r�sultat n'est pas en cache => il n'a pas �t� calcul� => le faire
				result = BigDecimal.ZERO;
				BigDecimal temp;
				for (int i = seuilMax - 1; i >= nbDes; i--)
				{
					temp = Proba.calculerProba(i, nbDes);
					result = result.add(temp);
				}
				CacheProba.getDefaultInstance().stockerDonnees(cle, result);

			}
			else
			{
				// Le r�sultat est en cache => on utilise le r�sultat calcul� auparavant
				result = resultatCache;
			}
		}
		return result;
	}

	/**
	 * M�thode servant � generer la cl� unique de chaque objet probaMoins
	 * 
	 * @param seuilMax : nombre � ne pas d�passer avec les d�s
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

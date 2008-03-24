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
	 * Méthode permettant de déterminer la probabilité de tomber sur une nombre inférieur à A en jetant B dés à 10 faces
	 * 
	 * @param seuilMax seuil maximum (A)
	 * @param nbDes nombre de dés (B)
	 * @return la probabilité de faire moins de A en lançant B dés à 10 faces
	 */
	public static BigDecimal calculerProba(int seuilMax, int nbDes)
	{
		BigDecimal result = null;
		if (seuilMax < nbDes)
		{
			// résultat inférieur au nombre de dés
			result = BigDecimal.ZERO;
		}
		else if (nbDes < 1)
		{
			// pas de dés lancés
			result = BigDecimal.ONE;
		}
		else if (seuilMax > 10 * nbDes)
		{
			// seuil supérieur au score maximum
			result = BigDecimal.ONE;
		}
		else if (seuilMax > 5.5 * nbDes)
		{
			// Seuil supérieur à la moyenne : on se simplifie la vie en passant
			// par le calcul de probaPlus qui est plus rapide dans ce cas
			BigDecimal probaPlus = ProbaPlus.calculerProba(seuilMax, nbDes);
			probaPlus = probaPlus.add(Proba.calculerProba(seuilMax, nbDes));
			result = BigDecimal.ONE.subtract(probaPlus);
		}
		else
		{
			// On calcule la probabilité de faire moins qu'un certain résultat en lançant un certain
			// nombre de dés. Pour cela on calcule la probabilité d'atteindre chacun des nombres
			// inférieurs au seuil avec le nombre de dés lancés.
			String cle = ProbaMoins.genererCle(seuilMax, nbDes);

			BigDecimal resultatCache = CacheProba.getDefaultInstance().recupererDonnees(cle);

			if (resultatCache == null)
			{
				// Le résultat n'est pas en cache => il n'a pas été calculé => le faire
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
				// Le résultat est en cache => on utilise le résultat calculé auparavant
				result = resultatCache;
			}
		}
		return result;
	}

	/**
	 * Méthode servant à generer la clé unique de chaque objet probaMoins
	 * 
	 * @param seuilMax : nombre à ne pas dépasser avec les dés
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

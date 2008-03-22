package destinee.commun.probas;

import java.math.BigDecimal;

import destinee.core.utils.CacheProba;

/**
 * @author Bubulle et No-one
 * 
 * Class utilitaire pour les calculs de probasPlus
 */
public class ProbaPlus
{

	/**
	 * Méthode permettant de déterminer la probabilité de tomber sur une nombre supérieur à A en jetant B dés à 10 faces
	 * 
	 * @param seuilMin seuil minimal à atteindre (A)
	 * @param nbDes nombre de dés (B)
	 * @return la probabilité de faire plus que A en jetant B dés
	 */
	public static BigDecimal calculerProba(int seuilMin, int nbDes)
	{
		BigDecimal result = null;
		if (seuilMin < nbDes)
		{
			// résultat inférieur au nombre de dés
			result = BigDecimal.ONE;
		}
		else if (nbDes < 1)
		{
			// pas de dés lancés
			result = BigDecimal.ZERO;
		}
		else if (seuilMin > 10 * nbDes)
		{
			// seuil supérieur au score maximum
			result = BigDecimal.ZERO;
		}
		else if (seuilMin < 5.5 * nbDes)
		{
			// Seuil inférieur à la moyenne : on va se simplifier la vie
			// en passant par le calcul de probaMoins qui est plus rapide
			BigDecimal probaMoins = ProbaMoins.calculerProba(seuilMin, nbDes);
			probaMoins = probaMoins.add(Proba.calculerProba(seuilMin, nbDes));
			result = BigDecimal.ONE.subtract(probaMoins);
		}
		else
		{
			// On calcule la probabilité de faire plus qu'un certain résultat en lançant un certain
			// nombre de dés. Pour cela on calcule la probabilité d'atteindre chacun des nombres
			// supérieurs au seuil avec le nombre de dés lancés.
			String cle = ProbaPlus.genererCle(seuilMin, nbDes);

			BigDecimal resultatCache = CacheProba.getDefaultInstance().recupererDonnees(cle);

			if (resultatCache == null)
			{
				// Le résultat n'est pas en cache => il n'a pas été calculé => le faire
				result = BigDecimal.ZERO;
				for (int i = seuilMin + 1; i <= (nbDes * 10); i++)
				{
					BigDecimal temp = Proba.calculerProba(i, nbDes);
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
	 * Méthode servant à generer la clé unique de chaque objet probaPlus
	 * 
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

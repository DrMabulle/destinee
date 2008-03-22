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
	 * M�thode permettant de d�terminer la probabilit� de tomber sur une nombre sup�rieur � A en jetant B d�s � 10 faces
	 * 
	 * @param seuilMin seuil minimal � atteindre (A)
	 * @param nbDes nombre de d�s (B)
	 * @return la probabilit� de faire plus que A en jetant B d�s
	 */
	public static BigDecimal calculerProba(int seuilMin, int nbDes)
	{
		BigDecimal result = null;
		if (seuilMin < nbDes)
		{
			// r�sultat inf�rieur au nombre de d�s
			result = BigDecimal.ONE;
		}
		else if (nbDes < 1)
		{
			// pas de d�s lanc�s
			result = BigDecimal.ZERO;
		}
		else if (seuilMin > 10 * nbDes)
		{
			// seuil sup�rieur au score maximum
			result = BigDecimal.ZERO;
		}
		else if (seuilMin < 5.5 * nbDes)
		{
			// Seuil inf�rieur � la moyenne : on va se simplifier la vie
			// en passant par le calcul de probaMoins qui est plus rapide
			BigDecimal probaMoins = ProbaMoins.calculerProba(seuilMin, nbDes);
			probaMoins = probaMoins.add(Proba.calculerProba(seuilMin, nbDes));
			result = BigDecimal.ONE.subtract(probaMoins);
		}
		else
		{
			// On calcule la probabilit� de faire plus qu'un certain r�sultat en lan�ant un certain
			// nombre de d�s. Pour cela on calcule la probabilit� d'atteindre chacun des nombres
			// sup�rieurs au seuil avec le nombre de d�s lanc�s.
			String cle = ProbaPlus.genererCle(seuilMin, nbDes);

			BigDecimal resultatCache = CacheProba.getDefaultInstance().recupererDonnees(cle);

			if (resultatCache == null)
			{
				// Le r�sultat n'est pas en cache => il n'a pas �t� calcul� => le faire
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
				// Le r�sultat est en cache => on utilise le r�sultat calcul� auparavant
				result = resultatCache;
			}
		}
		return result;
	}

	/**
	 * M�thode servant � generer la cl� unique de chaque objet probaPlus
	 * 
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

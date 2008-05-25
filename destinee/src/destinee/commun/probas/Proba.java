/**
 * 
 */
package destinee.commun.probas;

import java.math.BigDecimal;

import destinee.core.utils.CacheProba;

/**
 * @author Bubulle et No-one
 * 
 * Class utilitaire pour les calculs de probas
 */
public final class Proba
{
	private static final BigDecimal zeroVirguleUn = new BigDecimal("0.1");

	/**
	 * Crée une nouvelle instance de Proba.java
	 */
	private Proba()
	{
		super();
	}

	/**
	 * Permet de déterminer la probabilité de tomber sur une nombre A en jetant B dés à 10 faces
	 * 
	 * @param resultatCible résultat cible
	 * @param nbDes nombre de dés
	 * @return la probabilité de faire le résultat cible en lançant le nombre de dés donné
	 */
	public static final BigDecimal calculerProba(final int resultatCible, final int nbDes)
	{
		BigDecimal result = null;
		if (resultatCible < nbDes // résultat inférieur au nombre de dés
				|| nbDes < 1 // pas de dés lancés
				|| resultatCible > 10 * nbDes) // résultat supérieur au score maximum
		{
			result = BigDecimal.ZERO;
		}
		else if (nbDes == 1)
		{
			result = zeroVirguleUn; // pour un seul dé on a une équiprobabilité à 10%
		}
		else
		{
			// on calcule la probabilité de maniere récursive. pour calculer une probabilité A avec N dés,
			// on somme les probabilités d'obtenir entre A-1 et A-10 avec N-1 dés, et on multiplie le résultat pas 0,1
			// on met le resultat en cache pour future utilisation.
			// Les cas précédents permettent d'eviter de calculer les impossibilités
			String cle = Proba.genererCle(resultatCible, nbDes);

			BigDecimal resultatCache = CacheProba.recupererDonnees(cle);

			if (resultatCache == null)
			{
				// Le résultat n'est pas en cache => il n'a pas été calculé => le faire
				result = BigDecimal.ZERO;
				BigDecimal temp;
				int min = resultatCible - 10;
				for (int i = resultatCible - 1; i >= min; i--)
				{
					temp = Proba.calculerProba(i, nbDes - 1);
					temp = temp.multiply(zeroVirguleUn);
					result = result.add(temp);
				}
				CacheProba.stockerDonnees(cle, result);

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
	 * Méthode servant à generer la clé unique de chaque objet proba
	 * 
	 * @param resultatCible nombre à atteindre avec les dés
	 * @param nbDes nombre de dés
	 * @return proba(resultatCible , nbDes)
	 */
	private static final String genererCle(final int resultatCible, final int nbDes)
	{
		StringBuffer cle = new StringBuffer("proba(");
		cle.append(resultatCible).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

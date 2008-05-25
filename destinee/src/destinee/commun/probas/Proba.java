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
	 * Cr�e une nouvelle instance de Proba.java
	 */
	private Proba()
	{
		super();
	}

	/**
	 * Permet de d�terminer la probabilit� de tomber sur une nombre A en jetant B d�s � 10 faces
	 * 
	 * @param resultatCible r�sultat cible
	 * @param nbDes nombre de d�s
	 * @return la probabilit� de faire le r�sultat cible en lan�ant le nombre de d�s donn�
	 */
	public static final BigDecimal calculerProba(final int resultatCible, final int nbDes)
	{
		BigDecimal result = null;
		if (resultatCible < nbDes // r�sultat inf�rieur au nombre de d�s
				|| nbDes < 1 // pas de d�s lanc�s
				|| resultatCible > 10 * nbDes) // r�sultat sup�rieur au score maximum
		{
			result = BigDecimal.ZERO;
		}
		else if (nbDes == 1)
		{
			result = zeroVirguleUn; // pour un seul d� on a une �quiprobabilit� � 10%
		}
		else
		{
			// on calcule la probabilit� de maniere r�cursive. pour calculer une probabilit� A avec N d�s,
			// on somme les probabilit�s d'obtenir entre A-1 et A-10 avec N-1 d�s, et on multiplie le r�sultat pas 0,1
			// on met le resultat en cache pour future utilisation.
			// Les cas pr�c�dents permettent d'eviter de calculer les impossibilit�s
			String cle = Proba.genererCle(resultatCible, nbDes);

			BigDecimal resultatCache = CacheProba.recupererDonnees(cle);

			if (resultatCache == null)
			{
				// Le r�sultat n'est pas en cache => il n'a pas �t� calcul� => le faire
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
				// Le r�sultat est en cache => on utilise le r�sultat calcul� auparavant
				result = resultatCache;
			}
		}
		return result;
	}

	/**
	 * M�thode servant � generer la cl� unique de chaque objet proba
	 * 
	 * @param resultatCible nombre � atteindre avec les d�s
	 * @param nbDes nombre de d�s
	 * @return proba(resultatCible , nbDes)
	 */
	private static final String genererCle(final int resultatCible, final int nbDes)
	{
		StringBuffer cle = new StringBuffer("proba(");
		cle.append(resultatCible).append(",").append(nbDes).append(")");
		return cle.toString();
	}
}

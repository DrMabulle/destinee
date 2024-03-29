/**
 * 
 */
package destinee.commun.probas;

import java.math.BigDecimal;

import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueImparable;
import destinee.commun.data.Cible;
import destinee.core.utils.ConversionUtil;

/**
 * @author Bubulle et No-one
 * 
 */
public final class ResolutionAttaque
{
	public static final int RESOLUTION_COUP_CRITIQUE = 0;
	public static final int RESOLUTION_COUP_SIMPLE = 1;
	public static final int RESOLUTION_ESQUIVE_SIMPLE = 2;
	public static final int RESOLUTION_ESQUIVE_PARFAITE = 3;
	public static final int RESOLUTION_ECHEC_COMPETENCE = 4;

	/**
	 * Cr�e une nouvelle instance de ResolutionAttaque
	 */
	private ResolutionAttaque()
	{
		super();
	}

	/**
	 * M�thode servant � calculer les probabilit�s respectives des 4 resolutions possible pour certaines caract�ristiques d'attaque
	 * 
	 * @param nbDesAtt nombre de d�s d'attaque
	 * @param bonusAtt bonus fixe en attaque
	 * @param nbDesDef nombre de d�s de d�fense
	 * @param bonusDef bonus fixe en d�fense
	 * @param maitriseCompetence pourcentage de maitrise de la comp�tence
	 * @param typeResol type de r�solution
	 * @return la probabilit� d'obtenir la r�solution choisie
	 */
	public static final BigDecimal resoudreAttaque(final int nbDesAtt, final int bonusAtt, final int nbDesDef, final int bonusDef,
			final double maitriseCompetence, final int typeResol)
	{
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal temp = null;
		BigDecimal borneSup = null;
		BigDecimal borneInf = null;
		int attMin = nbDesAtt;
		int attMax = (nbDesAtt * 10);

		switch (typeResol)
		{
			case RESOLUTION_COUP_CRITIQUE: // attaque critique
				// i correspond a chaque resultat du jet de d�s d'attaque
				for (int i = Math.max(attMin, (bonusDef - bonusAtt)); i <= attMax; i++)
				{
					// pas d'attaque critique si le jet d'attaque est negatif
					temp = Proba.calculerProba(i, nbDesAtt);
					temp = temp.multiply(ProbaMoins.calculerProba((int) (0.5 * (i + 1 + bonusAtt) - bonusDef), nbDesDef));
					result = result.add(temp);
				}
				result = result.multiply(new BigDecimal("" + maitriseCompetence));
				break;
			case RESOLUTION_COUP_SIMPLE: // attaque reussie
				for (int i = attMin; i <= attMax; i++)
				{
					temp = Proba.calculerProba(i, nbDesAtt);
					borneSup = ProbaMoins.calculerProba((i + bonusAtt - bonusDef), nbDesDef);
					borneInf = ProbaMoins.calculerProba((int) (0.5 * (i + 1 + bonusAtt) - bonusDef), nbDesDef);
					temp = temp.multiply(borneSup.subtract(borneInf));
					result = result.add(temp);
				}
				result = result.multiply(new BigDecimal("" + maitriseCompetence));
				break;
			case RESOLUTION_ESQUIVE_SIMPLE: // esquive reussie
				for (int i = attMin; i <= attMax; i++)
				{
					temp = Proba.calculerProba(i, nbDesAtt);
					borneSup = ProbaMoins.calculerProba((2 * (i + bonusAtt) - bonusDef), nbDesDef);
					borneInf = ProbaMoins.calculerProba((i + bonusAtt - bonusDef), nbDesDef);
					temp = temp.multiply(borneSup.subtract(borneInf));
					result = result.add(temp);
				}
				result = result.multiply(new BigDecimal("" + maitriseCompetence));
				break;
			case RESOLUTION_ESQUIVE_PARFAITE: // esquive parfaite
				for (int i = attMin; i <= attMax; i++)
				{
					temp = Proba.calculerProba(i, nbDesAtt);
					borneInf = ProbaPlus.calculerProba((2 * (i + bonusAtt) - bonusDef - 1), nbDesDef);
					temp = temp.multiply(borneInf);
					result = result.add(temp);
				}
				result = result.multiply(new BigDecimal("" + maitriseCompetence));
				break;
			case RESOLUTION_ECHEC_COMPETENCE: // �chec de la comp�tence
				result = new BigDecimal("" + (1 - maitriseCompetence)).setScale(2, BigDecimal.ROUND_HALF_UP);
				break;
		}
		return result;
	}

	/**
	 * M�thode servant � calculer les probabilit�s respectives des 4 resolutions possible pour une attaque, face � une cible
	 * 
	 * @param attaque une attaque
	 * @param cible une cible
	 * @param typeResol type de r�solution
	 * @return la probabilit� d'obtenir la r�solution choisie
	 */
	public static final BigDecimal resoudreAttaque(final Attaque attaque, final Cible cible, final int typeResol)
	{
		if (attaque instanceof AttaqueImparable)
		{
			if (typeResol == RESOLUTION_COUP_SIMPLE)
			{
				return BigDecimal.ONE;
			}
			else
			{
				return BigDecimal.ZERO;
			}
		}

		return resoudreAttaque(attaque.getNbDesAtt(), attaque.getBonusAtt(), cible.getNbDesDefenseEffectif(),
			cible.getBonusDefenseEffectif(), attaque.getPerso().getMaitriseAttaque(attaque.getTypeAttaque()), typeResol);
	}

	/**
	 * Retourne l'esperance de d�gats d'une attaque donn�e avec un r�sultat (type de r�solution) connu a l'avance
	 * 
	 * @param aAttaque une attaque
	 * @param aCible une cible
	 * @param typeResol type de resolution
	 * @return l'esp�rance math�matique de d�gats avec le type de resolution choisi
	 */
	public static final double esperanceDeDegats(final Attaque aAttaque, final Cible aCible, final int typeResol)
	{

		BigDecimal result = BigDecimal.ZERO;
		int nbDesUtilises = 0;
		int bonusDeg = 0;

		// Cas sp�ciaux
		if (typeResol == RESOLUTION_ECHEC_COMPETENCE)
		{
			// Pas de d�g�ts en cas d'�chec de la comp�tence
			return 0;
		}
		else if (aAttaque instanceof AttaqueImparable && typeResol != RESOLUTION_COUP_SIMPLE)
		{
			// Le r�sultat d'une attaque imparable est toujours un coup normal
			return esperanceDeDegats(aAttaque, aCible, RESOLUTION_COUP_SIMPLE);
		}
		else if (typeResol == RESOLUTION_ESQUIVE_PARFAITE || typeResol == RESOLUTION_ESQUIVE_SIMPLE)
		{
			// Pas de d�g�ts en cas d'esquive
			return 0;
		}

		// D�s de d�g�ts et bonus de d�g�ts en cas de touche
		if (typeResol == RESOLUTION_COUP_CRITIQUE)
		{
			nbDesUtilises = aAttaque.getNbDesDegatsCritique();
			bonusDeg = aAttaque.getBonusDegatsCritique(aCible.getArmure());
		}
		else if (typeResol == RESOLUTION_COUP_SIMPLE)
		{
			nbDesUtilises = aAttaque.getNbDesDeg();
			bonusDeg = aAttaque.getBonusDeg(aCible.getArmure());
		}

		BigDecimal resultatDuDeBigDecimal;
		BigDecimal proba;
		int resultatMax = 10 * nbDesUtilises;
		for (int i = nbDesUtilises; i <= resultatMax; i++)
		{
			resultatDuDeBigDecimal = new BigDecimal(Math.max(i + bonusDeg, 1));
			proba = Proba.calculerProba(i, nbDesUtilises);
			result = result.add(proba.multiply(resultatDuDeBigDecimal));
		}

		return ConversionUtil.bigdecimalVersDouble(result, 5);
	}

	/**
	 * Retourne l'�sp�rance math�matique de d�gats d'une attaque donn�e, le type de r�solution etant inconnu
	 * 
	 * @param aAttaque une attaque
	 * @param aCible une cible
	 * @return l'esp�rance de d�gats
	 */
	public static final double esperanceDeDegats(final Attaque aAttaque, final Cible aCible)
	{
		double probaAttaqueCritique = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_COUP_CRITIQUE));
		double probaAttaqueNormale = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_COUP_SIMPLE));

		return probaAttaqueCritique * esperanceDeDegats(aAttaque, aCible, RESOLUTION_COUP_CRITIQUE) + probaAttaqueNormale
				* esperanceDeDegats(aAttaque, aCible, RESOLUTION_COUP_SIMPLE);
	}

	/**
	 * $ Retourne l'esp�rance math�matique de malus de d�fense pour une cible, suite � une attaque donn�e.
	 * 
	 * @param aAttaque une attaque
	 * @param aCible une cible
	 * @return l'esp�rance de malus de d�fense
	 */
	public static final double esperanceMalusDefense(final Attaque aAttaque, final Cible aCible)
	{
		double probaAttaqueCritique = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_COUP_CRITIQUE), 10);
		double probaAttaqueNormale = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_COUP_SIMPLE), 10);
		double probaEsquiveSimple = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_ESQUIVE_SIMPLE), 10);

		return probaAttaqueCritique + 0.5 * probaAttaqueNormale + 0.5 * probaEsquiveSimple;
	}
}

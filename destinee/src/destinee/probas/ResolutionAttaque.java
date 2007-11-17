/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;

import destinee.probas.Proba;
import destinee.probas.ProbaPlus;
import destinee.probas.ProbaMoins;
import destinee.core.utils.ConversionUtil;
import destinee.data.*;

/**
 * @author AMOROS
 * 
 */
public class ResolutionAttaque
{

	public static final int RESOLUTION_COUP_CRITIQUE = 0;
	public static final int RESOLUTION_COUP_SIMPLE = 1;
	public static final int RESOLUTION_ESQUIVE_SIMPLE = 2;
	public static final int RESOLUTION_ESQUIVE_PARFAITE = 3;

	/**
	 * Méthode servant à calculer les probabilités respectives des 4 resolutions
	 * possible pour certaines caractéristiques d'attaque
	 * 
	 * @param nbDesAtt nombre de dés d'attaque
	 * @param bonusAtt bonus fixe en attaque
	 * @param nbDesDef nombre de dés de défense
	 * @param bonusDef bonus fixe en défense
	 * @param typeResol type de résolution
	 * @return la probabilité d'obtenir la résolution choisie
	 */
	public static BigDecimal resoudreAttaque(int nbDesAtt, int bonusAtt, int nbDesDef, int bonusDef, int typeResol)
	{
		BigDecimal result = new BigDecimal(0);
		BigDecimal temp = null;
		BigDecimal borneSup = null;
		BigDecimal borneInf = null;
		int attMin = nbDesAtt;
		int attMax = (nbDesAtt * 10);

		switch (typeResol)
		{
			case RESOLUTION_COUP_CRITIQUE: // attaque critique
				// i correspond a chaque resultat du jet de dés d'attaque
				for (int i = Math.max(attMin - 1, (bonusDef - bonusAtt)); i <= attMax + 1; i++)
				{
					// pas d'attaque critique si le jet d'attaque est negatif
					temp = Proba.calculerProba(i, nbDesAtt);
					temp = temp.multiply(ProbaMoins.calculerProba((int) (0.5 * (i + 1 + bonusAtt) - bonusDef), nbDesDef));
					result = result.add(temp);
				}
				break;
			case RESOLUTION_COUP_SIMPLE: // attaque reussie
				for (int i = attMin - 1; i <= attMax + 1; i++)
				{
					temp = Proba.calculerProba(i, nbDesAtt);
					borneSup = ProbaMoins.calculerProba((i + bonusAtt - bonusDef), nbDesDef);
					borneInf = ProbaMoins.calculerProba((int) (0.5 * (i + 1 + bonusAtt) - bonusDef), nbDesDef);
					temp = temp.multiply(borneSup.subtract(borneInf));
					result = result.add(temp);
				}
				break;
			case RESOLUTION_ESQUIVE_SIMPLE: // esquive reussie
				for (int i = attMin - 1; i <= attMax + 1; i++)
				{
					temp = Proba.calculerProba(i, nbDesAtt);
					borneSup = ProbaMoins.calculerProba((2 * (i + bonusAtt) - bonusDef), nbDesDef);
					borneInf = ProbaMoins.calculerProba((i + bonusAtt - bonusDef), nbDesDef);
					temp = temp.multiply(borneSup.subtract(borneInf));
					result = result.add(temp);
				}
				break;
			case RESOLUTION_ESQUIVE_PARFAITE: // esquive parfaite
				for (int i = attMin - 1; i <= attMax + 1; i++)
				{
					temp = Proba.calculerProba(i, nbDesAtt);
					borneInf = ProbaPlus.calculerProba((2 * (i + bonusAtt) - bonusDef - 1), nbDesDef);
					temp = temp.multiply(borneInf);
					result = result.add(temp);
				}
				break;
		}
		return result;
	}

	/**
	 * Méthode servant à calculer les probabilités respectives des 4 resolutions
	 * possible pour une attaque, face à une cible
	 * 
	 * @param attaque une attaque
	 * @param cible une cible
	 * @param typeResol type de résolution
	 * @return la probabilité d'obtenir la résolution choisie
	 */
	public static BigDecimal resoudreAttaque(Attaque attaque, Cible cible, int typeResol)
	{
		if (attaque instanceof AttaqueImparable)
		{
			if (typeResol == RESOLUTION_COUP_SIMPLE)
			{
				return new BigDecimal(1);
			}
			else
			{
				return new BigDecimal(0);
			}
		}

		return resoudreAttaque(attaque.getNbDesAtt(), attaque.getBonusAtt(), cible.getNombreDeDesDefense(), cible.getBonusDefense(), typeResol);
	}

	/**
	 * Retourne l'esperance de dégats d'une attaque donnée avec un résultat (type de résolution) connu a l'avance
	 * @param aAttaque une attaque
	 * @param aCible une cible
	 * @param typeResol type de resolution
	 * @return l'espérance mathématique de dégats avec le type de resolution choisi
	 */
	public static double esperanceDeDegats(Attaque aAttaque, Cible aCible, int typeResol)
	{

		BigDecimal result = new BigDecimal(0);
		int nbDesUtilises = 0;
		int bonusDeg = 0;

		// Cas spéciaux
		if (aAttaque instanceof AttaqueImparable && typeResol != RESOLUTION_COUP_SIMPLE)
		{
			// Le résultat d'une attaque imparable est toujours un coup normal
			return esperanceDeDegats(aAttaque, aCible, RESOLUTION_COUP_SIMPLE);
		}
		else if (typeResol == RESOLUTION_ESQUIVE_PARFAITE || typeResol == RESOLUTION_ESQUIVE_SIMPLE)
		{
			// Pas de dégâts en cas d'esquive
			return 0;
		}

		// Dés de dégâts et bonus de dégâts en cas de touche
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

		for (int i = nbDesUtilises; i <= 10 * nbDesUtilises; i++)
		{
			BigDecimal resultatDuDeBigDecimal = new BigDecimal(Math.max(i + bonusDeg, 1));
			BigDecimal proba = (Proba.calculerProba(i, nbDesUtilises));
			result = result.add(proba.multiply(resultatDuDeBigDecimal));
		}

		return ConversionUtil.bigdecimalVersDouble(result);
	}

	/**
	 * Retourne l'éspérance mathématique de dégats d'une attaque donnée, le type
	 * de résolution etant inconnu
	 * 
	 * @param aAttaque une attaque
	 * @param aCible une cible
	 * @return l'espérance de dégats
	 */
	public static double esperanceDeDegats(Attaque aAttaque, Cible aCible)
	{
		double probaAttaqueCritique = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_COUP_CRITIQUE));
		double probaAttaqueNormale = ConversionUtil.bigdecimalVersDouble(resoudreAttaque(aAttaque, aCible, RESOLUTION_COUP_SIMPLE));

		return probaAttaqueCritique * esperanceDeDegats(aAttaque, aCible, RESOLUTION_COUP_CRITIQUE) + probaAttaqueNormale
				* esperanceDeDegats(aAttaque, aCible, RESOLUTION_COUP_SIMPLE);
	}
}

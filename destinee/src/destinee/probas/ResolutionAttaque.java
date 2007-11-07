/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;
import destinee.probas.Proba;
import destinee.probas.ProbaPlus;
import destinee.probas.ProbaMoins;
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
	 * Méthode servant à calculer les probabilités respectives des 4 resolutions possible pour une attaque
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
		int attMin = nbDesAtt - 1;
		int attMax = (nbDesAtt * 10) + 1;

		switch (typeResol)
		{
			case RESOLUTION_COUP_CRITIQUE: //attaque critique
				for (int i = Math.max(attMin, (bonusDef - bonusAtt)); i <= attMax; i++) // i correspond a chaque resultat du jet de dés d'attaque
				{ //pas d'attaque critique si le jet d'attaque est negatif
					temp = Proba.calculerProba(i, nbDesAtt);
					temp = temp.multiply(ProbaMoins.calculerProba((int) (0.5 * (i + 1 + bonusAtt) - bonusDef), nbDesDef));
					result = result.add(temp);
				}
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
				break;
			case RESOLUTION_ESQUIVE_PARFAITE: // esquive parfaite
				for (int i = attMin; i <= attMax; i++)
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
	public static BigDecimal ResoudreAttaque(Attaque attaque, Cible cible, int typeResol)
	{
	return resoudreAttaque(attaque.getNbDesAtt(), attaque.getBonusAtt(), cible.getNombreDeDesDefense(), cible.getBonusDefense(), typeResol);	
	}
}

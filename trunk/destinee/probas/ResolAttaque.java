/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;
import destinee.probas.Proba;
import destinee.probas.ProbaPlus;
import destinee.probas.ProbaMoins;

/**
 * @author AMOROS
 *sett a calculer les probabilités respectives des 4 resolutions possible spour une attaque
 */
public class ResolAttaque {
	
	public static BigDecimal resolAttaque(int nbDesAtt , int bonusAtt , int nbDesDef , int bonusDef, int typeResol)
	{
		BigDecimal result = new BigDecimal (0);
		BigDecimal temp = new BigDecimal (0);
		BigDecimal borneSup = new BigDecimal (0);
		BigDecimal borneInf = new BigDecimal (0);
		int attMin = nbDesAtt  -1;
		int attMax = (nbDesAtt * 10)+1 ;
		
		switch (typeResol) 
		{
			case 0 : 		//attaque critique
				for (int i = Math.max(attMin ,(bonusDef - bonusAtt)); i <= attMax ; i++) 	// i correspond a chaque resultat du jet de dés d'attaque
				{														//pas d'attaque critique si le jet d'attaque est negatif
					temp = Proba.calculerProba(i,nbDesAtt);
					temp = temp.multiply(ProbaMoins.calculerProba((int)(0.5 *(i +1+ bonusAtt)- bonusDef),nbDesDef));
					result = result.add(temp);
				}
				break;
			case 1 :		// attaque reussie
				for(int i = attMin ; i<= attMax ; i++)
				{
					temp = Proba.calculerProba(i,nbDesAtt);
					borneSup = ProbaMoins.calculerProba((i + bonusAtt - bonusDef),nbDesDef);
					borneInf = ProbaMoins.calculerProba((int) (0.5 *(i + 1 + bonusAtt) - bonusDef ),nbDesDef);
					temp = temp.multiply(borneSup.subtract(borneInf));
					result = result.add(temp);
				}
				break;
			case 2 :		// esquive reussie
				for(int i = attMin ; i<= attMax ; i++)
				{
					temp = Proba.calculerProba(i,nbDesAtt);
					borneSup = ProbaMoins.calculerProba((2 *(i + bonusAtt) - bonusDef),nbDesDef);
					borneInf = ProbaMoins.calculerProba((i + bonusAtt - bonusDef ),nbDesDef);
					temp = temp.multiply(borneSup.subtract(borneInf));
					result = result.add(temp);
				}
				break;
			case 3 :		// esquive parfaite
				for(int i = attMin ; i<= attMax ; i++)
				{
					temp = Proba.calculerProba(i,nbDesAtt);
					borneInf = ProbaPlus.calculerProba( (2 *(i + bonusAtt) - bonusDef -1 ),nbDesDef);
					temp = temp.multiply(borneInf);
					result = result.add(temp);
				}
				break;
		}
		return result;
	}

}

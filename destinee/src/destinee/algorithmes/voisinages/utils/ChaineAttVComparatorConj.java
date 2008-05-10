/**
 * 
 */
package destinee.algorithmes.voisinages.utils;

import java.util.Comparator;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.core.log.LogFactory;

/**
 * @author Bubulle
 * 
 * Comparateur de chaines d'attaques se basant sur l'esp�rance de d�g�ts conjectur�e
 */
public class ChaineAttVComparatorConj implements Comparator<ChaineAttaquesV>
{
	@Override
	public int compare(ChaineAttaquesV aO1, ChaineAttaquesV aO2)
	{
		try
		{
			Double espeDeg1 = new Double(aO1.getEsperanceDegatConjecturee());
			Double espeDeg2 = new Double(aO2.getEsperanceDegatConjecturee());

			// On veut l'ordre d�croissant => faire les tests � l'envers
			return espeDeg2.compareTo(espeDeg1);
		}
		catch (Exception e)
		{
			LogFactory.logError("Erreur lors de la comparaison de deux ChaineAttaquesV. M�thode ChaineAttVComparatorConj.compare()");
			return 0;
		}
	}
}

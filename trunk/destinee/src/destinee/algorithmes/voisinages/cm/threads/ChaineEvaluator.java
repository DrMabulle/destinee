/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.math.BigDecimal;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;

/**
 * @author Bubulle
 * 
 */
public class ChaineEvaluator implements Runnable
{
	private ChaineAttaquesV chaine;
	private double probaCible;
	private int voisinages;

	/**
	 * Crée une nouvelle instance de ChaineEvaluator
	 */
	public ChaineEvaluator(final ChaineAttaquesV aChaine, final double aProbaCible, final int aNbVoisinages)
	{
		super();
		chaine = aChaine;
		probaCible = aProbaCible;
		voisinages = aNbVoisinages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		try
		{
			chaine.evaluer(new BigDecimal(Double.toString(probaCible)), BigDecimal.ZERO, voisinages);
		}
		catch (TechnicalException e)
		{
			LogFactory.logError("Erreur lors de l'évaluation finale d'une chaine.", e);
		}
	}

}

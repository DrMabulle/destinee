/**
 * 
 */
package destinee.commun.utils;

import destinee.commun.data.AttaqueKamikaze;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.exception.DestineeException;
import destinee.core.log.LogFactory;

/**
 * @author Bubulle
 * 
 */
public final class KamikazeOptimiser
{
	/**
	 * Cr�e une nouvelle instance de KamikazeOptimiser.java
	 */
	private KamikazeOptimiser()
	{
		super();
	}

	/**
	 * M�thode permettant d'optimiser l'esp�rance de d�g�ts sur une attaque kamikaze, en jouant sur le sacrifice li� � l'attaque et le sacrifice li� aux d�g�ts
	 * 
	 * @param aAttaqueKamikaze une attaque kamikaze � optimiser
	 * @param aCible la cible de l'attaque
	 */
	public static final void optimiserAttaqueKamikaze(final AttaqueKamikaze aAttaqueKamikaze, final Cible aCible)
	{
		Perso perso = aAttaqueKamikaze.getPerso();
		Cible cible = aCible;
		int sacrificeAtt, sacrificeDeg;
		int sacrificeAttOptim = 0, sacrificeDegOptim = 0;
		double esperanceDegMax = 0.0, esperanceDegTemp;

		int sacrificeTotalMax = perso.getSacrificeMax();

		for (int i = 0; i < sacrificeTotalMax; i += 20)
		{
			sacrificeAtt = i;
			sacrificeDeg = sacrificeTotalMax - i;

			perso.setSacrificePourAttaque(sacrificeAtt);
			perso.setSacrificePourDegat(sacrificeDeg);

			esperanceDegTemp = ResolutionAttaque.esperanceDeDegats(aAttaqueKamikaze, cible);

			if (LogFactory.isLogDebugEnabled())
			{
				LogFactory.logDebug(aAttaqueKamikaze.toString() + " : " + esperanceDegTemp);
			}

			if (esperanceDegTemp > esperanceDegMax)
			{
				esperanceDegMax = esperanceDegTemp;
				sacrificeAttOptim = sacrificeAtt;
				sacrificeDegOptim = sacrificeDeg;
			}
		}

		perso.setSacrificePourAttaque(sacrificeAttOptim);
		perso.setSacrificePourDegat(sacrificeDegOptim);

		if (LogFactory.isLogDebugEnabled())
		{
			LogFactory.logDebug("Attaque Kamikaze optimale : " + aAttaqueKamikaze.toString());
		}
	}

	public static void main(final String[] args) throws DestineeException
	{
		PersoLoader.chargerPersos();
		Cible cible = new Cible(15, 0, 0);

		Perso perso = CachePersos.getEnsemblePersos().iterator().next();
		perso.setFatigue(15);
		AttaqueKamikaze att = new AttaqueKamikaze(perso);

		cible.setFatigue(12);
		cible.setMalusDesDefense(1.5);

		KamikazeOptimiser.optimiserAttaqueKamikaze(att, cible);
	}
}

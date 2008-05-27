/**
 * 
 */
package destinee.algorithmes.normal.cm;

import java.util.List;

import destinee.algorithmes.normal.cm.threads.DestineeQueryProcessor;
import destinee.algorithmes.normal.data.ChaineAttaques;
import destinee.algorithmes.normal.utils.GestionnaireChainesAttaques;
import destinee.commun.cm.AlgoTraitement;
import destinee.commun.data.Cible;
import destinee.commun.utils.CachePersos;
import destinee.commun.utils.PersoLoader;
import destinee.core.exception.DestineeException;
import destinee.core.log.LogFactory;
import destinee.core.utils.ConversionUtil;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public class CMAlgoNormal implements AlgoTraitement
{

	/**
	 * M�thode Main
	 * 
	 * @param args arguments
	 * @throws DestineeException DestineeException
	 */
	public static void main(final String[] args) throws DestineeException
	{
		AlgoTraitement theCMTraitement = new CMAlgoNormal();
		theCMTraitement.executerTraitement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.commun.cm.AlgoTraitement#executerTraitement()
	 */
	@Override
	public void executerTraitement() throws DestineeException
	{
		// long startTime = System.currentTimeMillis();

		PersoLoader.chargerPersos();
		Cible cible = CachePersos.getCible();

		DestineeQueryProcessor.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaques> chainesAtt = GestionnaireChainesAttaques.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaques theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			LogFactory.logInfo("Esp�rance de d�g�ts : " + theChaineAttaques.getEsperanceDegatCumulee());
			LogFactory.logInfo(theChaineAttaques.getIdentifiant() + " : " + theChaineAttaques.getEsperanceDegatCumulee());
			LogFactory.logInfo("Probabilit� de r�alisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
		}

		LogFactory.logInfo("-----------------------------");
		// long stopTime = System.currentTimeMillis();
		// LogFactory.logInfo("Temps total d'ex�cution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}
}

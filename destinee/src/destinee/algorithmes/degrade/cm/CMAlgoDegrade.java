/**
 * 
 */
package destinee.algorithmes.degrade.cm;

import java.util.List;
import java.util.Map;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.degrade.cm.threads.DestineeQueryProcessorDegrade;
import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.algorithmes.degrade.utils.GestionnaireChainesAttaquesD;
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
public class CMAlgoDegrade implements AlgoTraitement
{

	/**
	 * Méthode Main
	 * 
	 * @param args arguments
	 * @throws DestineeException DestineeException
	 */
	public static void main(final String[] args) throws DestineeException
	{
		AlgoTraitement theCMTraitement = new CMAlgoDegrade();
		theCMTraitement.executerTraitement();

		LogFactory.stopper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see destinee.commun.cm.AlgoTraitement#executerTraitement()
	 */
	@Override
	public void executerTraitement() throws DestineeException
	{
		long startTime = System.currentTimeMillis();

		PersoLoader.chargerPersos();
		Cible cible = CachePersos.getCible();

		DestineeToLogicGateway prolog = DestineeToLogicGatewayImpl.getDefaultInstance();
		String queryString = "generationChainesAttaques(Result).";
		List<Map<String, List<String>>> resultQuery = prolog.query(queryString);

		long tempsIntermediaire = System.currentTimeMillis();

		DestineeQueryProcessorDegrade.processQuery(resultQuery, "Result", cible);

		List<ChaineAttaquesD> chainesAtt = GestionnaireChainesAttaquesD.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaquesD theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo(new Object[] { "Chaine d'attaque : ", theChaineAttaques.getIdentifiant() });
			LogFactory.logInfo(new Object[] { "Espérance de dégâts : ", theChaineAttaques.getEsperanceDegatCumulee() });
		}

		LogFactory.logInfo("-----------------------------");
		long stopTime = System.currentTimeMillis();
		LogFactory.logInfo(new Object[] { "Temps total d'exécution : ", ConversionUtil.longVersStringFormat(stopTime - startTime), " ms" });
		LogFactory.logInfo(new Object[] { "Temps Prolog : ", ConversionUtil.longVersStringFormat(tempsIntermediaire - startTime), " ms" });
		LogFactory.logInfo(new Object[] { "Temps Process : ", ConversionUtil.longVersStringFormat(stopTime - tempsIntermediaire), " ms" });
	}

}

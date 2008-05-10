/**
 * 
 */
package destinee.algorithmes.normal.cm;

import java.util.List;

import destinee.algorithmes.normal.cm.threads.DestineeQueryProcessor;
import destinee.algorithmes.normal.data.ChaineAttaques;
import destinee.algorithmes.normal.utils.GestionnaireChainesAttaques;
import destinee.commun.data.Cible;
import destinee.commun.utils.PersoLoader;
import destinee.core.exception.DestineeException;
import destinee.core.log.LogFactory;
import destinee.core.utils.ConversionUtil;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public class CMAlgoNormal
{

	/**
	 * @param args
	 * @throws DestineeException
	 */
	public static void main(String[] args) throws DestineeException
	{
		// long startTime = System.currentTimeMillis();

		PersoLoader.chargerPersos();
		Cible cible = new Cible(10, 0, 0);

		DestineeQueryProcessor.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaques> chainesAtt = GestionnaireChainesAttaques.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaques theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			LogFactory.logInfo("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
			LogFactory.logInfo(theChaineAttaques.getIdentifiant() + " : " + theChaineAttaques.getEsperanceDegatCumulee());
			LogFactory.logInfo("Probabilité de réalisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
		}

		LogFactory.logInfo("-----------------------------");
		// long stopTime = System.currentTimeMillis();
		// LogFactory.logInfo("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}
}

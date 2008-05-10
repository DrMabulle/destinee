/**
 * 
 */
package destinee.algorithmes.voisinages.cm;

import java.util.List;

import destinee.algorithmes.voisinages.cm.threads.DestineeQueryProcessorOrdre;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireOrdresAttaquants;
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
public class CMAlgoOrdre
{

	/**
	 * @param args
	 * @throws DestineeException
	 */
	public static void main(String[] args) throws DestineeException
	{
		long startTime = System.currentTimeMillis();

		PersoLoader.chargerPersos();
		Cible cible = new Cible(12, 0, 0);

		DestineeQueryProcessorOrdre.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaquesV> chainesAtt = GestionnaireOrdresAttaquants.getInstance().getListeChainesOrdonneeConj();

		for (ChaineAttaquesV theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			LogFactory.logInfo("Esp�rance de d�g�ts : " + theChaineAttaques.getEsperanceDegatCumulee());
			LogFactory.logInfo("Probabilit� de r�alisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
			LogFactory.logInfo("Esp�rance de d�g�ts conjectur�e : " + theChaineAttaques.getEsperanceDegatConjecturee());
			LogFactory.logInfo("Indice de bourrinisme : " + theChaineAttaques.getIndiceBourrinisme());
			LogFactory.logInfo("Scenarios trait�s : " + theChaineAttaques.getNbScenariosTraites());
		}

		LogFactory.logInfo("-----------------------------");
		long stopTime = System.currentTimeMillis();
		LogFactory.logInfo("Temps total d'ex�cution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}

}

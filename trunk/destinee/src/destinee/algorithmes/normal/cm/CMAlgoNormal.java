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
		long startTime = System.currentTimeMillis();

		PersoLoader.chargerPersos();
		Cible cible = new Cible(15, 0, 30);

		DestineeQueryProcessor.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaques> chainesAtt = GestionnaireChainesAttaques.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaques theChaineAttaques : chainesAtt)
		{
			System.out.println("-----------------------------");
			System.out.println("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			System.out.println("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
			System.out.println("Probabilité de réalisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
		}

		System.out.println("-----------------------------");
		long stopTime = System.currentTimeMillis();
		System.out.println("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}
}

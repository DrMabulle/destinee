/**
 * 
 */
package destinee.algorithmes.voisinages.cm;

import java.util.List;

import destinee.algorithmes.voisinages.cm.threads.DestineeQueryProcessorVoisinage;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
import destinee.commun.data.Cible;
import destinee.commun.utils.PersoLoader;
import destinee.core.exception.DestineeException;
import destinee.core.utils.ConversionUtil;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public class CMAlgoVoisinage
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

		DestineeQueryProcessorVoisinage.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaquesV> chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesOrdonneeConj();

		for (ChaineAttaquesV theChaineAttaques : chainesAtt)
		{
			System.out.println("-----------------------------");
			System.out.println("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			System.out.println("Esp�rance de d�g�ts : " + theChaineAttaques.getEsperanceDegatCumulee());
			System.out.println("Probabilit� de r�alisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
			System.out.println("Esp�rance de d�g�ts conjectur�e : " + theChaineAttaques.getEsperanceDegatConjecturee());
			System.out.println("Indice de bourrinisme : " + theChaineAttaques.getIndiceBourrinisme());
			System.out.println("Scenarios trait�s : " + theChaineAttaques.getNbScenariosTraites());
		}

		System.out.println("-----------------------------");
		long stopTime = System.currentTimeMillis();
		System.out.println("Temps total d'ex�cution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}

}

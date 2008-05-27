/**
 * 
 */
package destinee.algorithmes.voisinages.cm;

import java.util.List;

import destinee.algorithmes.voisinages.cm.threads.DestineeQueryProcessorVoisinage;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
import destinee.commun.cm.AlgoTraitement;
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
public class CMAlgoVoisinage implements AlgoTraitement
{

	/**
	 * Méthode Main
	 * 
	 * @param args arguments
	 * @throws DestineeException DestineeException
	 */
	public static void main(final String[] args) throws DestineeException
	{
		AlgoTraitement theCMTraitement = new CMAlgoVoisinage();
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
		long startTime = System.currentTimeMillis();

		PersoLoader.chargerPersos();
		Cible cible = new Cible(12, 0, 0);

		DestineeQueryProcessorVoisinage.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaquesV> chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesOrdonneeConj();

		for (ChaineAttaquesV theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo("Chaine d'attaque : " + theChaineAttaques.getIdentifiant());
			LogFactory.logInfo("Espérance de dégâts : " + theChaineAttaques.getEsperanceDegatCumulee());
			LogFactory.logInfo("Probabilité de réalisation : " + ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10));
			LogFactory.logInfo("Espérance de dégâts conjecturée : " + theChaineAttaques.getEsperanceDegatConjecturee());
			LogFactory.logInfo("Indice de bourrinisme : " + theChaineAttaques.getIndiceBourrinisme());
			LogFactory.logInfo("Scenarios traités : " + theChaineAttaques.getNbScenariosTraites());
		}

		LogFactory.logInfo("-----------------------------");
		long stopTime = System.currentTimeMillis();
		LogFactory.logInfo("Temps total d'exécution : " + ConversionUtil.longVersStringFormat(stopTime - startTime) + " ms");
	}

}

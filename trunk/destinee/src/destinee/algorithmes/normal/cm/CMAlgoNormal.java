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
	 * Méthode Main
	 * 
	 * @param args arguments
	 * @throws DestineeException DestineeException
	 */
	public static void main(final String[] args) throws DestineeException
	{
		AlgoTraitement theCMTraitement = new CMAlgoNormal();
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
		PersoLoader.chargerPersos();
		Cible cible = CachePersos.getCible();

		DestineeQueryProcessor.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaques> chainesAtt = GestionnaireChainesAttaques.getInstance().getListeChainesOrdonnee();

		for (ChaineAttaques theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo(new Object[] { "Chaine d'attaque : ", theChaineAttaques.getIdentifiant() });
			LogFactory.logInfo(new Object[] { "Espérance de dégâts : ", theChaineAttaques.getEsperanceDegatCumulee() });
			LogFactory.logInfo(new Object[] { theChaineAttaques.getIdentifiant(), " : ", theChaineAttaques.getEsperanceDegatCumulee() });
			LogFactory.logInfo(new Object[] { "Probabilité de réalisation : ",
				ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10) });
		}

		LogFactory.logInfo("-----------------------------");
	}
}

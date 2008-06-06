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
public class CMAlgoVoisinage implements AlgoTraitement
{

	/**
	 * M�thode Main
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
		Cible cible = CachePersos.getCible();

		DestineeQueryProcessorVoisinage.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		List<ChaineAttaquesV> chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesOrdonneeConj();

		for (ChaineAttaquesV theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo(new Object[] { "Chaine d'attaque : ", theChaineAttaques.getIdentifiant() });
			LogFactory.logInfo(new Object[] { "Esp�rance de d�g�ts : ", theChaineAttaques.getEsperanceDegatCumulee() });
			LogFactory.logInfo(new Object[] { "Probabilit� de r�alisation : ",
				ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10) });
			LogFactory.logInfo(new Object[] { "Esp�rance de d�g�ts conjectur�e : ", theChaineAttaques.getEsperanceDegatConjecturee() });
			LogFactory.logInfo(new Object[] { "Indice de bourrinisme : ", theChaineAttaques.getIndiceBourrinisme() });
			LogFactory.logInfo(new Object[] { "Scenarios trait�s : ", theChaineAttaques.getNbScenariosTraites() });
		}

		LogFactory.logInfo("-----------------------------");
		long stopTime = System.currentTimeMillis();
		LogFactory.logInfo(new Object[] { "Temps total d'ex�cution : ", ConversionUtil.longVersStringFormat(stopTime - startTime), " ms" });
	}

}

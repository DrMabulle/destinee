/**
 * 
 */
package destinee.algorithmes.voisinages.cm;

import java.util.List;

import destinee.algorithmes.voisinages.cm.threads.DestineeQueryProcessorOrdre;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireOrdresAttaquants;
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
public class CMAlgoOrdre implements AlgoTraitement
{

	/**
	 * Méthode Main
	 * 
	 * @param args arguments
	 * @throws DestineeException DestineeException
	 */
	public static void main(final String[] args) throws DestineeException
	{
		AlgoTraitement theCMTraitement = new CMAlgoOrdre();
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

		DestineeQueryProcessorOrdre.processQuery(DestineeToLogicGatewayImpl.getDefaultInstance(), cible);

		GestionnaireOrdresAttaquants.getInstance().finaliserEvaluation();

		List<ChaineAttaquesV> chainesAtt = GestionnaireOrdresAttaquants.getInstance().getListeChainesOrdonneeConj();

		for (ChaineAttaquesV theChaineAttaques : chainesAtt)
		{
			LogFactory.logInfo("-----------------------------");
			LogFactory.logInfo(new Object[] { "Chaine d'attaque : ", theChaineAttaques.getIdentifiant() });
			LogFactory.logInfo(new Object[] { "Espérance de dégâts : ", theChaineAttaques.getEsperanceDegatCumulee() });
			LogFactory.logInfo(new Object[] { "Probabilité de réalisation : ",
				ConversionUtil.bigDecimalVersString(theChaineAttaques.getProbaRealisationCumulee(), 10) });
			LogFactory.logInfo(new Object[] { "Espérance de dégâts conjecturée : ", theChaineAttaques.getEsperanceDegatConjecturee() });
			LogFactory.logInfo(new Object[] { "Indice de bourrinisme : ", theChaineAttaques.getIndiceBourrinisme() });
			LogFactory.logInfo(new Object[] { "Scenarios traités : ", theChaineAttaques.getNbScenariosTraites() });
		}

		LogFactory.logInfo("-----------------------------");
		long stopTime = System.currentTimeMillis();
		LogFactory.logInfo(new Object[] { "Temps total d'exécution : ", ConversionUtil.longVersStringFormat(stopTime - startTime), " ms" });
	}

}

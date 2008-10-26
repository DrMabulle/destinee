/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.voisinages.utils.GestionnaireOrdresAttaquants;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.utils.CachePersos;
import destinee.core.exception.DestineeException;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;

/**
 * @author Bubulle et No-one
 * 
 * Class permettant d'analyser les résultats de la Query Prolog effectuée afin de créer les scénarios retournés par Prolog
 * 
 * Producer
 */
public class DestineeQueryProcessorOrdre extends Thread
{

	private static List<Thread> listeThreads = new ArrayList<Thread>(16);
	private DestineeToLogicGateway prologGateway;

	private static final String QUERY = "generationListeAttaquants(Result).";

	/**
	 * Constructeur par defaut
	 * 
	 * @param aGateway une DestineeToLogicGateway
	 * @param aScenarioVariableName nom de la variable Prolog utilisée
	 */
	public DestineeQueryProcessorOrdre(final DestineeToLogicGateway aGateway)
	{
		prologGateway = aGateway;

		start();
	}

	public static void processQuery(final DestineeToLogicGateway aGateway, final Cible aCible) throws DestineeException
	{
		Thread processor = new DestineeQueryProcessorOrdre(aGateway);
		traiterXVoisinages(processor, aCible);
	}

	private static void traiterXVoisinages(final Thread aProcessor, final Cible aCible) throws DestineeException
	{
		GestionnaireOrdresAttaquants.getInstance().declarerDebutTraitement();

		// Démarrer X Threads de traitement des scénarios
		instancierThreadsTraitement(aCible);

		try
		{
			aProcessor.join();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			GestionnaireOrdresAttaquants.getInstance().declarerFinTraitement();
			// Dire aux Threads de traitement de scénario qu'ils peuvent s'arrêter une fois les traitements effectués (pas de nouveaux scénarios)
			TraitementOrdreAttaques.arreterTraitements();
		}

		joinTreadsTraitement();

	}

	/**
	 * Instancie les Threads de traitement des Chaines d'attaques.
	 * 
	 * @param aCible Cible des attaques
	 */
	private static void instancierThreadsTraitement(final Cible aCible)
	{
		int nbCores = Runtime.getRuntime().availableProcessors();
		int nbThreads = nbCores * 6;
		for (int i = 0; i < nbThreads; i++)
		{
			listeThreads.add(new TraitementOrdreAttaques(aCible));
		}
	}

	/**
	 * Join threads traitement
	 * 
	 * @throws TechnicalException e
	 */
	private static void joinTreadsTraitement() throws TechnicalException
	{
		try
		{
			for (Thread thread : listeThreads)
			{
				thread.join();
			}
		}
		catch (InterruptedException e)
		{
			throw new TechnicalException("erreur lors du traitements des chaines d'attaques", e);
		}
	}

	public void run()
	{
		Map<String, List<String>> result = prologGateway.queryOnce(QUERY);

		// Variables temporaires
		List<String> vector;
		Perso attaquant;
		List<Perso> listeAttaques;

		while (result != null && !result.isEmpty())
		{
			vector = result.get("Result");
			/*
			 * On a une String de la forme [Perso1, ..., PersoN]
			 */

			CachePersos.getNouvellesInstances();
			listeAttaques = new ArrayList<Perso>();

			try
			{
				/*
				 * Parser la String représentant l'ordre d'attaque
				 */
				for (String perso : vector)
				{
					perso = perso.substring(1, perso.length() - 1);
					attaquant = CachePersos.getPerso(perso);
					listeAttaques.add(attaquant);
				}
			}
			catch (Exception e)
			{
				// throw new TechnicalException("erreur lors du parsing des résultats de la query Prolog", e);
				e.printStackTrace();
			}

			// Le scénario étant complet, l'ajouté au gestionnaire de chaines d'attaques
			GestionnaireOrdresAttaquants.getInstance().ajouterOrdreATraiter(listeAttaques);

			if (LogFactory.isLogDebugEnabled())
			{
				LogFactory.logDebug(new Object[] { "Ajout de l'ordre : ", listeAttaques });
			}

			// préparer la prochaine itération
			result = prologGateway.next();
		}

		// Une fois tous les résultats récupérés, fermer la Query Prolog
		prologGateway.stop();
	}
}

/**
 * 
 */
package destinee.algorithmes.normal.cm.threads;

import destinee.algorithmes.normal.data.Scenario;
import destinee.algorithmes.normal.utils.GestionnaireChainesAttaques;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;

/**
 * @author Bubulle
 * 
 * Consumer
 */
public class TraitementScenarios extends Thread
{
	private static boolean traitementsTermines = false;

	private int id = 0;
	private static int compteur = 0;

	public TraitementScenarios()
	{
		super();
		id = compteur++;

		start();
	}

	public void run()
	{
		try
		{
			LogFactory.logInfo(new Object[] { "Thread ", id, " : début des activités de traitement des scénarios." });

			// Variables temporaires
			Scenario scenar;

			// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
			while (!traitementsTermines || GestionnaireChainesAttaques.getInstance().hasNextScenarioATraiter())
			{
				// Récupérer le Scenario suivant pour le traiter
				scenar = GestionnaireChainesAttaques.getInstance().getNextScenarioATraiter();
				if (scenar != null)
				{
					if (LogFactory.isLogDebugEnabled())
					{
						LogFactory.logDebug(new Object[] { "Thread ", id, " : début traitement d'un scénario." });
						// Le traitement consiste simplement, ici, à demander l'espérance de dégâts, afin d'effectuer l'évaluation du scénario
					}

					scenar.getEsperanceDegats();

					// Ajouter le scénario une fois traité
					GestionnaireChainesAttaques.getInstance().ajouterScenarioTraite(scenar);
				}
			}
			LogFactory.logInfo(new Object[] { "Thread ", id, " : fin des activités de traitement des scénarios." });
		}
		catch (TechnicalException e)
		{
			LogFactory.logError(new Object[] { "Thread ", id, " : erreur lors du traitement des scénarios." });
			e.printStackTrace();
		}

	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

/**
 * 
 */
package destinee.algorithmes.normal.cm.threads;

import destinee.algorithmes.normal.data.Scenario;
import destinee.algorithmes.normal.utils.GestionnaireChainesAttaques;

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
		System.out.println("Thread " + id + " : début des activités de traitement des scénarios.");

		// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
		while (!traitementsTermines || GestionnaireChainesAttaques.getInstance().hasNextScenarioATraiter())
		{
			// Récupérer le Scenario suivant pour le traiter
			Scenario scenar = GestionnaireChainesAttaques.getInstance().getNextScenarioATraiter();
			if (scenar != null)
			{
//				System.out.println("Thread " + id + " : début traitement d'un scénario.");
				// Le traitement consiste simplement, ici, à demander l'espérance de dégâts, afin d'effectuer l'évaluation du scénario
				scenar.getEsperanceDegats();
				// Ajouter le scénario une fois traité
				GestionnaireChainesAttaques.getInstance().ajouterScenarioTraite(scenar);
			}
		}

		System.out.println("Thread " + id + " : fin des activités de traitement des scénarios.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

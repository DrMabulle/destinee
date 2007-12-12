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
		System.out.println("Thread " + id + " : d�but des activit�s de traitement des sc�narios.");

		// On continue les traitements tant qu'on ne nous dit pas de s'arr�ter et tant qu'il reste des traitements � faire
		while (!traitementsTermines || GestionnaireChainesAttaques.getInstance().hasNextScenarioATraiter())
		{
			// R�cup�rer le Scenario suivant pour le traiter
			Scenario scenar = GestionnaireChainesAttaques.getInstance().getNextScenarioATraiter();
			if (scenar != null)
			{
//				System.out.println("Thread " + id + " : d�but traitement d'un sc�nario.");
				// Le traitement consiste simplement, ici, � demander l'esp�rance de d�g�ts, afin d'effectuer l'�valuation du sc�nario
				scenar.getEsperanceDegats();
				// Ajouter le sc�nario une fois trait�
				GestionnaireChainesAttaques.getInstance().ajouterScenarioTraite(scenar);
			}
		}

		System.out.println("Thread " + id + " : fin des activit�s de traitement des sc�narios.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

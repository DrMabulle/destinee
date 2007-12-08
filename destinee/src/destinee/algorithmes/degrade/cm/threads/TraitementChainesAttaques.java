/**
 * 
 */
package destinee.algorithmes.degrade.cm.threads;

import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.algorithmes.degrade.utils.GestionnaireChainesAttaquesD;

/**
 * @author Bubulle
 * 
 * Consumer
 */
public class TraitementChainesAttaques extends Thread
{
	private static boolean traitementsTermines = false;

	private int id = 0;
	private static int compteur = 0;

	public TraitementChainesAttaques()
	{
		super();
		id = compteur++;

		start();
	}

	public void run()
	{
		System.out.println("Thread " + id + " : début des activités de traitement des Chaines d'attaques.");

		// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
		while (!traitementsTermines || GestionnaireChainesAttaquesD.getInstance().hasNextChaineAttaqueATraiter())
		{
			// Récupérer le Scenario suivant pour le traiter
			ChaineAttaquesD chaine = GestionnaireChainesAttaquesD.getInstance().getNextChaineAttaqueATraiter();
			if (chaine != null)
			{
				System.out.println("Thread " + id + " : début traitement d'une Chaine d'attaques.");
				// Le traitement consiste simplement, ici, à demander l'espérance de dégâts, afin d'effectuer l'évaluation du scénario
				chaine.getEsperanceDegatCumulee();
				// Ajouter le scénario une fois traité
				GestionnaireChainesAttaquesD.getInstance().ajouterChaineAttaqueTraitee(chaine);
			}
		}

		System.out.println("Thread " + id + " : fin des activités de traitement des Chaines d'attaques.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

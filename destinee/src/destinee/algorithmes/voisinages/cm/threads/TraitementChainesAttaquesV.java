/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;

/**
 * @author Bubulle
 * 
 * Consumer
 */
public class TraitementChainesAttaquesV extends Thread
{
	private static boolean traitementsTermines = false;

	private int id = 0;
	private static int compteur = 0;

	public TraitementChainesAttaquesV()
	{
		super();
		id = compteur++;

		start();
	}

	public void run()
	{
		System.out.println("Thread " + id + " : début des activités de traitement des chaines d'attaque.");

		// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
		while (!traitementsTermines || GestionnaireChainesAttaquesV.getInstance().hasNextChaineATraiter())
		{
			// Récupérer le Scenario suivant pour le traiter
			ChaineAttaquesV chaine = GestionnaireChainesAttaquesV.getInstance().getNextChaineATraiter();
			if (chaine != null)
			{
				// System.out.println("Thread " + id + " : début traitement d'un scénario.");
				// Le traitement consiste simplement, ici, à demander l'espérance de dégâts, afin d'effectuer l'évaluation de la chaine d'attaques
				chaine.getProbaRealisationCumulee();
				// Ajouter la chaine d'attaques une fois traitée
				GestionnaireChainesAttaquesV.getInstance().ajouterChaineTraitee(chaine);
			}
		}

		System.out.println("Thread " + id + " : fin des activités de traitement des chaines d'attaque.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

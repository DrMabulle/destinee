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
		System.out.println("Thread " + id + " : d�but des activit�s de traitement des chaines d'attaque.");

		// On continue les traitements tant qu'on ne nous dit pas de s'arr�ter et tant qu'il reste des traitements � faire
		while (!traitementsTermines || GestionnaireChainesAttaquesV.getInstance().hasNextChaineATraiter())
		{
			// R�cup�rer le Scenario suivant pour le traiter
			ChaineAttaquesV chaine = GestionnaireChainesAttaquesV.getInstance().getNextChaineATraiter();
			if (chaine != null)
			{
				// System.out.println("Thread " + id + " : d�but traitement d'un sc�nario.");
				// Le traitement consiste simplement, ici, � demander l'esp�rance de d�g�ts, afin d'effectuer l'�valuation de la chaine d'attaques
				chaine.getProbaRealisationCumulee();
				// Ajouter la chaine d'attaques une fois trait�e
				GestionnaireChainesAttaquesV.getInstance().ajouterChaineTraitee(chaine);
			}
		}

		System.out.println("Thread " + id + " : fin des activit�s de traitement des chaines d'attaque.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

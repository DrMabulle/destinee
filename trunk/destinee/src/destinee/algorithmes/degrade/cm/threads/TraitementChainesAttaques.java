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
		System.out.println("Thread " + id + " : d�but des activit�s de traitement des Chaines d'attaques.");

		// On continue les traitements tant qu'on ne nous dit pas de s'arr�ter et tant qu'il reste des traitements � faire
		while (!traitementsTermines || GestionnaireChainesAttaquesD.getInstance().hasNextChaineAttaqueATraiter())
		{
			// R�cup�rer le Scenario suivant pour le traiter
			ChaineAttaquesD chaine = GestionnaireChainesAttaquesD.getInstance().getNextChaineAttaqueATraiter();
			if (chaine != null)
			{
				System.out.println("Thread " + id + " : d�but traitement d'une Chaine d'attaques.");
				// Le traitement consiste simplement, ici, � demander l'esp�rance de d�g�ts, afin d'effectuer l'�valuation du sc�nario
				chaine.getEsperanceDegatCumulee();
				// Ajouter le sc�nario une fois trait�
				GestionnaireChainesAttaquesD.getInstance().ajouterChaineAttaqueTraitee(chaine);
			}
		}

		System.out.println("Thread " + id + " : fin des activit�s de traitement des Chaines d'attaques.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

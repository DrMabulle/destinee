/**
 * 
 */
package destinee.algorithmes.degrade.cm.threads;

import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.algorithmes.degrade.utils.GestionnaireChainesAttaquesD;
import destinee.core.log.LogFactory;

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
		LogFactory.logInfo("Thread " + id + " : début des activités de traitement des Chaines d'attaques.");

		// Variables temporaires
		ChaineAttaquesD chaine;

		// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
		while (!traitementsTermines || GestionnaireChainesAttaquesD.getInstance().hasNextChaineAttaqueATraiter())
		{
			// Récupérer le Scenario suivant pour le traiter
			chaine = GestionnaireChainesAttaquesD.getInstance().getNextChaineAttaqueATraiter();
			if (chaine != null)
			{
				if (LogFactory.isLogDebugEnabled())
					LogFactory.logDebug("Thread " + id + " : début traitement d'une Chaine d'attaques.");
				// Le traitement consiste simplement, ici, à demander l'espérance de dégâts, afin d'effectuer l'évaluation du scénario
				chaine.getEsperanceDegatCumulee();
				// Ajouter le scénario une fois traité
				GestionnaireChainesAttaquesD.getInstance().ajouterChaineAttaqueTraitee(chaine);
			}
		}

		LogFactory.logInfo("Thread " + id + " : fin des activités de traitement des Chaines d'attaques.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.math.BigDecimal;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;

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
	private int etapes = 1;
	private BigDecimal probaCible, probaMin;
	private boolean utiliseHeuristique;

	public TraitementChainesAttaquesV()
	{
		new TraitementChainesAttaquesV(1, BigDecimal.ONE, BigDecimal.ZERO, true);
	}

	public TraitementChainesAttaquesV(int nbEtapes, BigDecimal aProbaCible, BigDecimal aProbaMin, boolean doitUtiliserHeuristique)
	{
		super();
		id = compteur++;
		etapes = nbEtapes;
		probaCible = aProbaCible;
		probaMin = aProbaMin;
		utiliseHeuristique = doitUtiliserHeuristique;

		start();
	}

	public void run()
	{
		try
		{
			if (utiliseHeuristique)
			{
				traitementHeuristique();
			}
			else
			{
				traitementNormal();
			}
		}
		catch (TechnicalException e)
		{
			LogFactory.logError("Erreur lors du traitement des chaines d'attaques, méthode run()");
			e.printStackTrace();
		}
	}

	private void traitementNormal() throws TechnicalException
	{
		LogFactory.logInfo("Thread " + id + " : début des activités de traitement des chaines d'attaque.");

		// Variables temporaires
		ChaineAttaquesV chaine;
		boolean continuer = true;

		// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
		while (!traitementsTermines || continuer)
		{
			// Récupérer le Scenario suivant pour le traiter
			chaine = GestionnaireChainesAttaquesV.getInstance().getNextChaineATraiter();
			if (chaine != null)
			{
				// Le traitement consiste simplement, ici, à demander l'espérance de dégâts, afin d'effectuer l'évaluation de la chaine d'attaques
				chaine.getProbaRealisationCumulee();
				// Ajouter la chaine d'attaques une fois traitée
				GestionnaireChainesAttaquesV.getInstance().ajouterChaineTraitee(chaine);
			}
			else
			{
				continuer = false;
			}
		}

		LogFactory.logInfo("Thread " + id + " : fin des activités de traitement des chaines d'attaque.");
	}

	private void traitementHeuristique() throws TechnicalException
	{
		LogFactory.logInfo("Thread " + id + " : début des activités de traitement des chaines d'attaque.");

		// Variables temporaires
		ChaineAttaquesV chaine;
		boolean continuer = true;

		// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
		while (!traitementsTermines || continuer)
		{
			// Récupérer le Scenario suivant pour le traiter
			chaine = GestionnaireChainesAttaquesV.getInstance().getNextChaineATraiter();
			if (chaine != null)
			{
				// Le traitement consiste simplement, ici, à demander l'évaluation de la chaine selon certains critères
				chaine.evaluer(probaCible, probaMin, etapes);
				// Ajouter la chaine d'attaques une fois traitée
				GestionnaireChainesAttaquesV.getInstance().ajouterChaineTraitee(chaine);
			}
			else
			{
				continuer = false;
			}
		}

		LogFactory.logInfo("Thread " + id + " : fin des activités de traitement des chaines d'attaque.");
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}

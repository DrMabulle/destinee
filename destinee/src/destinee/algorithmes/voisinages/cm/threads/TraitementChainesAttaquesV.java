/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.math.BigDecimal;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
import destinee.core.exception.TechnicalException;
import destinee.core.properties.PropertiesFactory;

/**
 * @author Bubulle
 * 
 * Consumer
 */
public class TraitementChainesAttaquesV extends Thread
{
	private static final String CLE_UTILISATION_HEURISTIQUE = "destinee.voisinage.utiliser.heuristique";

	private static boolean traitementsTermines = false;

	private int id = 0;
	private static int compteur = 0;
	private int etapes = 1;
	private BigDecimal probaCible, probaMin;

	public TraitementChainesAttaquesV()
	{
		new TraitementChainesAttaquesV(1, BigDecimal.ONE, BigDecimal.ZERO);
	}

	public TraitementChainesAttaquesV(int nbEtapes, BigDecimal aProbaCible, BigDecimal aProbaMin)
	{
		super();
		id = compteur++;
		etapes = nbEtapes;
		probaCible = aProbaCible;
		probaMin = aProbaMin;

		start();
	}

	public void run()
	{
		Boolean utiliseHeuristique;
		try
		{
			utiliseHeuristique = PropertiesFactory.getOptionalBoolean(CLE_UTILISATION_HEURISTIQUE);

			if (Boolean.TRUE.equals(utiliseHeuristique))
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
			System.err.println("Erreur lors du traitement des chaines d'attaques, m�thode run()");
			e.printStackTrace();
		}
	}

	private void traitementNormal() throws TechnicalException
	{
		System.out.println("Thread " + id + " : d�but des activit�s de traitement des chaines d'attaque.");

		// Variables temporaires
		ChaineAttaquesV chaine;

		// On continue les traitements tant qu'on ne nous dit pas de s'arr�ter et tant qu'il reste des traitements � faire
		while (!traitementsTermines || GestionnaireChainesAttaquesV.getInstance().hasNextChaineATraiter())
		{
			// R�cup�rer le Scenario suivant pour le traiter
			chaine = GestionnaireChainesAttaquesV.getInstance().getNextChaineATraiter();
			if (chaine != null)
			{
				// Le traitement consiste simplement, ici, � demander l'esp�rance de d�g�ts, afin d'effectuer l'�valuation de la chaine d'attaques
				chaine.getProbaRealisationCumulee();
				// Ajouter la chaine d'attaques une fois trait�e
				GestionnaireChainesAttaquesV.getInstance().ajouterChaineTraitee(chaine);
			}
		}

		System.out.println("Thread " + id + " : fin des activit�s de traitement des chaines d'attaque.");
	}

	private void traitementHeuristique() throws TechnicalException
	{
		System.out.println("Thread " + id + " : d�but des activit�s de traitement des chaines d'attaque.");

		// Variables temporaires
		ChaineAttaquesV chaine;

		// On continue les traitements tant qu'on ne nous dit pas de s'arr�ter et tant qu'il reste des traitements � faire
		while (!traitementsTermines || GestionnaireChainesAttaquesV.getInstance().hasNextChaineATraiter())
		{
			// R�cup�rer le Scenario suivant pour le traiter
			chaine = GestionnaireChainesAttaquesV.getInstance().getNextChaineATraiter();
			if (chaine != null)
			{
				// Le traitement consiste simplement, ici, � demander l'�valuation de la chaine selon certains crit�res
				chaine.evaluer(probaCible, probaMin, etapes);
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
